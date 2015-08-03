package kimxu.hhs.z_volley.toolbox.thread;


import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import kimxu.hhs.z_volley.toolbox.exception_handler.CommonUncatchedExceptionHandler;
import kimxu.hhs.z_volley.tools.StringHelper;

public final class CommonThreadFactory implements ThreadFactory {
	private final AtomicInteger mCount = new AtomicInteger(1);
	private final ThreadGroup gp;
	private final String tag;
	static final UncaughtExceptionHandler handler = new CommonUncatchedExceptionHandler();

	public CommonThreadFactory(ThreadGroup gp, String tag) {
		this.gp = gp;
		this.tag = tag;
	}

	public Thread newThread(Runnable r) {

		Thread th = new WorkerThread(gp, r, StringHelper.concat(tag, " # "
				+ mCount.getAndIncrement()));
		th.setUncaughtExceptionHandler(handler);
		return th;
	}
}