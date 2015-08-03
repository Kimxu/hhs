package kimxu.hhs.z_volley.http;


//import com.app.china.widget.view.img.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import kimxu.hhs.basic.SSHApplication;
import kimxu.hhs.utils.logger.Klog;
import kimxu.hhs.z_api.network.Host_URL;
import kimxu.hhs.z_base.AppConstant;
import kimxu.hhs.z_volley.AuthFailureError;
import kimxu.hhs.z_volley.Network;
import kimxu.hhs.z_volley.NetworkResponse;
import kimxu.hhs.z_volley.Request;
import kimxu.hhs.z_volley.RequestQueue;
import kimxu.hhs.z_volley.Response;
import kimxu.hhs.z_volley.VolleyError;
import kimxu.hhs.z_volley.base.AppCallback;
import kimxu.hhs.z_volley.structure.CollectionBuilder;
import kimxu.hhs.z_volley.toolbox.BasicNetwork;
import kimxu.hhs.z_volley.toolbox.DiskBasedCache;
import kimxu.hhs.z_volley.toolbox.HttpClientStack;
import kimxu.hhs.z_volley.toolbox.HttpHeaderParser;
import kimxu.hhs.z_volley.toolbox.HttpStack;
import kimxu.hhs.z_volley.toolbox.HurlStack;
import kimxu.hhs.z_volley.toolbox.requests.ImageRequest;
import kimxu.hhs.z_volley.toolbox.requests.NewApiArrRequest;
import kimxu.hhs.z_volley.toolbox.requests.NewApiRequest;
import kimxu.hhs.z_volley.toolbox.requests.NoRespondApiRequest;
import kimxu.hhs.z_volley.toolbox.requests.OldApiGetRequest;
import kimxu.hhs.z_volley.toolbox.requests.OldApiRequest;
import kimxu.hhs.z_volley.toolbox.requests.PushContentRequest;
import kimxu.hhs.z_volley.toolbox.requests.RawDataRequest;
import kimxu.hhs.z_volley.tools.StringHelper;
import kimxu.hhs.z_volley.tools.client.CommonHttpClient;

public abstract class RequestFactory {
	/** Default on-disk cache directory. */
	private static final String DEFAULT_CACHE_DIR = "volley";
	private static volatile AbstractNetworkQueue queue;
	private static volatile boolean needWebPDecode = true;

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it.
	 * 
	 * @param stack
	 *            An {@link HttpStack} to use for the network, or null for
	 *            default.
	 * @return A started {@link RequestQueue} instance.
	 */
	static AbstractNetworkQueue newRequestQueue(HttpStack stack) {
		Context context = SSHApplication.getAppContext();
		File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

		String userAgent = StringHelper.concat(AppConstant.packName, "/",
				AppConstant.versionCode);

		if (stack == null) {
			if (Build.VERSION.SDK_INT >= 9) {
				stack = new HurlStack();
			} else {
				stack = new HttpClientStack(
						CommonHttpClient.newInstance(userAgent));
			}
		}

		Network network = new BasicNetwork(stack);

		AbstractNetworkQueue queue = new RequestQueue(new DiskBasedCache(
				cacheDir), network);
		queue.start();

		return queue;
	}

	public static AbstractNetworkQueue getRequestQueue() {
		if (queue == null) {
			synchronized (RequestFactory.class) {
				queue = newRequestQueue(null);
			}
		}
		return queue;
	}

	public static boolean isNeedWebPDecode() {
		return needWebPDecode;
	}

	public static void setNeedWebPDecode(boolean needWebPDecode) {
		RequestFactory.needWebPDecode = needWebPDecode;
	}

	public static GenericRequest<String> getOldRequest(QueuedRequest qr) {
		return new OldApiRequest(qr);
	}

	public static GenericRequest<JSONObject> getOldGetRequest(QueuedRequest qr) {
		return new OldApiGetRequest(qr);
	}

	public static GenericRequest<Object> getNoRespondRequest(Host_URL url,
			List<NameValuePair> nameValuePairs) {
		Map<String, Object> param = CollectionBuilder.newHashMap();
		for (NameValuePair pair : nameValuePairs) {
			param.put(pair.getName(), pair.getValue());
		}
		return new NoRespondApiRequest(url.getUrl(null, null), param, null);
	}

	public static GenericRequest<String> getPushContentRequest(String url,
			byte[] out, final AppCallback onSucc) {
		return new PushContentRequest(Request.Method.POST, url, out, onSucc);
	}

	public static GenericRequest<String> getPushContentRequest(String url,
			File out, final AppCallback onSucc) throws IOException {
		return new PushContentRequest(Request.Method.POST, url, out, onSucc);
	}

	public static <T> GenericRequest<T> getNewRequest(Host_URL url,
			Map<String, Object> param, CommonResponceListener<T> listener,
			Class<T> clazz) {
		return new NewApiRequest<T>(url, param, listener, clazz);
	}

	public static <T> GenericRequest<T> getNewRequest(Host_URL url,
			Map<String, Object> param, CommonResponceListener<T> listener) {
		return new NewApiRequest<T>(url, param, listener);
	}

//	@SuppressWarnings("rawtypes")
//	public static GenericRequest<List> getNewRequest(Host_URL url,
//			Map<String, Object> param, CommonResponceListener<List> listener,
//			Type cType) {
//		return new NewApiArrayRequest(url, param, listener, cType);
//	}

	public static <T> GenericRequest<List<T>> getNewArrRequest(Host_URL url,
			Map<String, Object> param,
			CommonResponceListener<List<T>> listener, Class<T> cType) {
		return new NewApiArrRequest<T>(url, param, listener, cType);
	}

	public static GenericRequest<byte[]> getRawDataRequest(String url,
			CommonResponceListener<byte[]> listener) {
		return new RawDataRequest(url, listener);
	}



	public static GenericRequest<Bitmap> getImageRequset(String url){

		return  new ImageRequest(url,100,100,Bitmap.Config.RGB_565,new CommonResponceListener<Bitmap>(){

			@Override
			protected void handleResponse(Bitmap response, GenericRequest<?> request) {
				Klog.e("ok");
			}

			@Override
			protected void handleErr(VolleyError err, GenericRequest<?> request, NetResponseCode code, String content) {
				Klog.e("error");
			}
		});
	}


	public static GenericRequest<String> getCommonGetRequest(String url,
			final AppCallback onSucc) {
		return new Request<String>(Request.Method.GET, url,
				new CommonResponceListener<String>() {

					@Override
					protected void handleResponse(String response,
							GenericRequest<?> request) {
						if (onSucc != null) {
							onSucc.onCallBack(true, response);
						}
					}

					@Override
					protected void handleErr(VolleyError err,
							GenericRequest<?> request, NetResponseCode code,
							String content) {
						if (onSucc != null) {
							String msg = code.getMsg();
							if (err != null)
								onSucc.onCallBack(false, msg);
						}
					}
				}) {

			@Override
			protected Response<String> parseNetworkResponse(
					NetworkResponse response) {
				String parsed;
				try {
					parsed = new String(response.data,
							HttpHeaderParser.parseCharset(response.headers));
					// return parsed;
				} catch (UnsupportedEncodingException e) {
					parsed = new String(response.data);
				}
				return Response.success(parsed,
						HttpHeaderParser.parseCacheHeaders(response));
			}

		};
	}

	public static GenericRequest<String> getCommonPostRequest(String url,
			final String contentType, final byte[] content,
			final AppCallback onSucc) {
		return new Request<String>(Request.Method.POST, url,
				new CommonResponceListener<String>() {

					@Override
					protected void handleResponse(String response,
							GenericRequest<?> request) {
						if (onSucc != null) {
							onSucc.onCallBack(true, response);
						}
					}

					@Override
					protected void handleErr(VolleyError err,
							GenericRequest<?> request, NetResponseCode code,
							String content) {
						if (onSucc != null) {
							String msg = code.getMsg();
							if (err != null)
								onSucc.onCallBack(false, msg);
						}
					}
				}) {
			@Override
			public byte[] getBody() throws AuthFailureError {
				// TODO Auto-generated method stub
				return content;
			}

			@Override
			public String getBodyContentType() {
				// TODO Auto-generated method stub
				return contentType;
			}

			@Override
			protected Response<String> parseNetworkResponse(
					NetworkResponse response) {
				String parsed;
				try {
					parsed = new String(response.data,
							HttpHeaderParser.parseCharset(response.headers));
					// return parsed;
				} catch (UnsupportedEncodingException e) {
					parsed = new String(response.data);
				}
				return Response.success(parsed,
						HttpHeaderParser.parseCacheHeaders(response));
			}

		};
	}

	public static void stopTheQueue() {
		queue.stop();
//		ImageLoader.clean();
		queue = null;
	}
}
