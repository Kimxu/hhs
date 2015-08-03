package kimxu.hhs.z_volley.toolbox.thread_pool;


import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kimxu.hhs.z_base.RuntimeConstant;

public class CommonThreadPoolExector extends ScheduledThreadPoolExecutor {
//	private static final int CORE_POOL_SIZE = RuntimeConstant.CONCURRENCY_ABILITY * 2;
	private static final int CORE_POOL_SIZE = RuntimeConstant.CONCURRENCY_ABILITY + 1;
	private static final int MAXIMUM_POOL_SIZE = RuntimeConstant.CONCURRENCY_ABILITY * 2 + 1;
	private static final int KEEP_ALIVE = 1;
	// private static final int LONG_RUN_TASK_TYPE_COUNT = 5;
//	private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE * 8;
//	private static final int KEEP_ALIVE = 2;
	private static final RejectedExecutionHandler handler = new RejectedExecutionHandler() {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

		}

	};

	CommonThreadPoolExector(ThreadFactory threadFactory) {
		super(CORE_POOL_SIZE, threadFactory, handler);
		super.setKeepAliveTime(KEEP_ALIVE, TimeUnit.SECONDS);
		super.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
	}

	CommonThreadPoolExector(int corePoolSize, int maxSize,
			int keepAliveSeconds, ThreadFactory threadFactory,
			RejectedExecutionHandler handler) {
		super(corePoolSize, threadFactory, handler);
		super.setKeepAliveTime(keepAliveSeconds, TimeUnit.SECONDS);
		super.setMaximumPoolSize(maxSize);
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		// TODO Auto-generated method stub
		super.afterExecute(r, t);
	}

	@Override
	protected void terminated() {
		super.terminated();
	}

}
