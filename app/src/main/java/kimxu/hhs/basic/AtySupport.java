package kimxu.hhs.basic;

import android.app.Activity;
import android.os.Bundle;

import kimxu.hhs.basic.impl.IAtyHelper;

/**
 * Created by xuzhiguo on 15/7/29.
 */
public abstract class AtySupport extends Activity implements IAtyHelper{
    protected Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=this;
    }
}
