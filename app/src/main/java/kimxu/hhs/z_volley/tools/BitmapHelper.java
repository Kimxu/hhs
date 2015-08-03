package kimxu.hhs.z_volley.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.Future;

import kimxu.hhs.basic.SSHApplication;
import kimxu.hhs.z_api.Api;
import kimxu.hhs.z_api.packs.PacksHelper;
import kimxu.hhs.z_api.zip.ZipHelper;
import kimxu.hhs.z_base.data.DefaultBitmapEnum;
import kimxu.hhs.z_volley.structure.ImageL1Cache;
import kimxu.hhs.z_volley.toolbox.thread_pool.CommonThreadPoolFactory;

public final class BitmapHelper {
	static final Context context = SSHApplication.getAppContext();
	// static volatile ScheduledExecutorService exec;
	static final Handler mainHandler = new Handler(Looper.getMainLooper());

	public static Bitmap decodeRes(int resId) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		return BitmapFactory.decodeStream(context.getResources()
				.openRawResource(resId), null, options);
	}

	private static class BitmapController {
		final Future<?> f;
		final String mUrl;

		public BitmapController(Future<?> f, String mUrl) {
			super();
			this.f = f;
			this.mUrl = mUrl;
		}

	}

	private static class BitmapLoadTask implements Runnable {
		private final String mUrl;
		private final String fPath;
		private final WeakReference<ImageView> viewRef;
		static PacksHelper ph = (PacksHelper) Api.pack.getHandler();
		static ZipHelper zh = (ZipHelper) Api.zip.getHandler();

		public BitmapLoadTask(String packageName, String fPath, ImageView view) {
			super();
			this.mUrl = packageName;
			this.fPath = fPath == null ? null : fPath.trim();
			this.viewRef = new WeakReference<ImageView>(view);
		}

		@Override
		public void run() {
			Bitmap bitmap = null;
			if (mUrl.contains("/") && mUrl.endsWith(".apk")) { // local apk
																// filepath
				Drawable icon = ph.getAppIconFromFile(mUrl);
				bitmap = getBitmapFromIcon(icon);
			} else if (mUrl.contains("/") && mUrl.endsWith(".xpk")) {// local
																		// xpk
				try {
					byte[] bs = zh.unzip(mUrl, "icon.png");
					bitmap = getFromBytes(bs);
				} catch (FileNotFoundException e) {
					//L.x(e);
				} catch (IOException e) {
					//L.x(e);
				}
				// bitmap = XPKUtil.getBitmapIcon(mUrl);

			} else { // local packageName

				Drawable icon = ph.getAppIcon(mUrl);

				if (icon == null && fPath != null) {
					if (fPath.endsWith(".xpk")) {
						try {
							byte[] bs = zh.unzip(fPath, "icon.png");
							bitmap = getFromBytes(bs);
						} catch (FileNotFoundException e) {
							//L.x(e);
						} catch (IOException e) {
							//L.x(e);
						}
					} else if (fPath.endsWith(".apk")) {
						icon = ph.getAppIconFromFile(fPath);
					}
				}

				bitmap = getBitmapFromIcon(icon);

			}
			final Bitmap bp = bitmap;
			ImageL1Cache cache = ImageL1Cache.getInstance();
			if (bp != null)
				cache.putBitmap(mUrl, bp);
			if (viewRef != null) {
				final ImageView im = viewRef.get();
				if (im != null) {
					mainHandler.post(new Runnable() {

						@Override
						public void run() {
							if (bp == null) {
								im.setImageBitmap(DefaultBitmapEnum.sDefaultIcon
										.getBitmap());
							} else {
								im.setImageBitmap(bp);
							}
						}
					});
				}
			}

		}
	}

	public static void loadFromLocal(String packageName, ImageView imageView) {
		loadFromLocal(packageName, imageView, null);
	}

	public static void loadFromLocal(String packageName, ImageView imageView,
			String apkFilePath) {
		if (imageView == null) {
			return;
		}
		if (packageName == null) {
			imageView
					.setImageBitmap(DefaultBitmapEnum.sDefaultIcon.getBitmap());
			return;
		}
		ImageL1Cache cache = ImageL1Cache.getInstance();
		Bitmap bitmap = cache.getBitmap(packageName);
		Object tag = imageView.getTag();
		if (bitmap == null) {
			// 图片为空，执行线程
			imageView
					.setImageBitmap(DefaultBitmapEnum.sDefaultIcon.getBitmap());
			if (tag != null && tag instanceof BitmapController) {
				BitmapController con = (BitmapController) tag;
				// 当前该view执行的task是正确的，不做任何操作
				if (con.mUrl.equals(packageName)) {
					return;
				} else {
					// 取消task
					con.f.cancel(true);

				}
			}
			// 为该view加入一条新的task
			startLoadTask(packageName, imageView, apkFilePath);
		} else {// 直接加载图片

			if (tag != null && tag instanceof BitmapController) {// 如果还有task在执行，取消该task
				// ((BitmapLoadTask) imageView.getTag()).cancel(true);
				BitmapController con = (BitmapController) imageView.getTag();
				con.f.cancel(true);
				imageView.setTag(null);
			}
			imageView.setImageBitmap(bitmap);
		}
	}

	private static void startLoadTask(String packageName, ImageView imageView,
			String apkFilePath) {
		// // if (exec == null) {
		// synchronized (BitmapHelper.class) {
		// exec = CommonThreadPoolFactory.getDefaultExecutor();
		// }
		// }
		BitmapLoadTask task = new BitmapLoadTask(packageName, apkFilePath,
				imageView);
		Future<?> f = CommonThreadPoolFactory.getDefaultExecutor().submit(task);
		BitmapController con = new BitmapController(f, packageName);
		imageView.setTag(con);
	}

	public static Bitmap getBitmapFromIcon(Drawable icon) {
		Bitmap bitmap = null;
		if (icon != null && icon instanceof BitmapDrawable) {
			bitmap = ((BitmapDrawable) icon).getBitmap();
		}
		return bitmap;
	}

	public static Bitmap getFromBytes(byte[] bytes) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}
}
