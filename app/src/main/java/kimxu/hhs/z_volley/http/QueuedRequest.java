package kimxu.hhs.z_volley.http;

import java.util.List;

import org.apache.http.NameValuePair;

import android.os.Handler;

public class QueuedRequest {
	public static final int requestTypeApi = 1;
	public static final int requestTypeLog = 3;
	public static final int requestTypeGet = 4;
	public int tag;
	public int responseHttpCode;
	public int requestType;
	public int requestId;
	public int applicationId;
	public String packageName;
	public String url;
	public List<NameValuePair> nameValuePairs;
	public Handler handler;
	public Object result;
	public int retry = 0;
	public boolean checkAvailable = true;

	// record log name
	public String logName;
	public byte[] logContent;
	
	//解决请求属性问题（临时变量）
	public int currentRequestTag;
}