package kimxu.hhs.z_volley.http;

public interface AbstractNetworkQueue {

	/**
	 * Starts the dispatchers in this queue.
	 */
	public abstract void start();

	/**
	 * Stops the cache and network dispatchers.
	 */
	public abstract void stop();

	public abstract int getRequestCount();

	public abstract boolean isEmpty();

}