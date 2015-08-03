/*
 * Copyright (C) 2011 The Android Open Source Project
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

package kimxu.hhs.z_volley.toolbox.requests;


import kimxu.hhs.z_volley.DefaultRetryPolicy;
import kimxu.hhs.z_volley.NetworkResponse;
import kimxu.hhs.z_volley.ParseError;
import kimxu.hhs.z_volley.Request;
import kimxu.hhs.z_volley.Response;
import kimxu.hhs.z_volley.http.CommonResponceListener;
import kimxu.hhs.z_volley.http.HttpRequestPriority;
import kimxu.hhs.z_volley.toolbox.HttpHeaderParser;

/**
 * A canned request for getting an image at a given URL and calling back with a
 * decoded Bitmap.
 */
public class RawDataRequest extends Request<byte[]> {
	/** Socket timeout in milliseconds for image requests */
	private static final int DATA_TIMEOUT_MS = 10000;

	/** Default number of retries for image requests */
	private static final int DATA_MAX_RETRIES = 3;

	/** Default backoff multiplier for image requests */
	private static final float DATA_BACKOFF_MULT = 2f;

	// private final Response.Listener<Bitmap> mListener;
	// protected final Config mDecodeConfig;
	// protected final int mMaxWidth;
	// protected final int mMaxHeight;

	/**
	 * Decoding lock so that we don't decode more than one image at a time (to
	 * avoid OOM's)
	 */
	// private static final Object sDecodeLock = new Object();

	/**
	 * Creates a new image request, decoding to a maximum specified width and
	 * height. If both width and height are zero, the image will be decoded to
	 * its natural size. If one of the two is nonzero, that dimension will be
	 * clamped and the other one will be set to preserve the image's aspect
	 * ratio. If both width and height are nonzero, the image will be decoded to
	 * be fit in the rectangle of dimensions width x height while keeping its
	 * aspect ratio.
	 * 
	 * @param url
	 *            URL of the image
	 * @param ls

	 *            Error listener, or null to ignore errors
	 */
	public RawDataRequest(String url, final CommonResponceListener<byte[]> ls) {
		super(Method.GET, url, ls);
		// super.setListener();
		setRetryPolicy(new DefaultRetryPolicy(DATA_TIMEOUT_MS,
				DATA_MAX_RETRIES, DATA_BACKOFF_MULT));
		// mListener = listener;

	}

	@Override
	public HttpRequestPriority getPriority() {
		return HttpRequestPriority.NORMAL;
	}

	@Override
	protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
		// Serialize all decode on a global lock to reduce concurrent heap
		// usage.
		// synchronized (sDecodeLock) {
		try {
			byte[] data = response.data;
			return Response.success(data,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (OutOfMemoryError e) {
			//L.e("Caught OOM for %d byte image, url=%s", response.data.length,
				//	getUrl());
			return Response.error(new ParseError(e));
		}
		// }
	}

}
