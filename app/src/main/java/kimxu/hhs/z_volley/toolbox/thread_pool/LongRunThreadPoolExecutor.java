package kimxu.hhs.z_volley.toolbox.thread_pool;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import kimxu.hhs.z_base.TaskRunnable;
import kimxu.hhs.z_volley.toolbox.thread.PausableExecutorService;

public final class LongRunThreadPoolExecutor extends ThreadPoolExecutor
		implements PausableExecutorService {
//	private static final List<LongRunThreadPoolExecutor> pools = new ArrayList<LongRunThreadPoolExecutor>(
//			17);
	// private static final int POOL_SIZE =
	// Application.RuntimeConstant.CONCURRENCY_ABILITY+1;
	private static final RejectedExecutionHandler handler = new RejectedExecutionHandler() {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			if (r instanceof TaskRunnable) {
				TaskRunnable task = (TaskRunnable) r;
				if (!task.cancel()) {
					//L.reportToServer("");
				}
			}
		}
	};

	LongRunThreadPoolExecutor(ThreadFactory threadFactory, int poolSize,
			int count) {
		super(poolSize, poolSize, 0, TimeUnit.NANOSECONDS,
				new LinkedBlockingQueue<Runnable>(count), threadFactory,
				handler);
		super.prestartCoreThread();
//		pools.add(this);
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
		// TODO Auto-generated method stub
		super.afterExecute(r, t);
	}

	@Override
	public BlockingQueue<Runnable> getQueue() {
		return super.getQueue();
	}

	@Override
	protected void terminated() {
		// TODO Auto-generated method stub
		super.terminated();
	}

	public static void shutDownAllPool() {
		
	}
}
