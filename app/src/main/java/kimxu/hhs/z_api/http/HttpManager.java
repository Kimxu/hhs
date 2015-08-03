package kimxu.hhs.z_api.http;

import kimxu.hhs.z_base.AbstractManager;
import kimxu.hhs.z_volley.Request;
import kimxu.hhs.z_volley.RequestQueue;
import kimxu.hhs.z_volley.http.GenericRequest;
import kimxu.hhs.z_volley.http.HttpRequestHelper;
import kimxu.hhs.z_volley.http.RequestFactory;

public final class HttpManager extends AbstractManager implements
		HttpRequestHelper {

	private static volatile HttpManager instance;

	public static HttpManager getInstance() {
		if (instance == null) {
			synchronized (HttpManager.class) {
				instance = new HttpManager();
			}
		}
		return instance;
	}

	// private volatile RequestQueue queue;

	private HttpManager() {
		// queue = (RequestQueue) RequestFactory.getRequestQueue();
	}

	// @Override
	// public Context getContext() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public int sendRequest(GenericRequest<?> request) {

		RequestQueue queue = (RequestQueue) RequestFactory.getRequestQueue();
		queue.add((Request<?>) request);
		// if (AppConstant.isDebug)
		// L.x("http queue", request.getUrl(), request.getId(),
		// request.getDebugBody());
		return (int) request.getId();
	}

	@Override
	public boolean cancelRequest(long seq) {
		RequestQueue queue = (RequestQueue) RequestFactory.getRequestQueue();
		Request<?> req = queue.getRequest(seq);
		if (req != null) {
			req.cancel();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public GenericRequest<?> getRequest(long seq) {
		RequestQueue queue = (RequestQueue) RequestFactory.getRequestQueue();
		return queue.getRequest(seq);
	}

	@Override
	public boolean isQueueEmpty() {
		RequestQueue queue = (RequestQueue) RequestFactory.getRequestQueue();
		return queue.isEmpty();
	}

	@Override
	public int stop() {
		// RequestQueue queue = (RequestQueue) RequestFactory.getRequestQueue();
		// queue.stop();
		//L.x("quit app", "stop the request queue");
		RequestFactory.stopTheQueue();
		return 0;
	}

}
