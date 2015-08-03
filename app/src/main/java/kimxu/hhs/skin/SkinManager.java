package kimxu.hhs.skin;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * 皮肤管理器，用来更改皮肤和获取所有皮肤
 */
public class SkinManager {
	private  static final String KEY_SKIN = "PREF_KEY_SKIN";
	private static final String COLOR_PRIMARY = "PREF_KEY_COLOR_PRIMARY";
	private static final String COLOR_PRIMARY_DARK = "PREF_KEY_COLOR_PRIMARY_DARK";
	private SharedPreferences preferences;
	private Context context;
	
	public SkinManager(Context context){
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String skinName = preferences.getString(KEY_SKIN, SkinEnum.LU_CAO.name());
		SkinHolder.setSkin(SkinEnum.valueOf(skinName));
		this.context = context;
	}
	
	/**
	 * 设置皮肤
	 * @param skinEnum
	 */
	public void setSkin(SkinEnum skinEnum){
		preferences.edit().putString(KEY_SKIN, skinEnum.name()).commit();
		SkinHolder.setSkin(skinEnum);
		setColorPrimary(skinEnum.getColorPrimary());
		setColorPrimaryDark(skinEnum.getColorPrimaryDark());
	}
	
	public void setColorPrimary(int color){
		preferences.edit().putInt(COLOR_PRIMARY, color).commit();
	}
	
	public void setColorPrimaryDark(int color){
		preferences.edit().putInt(COLOR_PRIMARY_DARK, color).commit();
	}
	
	public int getTabStripColor(){
		Skin skin = SkinHolder.getSkin(context);
		if(TextUtils.equals(skin.getEnumName(), "LIGHT") || TextUtils.equals(skin.getEnumName(), "DEFAULT")) {
			return Color.parseColor("#1588FE");
		} else if(TextUtils.equals(skin.getEnumName(), "DARK")) {
			return preferences.getInt(COLOR_PRIMARY, Color.parseColor("#2E3038"));
		} else {
			return skin.getColorPrimary();
		}
	}
	
	public int getColorPrimary(){
		Skin skin = SkinHolder.getSkin(context);
		if(TextUtils.equals(skin.getEnumName(), "LIGHT")) {
			return preferences.getInt(COLOR_PRIMARY, Color.parseColor("#F1F1F1"));
		} else if(TextUtils.equals(skin.getEnumName(), "DARK")) {
			return preferences.getInt(COLOR_PRIMARY, Color.parseColor("#2E3038"));
		} else {
			return skin.getColorPrimary();
		}
	}
	
	public int getColorPrimaryDark(){
		Skin skin = SkinHolder.getSkin(context);
		if(TextUtils.equals(skin.getEnumName(), "LIGHT")) {
			return preferences.getInt(COLOR_PRIMARY_DARK, Color.parseColor("#F1F1F1"));
		} else if(TextUtils.equals(skin.getEnumName(), "DARK")) {
			return preferences.getInt(COLOR_PRIMARY_DARK, Color.parseColor("#2E3038"));
		} else {
			return skin.getColorPrimaryDark();
		}
	}
	
	/**
	 * 获取所有皮肤
	 * @return
	 */
	public SkinEnum[] getSkins(){
		return SkinEnum.values();
	}
	
	/**
	 * 获取一个图片
	 * @param context
	 * @param attrId
	 * @return
	 */
	public static Drawable getDrawable(Context context, int attrId){
		Drawable result = null;
		TypedArray typedArray = context.obtainStyledAttributes(SkinHolder.getSkin(context).getThemeId(), new int[]{attrId});
		if(typedArray == null){
			return null;
		}
		result = typedArray.getDrawable(0);
		typedArray.recycle();
		return result;
	}
	
	/**
	 * 是默认的
	 * @param context
	 * @return
	 */
	public static boolean isDefault(Context context){
		return SkinEnum.LU_CAO == SkinHolder.getSkin(context);
	}
	
	public static boolean isLight(Context context){
		return SkinHolder.getSkin(context) == SkinEnum.DEFAULT || SkinHolder.getSkin(context) == SkinEnum.LIGHT;
	}
	
	public static boolean isDark(Context context){
		return !isLight(context);
	}
	
}
