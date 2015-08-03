package kimxu.hhs.atys.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kimxu.hhs.atys.frags.FragMain;

/**
 * 滑动视图适配器
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = null;


    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
        fragmentList=new ArrayList<>();
        fragmentList.add(new FragMain());
        fragmentList.add(new FragMain());
        fragmentList.add(new FragMain());
        fragmentList.add(new FragMain());
        fragmentList.add(new FragMain());

    }

    private static final int PAGE_INDEX_HOTLIST = 5;
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(position == PAGE_INDEX_HOTLIST){
            //do nothing
        }else{
            super.destroyItem(container, position, object);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
