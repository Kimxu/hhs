package kimxu.hhs.z_volley.toolbox.thread;

import android.content.Context;

public class WorkerThread extends Thread {

	protected volatile boolean isShutDown;
	protected final long timeStamp;
	protected Context context;

	/**
	 * @param gp
	 * @param threadName
	 */
	protected WorkerThread(ThreadGroup gp, String threadName) {
		super(gp, threadName);
		this.timeStamp = System.currentTimeMillis();
		context = null;
	}

	public WorkerThread(ThreadGroup gp, Runnable runnable, String threadName) {
		super(gp, runnable, threadName);
		this.timeStamp = System.currentTimeMillis();
		context = null;
	}

	// @Override
	// public void run() {
	// super.run();
	// }

	public synchronized void shutDown() {
		this.isShutDown = true;
	}

	public final long getTimeStamp() {
		return timeStamp;
	}

}
