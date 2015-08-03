package kimxu.hhs.z_volley.http;

import kimxu.hhs.z_api.Api;
import kimxu.hhs.z_api.device.DisplayHelper;
import kimxu.hhs.z_volley.AuthFailureError;
import kimxu.hhs.z_volley.NetworkError;
import kimxu.hhs.z_volley.NetworkResponse;
import kimxu.hhs.z_volley.NoConnectionError;
import kimxu.hhs.z_volley.ParseError;
import kimxu.hhs.z_volley.Response;
import kimxu.hhs.z_volley.ServerError;
import kimxu.hhs.z_volley.TimeoutError;
import kimxu.hhs.z_volley.TooMuchProcessing;
import kimxu.hhs.z_volley.VolleyError;

public class CommonErrListener implements Response.ErrorListener {
	// private final long id;
	DisplayHelper dh = (DisplayHelper) Api.display.getHandler();

	public CommonErrListener() {
		// this.id = id;
	}

	@Override
	public void onErrorResponse(VolleyError error, GenericRequest<?> request) {
		NetworkResponse rep = error.networkResponse;
		NetResponseCode code = null;
		/*
		 * L.x("http err", "called on error in common linstener",
		 * request.getUrl());
		 */
		if (rep != null) {
			code = NetResponseCode.toStatus(rep.statusCode);
			//L.x("http err", "send err to callback", code);
			handleErr(error, request, code, new String(rep.data));
		} else {
			//L.x("http err:", error.getCause(), error.getMessage(),
					error.toString();
			if (error instanceof ServerError) {
				code = NetResponseCode.server_error;
			} else if (error instanceof NoConnectionError) {
				code = NetResponseCode.no_reponse;
				// L.x(error);
			} else if (error instanceof ParseError) {
				// image not decode successfully
				// L.x(error);
				code = NetResponseCode.unable_to_resolve;
			} else if (error instanceof TimeoutError) {
				// Request<?> req=resourceQueue.get(this.id);
				// if(req!=null ){
				// Cache.Entry ce=req.getCacheEntry();
				// if(ce!=null){
				//
				// }
				// }
				// should not happen
				code = NetResponseCode.time_out;
			} else if (error instanceof AuthFailureError) {
				// should not happen
				code = NetResponseCode.forbidden;
			} else if (error instanceof NetworkError) {
				code = NetResponseCode.bad_request;
			} else if (error instanceof TooMuchProcessing) {
				code = NetResponseCode.too_much_processing;
			} else {
				// other exeception that cause the problem
				code = NetResponseCode.undefined;
			}
			//L.x("http err", "send err to callback", code);
			handleErr(error, request, code, null);
		}
		//L.x("http err", "in common listener ", code);
	}

	protected void handleErr(VolleyError error, GenericRequest<?> request,
			NetResponseCode code, String content) {
		dh.showToast("有请求失败，原因：" + code.getMsg());
	}

}