package kimxu.hhs.z_volley.toolbox.requests;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

import kimxu.hhs.z_volley.AuthFailureError;
import kimxu.hhs.z_volley.NetworkResponse;
import kimxu.hhs.z_volley.ParseError;
import kimxu.hhs.z_volley.Request;
import kimxu.hhs.z_volley.Response;
import kimxu.hhs.z_volley.VolleyError;
import kimxu.hhs.z_volley.http.CommonResponceListener;
import kimxu.hhs.z_volley.http.GenericRequest;
import kimxu.hhs.z_volley.http.HttpRequestPriority;
import kimxu.hhs.z_volley.http.NetResponseCode;
import kimxu.hhs.z_volley.http.QueuedRequest;
import kimxu.hhs.z_volley.toolbox.HttpHeaderParser;

public class OldApiGetRequest extends Request<JSONObject> {

	private static final String PROTOCOL_CHARSET = "utf-8";

	/** Content type for request. */
	private static final String PROTOCOL_CONTENT_TYPE = String.format(
			"application/json; charset=%s", PROTOCOL_CHARSET);
	/* private static */
	public static final int MSGCODE_HTTP_ERROR = 0;
	public static final int MSGCODE_HTTP_RESPONSE = 1;

//	private final Map<String, String> param;
	// private final Class<T> clazz;
	// private final Host_URL url;
	private final QueuedRequest qr;

	public OldApiGetRequest(final QueuedRequest qr) {
		super(Request.Method.GET, qr.url,
				new CommonResponceListener<JSONObject>(false) {

					@Override
					protected void handleResponse(JSONObject response,
							GenericRequest<?> request) {
						qr.responseHttpCode = 200;
						if (QueuedRequest.requestTypeLog == qr.requestType) {
							qr.result = 1;
						} else {
							qr.result = response;
						}
						if (qr.handler != null) {
							qr.handler.sendMessage(qr.handler.obtainMessage(
									MSGCODE_HTTP_RESPONSE, qr));
						}
					}

					@Override
					protected void handleErr(VolleyError err,
							GenericRequest<?> request, NetResponseCode code,
							String content) {
						qr.responseHttpCode = code.getStatus();
						qr.result = 2;
						if (qr.handler != null) {
							qr.handler.sendMessage(qr.handler.obtainMessage(
									MSGCODE_HTTP_ERROR, qr));
						}
					}
				});
		this.qr = qr;
//		param = CollectionBuilder.newHashMap();
//		for (NameValuePair pair : qr.nameValuePairs) {
//			param.put(pair.getName(), pair.getValue());
//		}
		this.setShouldCache(false);
	}

	@Override
	public HttpRequestPriority getPriority() {
		return HttpRequestPriority.HIGH;
	}

	@Override
	public GenericRequest<?> cloneNewRequest() {
		return new OldApiGetRequest(qr);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	// @Override
	// public byte[] getBody() throws AuthFailureError {
	// try {
	// UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
	// rq.nameValuePairs, PROTOCOL_CHARSET);
	// formEntity.
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// // TODO Auto-generated method stub
	// return super.getBody();
	// }

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// String paramStr = StringHelper.JsonHelper.toJson(this.param);
		// Map<String, String> ret = CollectionBuilder.newHashMap();
		// CommonParams.addCommonParams(ret);
		// ret.put("param", paramStr);
		return Collections.EMPTY_MAP;
	}

	@Override
	public String getBodyContentType() {
		return PROTOCOL_CONTENT_TYPE;
	}

}
