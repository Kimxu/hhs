package kimxu.hhs.atys.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import kimxu.hhs.R;
import kimxu.hhs.model.Asset;

/**
 * Created by xuzhiguo on 15/7/30.
 */
public class FragMainAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Asset> mAssets;

    /**
     * adapter items
     */
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_HORIZONTAL = 1;
    private static final int TYPE_VERTICAL = 2;
    private static final int TYPE_BANNER = 3;
    private static final int TYPE_HORIZONTAL_EQUAL_2 = 4;
    private static final int TYPE_HORIZONTAL_EQUAL_3 = 5;
    private static final int TYPE_HORIZONTAL_UNEQUAL_2 = 6;
    private static final int TYPE_FOOTER = 7;


    public FragMainAdapter(Context context, ArrayList<Asset> assets) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAssets = assets;
    }


    @Override
    public int getCount() {
        return mAssets.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return mAssets.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_frag_main,
                    parent, false);
            viewHolder =new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.image_view)
        ImageView imageView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
