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


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import kimxu.hhs.z_api.Api;
import kimxu.hhs.z_api.network.NetworkInfoHelper;
import kimxu.hhs.z_api.pref.PreferanceHelper;
import kimxu.hhs.z_base.ConstantValues;
import kimxu.hhs.z_base.data.DefaultBitmapEnum;
import kimxu.hhs.z_volley.VolleyError;
import kimxu.hhs.z_volley.base.AppCallback;
import kimxu.hhs.z_volley.http.GenericRequest;

/**
 * Handles fetching an image from a URL as well as the life-cycle of the
 * associated request.
 */
public class NetworkImageView extends ImageView {
	public static final int DEFAULT_IMAGE = 0, CIRCLE_IMAGE = 1,
			ROUND_RECT_IMAGE = 2;
	/** The URL of the network image to load */
	private String mUrl;
	private Path coverPath;
	private int radius, narrowX, narrowY;
	/**
	 * Resource ID of the image to be used as a placeholder until the network
	 * image is loaded.
	 */
	private int mDefaultImageId;

	/**
	 * Resource ID of the image to be used if the network response fails.
	 */
	private int mErrorImageId;
	private int mImgShapeType;
	/** Local copy of the ImageLoader. */
	private ImageLoader mImageLoader;

	private int mImageType = -1;

	/** Current ImageContainer. (either in-flight or finished) */
	private ImageLoader.ImageContainer mImageContainer;
	private PreferanceHelper pref;
	private NetworkInfoHelper netInfo;

	public NetworkImageView(Context context) {
		this(context, null);
	}

	public NetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		pref = (PreferanceHelper) Api.pref.getHandler();
		if(!isInEditMode()){
			netInfo = (NetworkInfoHelper) Api.netInfo.getHandler();
		}
		mImgShapeType = DEFAULT_IMAGE;
		radius = 0;
	}

	/**
	 * Sets URL of the image that should be loaded into this view. Note that
	 * calling this will immediately either set the cached image (if available)
	 * or the default image specified by
	 * {@link NetworkImageView#setDefaultImageResId(int)} on the view.
	 * 
	 * NOTE: If applicable, {@link NetworkImageView#setDefaultImageResId(int)}
	 * and {@link NetworkImageView#setErrorImageResId(int)} should be called
	 * prior to calling this function.
	 * 
	 * @param url
	 *            The URL that should be loaded into this ImageView.
	 * @param defaultImageId
	 */
	public void setImageUrl(String url, int defaultImageId) {
		mUrl = url;
		mImageLoader = ImageLoader.newInstance();
		// The URL has potentially changed. See if we need to load it.
		// this.setDefaultImageResId(defaultImageId);
		this.mImageType = defaultImageId;
		loadImageIfNecessary(false);
	}

	public void setImageShapeType(int tp) {
		this.mImgShapeType = tp;
	}

	public void setRadius(int radius, int narrowX, int narrowY) {
		this.radius = radius;
		this.narrowX = narrowX;
		this.narrowY = narrowY;
	}

	/**
	 * Sets the default image resource ID to be used for this view until the
	 * attempt to load it completes.
	 */
	public void setDefaultImageResId(int defaultImage) {
		mDefaultImageId = defaultImage;
	}

	/**
	 * Sets the error image resource ID to be used for this view in the event
	 * that the image requested fails to load.
	 */
	public void setErrorImageResId(int errorImage) {
		mErrorImageId = errorImage;
	}

	/**
	 * Loads the image for the view if it isn't already loaded.
	 * 
	 * @param isInLayoutPass
	 *            True if this was invoked from a layout pass, false otherwise.
	 */
	void loadImageIfNecessary(final boolean isInLayoutPass) {
		if (mImageLoader == null)
			return;// 可以让Network替代普通的imageview使用
		int width = getWidth();
		int height = getHeight();

		boolean wrapWidth = false, wrapHeight = false;
		if (getLayoutParams() != null) {
			wrapWidth = getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT;
			wrapHeight = getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT;
		}

		// if the view's bounds aren't known yet, and this is not a
		// wrap-content/wrap-content
		// view, hold off on loading the image.
		boolean isFullyWrapContent = wrapWidth && wrapHeight;
		if (width == 0 && height == 0 && !isFullyWrapContent) {
			return;
		}

		// if the URL to be loaded in this view is empty, cancel any old
		// requests and clear the
		// currently loaded image.
		if (TextUtils.isEmpty(mUrl)) {
			if (mImageContainer != null) {
				mImageContainer.cancelRequest();
				mImageContainer = null;
			}
			setDefaultImage(this.mImageType);
			// setDefaultImageOrNull();
			return;
		}

		// if there was an old request in this view, check if it needs to be
		// canceled.
		if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
			if (mImageContainer.getRequestUrl().equals(mUrl)) {
				// if the request is from the same URL, return.
				return;
			} else {
				// if there is a pre-existing request, cancel it if it's
				// fetching a different URL.
				mImageContainer.cancelRequest();
				// setDefaultImageOrNull();
				setDefaultImage(this.mImageType);
			}
		}

		if (mImageLoader != null) {
			if (!netInfo.isWifi()) {
				if (this.mImageType == ImageLoader.IMAGE_TYPE_ICON) {
					boolean flag = pref.getBoolean(null,
							ConstantValues.KEY_LOAD_ICON, true);
					if (!flag) {
						set2GImage(mImageType);
						return;
					}
				} else if (this.mImageType == ImageLoader.IMAGE_TYPE_PREVIEW) {
					// 需求图片选择器中的大图，无论如何都要显示
				} else if (this.mImageType == ImageLoader.IMAGE_TYPE_PORTRAIT) {
					// 用户头像，无论如何都要显示
				} else if (this.mImageType == ImageLoader.IMAGE_TYPE_ACCOUNT_CENTER_BACK) {
					// 个人中心背景图，无论如何都要显示
				} else if (this.mImageType == ImageLoader.IMAGE_TYPE_NAVIGATION_AVT) {
					// Actionbar头像，无论如何都要显示
				} else {
					boolean flag = pref.getBoolean(null,
							ConstantValues.KEY_LOAD_IMAGE, true);
					if (!flag) {
						set2GImage(mImageType);
						return;
					}
				}
			}
		}

		// Calculate the max image width / height to use while ignoring
		// WRAP_CONTENT dimens.
		int maxWidth = wrapWidth ? 0 : width;
		int maxHeight = wrapHeight ? 0 : height;

		// The pre-existing content of this view didn't match the current URL.
		// Load the new image
		// from the network.
		ImageLoader.ImageContainer newContainer = mImageLoader.get(mUrl,
				new ImageLoader.ImageListener() {
					@Override
					public void onErrorResponse(VolleyError error,
							GenericRequest<?> request) {
						// if (mErrorImageId != 0) {
						// setImageResource(mErrorImageId);
						// }
						setDefaultImage(mImageType);
					}

					@Override
					public void onResponse(final ImageLoader.ImageContainer response,
							boolean isImmediate) {
						// If this was an immediate response that was delivered
						// inside of a layout
						// pass do not set the image immediately as it will
						// trigger a requestLayout
						// inside of a layout. Instead, defer setting the image
						// by posting back to
						// the main thread.
						if (isImmediate && isInLayoutPass) {
							post(new Runnable() {
								@Override
								public void run() {
									onResponse(response, false);
									if (imageCallback != null) {
										imageCallback.onCallBack(identify);
									}
								}
							});
							return;
						}

						if (response.getBitmap() != null) {
							if (mImgShapeType == DEFAULT_IMAGE) {
								setImageBitmap(response.getBitmap());
							} else if (mImgShapeType == CIRCLE_IMAGE) {
								// setImageBitmap(createCircleImage(response.getBitmap(),
								// DeviceUtil.convertDipToPx(getContext(),
								// 30)));
								setImageBitmap(toRoundBitmap(response
										.getBitmap()));
							} else if (mImgShapeType == ROUND_RECT_IMAGE) {
								setImageBitmap(toRoundRectBitmap(radius,
										narrowX, narrowY, response.getBitmap()));
							} else {
								if (coverPath != null) {
									Bitmap bb = toMaskBitmap(coverPath,
											response.getBitmap());
									setImageBitmap(bb);
								} else {
									setImageBitmap(response.getBitmap());
								}
							}
							// setImageBitmap(response.getBitmap());
						} else {
							// setImageResource(mDefaultImageId);
							setDefaultImage(mImageType);
						}
					}
				}, maxWidth, maxHeight);

		// update the ImageContainer to be the new bitmap container.
		mImageContainer = newContainer;
	}

	private void setDefaultImageOrNull() {
		if (mDefaultImageId != 0) {
			setImageResource(mDefaultImageId);
		} else {
			setImageBitmap(null);
		}
	}

	public void setCoverPath(Path coverPath) {
		this.coverPath = coverPath;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		loadImageIfNecessary(true);
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mImageContainer != null) {
			// If the view was bound to an image request, cancel it and clear
			// out the image from the view.
			mImageContainer.cancelRequest();
			setImageBitmap(null);
			// also clear out the container so we can reload the image if
			// necessary.
			mImageContainer = null;
		}
		super.onDetachedFromWindow();
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}

	private AppCallback imageCallback;
	private int identify;

	public void setImageCallback(AppCallback imageCallback, int identify) {
		this.imageCallback = imageCallback;
		this.identify = identify;
	}

	public void setDefaultImage(final int imageType) {
		if (imageType < 0) {
			return;
		}
		if (imageType == ImageLoader.IMAGE_TYPE_ICON) {
			setImageBitmap(DefaultBitmapEnum.sDefaultIcon.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_BANNER) {
			setImageBitmap(DefaultBitmapEnum.sDefaultBanner.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_NEWS_BANNER) {
			setImageBitmap(DefaultBitmapEnum.sDefaultNewsBanner.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_PORTRAIT) {
			setImageBitmap(DefaultBitmapEnum.sDefaultPortrait.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_BOOKS_BANNER) {
			setImageBitmap(DefaultBitmapEnum.sDefaultBookBanner.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_ACCOUNT_CENTER_BACK) {
			setImageBitmap(DefaultBitmapEnum.sDefaultAccountCenterBack
					.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_COMENT_THUMBNAIL) {
			setImageBitmap(DefaultBitmapEnum.sDefaultIcon.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_PREVIEW) {
			// 图片选择器进行大图预览的时候调用，这里不需要做处理，也不能设置null
		} else if (imageType == ImageLoader.IMAGE_TYPE_NAVIGATION_AVT) {
			setImageBitmap(DefaultBitmapEnum.sDefaultNavigationAvt.getBitmap());
		}else if(imageType == ImageLoader.IMAGE_TYPE_PORTRAIT_CIRCLE){
			setImageBitmap(DefaultBitmapEnum.sDefaultPortraitCircle.getBitmap());
		} else {
			setImageBitmap(null);
		}
	}

	// 2g情况下默认显示的image
	public void set2GImage(final int imageType) {
		if (imageType < 0) {
			return;
		}
		if (imageType == ImageLoader.IMAGE_TYPE_ICON) {
			setImageBitmap(DefaultBitmapEnum.s2gIcon.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_BANNER) {
			setImageBitmap(DefaultBitmapEnum.s2gBanner.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_NEWS_BANNER) {
			setImageBitmap(DefaultBitmapEnum.s2gNewsBanner.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_PORTRAIT) {
			setImageBitmap(null);
		} else if (imageType == ImageLoader.IMAGE_TYPE_BOOKS_BANNER) {
			setImageBitmap(DefaultBitmapEnum.s2gBookBanner.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_ACCOUNT_CENTER_BACK) {
			setImageBitmap(DefaultBitmapEnum.sDefaultAccountCenterBack
					.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_PREVIEW) {
			// 图片选择器进行大图预览的时候调用，这里不需要做处理,也不能设置null
		} else if (imageType == ImageLoader.IMAGE_TYPE_CIRCLE_NEWS) {
			setImageBitmap(DefaultBitmapEnum.sDefaultCircleIcon.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_NAVIGATION_AVT) {
			setImageBitmap(DefaultBitmapEnum.sDefaultNavigationAvt.getBitmap());
		} else if (imageType == ImageLoader.IMAGE_TYPE_COMENT_THUMBNAIL) {
			setImageBitmap(DefaultBitmapEnum.s2gIcon.getBitmap());
		} else {
			setImageBitmap(null);
		}
	}

	private Bitmap toRoundRectBitmap(int r, int rx, int ry, Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		RectF dst = new RectF(rx, ry, width - rx, height - ry);

		// path.computeBounds(dst, true);

		Rect src = new Rect(0, 0, width, height);

		Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		paint.setAntiAlias(true);// 设置画笔无锯齿

		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

		// 以下有两种方法画圆,drawRounRect和drawCircle
		canvas.drawRoundRect(dst, r, r, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
		// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, src, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle
		// bitmap.recycle();
		return output;
	}

	private Bitmap toMaskBitmap(Path path, Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		RectF dst = new RectF();
		path.computeBounds(dst, true);
		Rect src = new Rect(0, 0, width, height);

		Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		paint.setAntiAlias(true);// 设置画笔无锯齿

		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

		// 以下有两种方法画圆,drawRounRect和drawCircle
		canvas.drawPath(path, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
		// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle
		// bitmap.recycle();
		return output;
	}

	public Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;

			left = 0;
			top = 0;
			right = width;
			bottom = width;

			height = width;

			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;

			float clip = (width - height) / 2;

			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;

			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);// 设置画笔无锯齿

		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

		// 以下有两种方法画圆,drawRounRect和drawCircle
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
		// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle
		// bitmap.recycle();
		return output;
	}
}
