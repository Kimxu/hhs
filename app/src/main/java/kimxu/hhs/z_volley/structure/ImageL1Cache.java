package kimxu.hhs.z_volley.structure;

import android.graphics.Bitmap;

public final class ImageL1Cache extends CommonImageCache<String> implements
		ImageCache {
	// LRUCache<String, Bitmap> cache;
	private static volatile ImageL1Cache instance;

	public static ImageL1Cache getInstance() {
		if (instance == null) {
			synchronized (ImageL1Cache.class) {
				instance = new ImageL1Cache();
			}
		}
		return instance;
	}

	// public static void registerDefaultImg(ImageL1Cache c) {
	// // DefaultBitmap[] bs=DefaultBitmap.values();
	// // for(DefaultBitmap b:bs){
	// // c.putBitmap(url, bitmap)
	// // }
	// }

	private ImageL1Cache() {
		super();
	}

	@Override
	public Bitmap getBitmap(String url) {
		// TODO Auto-generated method stub
		return super.getBitmap(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		super.putBitmap(url, bitmap);
	}

	public static void release() {
		synchronized (ImageL1Cache.class) {
			//L.x("clear cache", "bitmap cache L1 cleared");
			instance.clear();
			instance = null;
		}
	}
}
