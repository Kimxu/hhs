package kimxu.hhs.basic;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.ref.WeakReference;

import kimxu.hhs.utils.logger.Klog;
import kimxu.hhs.z_base.AppConstant;

/**
 * Created by xuzhiguo on 15/7/29.
 */
public class SSHApplication extends Application {
    private final String TAG = "kimXu";
    private static Context context;
    private static SSHApplication instance;
    static WeakReference<Activity> currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        context = instance.getApplicationContext();
        Klog.getInstance().setDebug(true);

    }
    public static Context getAppContext() {
        return context;
    }


    public static SSHApplication getInstance() {
        return instance;
    }




    public static int getLayoutId(String name) {
        return instance.getResources().getIdentifier(name, "layout",
                AppConstant.packName);
    }


    public static int getColor(int r) {
        return instance.getResources().getColor(r);
    }

    public static Drawable getDrawableRes(int r) {
        return instance.getResources().getDrawable(r);
    }

    public static String getStringRes(int id) {
        return instance.getResources().getString(id);
    }

    public static String getStringRes(String name) {
        int id = getResourceId(name, "string");
        if (id != 0)
            return getStringRes(id);
        else
            return "";
    }

    public static int getResourceId(String name, String pack) {
        Resources res = context.getResources();
        int resId = res.getIdentifier(name, pack, context.getPackageName());
        return resId;
    }
    public synchronized static Activity getCurrentActivity() {
        return currentActivity == null ? null : currentActivity.get();
    }
    public synchronized static void clearCurrentContext(Activity ac) {
        Activity cur = currentActivity == null ? null : currentActivity.get();
        if (cur != null && cur == ac) {
            currentActivity = null;
        }
    }

    public synchronized static void setCurrentContext(Activity ac) {
        if (ac == null) {
            currentActivity = null;
        } else {
            currentActivity = new WeakReference<Activity>(ac);
        }
    }
}
