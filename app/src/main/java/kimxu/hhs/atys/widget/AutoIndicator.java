package kimxu.hhs.atys.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import kimxu.hhs.utils.GlobalUtil;


/**
 * Created by xuzhiguo on 15/7/30.
 */

public class AutoIndicator extends LinearLayout {

    public AutoIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AutoIndicator(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    /**
     * 添加指定个数的指示器
     *
     * @param count
     */
    public void setIndicatorCount(int count) {
        removeAllViews();
        while (count > 0) {
            ImageView iv = new ImageView(getContext());
            LayoutParams params = new LayoutParams(GlobalUtil.convertDiptoPx(
                    getContext(), 15), GlobalUtil.convertDiptoPx(getContext(),
                    2));
            params.setMargins(5, 0, 5, 0);
            iv.setLayoutParams(params);
            addView(iv);
            count--;
        }
    }

    /**
     * 设置选中的指示器
     *
     * @param index
     */
    public void setSelectedIndicator(int index) {
        int count = getChildCount();
        ImageView child = null;
        for (int i = 0; i < count; i++) {
            child = (ImageView) getChildAt(i);
            if (index == i) {
                child.setImageDrawable(new ColorDrawable(Color.parseColor("#1588FE")));
            } else {
                child.setImageDrawable(new ColorDrawable(Color.parseColor("#3F000000")));
            }
        }
    }

}
