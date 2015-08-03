package kimxu.hhs.z_volley.structure;


//import com.app.china.framework.util.http.toolbox.ImageL1Cache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;

import java.util.Collection;

import kimxu.hhs.z_base.RuntimeConstant;

public class CommonImageCache<T> {
	public static final int maxSize = 5 * 1024 * 1024;
	LRUCache<T, Bitmap> cache;

	public static <T> CommonImageCache<T> getImageCache(int size) {
		if (size > 0)
			return new CommonImageCache<T>(size);
		else
			return new CommonImageCache<T>();
	}

	protected CommonImageCache() {
		int rate = Integer.parseInt(RuntimeConstant.MEMORY_TYPE
				.getConfig("image_cache"));
		int size = (int) (RuntimeConstant.MEMORY_MAX / rate);
		size = Math.min(maxSize, size);
		cache = new LRUCache<T, Bitmap>(size) {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
			@Override
			protected int sizeOf(T key, Bitmap value) {
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
					return value.getByteCount();
				} else {
					return value.getRowBytes() * value.getHeight();
				}
			}
		};
	}

	protected CommonImageCache(int size) {
		cache = new LRUCache<T, Bitmap>(size) {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
			@Override
			protected int sizeOf(T key, Bitmap value) {
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
					return value.getByteCount();
				} else {
					return value.getRowBytes() * value.getHeight();
				}
			}
		};
	}

	public Bitmap getBitmap(T id) {
		return cache.get(id);
	}

	public void putBitmap(T id, Bitmap bitmap) {
		cache.put(id, bitmap);
	}

	public void clear() {
		Collection<Bitmap> vs = cache.values();
		for (Bitmap m : vs) {
			m.recycle();
		}
		// Map<T,Bitmap> snapShot=cache.snapshot();
		cache.evictAll();
	}

}
