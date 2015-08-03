package kimxu.hhs.z_volley.toolbox.requests;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import kimxu.hhs.z_volley.NetworkResponse;
import kimxu.hhs.z_volley.ParseError;
import kimxu.hhs.z_volley.Response;
import kimxu.hhs.z_volley.http.CommonResponceListener;
import kimxu.hhs.z_volley.toolbox.HttpHeaderParser;

/**
 *
 *
 * TODO 5.0版本图片处理
 */
public class YYHImageRequest extends ImageRequest {

	public static final int BG_RED = 254, BG_GREEN = 254, BG_BLUE = 254;

	public YYHImageRequest(String url, int maxWidth, int maxHeight,
			Bitmap.Config decodeConfig, CommonResponceListener<Bitmap> ls) {
		super(url, maxWidth, maxHeight, decodeConfig, ls);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Response<Bitmap> doParse(NetworkResponse response) {
		if (response == null) {
			return Response.error(new ParseError());
		}

		byte[] data = response.data;

		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inPreferredConfig = mDecodeConfig;
		if (getUrl().contains(".webp")) {// 在android
			// L中发现有alpha的png用565无法正常decode出来。所以png和webp都用8888解析。等5.0正式出来后再看有没有其他方法
			// || getUrl().contains(".png")
			decodeOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		}
		decodeOptions.inPurgeable = true;
		decodeOptions.inInputShareable = true;
		Bitmap bitmap = null;
		if (mMaxWidth == 0 && mMaxHeight == 0) {
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					decodeOptions);

		} else {

			// If we have to resize this image, first get the natural bounds.
			decodeOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
			int actualWidth = decodeOptions.outWidth;
			int actualHeight = decodeOptions.outHeight;

			// Then compute the dimensions we would ideally like to decode to.
			int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
					actualWidth, actualHeight);
			int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
					actualHeight, actualWidth);

			// Decode to the nearest power of two scaling factor.
			decodeOptions.inJustDecodeBounds = false;

			// TODO(ficus): Do we need this or is it okay since API 8 doesn't
			// support it?
			// decodeOptions.inPreferQualityOverSpeed =
			// PREFER_QUALITY_OVER_SPEED;
			decodeOptions.inSampleSize = findBestSampleSize(actualWidth,
					actualHeight, desiredWidth, desiredHeight);
			Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0,
					data.length, decodeOptions);

			// If necessary, scale down to the maximal acceptable size.
			if (getUrl() != null && getUrl().contains("berry")) { // 业务逻辑，banner图就不再缩了..
				bitmap = tempBitmap;
			} else {

				if (tempBitmap != null
						&& (tempBitmap.getWidth() > desiredWidth || tempBitmap
								.getHeight() > desiredHeight)) {
					bitmap = Bitmap.createScaledBitmap(tempBitmap,
							desiredWidth, desiredHeight, true);
					if (bitmap != tempBitmap)
						tempBitmap.recycle();
				} else {

					bitmap = tempBitmap;
				}
			}

		}

		if (bitmap != null && bitmap.getConfig() != null) {
			// android.util.Log.e("volley",getUrl()+" "+bitmap.getByteCount()+" "+bitmap.getConfig().toString());
			if (bitmap.getConfig().compareTo(Bitmap.Config.ARGB_8888) == 0
					&& ((getUrl().contains(".webp")))) {// 对webp和png中ArGB888的bitmap进行转换，缩小一半大小
				// || getUrl().contains(".png")
				Bitmap convertTemp = bitmap;
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();
				int[] colorPixels = new int[w * h];
				bitmap.getPixels(colorPixels, 0, w, 0, 0, w, h);
				double alpha;
				double rate;
				double otherRate; // 存储1-rate的值，因为某些手机浮点运算有问题，特单独列一个值，特蛋疼！！！
									// gaoyue 到此一游
				int red;
				int green;
				int blue;

				// int length = colorPixels.length
				for (int i = 0; i < colorPixels.length; i++) {
					alpha = Color.alpha(colorPixels[i]); // 得到alpha

					rate = alpha / 255.0;// /得到aloha比率
					otherRate = 1 - rate;
					red = (int) (Color.red(colorPixels[i]) * rate + BG_RED
							* (otherRate));
					green = (int) (Color.green(colorPixels[i]) * rate + BG_GREEN
							* (otherRate));
					blue = (int) (Color.blue(colorPixels[i]) * rate + BG_BLUE
							* (otherRate));
					colorPixels[i] = Color.rgb(red, green, blue);
				}

				bitmap = Bitmap.createBitmap(colorPixels, w, h, Bitmap.Config.RGB_565);
				if (!convertTemp.isRecycled() && convertTemp != bitmap) {
					convertTemp.recycle();
				}
				// android.util.Log.e("volley_test",getUrl()+" "+bitmap.getByteCount()+" "+bitmap.getConfig().toString());
			}
		}

		if (bitmap == null) {
			return Response.error(new ParseError());
		} else {
			return Response.success(bitmap,
					HttpHeaderParser.parseCacheHeaders(response));
		}
	}
}
