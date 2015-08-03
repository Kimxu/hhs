package kimxu.hhs.online;

/**
 * Created by xuzhiguo on 15/7/30.
 */

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import kimxu.hhs.Config;
import kimxu.hhs.utils.DeviceUtil;
import kimxu.hhs.utils.GlobalUtil;
import kimxu.hhs.utils.PreferenceUtil;
import kimxu.hhs.utils.json.JsonParams;


/**
 * 网络连接服务
 */
public class HttpService {

    private static HttpService httpService;
    private HttpManager httpManager;
    private Context mContext;
    public static String API = "market.MarketAPI";
    public static final AtomicInteger seqMaker = new AtomicInteger(0);

    public static HttpService getInstance(Context context) {
        if (httpService == null) {
            httpService = new HttpService(context.getApplicationContext());
        }
        return httpService;
    }

    private HttpService(Context context) {
        mContext = context;
        httpManager = HttpManager.getInstance(context);
    }

    public void getDownloadLog(final String packageName,
                               final String packageSize, final String versionCode,
                               final String networkType, final String speed, final String url,
                               final String time, final String guid, final String md5,
                               final String ip, final String finalUrl,final int yyhVersionCode) {
        //转换josn
        final String params = JsonParams.getDownloadLogParams(packageName,
                packageSize, versionCode, networkType, speed, url, time, guid,
                md5, ip, finalUrl, yyhVersionCode);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("param", params));

        httpManager.addDebugDownloadRequest(nameValuePairs);
    }

    /**
     *  主页banner
     * @param notifyId
     * @param handler
     */
    public void getAppShowList(int notifyId,Handler handler) {



        httpManager.addHttpsApiRequest(null,notifyId, handler);
    }
    /**
     * 注册新用户
     *
     * @param userId
     * @param password
     * @param notifyId
     * @param handler
     */
    public void registerNewAccount(final String captchaType,
                                   final String userId, final String nickname, final String password,
                                   final String captcha, final int notifyId, Handler handler) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        appendUriParamater(nameValuePairs);

        final String params = JsonParams.getRegisterParams(mContext,
                captchaType, userId, nickname, password, captcha);

        nameValuePairs.add(new BasicNameValuePair("param", params));

        httpManager.addHttpsApiRequest(nameValuePairs, notifyId, handler);
    }

    /**
     * 用户登录
     *
     * @param password
     * @param notifyId
     * @param handler
     */
    public void accountLogin(final String loginName, final String password,
                             final int notifyId, Handler handler) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        appendUriParamater(nameValuePairs);

        final String params = JsonParams.getLoginParams(mContext, loginName,
                password);

        nameValuePairs.add(new BasicNameValuePair("param", params));

        httpManager.addHttpsApiRequest(nameValuePairs, notifyId, handler);
    }

    private void appendUriParamater(List<NameValuePair> nameValuePairs) {
        nameValuePairs.add(new BasicNameValuePair("key", ""));
        ArrayList<BasicNameValuePair> addParams = GlobalUtil
                .parseNameValuePair(PreferenceUtil.getString(this.mContext,
                        Config.ADD_PARAM, ""));
        nameValuePairs.addAll(addParams);
        nameValuePairs.add(new BasicNameValuePair("referer", GlobalUtil
                .getUUIDString(mContext)));
        nameValuePairs.add(new BasicNameValuePair("api", API));
        nameValuePairs.add(new BasicNameValuePair("deviceId", DeviceUtil
                .getDeviceId(mContext)));
    }



    /**
     * 上传头像
     *
     * @param ticket
     *            唯一标识
     * @param smallB64
     *            小图片的base64
     * @param bigB64
     *            大图片的base64
     * @param notifyID
     *            通知id
     * @param handler
     */
    public void uploadHeadPicture(String ticket, String smallB64,
                                  String bigB64, final int notifyID, Handler handler) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        appendUriParamater(nameValuePairs);

        final String params = JsonParams.getParamsForHeadPictureUpload(
                mContext, smallB64, bigB64, ticket);
        nameValuePairs.add(new BasicNameValuePair("param", params));
        Log.i("json", nameValuePairs.toString());
        httpManager.addHttpsApiRequest(nameValuePairs, notifyID, handler);
    }





}

