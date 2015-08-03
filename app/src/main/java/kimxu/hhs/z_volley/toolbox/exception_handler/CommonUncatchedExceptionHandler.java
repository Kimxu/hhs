package kimxu.hhs.z_volley.toolbox.exception_handler;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

import kimxu.hhs.z_volley.base.AppCallback;
import kimxu.hhs.z_volley.structure.CollectionBuilder;


public class CommonUncatchedExceptionHandler implements
		UncaughtExceptionHandler {

	List<AppCallback> onErr;

	public CommonUncatchedExceptionHandler() {
		onErr = CollectionBuilder.newArrayList();
	}

	public void addOnErr(AppCallback onErr) {
		this.onErr.add(onErr);
	}

	protected UncaughtExceptionHandler defaultHandler = Thread
			.getDefaultUncaughtExceptionHandler();

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		// L.x(arg1);
		if (onErr != null && onErr.size() > 0) {
			for (AppCallback c : onErr)
				c.onCallBack(arg0, arg1);
		}

		//L.reportToServer(arg0, arg1);
		defaultHandler.uncaughtException(arg0, arg1);
	}

}
