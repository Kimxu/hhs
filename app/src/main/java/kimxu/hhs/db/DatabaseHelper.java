package kimxu.hhs.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import kimxu.hhs.utils.logger.Klog;

/**
 * Created by xuzhiguo on 15/8/1.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "hhs_database.db"; //数据库名称
    private static final int version = 1; //数据库版本


    private static String USER_TABLE_CREATE = "";


    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context) {
        synchronized (DatabaseHelper.class) {
            if (instance == null) {
                instance = new DatabaseHelper(context);
            }
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);

    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Klog.e("开始创建数据库表");
        sqLiteDatabase.execSQL(USER_TABLE_CREATE);
        Klog.e("创建完成.........");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
