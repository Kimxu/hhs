package kimxu.hhs.z_volley.toolbox.thread;


import kimxu.hhs.z_volley.base.AppCallback;

public interface ControledExecutorService extends PausableExecutorService {
	public void setFilter(AppCallback callback);

	public void setFinishCallback(AppCallback callback);
}
