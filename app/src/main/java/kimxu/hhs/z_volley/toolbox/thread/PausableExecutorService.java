package kimxu.hhs.z_volley.toolbox.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public interface PausableExecutorService extends ExecutorService {
	public void pause();

	public void resume();

	public BlockingQueue<Runnable> getQueue();
}
