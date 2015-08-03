package kimxu.hhs.z_volley.http;

import android.os.Handler;
import android.os.Looper;

import kimxu.hhs.z_volley.Response;
import kimxu.hhs.z_volley.VolleyError;


public abstract class CommonResponceListener<T> extends CommonErrListener
		implements Response.Listener<T> {

	static volatile Handler mainHandler;
	private final boolean inUi;
	private Response.Listener<T> ts;
	private Response.ErrorListener es;

	public CommonResponceListener(boolean inUi) {
		if (mainHandler == null) {
			synchronized (CommonResponceListener.class) {
				mainHandler = new Handler(Looper.getMainLooper());
			}
		}
		this.inUi = inUi;
		// this.lsRegistered = false;
	}

	public CommonResponceListener() {
		this(true);
	}

	// public CommonResponceListener(ErrorListener es, Listener<T> ls) {
	// this.inUi = true;
	// this.lsRegistered = true;
	// this.es = es;
	// this.ts = ls;
	// }

	// @Override
	// protected void handleErr(long id, NetResponseCode code, String content) {
	//
	// }

	@Override
	public final void onResponse(final T response,
			final GenericRequest<?> request) {
		// if (lsRegistered) {
		if (inUi) {
			mainHandler.post(new Runnable() {

				@Override
				public void run() {
					handleResponse(response, request);
				}

			});
		} else {
			handleResponse(response, request);
		}
		// } else {
		// ts.onResponse(response, request);
		// }
	}

	@Override
	public final void onErrorResponse(VolleyError error,
			GenericRequest<?> request) {
		// if (!lsRegistered) {
		super.onErrorResponse(error, request);
		// } else {
		// es.onErrorResponse(error, request);
		// }
	}

	protected abstract void handleResponse(T response, GenericRequest<?> request);

	@Override
	protected abstract void handleErr( VolleyError err, GenericRequest<?> request,
			NetResponseCode code, String content);
}
