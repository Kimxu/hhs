package kimxu.hhs.z_volley.toolbox.requests;


import java.util.Map;

import kimxu.hhs.z_volley.AuthFailureError;
import kimxu.hhs.z_volley.NetworkResponse;
import kimxu.hhs.z_volley.Request;
import kimxu.hhs.z_volley.Response;
import kimxu.hhs.z_volley.http.CommonParams;
import kimxu.hhs.z_volley.http.CommonResponceListener;
import kimxu.hhs.z_volley.http.GenericRequest;
import kimxu.hhs.z_volley.http.HttpRequestPriority;
import kimxu.hhs.z_volley.structure.CollectionBuilder;
import kimxu.hhs.z_volley.toolbox.HttpHeaderParser;
import kimxu.hhs.z_volley.tools.StringHelper;

public class NoRespondApiRequest extends Request<Object> {

	private static final String PROTOCOL_CHARSET = "utf-8";

	/** Content type for request. */
	private static final String PROTOCOL_CONTENT_TYPE = String.format(
			"application/x-www-form-urlencoded; charset=%s", PROTOCOL_CHARSET);
	/* private static */

	private final Map<String, Object> param;
	// private final Class<T> clazz;
	private final String url;

	// private final Type cType;

	public NoRespondApiRequest(String url, Map<String, Object> param,
			CommonResponceListener<Object> listener) {
		super(Request.Method.POST, url, listener);
		this.param = param;
		// this.clazz = clazz;
		this.url = url;
		// this.cType = null;
		this.setShouldCache(false);
		// this.flag = flag;
	}

	// public NewApiRequest(Host_URL url, Map<String, Object> param,
	// CommonResponceListener<T> listener, Type cType) {
	// super(Request.Method.POST, url.getUrl(Host_URL.HOST, null), listener);
	// this.param = param;
	// this.clazz = null;
	// this.url = url;
	// this.setShouldCache(false);
	// this.cType = cType;
	// }

	@Override
	public HttpRequestPriority getPriority() {
		return HttpRequestPriority.LOW;
	}

	@Override
	public GenericRequest<?> cloneNewRequest() {
		return new NoRespondApiRequest(url, param, listener);
	}

	@Override
	protected Response<Object> parseNetworkResponse(NetworkResponse response) {
		return (Response<Object>) Response.success(null,
				HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		String paramStr = null;
		try{
			paramStr = StringHelper.JsonHelper.toJson(this.param);
		}catch(VerifyError e){
			e.printStackTrace();
			android.util.Log.w("Exception", "转换参数时发生异常："+param!=null?param.toString():"没有参数");
		}
		Map<String, String> ret = CollectionBuilder.newHashMap();
		CommonParams.addCommonParams(ret);
		ret.put("param", paramStr!=null?paramStr:"");
		return ret;
	}

	@Override
	public String getBodyContentType() {
		return PROTOCOL_CONTENT_TYPE;
	}

}
