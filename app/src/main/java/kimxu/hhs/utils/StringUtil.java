package kimxu.hhs.utils;

/**
 * Created by xuzhiguo on 15/7/31.
 */
public class StringUtil {
    public static final String EMPTY_STRING = "";
    public static String makeSafe(String s) {
        return (s == null) ? "" : s;
    }
}
