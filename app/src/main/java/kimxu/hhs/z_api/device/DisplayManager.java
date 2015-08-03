package kimxu.hhs.z_api.device;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import kimxu.hhs.basic.SSHApplication;
import kimxu.hhs.z_base.AbstractManager;
import kimxu.hhs.z_api.Api;
import kimxu.hhs.z_api.pref.PreferanceHelper;
import kimxu.hhs.z_base.ConstantValues;
import kimxu.hhs.z_volley.base.AppCallback;
import kimxu.hhs.z_volley.structure.CollectionBuilder;


public final class DisplayManager extends AbstractManager implements
		DisplayHelper {
	private static final float MASK_ALPHA = 0.25f;
	static Handler mainHandler;

	public static class CommonLs implements DialogInterface.OnClickListener {
		AppCallback c;
		boolean d;

		public CommonLs(AppCallback callback, boolean dismiss) {
			c = callback;
			d = dismiss;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (c != null) {
				c.onCallBack(dialog, which);
			}
			if (d) {
				dialog.dismiss();
			}
		}
	}

	public static final int MIN_BRIGHTNESS = 2;
	public static final int DEFAULT_COLOR_FOR_MASK = ConstantValues.DEFAULT_COVER_COLOR;
	private static volatile DisplayManager instance, customInstance;

	public static DisplayManager getInstance() {
		if (instance == null) {
			synchronized (DisplayManager.class) {
				instance = new DisplayManager();
				if (mainHandler == null)
					mainHandler = new Handler(Looper.getMainLooper());
			}
		}
		return instance;
	}

	public static DisplayManager getInstance(Context c) {
		if (customInstance == null) {
			synchronized (DisplayManager.class) {
				customInstance = new DisplayManager(c);
				customInstance.setC(c);
				if (mainHandler == null)
					mainHandler = new Handler(Looper.getMainLooper());
			}
		}
		return customInstance;
	}

	private final WindowManager wm;
	private final LayoutInflater inf;
	private final Map<Integer, View> mViews;
	private final PreferanceHelper ph;
	private final AtomicInteger seq;
	private final CommonLs defaultLs;

	static String title;
	static String txOk;
	static String txCancel;

	public int getMaskColor(int oc) {
		int a = Color.alpha(oc);
		int r = Color.red(oc);
		int g = Color.green(oc);
		int b = Color.blue(oc);
		if (r != g || g != b || r != b) {
			r = (int) (r * MASK_ALPHA + 0x11 * (1 - MASK_ALPHA));
			g = (int) (g * MASK_ALPHA + 0x11 * (1 - MASK_ALPHA));
			b = (int) (b * MASK_ALPHA + 0x11 * (1 - MASK_ALPHA));
			if (a < 0x25) {
				a = 0x25;
			} else if (a > 0xe5) {
				a = 0xe5;
			}
		}
		return Color.argb(a, r, g, b);
	}

	private DisplayManager(Context c) {
		wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		inf = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mViews = CollectionBuilder.newHashMap();
		ph = (PreferanceHelper) Api.pref.getHandler();
		seq = new AtomicInteger(0);
		//TODO
		String title = SSHApplication.getStringRes("tx_info_title_of_dialog");
		String txOk = SSHApplication.getStringRes("txt_ok_of_dialog");
		String txCancel = SSHApplication.getStringRes("txt_cancel_of_dialog");
		DisplayManager.title = (title == null || title.trim().length() == 0) ? "信息"
				: title;
		DisplayManager.txOk = (txOk == null || txOk.trim().length() == 0) ? "确认"
				: txOk;
		DisplayManager.txCancel = (txCancel == null || txCancel.trim().length() == 0) ? "取消"
				: txCancel;
		defaultLs = new CommonLs(null, true);

	}

	@Override
	public Context getContext() {
		return super.getContext();
	}

	private DisplayManager() {
		wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		inf = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		mViews = CollectionBuilder.newHashMap();
		ph = (PreferanceHelper) Api.pref.getHandler();
		seq = new AtomicInteger(0);
		String title = SSHApplication.getStringRes("tx_info_title_of_dialog");
		String txOk = SSHApplication.getStringRes("txt_ok_of_dialog");
		String txCancel = SSHApplication.getStringRes("txt_cancel_of_dialog");
		DisplayManager.title = (title == null || title.trim().length() == 0) ? "信息"
				: title;
		DisplayManager.txOk = (txOk == null || txOk.trim().length() == 0) ? "确认"
				: txOk;
		DisplayManager.txCancel = (txCancel == null || txCancel.trim().length() == 0) ? "取消"
				: txCancel;
		defaultLs = new CommonLs(null, true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.china.framework.util.device.Dh#showToast(java.lang.String)
	 */
	@Override
	public long showToast(final String msg) {
		mainHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(SSHApplication.getAppContext(), msg,
						Toast.LENGTH_LONG).show();
			}
		});

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.china.framework.util.device.Dh#cancelToast(long)
	 */
	@Override
	public long cancelToast(long id) {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.china.framework.util.device.Dh#showNotifications(int)
	 */
	@Override
	public long showNotifications(int resId, AppCallback initView) {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.china.framework.util.device.Dh#getPixFromDp(int)
	 */
	public int getPixFromDp(int pix) {
		float scale = getContext().getResources().getDisplayMetrics().density;
		int wp = (int) (scale * pix + 0.5f);
		return wp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.china.framework.util.device.Dh#addMaskView(android.view.View)
	 */
	public boolean addMaskView(View aw) {
		if (aw != null) {
			showAView(aw);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.china.framework.util.device.Dh#addMaskView(java.lang.String,
	 * int)
	 */
	public int addMaskView(String aw, int color) {
		if (aw != null) {
			if (color == 0) {
				color = ph.getInt(ConstantValues.TAG_SYSTEM,
						ConstantValues.KEY_MASK_COLOR, DEFAULT_COLOR_FOR_MASK);
			}
			int id = seq.getAndIncrement();
			showMaskView(id, aw, color);
			return id;
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.china.framework.util.device.Dh#removeMaskView(android.view.View)
	 */
	public boolean removeMaskView(View aw) {
		if (aw != null) {
			try {
				wm.removeView(aw);
				// if (destroy) {
				aw.destroyDrawingCache();

				// }
			} catch (Exception ex) {
				//L.e(ex);
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.china.framework.util.device.Dh#removeMaskView(java.lang.String)
	 */
	public boolean removeMaskView(int aw) {
		View mView = mViews.get(aw);
		if (mView != null) {
			wm.removeView(mView);
			// if (destroy) {
			mViews.remove(aw);
			mView.destroyDrawingCache();
			mView = null;
			// }
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.china.framework.util.device.Dh#hideMaskView(java.lang.String)
	 */
	public boolean hideMaskView(String aw) {
		if (mViews.containsValue(aw)) {
			View mView = mViews.get(aw);
			mView.setVisibility(View.GONE);
			return true;
		}
		return false;
	}

	private void showMaskView(int id, String res, int color) {
		View mView = mViews.get(res);
		if (mView == null) {
			int vid = SSHApplication.getLayoutId(res);
			mView = inf.inflate(vid, null, false);
			mView.setBackgroundColor(color);
			mViews.put(id, mView);
		}
		showAView(mView);
	}

	private void showAView(View mView) {
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.CENTER;
		params.setTitle("view cover");
		//L.x("view", "show view");
		wm.addView(mView, params);
	}

	public int getMaskViewCount() {
		return mViews.size();
	}

	public int removeAllView() {
		int n = 0;
		Set<Integer> set = CollectionBuilder.newHashSet();
		set.addAll(mViews.keySet());
		for (int i : set) {
			this.removeMaskView(i);
			++n;
		}
		return n;
	}

	@Override
	public boolean cancelNotifications(long id) {
		return false;
	}

	@Override
	public boolean cancelAllNotifications(AppCallback filter) {
		return false;
	}

	@Override
	public View getView(int type, int id) {
		View ret = null;
		if (type == MASK_VIEW) {
			ret = this.mViews.get(id);
		} else if (type == NOTIFICATION_VIEW) {

		}
		return ret;
	}

	@Override
	public float getPixFromUnits(int unit, float value) {
		Context c = getContext();
		Resources r;

		if (c == null)
			r = Resources.getSystem();
		else
			r = c.getResources();

		float ret = TypedValue.applyDimension(unit, value,
				r.getDisplayMetrics());
		return ret;
	}

	@Override
	public float getScreenRate() {
		return 3.5f;
	}

	@Override
	public float[] getViewSize(View aView) {
		aView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int width = aView.getMeasuredWidth();
		int heigth = aView.getMeasuredHeight();
		return new float[] { width, heigth };
	}
}