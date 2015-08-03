package kimxu.hhs.z_volley.toolbox.thread_pool;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ShortRunThreadPoolExecutor extends ThreadPoolExecutor {
	private static final int POOL_SIZE = 2;
	private static final int MAX_POOL_SIZE = 200;
	private static final int TASK_COUNT = 10;
	private static final int MAX_IDEL_TIME = 45;
	private static final RejectedExecutionHandler handler = new RejectedExecutionHandler() {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			//L.reportToServer("");
		}

	};

	// private static volatile ShortRunThreadPoolExecutor instance;

	// public static ShortRunThreadPoolExecutor getInstance(ThreadFactory
	// threadFactory){
	// if(instance==null){
	// synchronized(ShortRunThreadPoolExecutor.class){
	// instance=new ShortRunThreadPoolExecutor(threadFactory);
	// }
	// }
	// return instance;
	// }

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		// TODO Auto-generated method stub
		super.afterExecute(r, t);
	}

	ShortRunThreadPoolExecutor(ThreadFactory threadFactory) {
		super(POOL_SIZE, MAX_POOL_SIZE, MAX_IDEL_TIME, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(TASK_COUNT), threadFactory,
				handler);
		super.prestartAllCoreThreads();
		// TODO Auto-generated constructor stub
	}

}
