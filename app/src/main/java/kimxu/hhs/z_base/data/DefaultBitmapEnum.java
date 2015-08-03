package kimxu.hhs.z_base.data;

import android.graphics.Bitmap;

import kimxu.hhs.R;
import kimxu.hhs.basic.SSHApplication;
import kimxu.hhs.z_volley.structure.CommonImageCache;
import kimxu.hhs.z_volley.tools.BitmapHelper;

public enum DefaultBitmapEnum {

	/**
	 * 
	 */
	sDefaultIcon(R.drawable.ic_null, false),
	/**
	 * 
	 */

	sDefaultBanner(R.drawable.ic_null, false),
	/**
	 * 
	 */

	sDefaultBookBanner(R.drawable.ic_null, false),
	/**
	 * 
	 */

	sDefaultPortrait(R.drawable.ic_null, false),
	sDefaultPortraitCircle(R.drawable.ic_null, false),
	/**
	 * 
	 */

	sDefaultNewsBanner(R.drawable.ic_null, false),
	/**
	 * 
	 */

	s2gIcon(R.drawable.ic_null, false),
	/**
	 * 
	 */

	s2gBanner(R.drawable.ic_null, false),
	/**
	 *
	 */
	s2gBookBanner(R.drawable.ic_null, false),
	/**
	 * 
	 */
	s2gNewsBanner(R.drawable.ic_null, false),
	/**
	 * 
	 */
	sDefaultAccountCenterBack(R.drawable.ic_null, false),
	/**
	 * 
	 */
	sDefaultCircleIcon(R.drawable.ic_null, false),
	/**
		 * 
		 */
	sDefaultNavigationAvt(R.drawable.ic_null, false);
	private Bitmap bitmap;
	private final int resId;
	private static volatile CommonImageCache<Integer> cache;

	private DefaultBitmapEnum(int resId, boolean preDecode) {
		this.resId = resId;
		if (preDecode) {
			bitmap = BitmapHelper.decodeRes(resId);
		} else {
			bitmap = null;
		}
	}

	private DefaultBitmapEnum(String resName, boolean preDecode) {
		this.resId = SSHApplication.getResourceId(resName, "drawable");
		if (resId > 0) {
			if (preDecode) {
				bitmap = BitmapHelper.decodeRes(resId);
			} else {
				bitmap = null;
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	public Bitmap getBitmap() {
		if (bitmap != null) {
			if (bitmap.isRecycled()) {
				bitmap = BitmapHelper.decodeRes(resId);
			}
			return bitmap;
		} else {
			if (cache == null) {
				synchronized (DefaultBitmapEnum.class) {
					cache = CommonImageCache.<Integer> getImageCache(204800);
				}
			}
			Bitmap re = cache.getBitmap(this.resId);
			if (re != null) {
				if (re.isRecycled()) {
					re = BitmapHelper.decodeRes(resId);
					cache.putBitmap(this.resId, re);
				}
				return re;
			} else {
				Bitmap bt = BitmapHelper.decodeRes(resId);
				cache.putBitmap(this.resId, bt);
				return bt;
			}
		}
	}

	public int getResId() {
		return resId;
	}

	public static void release() {
//		synchronized (DefaultBitmapEnum.class) {
//			L.x("clear cache", "default bitmap cache cleared");
//			if (cache != null) {
//				cache.clear();
//				cache = null;
//			}
//		}
	}

	// /**
	// *
	// */
	// ic_default_icon("ic_launcher", false),
	// /**
	// *
	// */
	// ic_tag_xpk("ic_xpk_large", false),
	// /**
	// *
	// */
	// ic_tag_official("ic_official", false),
	// /**
	// *
	// */
	// ic_tag_unlock("ic_unlock", false),
	// /**
	// *
	// */
	// ic_corner_first("ic_firstpublich", false),
	// /**
	// *
	// */
	// ic_corner_act("ic_activity", false),
	// /**
	// *
	// */
	// ic_corner_gift("ic_gift", false),
	// /**
	// *
	// */
	// ic_corner_recom("ic_recommed", false),
	// /**
	// *
	// */
	// ic_corner_hot("ic_hot", false),
}
