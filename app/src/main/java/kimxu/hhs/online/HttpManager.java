package kimxu.hhs.online;

import android.content.Context;
import android.os.Handler;

import org.apache.http.NameValuePair;

import java.util.List;

import kimxu.hhs.z_api.Api;
import kimxu.hhs.z_api.network.Host_URL;
import kimxu.hhs.z_volley.http.HttpRequestHelper;
import kimxu.hhs.z_volley.http.QueuedRequest;
import kimxu.hhs.z_volley.http.RequestFactory;

/**
 * Created by xuzhiguo on 15/7/30.
 */




public class HttpManager {

    public static String HOST_URI = "http://mobile.appchina.com/market/";
    public static String HOST_API_URL_HTTPS = "http://mobile.appchina.com/market/api";

    public static String HOST_API_URI;


    public static final int MSGCODE_HTTP_ERROR = 0;
    public static final int MSGCODE_HTTP_RESPONSE = 1;


    private static HttpManager httpManager;

    private Context mContext;


    public static void initUrl() {
       // HOST_API_URI = HOST_URI + "api";
        HOST_API_URI="http://pica.nipic.com/2007-11-09/2007119124413448_2.jpg";
        HOST_API_URL_HTTPS="http://pica.nipic.com/2007-11-09/2007119124413448_2.jpg";
    }

    private HttpManager(Context context) {
        mContext = context;
        initUrl();
    }

    public static HttpManager getInstance(Context context) {
        synchronized (HttpManager.class) {
            if (httpManager == null) {
                httpManager = new HttpManager(context);
            }
        }
        return httpManager;
    }

    /**
     * post 请求方式
     * @param qr 请求参数
     */
    private void download(final QueuedRequest qr) {
        if (qr == null) {
            return;
        }
        HttpRequestHelper hh = (HttpRequestHelper) Api.http.getHandler();
        hh.sendRequest(RequestFactory.getOldRequest(qr));
    }

    /**
     * get请求方式
     * @param qr 请求参数
     */
    private void getDownload(final QueuedRequest qr) {
        if (qr == null) {
            return;
        }
        HttpRequestHelper hh = (HttpRequestHelper) Api.http.getHandler();
        hh.sendRequest(RequestFactory.getOldGetRequest(qr));

    }

    /**
     * 图片请求方式
     * @param qr 请求参数
     */
    private void getImageDownload(final QueuedRequest qr) {
        if (qr == null) {
            return;
        }
        HttpRequestHelper hh = (HttpRequestHelper) Api.http.getHandler();
        hh.sendRequest(RequestFactory.getImageRequset(qr.url));

    }


    public void addDebugDownloadRequest(List<NameValuePair> nameValuePairs) {
        HttpRequestHelper hh = (HttpRequestHelper) Api.http.getHandler();
        hh.sendRequest(RequestFactory.getNoRespondRequest(
                Host_URL.download_debug, nameValuePairs));


    }

    public void addApiRequest(List<NameValuePair> nameValuePairs) {
        // Log.i("http_result", "Add request");
        QueuedRequest qr = new QueuedRequest();
        qr.requestType = QueuedRequest.requestTypeApi;
        qr.url = HOST_API_URI;
        qr.nameValuePairs = nameValuePairs;

        download(qr);
    }

    // 用户账户，评论等需要使用https的相关API
    public void addHttpsApiRequest(List<NameValuePair> nameValuePairs,
                                   int requestId, Handler handler) {
        QueuedRequest qr = new QueuedRequest();
        qr.requestType = QueuedRequest.requestTypeApi;
        qr.requestId = requestId;
        qr.url = HOST_API_URL_HTTPS;
        qr.nameValuePairs = nameValuePairs;
        qr.handler = handler;
        getDownload(qr);
    }

    public void addApiRequest(List<NameValuePair> nameValuePairs,
                              int requestId, Handler handler) {
        // Log.i("http_result", "Add request");
        QueuedRequest qr = new QueuedRequest();
        qr.requestType = QueuedRequest.requestTypeApi;
        qr.requestId = requestId;
        qr.url = HOST_API_URI;
        qr.nameValuePairs = nameValuePairs;
        qr.handler = handler;
        download(qr);
    }







}

