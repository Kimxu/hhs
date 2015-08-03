package kimxu.hhs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.UUID;

import kimxu.hhs.Config;

/**
 * Created by xuzhiguo on 15/7/31.
 */
public class GlobalUtil {

    private static String mUUIDString;

    public static String getUUIDString(Context context) {
        if (mUUIDString != null) {
            return mUUIDString;
        }

        String uuidString = PreferenceManager.getDefaultSharedPreferences(
                context).getString(Config.UUID, null);
        if (uuidString != null) {
            mUUIDString = uuidString;
        } else {
            UUID uuid = UUID.randomUUID();
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Config.UUID, uuid.toString());
            editor.commit();

            mUUIDString = uuid.toString();
        }

        return mUUIDString;
    }
    public static ArrayList<BasicNameValuePair> parseNameValuePair(String str) {
        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        try {
            String[] nameValueStrs = str.split("&");
            for (String nameValueStr : nameValueStrs) {
                nameValueStr = nameValueStr.trim();
                char equalChar = '=';
                int startEqualIdx = nameValueStr.indexOf(equalChar);
                int endEqualIdx = nameValueStr.lastIndexOf(equalChar);
                if (startEqualIdx > 0 && startEqualIdx == endEqualIdx) {
                    String key = nameValueStr.substring(0, startEqualIdx);
                    String value = nameValueStr.substring(startEqualIdx + 1);
                    if (key.length() > 0 && value.length() > 0) {
                        params.add(new BasicNameValuePair(key, value));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }
    public static int convertDiptoPx(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }
}
