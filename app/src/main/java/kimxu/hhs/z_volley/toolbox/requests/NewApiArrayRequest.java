package kimxu.hhs.z_volley.toolbox.requests;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import kimxu.hhs.z_api.network.Host_URL;
import kimxu.hhs.z_volley.AuthFailureError;
import kimxu.hhs.z_volley.NetworkResponse;
import kimxu.hhs.z_volley.ParseError;
import kimxu.hhs.z_volley.Request;
import kimxu.hhs.z_volley.Response;
import kimxu.hhs.z_volley.http.CommonParams;
import kimxu.hhs.z_volley.http.CommonResponceListener;
import kimxu.hhs.z_volley.http.GenericRequest;
import kimxu.hhs.z_volley.http.HttpRequestPriority;
import kimxu.hhs.z_volley.structure.CollectionBuilder;
import kimxu.hhs.z_volley.toolbox.HttpHeaderParser;
import kimxu.hhs.z_volley.tools.StringHelper;

public class NewApiArrayRequest extends Request<List> {

	private static final String PROTOCOL_CHARSET = "utf-8";

	/** Content type for request. */
	private static final String PROTOCOL_CONTENT_TYPE = String.format(
			"application/x-www-form-urlencoded; charset=%s", PROTOCOL_CHARSET);
	/* private static */

	private final Map<String, Object> param;
	// private final Class<T> clazz;
	private final Host_URL url;
	private final Type cType;

	public NewApiArrayRequest(Host_URL url, Map<String, Object> param,
			CommonResponceListener<List> listener, Type cType) {
		super(Request.Method.POST, url.getUrl(Host_URL.HOST, null), listener);
		this.param = param;
		// this.clazz = clazz;
		this.url = url;
		this.cType = cType;
		this.setShouldCache(false);
		// this.flag = flag;
	}

	@Override
	public HttpRequestPriority getPriority() {
		return HttpRequestPriority.HIGH;
	}

	@Override
	public GenericRequest<?> cloneNewRequest() {

		return new NewApiArrayRequest(url, param, super.listener, cType);

	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Response<List> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		
		List ret = StringHelper.JsonHelper.fromJson(parsed, cType);
		try {
			return (Response<List>) Response.success(ret,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (Exception ex) {
			return Response.error(new ParseError(ex));
		}
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		String paramStr = StringHelper.JsonHelper.toJson(this.param);
		Map<String, String> ret = CollectionBuilder.newHashMap();
		CommonParams.addCommonParams(ret);
		ret.put("param", paramStr);
		return ret;
	}

	@Override
	public String getBodyContentType() {
		return PROTOCOL_CONTENT_TYPE;
	}

}
