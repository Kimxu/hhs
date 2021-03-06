/**
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kimxu.hhs.atys.widget.img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.LinkedList;

import kimxu.hhs.z_volley.Request;
import kimxu.hhs.z_volley.RequestQueue;
import kimxu.hhs.z_volley.Response;
import kimxu.hhs.z_volley.VolleyError;
import kimxu.hhs.z_volley.http.CommonResponceListener;
import kimxu.hhs.z_volley.http.GenericRequest;
import kimxu.hhs.z_volley.http.NetResponseCode;
import kimxu.hhs.z_volley.http.RequestFactory;
import kimxu.hhs.z_volley.structure.ImageCache;
import kimxu.hhs.z_volley.structure.ImageL1Cache;
import kimxu.hhs.z_volley.toolbox.requests.ImageRequest;
import kimxu.hhs.z_volley.toolbox.requests.YYHImageRequest;

/**
 * Helper that handles loading and caching images from remote URLs.
 * 
 * The simple way to use this class is to call
 * {@link ImageLoader#get(String, ImageListener)} and to pass in the default
 * image listener provided by
 * {@link ImageLoader#getImageListener(ImageView, int, int)}. Note that all
 * function calls to this class must be made from the main thead, and all
 * responses will be delivered to the main thread as well.
 */
public class ImageLoader {

	public static final int IMAGE_TYPE_ICON = 0;
	public static final int IMAGE_TYPE_BANNER = 1;
	public static final int IMAGE_TYPE_SNAPSHOT = 2;
	public static final int IMAGE_TYPE_NEWS = 3;
	public static final int IMAGE_TYPE_PORTRAIT = 4;
	public static final int IMAGE_TYPE_NEWS_BANNER = 5;
	public static final int IMAGE_TYPE_BOOKS_BANNER = 6;
	public static final int IMAGE_TYPE_OTHER = 10;
	public static final int CACHED_ID_IMAGE = 99;
	public static final int IMAGE_TYPE_LARGE_IMAGE = 11;// 包括banner包括snapshot
	public static final int IMAGE_TYPE_ACCOUNT_CENTER_BACK = 13;
	public static final int IMAGE_TYPE_PREVIEW = 14;
	public static final int IMAGE_TYPE_CIRCLE_NEWS = 15;
	public static final int IMAGE_TYPE_NAVIGATION_AVT = 16;
	public static final int IMAGE_TYPE_COMENT_THUMBNAIL = 17;
	public static final int IMAGE_TYPE_PORTRAIT_CIRCLE = 18;
	// private static volatile ImageLoader instance;
	/** RequestQueue for dispatching ImageRequests onto. */
	private final RequestQueue mRequestQueue;

	/**
	 * Amount of time to wait after first response arrives before delivering all
	 * responses.
	 */
	private int mBatchResponseDelayMs = 100;

	/**
	 * The cache implementation to be used as an L1 cache before calling into
	 * volley.
	 */
	private final ImageCache mCache;

	/**
	 * HashMap of Cache keys -> BatchedImageRequest used to track in-flight
	 * requests so that we can coalesce multiple requests to the same URL into a
	 * single network request.
	 */
	private final HashMap<String, BatchedImageRequest> mInFlightRequests = new HashMap<String, BatchedImageRequest>();

	/** HashMap of the currently pending responses (waiting to be delivered). */
	private final HashMap<String, BatchedImageRequest> mBatchedResponses = new HashMap<String, BatchedImageRequest>();

	/** Handler to the main thread. */
	private final Handler mHandler = new Handler(Looper.getMainLooper());

	/** Runnable for in-flight response delivery. */
	private Runnable mRunnable;

	public static ImageLoader newInstance() {
		// if (instance == null) {
		// synchronized (ImageLoader.class) {
		// RequestQueue queue = (RequestQueue) RequestFactory
		// .getRequestQueue();
		// instance = new ImageLoader(queue, ImageL1Cache.getInstance());
		// }
		// }
		RequestQueue queue = (RequestQueue) RequestFactory.getRequestQueue();
		return new ImageLoader(queue, ImageL1Cache.getInstance());
	}

	/**
	 * Constructs a new ImageLoader.
	 * 
	 * @param queue
	 *            The RequestQueue to use for making image requests.
	 * @param imageCache
	 *            The cache to use as an L1 cache.
	 */
	private ImageLoader(RequestQueue queue, ImageCache imageCache) {
		mRequestQueue = queue;
		mCache = imageCache;
	}

	/**
	 * The default implementation of ImageListener which handles basic
	 * functionality of showing a default image until the network response is
	 * received, at which point it will switch to either the actual image or the
	 * error image.
	 * 
	 * @param view
	 * @param defaultImageResId
	 *            Default image resource ID to use, or 0 if it doesn't exist.
	 * @param errorImageResId
	 *            Error image resource ID to use, or 0 if it doesn't exist.
	 * @return
	 */
	public static ImageListener getImageListener(final ImageView view,
			final int defaultImageResId, final int errorImageResId) {
		return new ImageListener() {
			@Override
			public void onErrorResponse(VolleyError error,
					GenericRequest<?> request) {
				if (errorImageResId != 0) {
					view.setImageResource(errorImageResId);
				}
			}

			@Override
			public void onResponse(ImageContainer response, boolean isImmediate) {
				if (response.getBitmap() != null) {
					view.setImageBitmap(response.getBitmap());
				} else if (defaultImageResId != 0) {
					view.setImageResource(defaultImageResId);
				}
			}
		};
	}

	/**
	 * Interface for the response handlers on image requests.
	 * 
	 * The call flow is this: 1. Upon being attached to a request,
	 * onResponse(response, true) will be invoked to reflect any cached data
	 * that was already available. If the data was available,
	 * response.getBitmap() will be non-null.
	 * 
	 * 2. After a network response returns, only one of the following cases will
	 * happen: - onResponse(response, false) will be called if the image was
	 * loaded. or - onErrorResponse will be called if there was an error loading
	 * the image.
	 */
	public interface ImageListener extends Response.ErrorListener {
		/**
		 * Listens for non-error changes to the loading of the image request.
		 * 
		 * @param response
		 *            Holds all information pertaining to the request, as well
		 *            as the bitmap (if it is loaded).
		 * @param isImmediate
		 *            True if this was called during ImageLoader.get() variants.
		 *            This can be used to differentiate between a cached image
		 *            loading and a network image loading in order to, for
		 *            example, run an animation to fade in network loaded
		 *            images.
		 */
		public void onResponse(ImageContainer response, boolean isImmediate);
	}

	/**
	 * Checks if the item is available in the cache.
	 * 
	 * @param requestUrl
	 *            The url of the remote image
	 * @param maxWidth
	 *            The maximum width of the returned image.
	 * @param maxHeight
	 *            The maximum height of the returned image.
	 * @return True if the item exists in cache, false otherwise.
	 */
	public boolean isCached(String requestUrl, int maxWidth, int maxHeight) {
		throwIfNotOnMainThread();

		String cacheKey = getCacheKey(requestUrl, maxWidth, maxHeight);
		return mCache.getBitmap(cacheKey) != null;
	}

	/**
	 * Returns an ImageContainer for the requested URL.
	 * 
	 * The ImageContainer will contain either the specified default bitmap or
	 * the loaded bitmap. If the default was returned, the {@link ImageLoader}
	 * will be invoked when the request is fulfilled.
	 * 
	 * @param requestUrl
	 *            The URL of the image to be loaded.
	 * @param listener
	 * @return
	 */
	public ImageContainer get(String requestUrl, final ImageListener listener) {
		return get(requestUrl, listener, 0, 0);
	}

	/**
	 * Issues a bitmap request with the given URL if that image is not available
	 * in the cache, and returns a bitmap container that contains all of the
	 * data relating to the request (as well as the default image if the
	 * requested image is not available).
	 * 
	 * @param requestUrl
	 *            The url of the remote image
	 * @param imageListener
	 *            The listener to call when the remote image is loaded
	 * @param maxWidth
	 *            The maximum width of the returned image.
	 * @param maxHeight
	 *            The maximum height of the returned image.
	 * @return A container object that contains all of the properties of the
	 *         request, as well as the currently available image (default if
	 *         remote is not loaded).
	 */
	public ImageContainer get(String requestUrl, ImageListener imageListener,
			int maxWidth, int maxHeight) {
		// only fulfill requests that were initiated from the main thread.
		throwIfNotOnMainThread();

		final String cacheKey = getCacheKey(requestUrl, maxWidth, maxHeight);

		// Try to look up the request in the cache of remote images.
		Bitmap cachedBitmap = mCache.getBitmap(cacheKey);
		if (cachedBitmap != null) {
			// Return the cached bitmap.
			ImageContainer container = new ImageContainer(cachedBitmap,
					requestUrl, null, null);
			imageListener.onResponse(container, true);
			return container;
		}

		// The bitmap did not exist in the cache, fetch it!
		ImageContainer imageContainer = new ImageContainer(null, requestUrl,
				cacheKey, imageListener);

		// Update the caller to let them know that they should use the default
		// bitmap.
		imageListener.onResponse(imageContainer, true);

		// Check to see if a request is already in-flight.
		BatchedImageRequest request = mInFlightRequests.get(cacheKey);
		if (request != null) {
			// If it is, add this request to the list of listeners.
			request.addContainer(imageContainer);
			return imageContainer;
		}

		// The request is not already in flight. Send the new request to the
		// network and
		// track it.
		Request<?> newRequest;
		if (RequestFactory.isNeedWebPDecode()) {
			newRequest = new YYHImageRequest(requestUrl, maxWidth, maxHeight,
					Bitmap.Config.RGB_565, new CommonResponceListener<Bitmap>() {

						@Override
						protected void handleResponse(Bitmap response,
								GenericRequest<?> request) {
							onGetImageSuccess(cacheKey, response);
						}

						@Override
						protected void handleErr(VolleyError err,
								GenericRequest<?> request,
								NetResponseCode code, String content) {
							onGetImageError(cacheKey, err);

						}
					});
			/*
			 * () {
			 * 
			 * @Override public void onErrorResponse(VolleyError
			 * error,GenericRequest<?> request) {
			 * 
			 * } }); new CommonResponceListener<Bitmap>() {
			 * 
			 * @Override protected void handleResponse(Bitmap response,
			 * GenericRequest<?> request) { listener.onResponse(response,
			 * request); }
			 * 
			 * @Override protected void handleErr(VolleyError err,
			 * GenericRequest<?> request, NetResponseCode code, String content)
			 * { errorListener.onErrorResponse(err, request); } };
			 */
		} else {
			newRequest = new ImageRequest(requestUrl, maxWidth, maxHeight,
					Bitmap.Config.RGB_565, new CommonResponceListener<Bitmap>() {

						@Override
						protected void handleResponse(Bitmap response,
								GenericRequest<?> request) {
							onGetImageSuccess(cacheKey, response);
						}

						@Override
						protected void handleErr(VolleyError err,
								GenericRequest<?> request,
								NetResponseCode code, String content) {
							onGetImageError(cacheKey, err);

						}
					});
		}
		mRequestQueue.add(newRequest);
		mInFlightRequests.put(cacheKey, new BatchedImageRequest(newRequest,
				imageContainer));
		return imageContainer;
	}

	/**
	 * Sets the amount of time to wait after the first response arrives before
	 * delivering all responses. Batching can be disabled entirely by passing in
	 * 0.
	 * 
	 * @param newBatchedResponseDelayMs
	 *            The time in milliseconds to wait.
	 */
	public void setBatchedResponseDelay(int newBatchedResponseDelayMs) {
		mBatchResponseDelayMs = newBatchedResponseDelayMs;
	}

	/**
	 * Handler for when an image was successfully loaded.
	 * 
	 * @param cacheKey
	 *            The cache key that is associated with the image request.
	 * @param response
	 *            The bitmap that was returned from the network.
	 */
	private void onGetImageSuccess(String cacheKey, Bitmap response) {
		// cache the image that was fetched.
		mCache.putBitmap(cacheKey, response);

		// remove the request from the list of in-flight requests.
		BatchedImageRequest request = mInFlightRequests.remove(cacheKey);

		if (request != null) {
			// Update the response bitmap.
			request.mResponseBitmap = response;

			// Send the batched response
			batchResponse(cacheKey, request);
		}
	}

	/**
	 * Handler for when an image failed to load.
	 * 
	 * @param cacheKey
	 *            The cache key that is associated with the image request.
	 */
	private void onGetImageError(String cacheKey, VolleyError error) {
		// Notify the requesters that something failed via a null result.
		// Remove this request from the list of in-flight requests.
		BatchedImageRequest request = mInFlightRequests.remove(cacheKey);

		if (request != null) {
			// Set the error for this request
			request.setError(error);

			// Send the batched response
			batchResponse(cacheKey, request);
		}
	}

	/**
	 * Container object for all of the data surrounding an image request.
	 */
	public class ImageContainer {
		/**
		 * The most relevant bitmap for the container. If the image was in
		 * cache, the Holder to use for the final bitmap (the one that pairs to
		 * the requested URL).
		 */
		private Bitmap mBitmap;

		private final ImageListener mListener;

		/** The cache key that was associated with the request */
		private final String mCacheKey;

		/** The request URL that was specified */
		private final String mRequestUrl;

		/**
		 * Constructs a BitmapContainer object.
		 * 
		 * @param bitmap
		 *            The final bitmap (if it exists).
		 * @param requestUrl
		 *            The requested URL for this container.
		 * @param cacheKey
		 *            The cache key that identifies the requested URL for this
		 *            container.
		 */
		public ImageContainer(Bitmap bitmap, String requestUrl,
				String cacheKey, ImageListener listener) {
			mBitmap = bitmap;
			mRequestUrl = requestUrl;
			mCacheKey = cacheKey;
			mListener = listener;
		}

		/**
		 * Releases interest in the in-flight request (and cancels it if no one
		 * else is listening).
		 */
		public void cancelRequest() {
			if (mListener == null) {
				return;
			}

			BatchedImageRequest request = mInFlightRequests.get(mCacheKey);
			if (request != null) {
				boolean canceled = request
						.removeContainerAndCancelIfNecessary(this);
				if (canceled) {
					mInFlightRequests.remove(mCacheKey);
				}
			} else {
				// check to see if it is already batched for delivery.
				request = mBatchedResponses.get(mCacheKey);
				if (request != null) {
					request.removeContainerAndCancelIfNecessary(this);
					if (request.mContainers.size() == 0) {
						mBatchedResponses.remove(mCacheKey);
					}
				}
			}
		}

		/**
		 * Returns the bitmap associated with the request URL if it has been
		 * loaded, null otherwise.
		 */
		public Bitmap getBitmap() {
			return mBitmap;
		}

		/**
		 * Returns the requested URL for this container.
		 */
		public String getRequestUrl() {
			return mRequestUrl;
		}
	}

	/**
	 * Wrapper class used to map a Request to the set of active ImageContainer
	 * objects that are interested in its results.
	 */
	private class BatchedImageRequest {
		/** The request being tracked */
		private final Request<?> mRequest;

		/** The result of the request being tracked by this item */
		private Bitmap mResponseBitmap;

		/** Error if one occurred for this response */
		private VolleyError mError;

		/**
		 * List of all of the active ImageContainers that are interested in the
		 * request
		 */
		private final LinkedList<ImageContainer> mContainers = new LinkedList<ImageContainer>();

		/**
		 * Constructs a new BatchedImageRequest object
		 * 
		 * @param request
		 *            The request being tracked
		 * @param container
		 *            The ImageContainer of the person who initiated the
		 *            request.
		 */
		public BatchedImageRequest(Request<?> request, ImageContainer container) {
			mRequest = request;
			mContainers.add(container);
		}

		/**
		 * Set the error for this response
		 */
		public void setError(VolleyError error) {
			mError = error;
		}

		/**
		 * Get the error for this response
		 */
		public VolleyError getError() {
			return mError;
		}

		/**
		 * Adds another ImageContainer to the list of those interested in the
		 * results of the request.
		 */
		public void addContainer(ImageContainer container) {
			mContainers.add(container);
		}

		/**
		 * Detatches the bitmap container from the request and cancels the
		 * request if no one is left listening.
		 * 
		 * @param container
		 *            The container to remove from the list
		 * @return True if the request was canceled, false otherwise.
		 */
		public boolean removeContainerAndCancelIfNecessary(
				ImageContainer container) {
			mContainers.remove(container);
			if (mContainers.size() == 0) {
				mRequest.cancel();
				return true;
			}
			return false;
		}
	}

	/**
	 * Starts the runnable for batched delivery of responses if it is not
	 * already started.
	 * 
	 * @param cacheKey
	 *            The cacheKey of the response being delivered.
	 * @param request
	 *            The BatchedImageRequest to be delivered.
	 */
	private void batchResponse(String cacheKey, BatchedImageRequest request) {
		mBatchedResponses.put(cacheKey, request);
		// If we don't already have a batch delivery runnable in flight, make a
		// new one.
		// Note that this will be used to deliver responses to all callers in
		// mBatchedResponses.
		if (mRunnable == null) {
			mRunnable = new Runnable() {
				@Override
				public void run() {
					synchronized (mBatchedResponses) {
						for (BatchedImageRequest bir : mBatchedResponses
								.values()) {
							for (ImageContainer container : bir.mContainers) {
								// If one of the callers in the batched request
								// canceled the request
								// after the response was received but before it
								// was
								// delivered,
								// skip them.
								if (container.mListener == null) {
									continue;
								}
								if (bir.getError() == null) {
									container.mBitmap = bir.mResponseBitmap;
									container.mListener.onResponse(container,
											false);
								} else {
									container.mListener.onErrorResponse(
											bir.getError(), bir.mRequest);
								}
							}
						}
						mBatchedResponses.clear();
					}
					mRunnable = null;
				}

			};
			// Post the runnable.
			mHandler.postDelayed(mRunnable, mBatchResponseDelayMs);
		}
	}

	private void throwIfNotOnMainThread() {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			throw new IllegalStateException(
					"ImageLoader must be invoked from the main thread.");
		}
	}

	/**
	 * Creates a cache key for use with the L1 cache.
	 * 
	 * @param url
	 *            The URL of the request.
	 * @param maxWidth
	 *            The max-width of the output.
	 * @param maxHeight
	 *            The max-height of the output.
	 */
	private static String getCacheKey(String url, int maxWidth, int maxHeight) {
		// return new StringBuilder(url.length() + 12).append("#W")
		// .append(maxWidth).append("#H").append(maxHeight).append(url)
		// .toString();
		return url;
	}

	// 只是加入到内存换村中，没有加入到文件缓存中
	public void addBitmapToCache(String url, byte[] bytes_bitmap) {
		if (bytes_bitmap == null || bytes_bitmap.length == 0) {
//			L.e("imageLoader",
//					"needed bytes is null in addUrlWithBitmapTocache");
			return;
		}
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes_bitmap, 0,
				bytes_bitmap.length);
		if (mCache != null)
			mCache.putBitmap(url, bitmap);
	}

	// 从本地读取应用icon
	// public void loadFromLocal(String packageName, ImageView imageView) {
	// // loadFromLocal(packageName, imageView, null);
	// }

	// 先读取安装的应用的icon，如果没有缓存，读取apk文件的icon
	// public void loadFromLocal(String packageName, ImageView imageView,
	// String apkFilePath) {
	// if (packageName == null || imageView == null) {
	// return;
	// }
	// Bitmap bitmap = mCache.getBitmap(packageName);
	// if (bitmap == null) {// 图片为空，执行线程
	//
	// imageView.setImageBitmap(YYHImageCache.sDefaultIcon);
	//
	// if (imageView.getTag() != null
	// && ((BitmapLoadTask) imageView.getTag()).mUrl
	// .equals(packageName)) {// 当前该view执行的task是正确的，不做任何操作
	// return;
	// } else {// 重新启动一条task
	// if (imageView.getTag() != null) {// 该imageview中在执行一条其他的task，删除之
	// ((BitmapLoadTask) imageView.getTag()).cancel(true);
	// }
	//
	// // 为该view加入一条新的task
	// BitmapLoadTask task = new BitmapLoadTask(mContext, packageName,
	// imageView, IMAGE_TYPE_ICON, mCache, null, apkFilePath);
	// imageView.setTag(task);
	// task.execute();
	// }
	// } else {// 直接加载图片
	// if (imageView.getTag() != null) {// 如果还有task在执行，取消该task
	// ((BitmapLoadTask) imageView.getTag()).cancel(true);
	// }
	// imageView.setImageBitmap(bitmap);
	// }

	// }

	public static class ImageResponse {
		public int id;
		public Bitmap bitmap;

		public ImageResponse(int appId, Bitmap bitmap) {
			this.id = appId;
			this.bitmap = bitmap;
		}
	}

	public static void clean() {
		// instance.mCache.clear();
		// instance = null;

	}

}
