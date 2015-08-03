package kimxu.hhs.atys;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import kimxu.hhs.Config;
import kimxu.hhs.R;
import kimxu.hhs.basic.AtySupport;
import kimxu.hhs.utils.logger.Klog;
import kimxu.hhs.z_api.pref.PreferanceManager;

/**
 * Created by xuzhiguo on 15/8/2.
 */
public class AtyWelcome extends AtySupport {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_wel);
        if (isFirst()){
            Klog.i("the first");


        }else {

            AtyLogin.startMe(this);
            Klog.i("no the first");
        }
    }

    private boolean isFirst() {
        boolean isFirst =PreferanceManager.getInstance().getBoolean(Config.PREFERENCE_ID,"isFirst",true);
        if (isFirst){
            PreferanceManager.getInstance().putBoolean(Config.PREFERENCE_ID, "isFirst", false);
        }
        return isFirst;
    }
}
