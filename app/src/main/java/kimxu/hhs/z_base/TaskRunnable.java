package kimxu.hhs.z_base;

import java.util.concurrent.Future;

import kimxu.hhs.z_volley.base.AppCallback;

public interface TaskRunnable extends Runnable {
	public boolean setCallBack(AppCallback callback);

	public boolean pause();

	public boolean resume();

	public boolean cancel();
	
	public boolean fin();

	public void initData(Object... args);

	public long getId();
	
	public void setFuture(Future<?> f);
}
