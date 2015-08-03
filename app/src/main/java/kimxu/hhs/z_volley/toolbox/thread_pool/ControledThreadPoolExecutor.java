package kimxu.hhs.z_volley.toolbox.thread_pool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import kimxu.hhs.z_volley.base.AppCallback;
import kimxu.hhs.z_volley.toolbox.thread.ControledExecutorService;

public final class ControledThreadPoolExecutor extends ThreadPoolExecutor
		implements ControledExecutorService {
	private static final RejectedExecutionHandler handler = new RejectedExecutionHandler() {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

		}
	};
	private AppCallback filterCallback;
	private AppCallback finsishCallback;

	ControledThreadPoolExecutor(ThreadFactory threadFactory, int poolSize) {
		super(poolSize, poolSize, 0, TimeUnit.NANOSECONDS,
				new SynchronousQueue<Runnable>(), threadFactory, handler);
		super.prestartCoreThread();
	}

	// @Override
	// protected void beforeExecute(Thread t, Runnable r) {
	//
	// }

	@Override
	public void execute(Runnable command) {
		if (filterCallback == null || filterCallback.onCallBack(command) > 0)
			super.execute(command);
	}

	private boolean isPaused;
	private ReentrantLock pauseLock = new ReentrantLock();
	private Condition unpaused = pauseLock.newCondition();

	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		pauseLock.lock();
		try {
			while (isPaused)
				unpaused.await();
		} catch (InterruptedException ie) {
			t.interrupt();
		} finally {
			pauseLock.unlock();
		}
	}

	public void pause() {
		pauseLock.lock();
		try {
			isPaused = true;
		} finally {
			pauseLock.unlock();
		}
	}

	public void resume() {
		pauseLock.lock();
		try {
			isPaused = false;
			unpaused.signalAll();
		} finally {
			pauseLock.unlock();
		}
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		if (finsishCallback != null)
			finsishCallback.onCallBack(r);
		super.afterExecute(r, t);

	}

	@Override
	public void setFilter(AppCallback callback) {
		this.filterCallback = callback;
	}

	@Override
	public void setFinishCallback(AppCallback callback) {
		this.finsishCallback = callback;
	}

}
