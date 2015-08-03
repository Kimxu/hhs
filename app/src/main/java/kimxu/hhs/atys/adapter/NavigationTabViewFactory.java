package kimxu.hhs.atys.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kimxu.hhs.R;
import kimxu.hhs.utils.Colors;
import me.xiaopan.psts.PagerSlidingTabStrip;

/**
 * Created by xuzhiguo on 15/7/29.
 */
public class NavigationTabViewFactory implements PagerSlidingTabStrip.TabViewFactory {
    @Override
    public void addTabs(ViewGroup parent, int defaultPosition) {
        // 先清除已有的Tab View
        parent.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        int textColor = Colors.BLUE_TRANSLUCENT;
        parent.addView(createTabStrip(parent, layoutInflater, "推荐1", textColor, R.drawable.ic_tab_category_white, defaultPosition == 0));
        parent.addView(createTabStrip(parent, layoutInflater, "推荐2", textColor, R.drawable.ic_tab_group_white, defaultPosition == 1));
        parent.addView(createTabStrip(parent, layoutInflater, "推荐3", textColor, R.drawable.ic_tab_list_white, defaultPosition == 2));
        parent.addView(createTabStrip(parent, layoutInflater, "推荐4", textColor, R.drawable.ic_tab_news_white, defaultPosition == 3));
        parent.addView(createTabStrip(parent, layoutInflater, "推荐5", textColor, R.drawable.ic_tab_recomment_white, defaultPosition == 4));

    }

    /**
     * 创建TAB
     *
     * @param layoutInflater
     * @param title
     * @param iconDrawable
     * @param checked
     * @return
     */
    private View createTabStrip(ViewGroup parent,
                                LayoutInflater layoutInflater, String title, int textColor,
                                int iconDrawable, boolean checked) {
        View tabStripView = layoutInflater.inflate(R.layout.tab_strip,
                parent, false);
        TextView titleTextView = (TextView) tabStripView
                .findViewById(R.id.text_tabStrip_title);
        ImageView iconImageView = (ImageView) tabStripView
                .findViewById(R.id.image_tabStrip_icon);
        titleTextView.setText(title);
        titleTextView.setTextColor(textColor);
        iconImageView.setBackgroundResource(iconDrawable);
        if (checked) {
            titleTextView.setVisibility(View.INVISIBLE);
            iconImageView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            iconImageView.setVisibility(View.INVISIBLE);
        }
        return tabStripView;
    }

}
