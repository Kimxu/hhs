package kimxu.hhs.z_volley.http;


import kimxu.hhs.Config;
import kimxu.hhs.z_volley.base.GenericHelper;

public interface HttpRequestHelper extends GenericHelper {
	//TODO Volley HOST
	public static final String HOST = Config.getInstance().get("api.host")
			.toString();

	public int sendRequest(GenericRequest<?> request);

	public boolean cancelRequest(long seq);

	public GenericRequest<?> getRequest(long seq);

	public boolean isQueueEmpty();

	public int stop();

}