package kimxu.hhs.utils;

/**
 * Created by xuzhiguo on 15/7/31.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;



/**
 *
 * 包含设备处理方法的工具类
 *
 * @author <a href="mailto:sprite1225@gmail.com">王刚</a>
 * @since 1.6
 * @version 1.0.0 2010-11-06
 */
public class DeviceUtil {

    public static final int SDK_VERSION_1_5 = 3;

    public static final int SDK_VERSION_1_6 = 4;

    public static final int SDK_VERSION_2_0 = 5;

    public static final int SDK_VERSION_2_0_1 = 6;

    public static final int SDK_VERSION_2_1 = 7;

    public static final int SDK_VERSION_2_2 = 8;

    public static final int SDK_VERSION_2_3 = 9;

    public static final int SDK_VERSION_2_3_3 = 10;

    public static final int SDK_VERSION_3_0 = 11;

    public static final int SDK_VERSION_4_0 = 14;

    public static final int SDK_VERSION_4_0_3 = 15;

    /**
     * 获得设备型号
     *
     * @return
     */
    public static String getDeviceModel() {
        return StringUtil.makeSafe(Build.MODEL);
    }

    public static String getDeviceName() {
        return StringUtil.makeSafe(Build.DEVICE);
    }

    /**
     * 获得设备的固件版本号
     */
    public static String getReleaseVersion() {
        return StringUtil.makeSafe(Build.VERSION.RELEASE);
    }

    /**
     * 获得国际移动设备身份码
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 获得MSISDN
     *
     * 修改记录： 2012-11-20 litingchang 调用getLine1Number()360会弹出获取手机号码的提示，故注释掉。
     *
     * @param context
     * @return 没有使用
     */
    public static String getMSISDN(Context context) {
        // if(context==null)
        // return "NoNumber";
        // String
        // msisdn=((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
        // if(msisdn==null)
        // return "NoNumber";
        // return (msisdn.equals("")?"NoNumber":Hash.EncoderByMd5(msisdn));
        return "";
    }

    /**
     * 获得国际移动用户识别码
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * 获得设备屏幕矩形区域范围
     *
     * @param context
     * @return
     */
    public static Rect getScreenRect(Context context) {
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        return new Rect(0, 0, w, h);
    }

    public static int getSceenWidth(Context context) {
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获得设备屏幕密度
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics metrics = context.getApplicationContext().getResources()
                .getDisplayMetrics();
        return metrics.density;
    }

    public static int getScreenDensityDpi(Context context) {
        DisplayMetrics metrics = context.getApplicationContext().getResources()
                .getDisplayMetrics();
        return (int) (metrics.density * 160);
    }

    /**
     * 获得系统版本
     */
    public static String getSDKVersion() {
        return android.os.Build.VERSION.SDK;
    }

    public static int getSDKVersionInt() {
        try {
            return Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获得android_id
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Secure
                .getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * 获得deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return getIMEI(context);
    }

    /**
     * 获得屏幕尺寸
     *
     * @param context
     * @return
     */
    public static String getResolution(Context context) {
        Rect rect = getScreenRect(context);
        return rect.right + "x" + rect.bottom;
    }

    public static String getSerialNumber() {
        String serialNumber = "";

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serialNumber = (String) get.invoke(c, "ro.serialno");

            if (serialNumber.equals("")) {
                serialNumber = "?";
            }
        } catch (Exception e) {
            if (serialNumber.equals("")) {
                serialNumber = "?";
            }
        }

        return serialNumber;
    }

    public static String getABI() {
        String abi = "";
        try {
            abi = Build.CPU_ABI;
        } catch (Error err) { // Possible throw NoSuchFiledError here

        } catch (Exception e) {

        }
        return abi;
    }

    @TargetApi(8)
    public static String getABI2() {
        String abi2 = "";
        try {
            abi2 = Build.CPU_ABI2;
        } catch (Error err) { // Possible throw NoSuchFiledError here

        } catch (Exception e) {

        }
        return abi2;
    }

    @TargetApi(8)
    public static String getHardware() {
        if (Build.VERSION.SDK_INT < 8)
            return "";
        return Build.HARDWARE;
    }

    /**
     * 获取wifi mac地址
     *
     * @return wifi mac地址(xx:xx:xx:xx:xx)
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();

        return info == null ? "unknown" : info.getMacAddress();
    }

    public static int convertDipToPx(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dip*scale + 0.5f*(dip>=0?1:-1));
    }

    public static float convertPxToSp(Context context, int px) {
        return Float.valueOf(px) / context.getResources().getDisplayMetrics().scaledDensity + 0.5f;
    }

    public static float convertDipToSp(Context context, int dip){
        return convertPxToSp(context, convertDipToPx(context, dip));
    }

    /**
     * 获取手机号码
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNum = manager.getLine1Number();
        return phoneNum == null ? "null" : phoneNum;
    }

    public static int getStatusBarHeight(Activity activity){
        int result = 0;
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        result = frame.top;
        if(result <= 0){
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                result = activity.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return result;
    }
}
