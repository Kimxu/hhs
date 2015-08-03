package kimxu.hhs.skin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 当前皮肤持有者，用来获取和设置当前皮肤
 */
public class SkinHolder {
	private static Skin currentSkin;
	
	private SkinHolder(){
	}
	
	/**
	 * 设置皮肤
	 * @param shin
	 */
	public static void setSkin(Skin shin){
		SkinHolder.currentSkin = shin;
	}
	
	/**
	 * 获取当前皮肤
	 * @param context
	 * @return
	 */
	public static Skin getSkin(Context context){
		if(currentSkin == null){
			try {
				Class<?> skinManagerClass = Class.forName("com.yingyonghui.market.skin.SkinManager");
				Constructor<?> constructor = skinManagerClass.getConstructor(Context.class);
				constructor.newInstance(context);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return currentSkin;
	}
	
	public static int getColorPrimary(Context context){
		int color = Color.BLACK;
		try{
			Class<?> skinManagerClass = Class.forName("com.yingyonghui.market.skin.SkinManager");
			Constructor<?> constructor = skinManagerClass.getConstructor(Context.class);
			Method method = skinManagerClass.getMethod("getColorPrimary");
			color = (Integer) method.invoke(constructor.newInstance(context));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return color;
	}
	
	public static int getColorPrimaryDark(Context context){
		int color = Color.BLACK;
		try{
			Class<?> skinManagerClass = Class.forName("com.yingyonghui.market.skin.SkinManager");
			Constructor<?> constructor = skinManagerClass.getConstructor(Context.class);
			Method method = skinManagerClass.getMethod("getColorPrimaryDark");
			color = (Integer) method.invoke(constructor.newInstance(context));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return color;
	}
    
    /**
     * 应用皮肤
     */
    public static void applySkin(Activity activity){
    	if(activity == null){
    		new IllegalArgumentException("activity is null").printStackTrace();
    		return;
    	}
    	if(activity.getClass().isAnnotationPresent(DisableUseSkin.class)){
    		Log.d("Skin", activity.getClass().getName()+" disable use skin");
    		return;
    	}
		Skin skin = SkinHolder.getSkin(activity.getBaseContext());
		if(skin == null){
			new IllegalArgumentException("not found skin").printStackTrace();
			return;
		}
		int themeId = activity.getClass().isAnnotationPresent(NoShadowTheme.class)?skin.getNoShadowThemeId():skin.getThemeId();
		activity.setTheme(themeId);
    }
}
