package kimxu.hhs.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by xuzhiguo on 15/7/31.
 */
public class PreferenceUtil {

    public static String getString(Context context, String key, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, defValue);
    }
}
