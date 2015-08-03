package kimxu.hhs.utils.logger;

import com.orhanobut.logger.Logger;

/**
 * Created by xuzhiguo on 15/8/3.
 */
public class Klog  {

    private static Klog instance;
    private static boolean isDebug;
    private Klog(){


    }

    public static Klog getInstance(){
        if (instance==null){
            instance =new Klog();
        }
        return instance;
    }

    public static void i(String message, Object... args) {
        if (isDebug)
        Logger.i(message, args);
    }
    public static void e(String message, Object... args) {
        if (isDebug)
            Logger.e(message, args);
    }
    public static void json(String json) {
        if (isDebug)
        Logger.json(json);
    }

    public static void xml(String xml) {
        if (isDebug)
        Logger.xml(xml);
    }

    public void setDebug(boolean b){
        //PreferanceManager.getInstance().putBoolean(Config.PREFERENCE_ID,"isDebug",b);
        isDebug=b;
    }
}
