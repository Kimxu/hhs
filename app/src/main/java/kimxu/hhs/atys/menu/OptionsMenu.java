package kimxu.hhs.atys.menu;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import kimxu.hhs.R;
import kimxu.hhs.atys.widget.SimpleSearchView;
import kimxu.hhs.atys.widget.img.NetworkImageView;
import kimxu.hhs.utils.DeviceUtil;
import kimxu.hhs.utils.logger.Klog;
import kimxu.hhs.z_volley.toolbox.thread_pool.CommonThreadPoolFactory;

/**
 * Created by xuzhiguo on 15/7/31.
 */
public class OptionsMenu {

    public static void onCreateNavigationMenu(
            MenuInflater inflate,
            Menu menu, final Context context,
            String searchHint) {
        inflate.inflate(R.menu.menu_items_navigation, menu);

        setupSearchView(menu, context, searchHint);
    }

    public static void setupSearchView(Menu menu,
                                       final Context context, String searchHint) {
        final SimpleSearchView mSearchView = (SimpleSearchView) menu.findItem(
                R.id.search).getActionView();
        int screenWidth = DeviceUtil.getSceenWidth(context);
        int itemWidth = context.getResources().getDimensionPixelSize(
                R.dimen.abs__action_button_min_width);
        int width = screenWidth - 2 * itemWidth;
        mSearchView.setWidth(width);
        mSearchView.setQueryHint(TextUtils.isEmpty(searchHint) ? context
                .getString(R.string.search_hint_msg) : searchHint);
        if (mSearchView.getEditor() != null) {
            mSearchView.getEditor().setHintTextColor(Color.parseColor("#b9b9b9"));
        }

        mSearchView.setActionButtonEnabled(true);
        mSearchView.setActionButtonListener(new SimpleSearchView.OnActionButtonClickListener() {

            @Override
            public void doAction() {
                Runnable task = new Runnable() {

                    @Override
                    public void run() {
                        try {
                            System.gc();
                            Thread.sleep(500);
                        } catch (Exception ignored) {

                        }
                    }

                };
                CommonThreadPoolFactory.getDefaultExecutor().submit(task);
            }
        });
        mSearchView.setImeEnabled(false);

        mSearchView
                .setOnQueryTextFocusChangeListener(new SimpleSearchView.OnQueryTextFocusChangeListener() {

                    @Override
                    public boolean onFocused(boolean isFocus, String text) {
                        if (isFocus) {
                            Klog.i("onFocused");
                            mSearchView.refresh();

                        }
                        return false;
                    }
                });

    }

    /**
     * 刷新搜索框
     * @param menu 菜单
     * @param context 上下文
     */
    public static void refreshSearchView(Menu menu,
                                         final Context context){
        if(menu == null || context == null)
            return;
        MenuItem item = menu.findItem(R.id.search);
        if(item == null)
            return;
        final SimpleSearchView mSearchView = (SimpleSearchView) item.getActionView();
        int screenWidth = DeviceUtil.getSceenWidth(context);
        int itemWidth = context.getResources().getDimensionPixelSize(
                R.dimen.abs__action_button_min_width);
        int width = (int) (screenWidth - 2 * itemWidth);
        mSearchView.setWidth(width);
    }
    /**
     * 获得菜单
     * @param menu 菜单
     * @return 菜单按钮
     */
    public static ImageView getManageCenterImageView(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.manage);

        if (menuItem == null) {
            return null;
        }

        View actionView = menuItem.getActionView();
        View target = actionView.findViewById(R.id.tab_view);
        if (target instanceof ImageView) {
            return (ImageView) target;
        } else {
            return null;
        }
    }

    /**
     * 获得头像
     * @param menu 菜单
     * @return 头像按钮
     */
    public static NetworkImageView getcAccountCenterImageView(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.account);

        if (menuItem == null) {
            return null;
        }

        View actionView = menuItem.getActionView();
        View target = actionView.findViewById(R.id.actionbar_avt);
        if (target instanceof ImageView) {
            return (NetworkImageView) target;
        } else {
            return null;
        }
    }


}
