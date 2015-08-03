package kimxu.hhs.z_volley.http;


public abstract class GenericRequest<T> {

	/**
	 * Returns this request's tag.
	 * 
	 * @see Request#setTag(Object)
	 */
	public abstract Object getTag();

	/**
	 * Returns the URL of this request.
	 */
	public abstract String getUrl();

	public abstract String getOriginUrl();

	/**
	 * Mark this request as canceled. No callback will be delivered.
	 */
	public abstract void cancel();

	/**
	 * Returns true if this request has been canceled.
	 */
	public abstract boolean isCanceled();

	public abstract String getBodyContentType();

	public abstract GenericRequest<?> cloneNewRequest();

	public abstract long getId();

	public abstract void dumpLifeCycle();

	public abstract GenericRequest<?> setShouldCache(boolean flag);

	public abstract String getDebugBody() ;

}