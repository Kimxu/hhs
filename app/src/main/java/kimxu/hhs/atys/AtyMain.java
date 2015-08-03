package kimxu.hhs.atys;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import kimxu.hhs.R;
import kimxu.hhs.atys.adapter.NavigationTabViewFactory;
import kimxu.hhs.atys.adapter.ViewPagerAdapter;
import kimxu.hhs.atys.menu.OptionsMenu;
import kimxu.hhs.atys.widget.img.NetworkImageView;
import kimxu.hhs.basic.AtyFragSupport;
import kimxu.hhs.online.HttpManager;
import kimxu.hhs.online.HttpService;
import kimxu.hhs.utils.logger.Klog;
import me.xiaopan.psts.PagerSlidingTabStrip;

public class AtyMain extends AtyFragSupport implements ViewPager.OnPageChangeListener {
    @Bind(R.id.slidingTabStrip)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.viewPager)
    ViewPager pager;

    private int lastPositon;
    private ViewPagerAdapter adapter;

    private Activity mActivity;

    private Menu mMenu;

    private NetworkImageView avtImageView;
    private ImageView manageCenterImageView;

    /**
     * set 头像
     */
    private final int MSG_FOR_REFRESH_AVT = 101;
    private HttpService mHttpService;



    private Handler mHttpHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null) {
                switch (msg.what) {
                    case HttpManager.MSGCODE_HTTP_ERROR:
                        processHttpError(msg);
                        break;
                    case HttpManager.MSGCODE_HTTP_RESPONSE:
                        processHttpResponse(msg);
                        break;

                }
            }
        }
    };

    private void processHttpResponse(Message msg) {

    }

    private void processHttpError(Message msg) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        OptionsMenu.onCreateNavigationMenu(
                getMenuInflater(),menu,mActivity,"初始化搜索框");

//
        avtImageView = OptionsMenu.getcAccountCenterImageView(menu);
                //TODO 设置头像
        assert avtImageView != null;
        avtImageView.setImageShapeType(NetworkImageView.CIRCLE_IMAGE);
        avtImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Klog.i("头像");
            }
        });
        manageCenterImageView = OptionsMenu.getManageCenterImageView(menu);
        manageCenterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Klog.i("菜单");
            }
        });

        return true;
    }

   //TODO onWindowFocusChanged

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
        ButterKnife.bind(this);
        mActivity=AtyMain.this;
        ActionBar actionBar=getActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        init(0, tabs, pager);
        mHttpService = HttpService.getInstance(this);
    }

  //TODO onConfigurationChanged

    /**
     * 初始化tabs
     * @param index 初始化页数
     * @param pagerSlidingTabStrip 初始化tabs
     * @param viewPager 初始化viewpager
     */
    private void init(int index, PagerSlidingTabStrip pagerSlidingTabStrip, ViewPager viewPager) {
        tabs.setTabViewFactory(new NavigationTabViewFactory());
        int length = pagerSlidingTabStrip.getTabCount();
        lastPositon = viewPager.getCurrentItem();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index < length ? index : length);
        pagerSlidingTabStrip.setViewPager(viewPager);
        tabs.setOnPageChangeListener(this);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (lastPositon == position) {
            return;
        }

        View currentView = tabs.getTab(position);
        if (currentView != null) {
            executeAnimation(currentView.findViewById(R.id.text_tabStrip_title), R.anim.tab_to_top_out, false);
            executeAnimation(currentView.findViewById(R.id.image_tabStrip_icon), R.anim.tab_to_top_enter, true);
        }

        View lastView = tabs.getTab(lastPositon);
        if (lastView != null) {
            executeAnimation(lastView.findViewById(R.id.text_tabStrip_title), R.anim.tab_to_bottom_enter, true);
            executeAnimation(lastView.findViewById(R.id.image_tabStrip_icon), R.anim.tab_to_bottom_out, false);
        }

        lastPositon = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void executeAnimation(final View view, int resId, final boolean show) {
        Animation animation = AnimationUtils.loadAnimation(tabs.getContext(), resId);
        view.setVisibility(View.VISIBLE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            }
        });
        view.startAnimation(animation);
    }

    public static void startMe(Context context) {
        Intent intent = new Intent(context, AtyMain.class);
        context.startActivity(intent);
    }
}
