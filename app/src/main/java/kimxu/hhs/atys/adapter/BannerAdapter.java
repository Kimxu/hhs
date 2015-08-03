package kimxu.hhs.atys.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import kimxu.hhs.atys.widget.img.NetworkImageView;
import kimxu.hhs.model.Banner;
import kimxu.hhs.utils.logger.Klog;

/**
 * Created by xuzhiguo on 15/7/30.
 */
public class BannerAdapter extends PagerAdapter{
    private Context mContext;
    private ArrayList<Banner> mBannerList;
    private NetworkImageView[] views;


    public BannerAdapter(Context context, ArrayList<Banner> mBannerList) {
        mContext = context;
        this.mBannerList = mBannerList;
        views = new NetworkImageView[mBannerList.size() + 2];
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views[position]);
    }

    // @Override
    public int getCount() {
        return views == null ? 0 : views.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        ImageView img = initBannerView(position);
        container.addView(img);
        return img;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        // TODO Auto-generated method stub
        return view == (View) obj;
    }

    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Klog.e("Touch");
        }
    };

    private NetworkImageView initBannerView(int position) {
//        RequestQueue mQueue = Volley.newRequestQueue(mContext);
//        LruImageCache lruImageCache =LruImageCache.instance();
//        ImageLoader imageLoader =new ImageLoader(mQueue,lruImageCache);
//        NetworkImageView imageView = views[position];
//        if (imageView == null) {
//            if (position == 0) {
//                imageView = new NetworkImageView(mContext);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                imageView.setLayoutParams(layoutParams);
//                imageView.setImageResource(R.drawable.banner_left);
//            } else if (position == views.length - 1) {
//                imageView = new NetworkImageView(mContext);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                imageView.setLayoutParams(layoutParams);
//                imageView.setImageResource(R.drawable.banner_right);
//            } else {
//                imageView = new NetworkImageView(mContext);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageView.setTag(position);
//                imageView.setOnClickListener(listener);
//                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                imageView.setLayoutParams(layoutParams);
//
////                imageView.setImageUrl(mBannerList.get(position - 1).imgUrl,
////                        imageLoader);
//                imageView.setImageUrl("http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg",
//                        imageLoader);
//            }
//            views[position] = imageView;
      //  }

        return new NetworkImageView(mContext);
    }

}
