package kimxu.hhs.atys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kimxu.hhs.R;
import kimxu.hhs.basic.AtySupport;
import kimxu.hhs.utils.logger.Klog;

/**
 * Created by xuzhiguo on 15/8/2.
 */
public class AtyLogin extends AtySupport{
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_login);
        ButterKnife.bind(this);



        Klog.i("...");



    }

    public static void startMe(Context context) {
        Intent intent = new Intent(context, AtyLogin.class);
        context.startActivity(intent);
    }
    @OnClick(R.id.login)
    public void login(){
        AtyMain.startMe(mActivity);
    }
}
