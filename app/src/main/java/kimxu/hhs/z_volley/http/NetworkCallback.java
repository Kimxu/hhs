package kimxu.hhs.z_volley.http;


import kimxu.hhs.z_base.UiCallBack;
import kimxu.hhs.z_volley.base.AppCallback;

public abstract class NetworkCallback extends UiCallBack implements AppCallback {
	private boolean runInUi;

	public NetworkCallback() {
		runInUi = true;
	}

	public NetworkCallback(boolean flag) {
		runInUi = flag;
	}

	@Override
	protected int runInUi(Object... args) {
		return 0;
	}

	public abstract int OnNetworkReturned(int id, NetResponseCode code,
			Object result, GenericRequest<?> request, boolean refresh);

	// public abstract int OnNetworkReturned(int id, NetworkResponse code);

	@SuppressWarnings("unchecked")
	@Override
	public int onCallBack(final Object... args) {
		try {
			if (!runInUi) {
				if (args != null && args.length == 5) {
					return this.OnNetworkReturned(
							Integer.parseInt(args[0].toString()),
							(NetResponseCode) args[1], args[2],
							(GenericRequest<?>) args[3], (Boolean) args[4]);
				}
			} else {
				if (args != null && args.length == 5) {
					super.mainThreadHandler.post(new Runnable() {

						@Override
						public void run() {
							OnNetworkReturned(
									Integer.parseInt(args[0].toString()),
									(NetResponseCode) args[1], args[2],
									(GenericRequest<?>) args[3],
									(Boolean) args[4]);
						}
					});
					return 0;
				}

			}
		} catch (ClassCastException ex) {
			//L.x(ex);
		}
		return -1;
	}
}
