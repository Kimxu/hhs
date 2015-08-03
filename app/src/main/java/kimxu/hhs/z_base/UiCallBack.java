package kimxu.hhs.z_base;

import android.os.Handler;
import android.os.Looper;


import kimxu.hhs.z_volley.base.AppCallback;

public abstract class UiCallBack implements AppCallback {
	// boolean ui;
	protected final Handler mainThreadHandler;

	public UiCallBack() {
		mainThreadHandler = new Handler(Looper.getMainLooper());
	}

	@Override
	public int onCallBack(final Object... args) {
		mainThreadHandler.post(new Runnable() {

			@Override
			public void run() {
				try {
					runInUi(args);
				} catch (NullPointerException ex) {
					//L.reportToServer(Thread.currentThread(), ex);
				}
			}

		});
		return 0;
	}

	protected abstract int runInUi(final Object... args);
}
