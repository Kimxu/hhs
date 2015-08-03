package kimxu.hhs.utils.json;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xuzhiguo on 15/7/31.
 */
public class JsonParams {
    public static String getDownloadLogParams(String packageName,
                                              String packageSize, String versionCode, String networkType,
                                              String speed, String url, String time, String guid, String md5,
                                              String ip, String finalUrl, int yyhVersionCode) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
            jsonObject.put("packagename", packageName);
            jsonObject.put("size", packageSize);
            jsonObject.put("versionCode", versionCode);
            jsonObject.put("ipAddress", "");
            jsonObject.put("networkType", networkType);
            jsonObject.put("downloadStart", "");
            jsonObject.put("downloadEnd", time);
            jsonObject.put("md5", md5);
            jsonObject.put("averSpeed", speed);
            jsonObject.put("url", url);
            jsonObject.put("finalIp", ip);
            jsonObject.put("finalUrl", finalUrl);
            jsonObject.put("yyhVersionCode", yyhVersionCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 主页banner参数设置
     * @param context
     * @param showPlace
     * @param distinctId
     * @param version
     * @param start
     * @param size
     * @param forCache
     * @param imageType
     * @return
     */
    public static String getAppShowListParams(Context context,
                                              String showPlace, int distinctId, int version, int start, int size,
                                              boolean forCache, int imageType) {
        JSONObject jsonObject = new JSONObject();
        try {
//            appendBasicParamaterV5(context, jsonObject);
//            jsonObject.put(LABEL_TYPE, MarketConstants.API_TYPE_SHOW_LIST);
//            JSONObject paramas = new JSONObject();
//            paramas.put(MarketConstants.PARAM_SHOW_PLACE, showPlace);
//            paramas.put(MarketConstants.PARAM_DISTINCT_ID, distinctId);
//            paramas.put(MarketConstants.PARAM_SHOW_LIST_VERSION, version);
//            paramas.put("indexStart", start);
//            paramas.put("size", size);
//            jsonObject.put("params", paramas);
//            jsonObject.put("forCache", forCache);
//            jsonObject.put("gpuType", GlobalUtil.getGpuType(context));

            if (imageType != 0) {
                jsonObject.put("imgType", imageType);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    /**
     * 登录参数
     *
     * @param context
     * @param userId
     * @param password
     * @return
     */
    public static String getLoginParams(final Context context,
                                        final String userId, final String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            //appendBasicParamater(context, jsonObject);
            //jsonObject.put(LABEL_TYPE, MarketConstants.API_TYPE_ACCOUNT_LOGIN);
            jsonObject.put("login", userId);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    /**
     * 注册新用户
     *
     * @param context
     * @param userId
     * @param password
     * @return
     */
    public static String getRegisterParams(final Context context,
                                           final String captchaType, final String userId,
                                           final String nickname, final String password, final String captcha) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("login", userId);
            jsonObject.put("nickname", nickname);
            jsonObject.put("password", password);
            jsonObject.put("captcha", captcha);
            jsonObject.put("register_type", captchaType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    /**
     * 上传头像的params author:zhenchuan {"apiVer":1,
     * "guid":"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
     * "type":"account.changePicture", "ticket":"sdafsadfsda", "picture"://///,
     * "smallpicture":"sfasdfdsafsdfsdf"}
     *
     * @param context
     * @param smallB64
     *            小图片的base64
     * @param bigB64
     *            大图片的base64
     * @param ticket
     *            账户的ticket
     * @return params
     */
    public static String getParamsForHeadPictureUpload(final Context context,
                                                       final String smallB64, final String bigB64, String ticket) {
        JSONObject obj = new JSONObject();
        try {
            //appendBasicParamater(context, obj);
            //obj.put(LABEL_TYPE, MarketConstants.API_TYPE_ACCOUNT_PICUPLOAD);// "account.changePicture"
            obj.put("ticket", ticket);
            obj.put("picture", bigB64);
            obj.put("small_picture", smallB64);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

}
