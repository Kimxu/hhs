/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kimxu.hhs.z_volley.toolbox.thread_pool;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import kimxu.hhs.z_volley.structure.CollectionBuilder;
import kimxu.hhs.z_volley.toolbox.thread.CommonThreadFactory;
import kimxu.hhs.z_volley.toolbox.thread.CommonThreadGroup;
import kimxu.hhs.z_volley.toolbox.thread.ControledExecutorService;
import kimxu.hhs.z_volley.toolbox.thread.PausableExecutorService;


/**
 * 
 * @author ss
 */
public class CommonThreadPoolFactory {
	private static final String tag = "Worker_Thread_Group";
	private static final String LONG_POOL_NAME = "Long_Run_Exectutor";
	private static final String LONG_MUL_POOL_NAME = "Long_Run_MULTIPLE_Exectutor";
	private static final String CONTROLED_POOL_NAME = "CONTROLED_MULTIPLE_EXECUTOR";

	private static final CommonThreadGroup gp = new CommonThreadGroup(tag);
	// private static final ThreadFactory sThreadFactory = new
	// CommonThreadFactory(gp,"common");

	private static volatile ScheduledExecutorService threadPoolExecutor;
	// private static volatile ExecutorService longRunExecutor;
	private static volatile PausableExecutorService systemExecutor;
	private static volatile ExecutorService shortRunExecutor;
	private static final Map<String, PausableExecutorService> longExecutors = CollectionBuilder
			.newHashMap();
	private static final int LONG_RUN_THREAD_COUNT = 1;
	public static final int LONG_TASK_WAIT_CAPICITY = 25;
	public static final int LONT_TASK_CAPICITY = 70;

	// private static volatile ExecutorService singleExecutor;

	public static PausableExecutorService getSystemExecutor() {
		if (systemExecutor == null) {
			synchronized (CommonThreadPoolFactory.class) {
				ThreadFactory sThreadFactory = new CommonThreadFactory(gp,
						LONG_POOL_NAME);
				systemExecutor = new LongRunThreadPoolExecutor(sThreadFactory,
						1, 3);
			}
		}
		return systemExecutor;
	}

	public static final ScheduledExecutorService getDefaultExecutor() {
		if (threadPoolExecutor == null) {
			synchronized (CommonThreadPoolFactory.class) {
				ThreadFactory sThreadFactory = new CommonThreadFactory(gp,
						"common");
				// threadPoolExecutor=
				threadPoolExecutor = new CommonThreadPoolExector(sThreadFactory);
				// ScheduledThreadPoolExecutor ins =
				// (ScheduledThreadPoolExecutor) threadPoolExecutor;
				// ins.setKeepAliveTime(KEEP_ALIVE, TimeUnit.SECONDS);
				// ins.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
			}
		}
		return threadPoolExecutor;
	}

	public static final PausableExecutorService getLongRunExecutor(String name) {
		PausableExecutorService longRunExecutor;
		if (longExecutors.containsKey(name)) {
			longRunExecutor = longExecutors.get(name);
		} else {
			ThreadFactory sThreadFactory = new CommonThreadFactory(gp,
					LONG_POOL_NAME);
			longRunExecutor = new LongRunThreadPoolExecutor(sThreadFactory,
					LONG_RUN_THREAD_COUNT, LONG_TASK_WAIT_CAPICITY);
			longExecutors.put(name, longRunExecutor);
		}
		return longRunExecutor;
	}

	public static final PausableExecutorService getLongRunExecutor(int count,
			int capicity, String name) {
		PausableExecutorService longRunExecutor;
		if (longExecutors.containsKey(name)) {
			longRunExecutor = longExecutors.get(name);
		} else {
			ThreadFactory sThreadFactory = new CommonThreadFactory(gp,
					LONG_MUL_POOL_NAME);
			longRunExecutor = new LongRunThreadPoolExecutor(sThreadFactory,
					count, capicity);
			longExecutors.put(name, longRunExecutor);
		}
		return longRunExecutor;
		// longRunExecutor = Executors.newFixedThreadPool(
		// LONG_RUN_TASK_TYPE_COUNT, sThreadFactory);
		// ThreadPoolExecutor ex = (ThreadPoolExecutor) longRunExecutor;
		// ex.setRejectedExecutionHandler(new ReputHandler());

	}

	public static final ControledExecutorService getSelectiveExecutor(int count) {
		ThreadFactory sThreadFactory = new CommonThreadFactory(gp,
				CONTROLED_POOL_NAME);
		ControledExecutorService executor = new ControledThreadPoolExecutor(
				sThreadFactory, count);
		return executor;
	}

	public static final ExecutorService getShortRunExecutor() {
		if (shortRunExecutor == null) {
			synchronized (CommonThreadPoolFactory.class) {
				ThreadFactory sThreadFactory = new CommonThreadFactory(gp,
						"short_run");
				shortRunExecutor = new ShortRunThreadPoolExecutor(
						sThreadFactory);
			}
		}
		return shortRunExecutor;
	}

	// public static final ExecutorService getSingleExecutor(String name) {
	// ThreadFactory sThreadFactory = new CommonThreadFactory(gp, "long_run");
	// return Executors.newSingleThreadExecutor(sThreadFactory);
	// }

	public static CommonThreadGroup getThreadGroup() {
		return gp;
	}

	public static void shutDownAllPools() {
		if (shortRunExecutor != null) {
			shortRunExecutor.shutdown();
			shortRunExecutor = null;
		}
		if (threadPoolExecutor != null) {
			threadPoolExecutor.shutdown();
			threadPoolExecutor = null;
		}
		Collection<PausableExecutorService> pools = longExecutors.values();
		for (PausableExecutorService pool : pools) {
			pool.shutdown();
		}
		LongRunThreadPoolExecutor.shutDownAllPool();
		longExecutors.clear();
	}

	public static int shutDownLongRunPool(String name) {
		PausableExecutorService le;
		le = longExecutors.remove(name);
		if (le != null) {
			List<Runnable> runables = le.shutdownNow();
			return runables.size();
		}
		return -1;
	}
}
