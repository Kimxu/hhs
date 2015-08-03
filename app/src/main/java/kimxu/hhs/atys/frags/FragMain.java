package kimxu.hhs.atys.frags;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import kimxu.hhs.Config;
import kimxu.hhs.R;
import kimxu.hhs.atys.adapter.BannerAdapter;
import kimxu.hhs.atys.adapter.FragMainAdapter;
import kimxu.hhs.atys.widget.AutoIndicator;
import kimxu.hhs.model.Asset;
import kimxu.hhs.model.Banner;
import kimxu.hhs.online.HttpManager;
import kimxu.hhs.online.HttpService;
import kimxu.hhs.utils.logger.Klog;
import kimxu.hhs.z_volley.http.QueuedRequest;

/**
 * Created by xuzhiguo on 15/7/30.
 * onCreate()方法只会被调用一次，而 onCreateView() 方法，
 * 每次Fragment从不可见到可见时会被调用
 */
public class FragMain extends Fragment {
    @Bind(R.id.listview)
    PullToRefreshListView listView;

    private View mBannerArea;

    private AutoIndicator mBannerIndicator;

    private ViewPager mBannerComponent;

    private ArrayList<Banner> mBannerList;

    private BannerAdapter mBannerAdapter;

    private Activity activity;

    private Fragment fragment;

    private FragMainAdapter mAdpter;

    protected HttpService mHttpService;

    protected Handler mHttpHandler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        fragment = this;


        //TODO
        //mBannerList = (ArrayList<Banner>) objects[1];

        mHttpHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (activity != null && fragment != null) {
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
        getBannerDatas();
    }



    //TODO Volley processHttpResponse
    protected void processHttpResponse(Message msg) {
        QueuedRequest qr = (QueuedRequest) msg.obj;
        Object[] objects;

        switch (qr.requestId) {
            case 1:
//               ArrayList<Asset> assets = null;
//                objects = JsonUtils.getAssetShowList(activity, (String) qr.result);
//                if (objects != null && objects.length >= 2) {
//                    mNextIndexStart = (Integer) objects[0];
//                    assets = (ArrayList<Asset>) objects[1];
//                }
//
//                setFooterViewState(FOOTER_VIEW_GONE);
//                inflatingAppList = false;
//                processAppListHandler(assets, false);
//
//                final int appSize = assets == null ? 0 : assets.size();
//                final int appStartIndex = mNextIndexStart - appSize;
//                ClientLogger.addDisplayListLog(activity, getPage(), appStartIndex,
//                        appSize, assets, mListType);

                break;
            case Config.SHOW_PLACE_BANNER:
                // qr.result;
                Klog.e("接收到了");
                //mHttpHandler.removeMessages(MSG_BANNER_MOVE);
                // processBannerHandler();
                break;
            case 3:
//                String jsonStr = (String) qr.result;
//                try{
//                    JSONObject object = new JSONObject(jsonStr);
//                    int result = object.optInt("result");
//                    int isUpdate = object.optInt("isUpdate");
//                    if(result == 0){//返回结果成功
//                        if(isUpdate == 1){//代表红包数据有更新
//                            llActTv.setVisibility(View.VISIBLE);
//                            break;
//                        }
//                    }
//                }catch(Exception e){
//
//                }
//                llActTv.setVisibility(View.INVISIBLE);
                break;
        }
    }







    protected void processHttpError(Message msg) {
        QueuedRequest qr = (QueuedRequest) msg.obj;


        setNoResponseState();
        Klog.i(qr.responseHttpCode + "");

//        switch (qr.requestId){
//            case 1:
//                //msg.obj;
//                break;
//            case 2:
//                //msg.obj;
//                break;
//            case 3:
//                //msg.obj;
//                break;
//
//        }



    }

    private void setNoResponseState(){

    }


    /**
     * 从本地取缓存的banner列表，如果有wifi的话就去取新的列表 如果没有取到缓存的banner列表，就必须去取新的列表
     */
    private void getBannerDatas() {
        //TODO Volley mHttpService
        mHttpService = HttpService.getInstance(activity);
        mHttpService.getAppShowList(Config.SHOW_PLACE_BANNER,
                mHttpHandler);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main, container,
                false);
        ButterKnife.bind(this, view);
        initListView();
        initBannerView(inflater);
        return view;
    }

    private void initListView() {
        ArrayList<Asset>lists= new ArrayList<>();
        for (int i=0;i<5;i++){
            lists.add(new Asset());
        }
        mAdpter = new FragMainAdapter(activity,lists);

        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(activity, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                ILoadingLayout iLoadingLayout = refreshView.getLoadingLayoutProxy(
                        false, true);
                iLoadingLayout.setPullLabel(label);// 刚下拉时，显示的提示
                iLoadingLayout.setRefreshingLabel("正在载入…");// 刷新时
                iLoadingLayout.setPullLabel("上拉刷新…");
                iLoadingLayout.setReleaseLabel("放开刷新…");// 下来达到一定距离时，显示的提示

            }
        });

        listView.setAdapter(mAdpter);

    }
    private void initBannerView(LayoutInflater inflater) {
        mBannerList =new ArrayList<>();
        for (int i=0;i<5;i++){
            mBannerList.add(new Banner());
        }
        mBannerAdapter = new BannerAdapter(activity, mBannerList);
        mBannerArea = inflater.inflate(R.layout.vg_banner, null);
        mBannerIndicator = (AutoIndicator) mBannerArea
                .findViewById(R.id.banner_indicator);
        mBannerIndicator.setIndicatorCount(mBannerList.size());
        final TextView mBannerDescription = (TextView) mBannerArea
                .findViewById(R.id.banner_promotion_description);

        mBannerComponent = (ViewPager) mBannerArea.findViewById(R.id.banner);
        mBannerComponent.setAdapter(mBannerAdapter);
        mBannerComponent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Klog.e("onTouch");

                return false;
            }

        });
        mBannerComponent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageSelected(int position) {
                // 设置banner下面的indicator
                if (position == 0) {
                    mBannerComponent.setCurrentItem(1);
                    mBannerIndicator.setSelectedIndicator(0);
                } else {
                    mBannerIndicator.setSelectedIndicator(position-1);
                }
                if (position == mBannerList.size() + 1) {
                    mBannerComponent.setCurrentItem(mBannerList.size());
                }

                // 设置banner描述

                if (position > 0 && position < mBannerList.size() + 1) {
                    String description = mBannerList.get(position - 1).description;
                    if (!TextUtils.isEmpty(description)) {
                        mBannerDescription.setText(description);
                        mBannerDescription.setVisibility(View.VISIBLE);
                    } else {
                        mBannerDescription.setVisibility(View.GONE);
                    }
                }

            }

        });
        listView.getRefreshableView().addHeaderView(mBannerArea);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
