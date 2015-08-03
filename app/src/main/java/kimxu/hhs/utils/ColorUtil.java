package kimxu.hhs.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.util.Random;

public class ColorUtil {
	
	public static final int[] BASIC_RANDOM_COLORS = new int[]{
    	Color.parseColor("#007a87"), Color.parseColor("#7b0051"), Color.parseColor("#1b67ac"), Color.parseColor("#638962")};
	
	public static final int desaturation(int color, float rate) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[1] = hsv[1] * rate;
		return Color.HSVToColor(hsv);
	}
	
	public static final int desaturation(int color) {
		return desaturation(color, 0.8f);
	}

	public static final int darken(int color, float rate) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] = hsv[2] * rate;
		return Color.HSVToColor(hsv);
	}
	
	public static final int darken(int color) {
		return darken(color, 0.8f);
	}
	
	public static Bitmap updateBitmapColor(Bitmap bitmap, int r, int g, int b) {
		if (bitmap != null) {
			Bitmap tmp = bitmap.copy(Config.ARGB_8888,
					true);
			Canvas c = new Canvas(tmp);
			Paint mPaint = new Paint();
			mPaint.setAntiAlias(true);
			float[] f = { 0f, 0f, 0f, 0f, r, 0f, 0f, 0f, 0f, g, 0f, 0f, 0f,
					0f, b, 0f, 0f, 0f, 1f, 0f };
			ColorMatrix cm = new ColorMatrix(f);
			mPaint.setColorFilter(new ColorMatrixColorFilter(cm));
			c.drawBitmap(tmp, 0, 0, mPaint);
			return tmp;
		}
		return null;
	}
	
	public static int randomBasicColorIndex(int indexBefore){
		int resultIndex;
		Random rand = new Random();
		resultIndex = indexBefore + rand.nextInt(BASIC_RANDOM_COLORS.length - 2) + 1;
		return resultIndex % BASIC_RANDOM_COLORS.length;
	}
}
