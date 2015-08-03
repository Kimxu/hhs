package kimxu.hhs.z_volley.toolbox.requests;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import kimxu.hhs.z_volley.AuthFailureError;
import kimxu.hhs.z_volley.NetworkResponse;
import kimxu.hhs.z_volley.Request;
import kimxu.hhs.z_volley.Response;
import kimxu.hhs.z_volley.VolleyError;
import kimxu.hhs.z_volley.base.AppCallback;
import kimxu.hhs.z_volley.http.CommonResponceListener;
import kimxu.hhs.z_volley.http.GenericRequest;
import kimxu.hhs.z_volley.http.NetResponseCode;
import kimxu.hhs.z_volley.toolbox.HttpHeaderParser;
import kimxu.hhs.z_volley.tools.FileHelper;

public final class PushContentRequest extends Request<String> {

	// File f;
	byte[] data;

	// AppCallback onSucc;

	public PushContentRequest(int method, String url, File out,
			final AppCallback onSucc) throws IOException {
		super(method, url, new CommonResponceListener<String>() {

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
		});
		File f = out;
		if (data == null && f != null && f.exists()) {
			data = FileHelper.readBinaryFile(f);
			if (data == null) {
				throw new IOException();
			}
		}
	}

	public PushContentRequest(int method, String url, byte[] out,
			final AppCallback onSucc) {
		super(method, url, new CommonResponceListener<String>() {

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
		});
		int n = out.length;
		//L.x("upload", "byte ", n);
		this.data = new byte[n];
		System.arraycopy(out, 0, this.data, 0, n);
		//L.x("upload", this.data.length);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed = parseStr(response.data, response.headers);

		return Response.success(parsed,
				HttpHeaderParser.parseCacheHeaders(response));
	}

	private String parseStr(byte[] data, Map<String, String> headers) {
		String parsed;
		try {
			parsed = new String(data, HttpHeaderParser.parseCharset(headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(data);
		}
		return parsed;
	}

	// @Override
	// protected void deliverResponse(String response) {
	// L.f("deliverResponse", response);
	// if (onSucc != null) {
	// onSucc.onCallBack(true, response);
	// }
	// }
	//
	// @Override
	// public void deliverError(VolleyError error) {
	// L.f("deliverError", error, error.getMessage());
	// if (onSucc != null) {
	// onSucc.onCallBack(false, error.getMessage());
	// }
	// }

	// @Override
	// public Request<?> cloneNewRequest() {
	// return new PushContentRequest(this.getMethod(), this.getUrl(), data);
	// }

	@Override
	public byte[] getBody() throws AuthFailureError {

		return data;
	}

	@Override
	public String getBodyContentType() {
		return "application/octet-stream;";
	}

}
