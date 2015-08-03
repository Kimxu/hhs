package kimxu.hhs.z_api.device;

import android.view.View;

import kimxu.hhs.z_volley.base.AppCallback;
import kimxu.hhs.z_volley.base.GenericHelper;


public interface DisplayHelper extends GenericHelper {
	public static final int DIALOG_VIEW = 0;
	public static final int MASK_VIEW = 1;
	public static final int NOTIFICATION_VIEW = 2;

	public static final String MASK_RES = "pw_cover_mask";

	public abstract long showToast(String msg);

	public abstract long cancelToast(long id);

	public abstract long showNotifications(int resId, AppCallback initView);

	public abstract boolean cancelNotifications(long id);

	public abstract boolean cancelAllNotifications(AppCallback filter);

	public abstract int getPixFromDp(int pix);

	public abstract float getPixFromUnits(int unit, float value);
	
	public float[] getViewSize(View aView);

	public float getScreenRate();

	public abstract boolean addMaskView(View aw);

	public abstract int addMaskView(String aw, int color);

	public abstract boolean removeMaskView(View aw);

	public abstract boolean removeMaskView(int aw);

	public abstract boolean hideMaskView(String aw);

	public View getView(int type, int id);

	public int getMaskColor(int oc);

	public int getMaskViewCount();

	public int removeAllView();
}
