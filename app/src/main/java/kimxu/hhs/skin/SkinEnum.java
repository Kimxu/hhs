package kimxu.hhs.skin;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import kimxu.hhs.Config;
import kimxu.hhs.R;
import kimxu.hhs.utils.ColorUtil;

/**
 * 皮肤枚举对象
 */
public enum SkinEnum implements Skin{
	
	DEFAULT("\ue60d\ue614", R.style.Skin_Light, R.style.Skin_Light_NoShadow, Color.parseColor("#F1F1F1"), Config.SKIN_SELECTED_DEFAULT){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#F1F1F1"));
		}
	},
	
	HUANG_NIE("\ue609\ue60e", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#FFDA44"), Config.SKIN_SELECTED_HUANG_NIE){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#FFDA44"));
		}
	},
	
	ZHONG_HONG("\ue619\ue608", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#DB4D6D"), Config.SKIN_SELECTED_ZHONG_HONG){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#DB4D6D"));
		}
	},
	
	YIN_ZHU("\ue618\ue61a", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#C73E3A"), Config.SKIN_SELECTED_YIN_ZHU){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#C73E3A"));
		}
	},
	
	PU_TAO("\ue610\ue611", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#7B0051"), Config.SKIN_SELECTED_PU_TAO){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#7B0051"));
		}
	},
	
	QIAN_CONG("\ue612\ue605", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#00D1C1"), Config.SKIN_SELECTED_QIAN_CONG){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#00D1C1"));
		}
	},
	
	LU_CAO("\ue60c\ue602", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#0382da"), Config.SKIN_SELECTED_LU_CAO){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#0382da"));
		}
	},
	
	CHANG_PAN("\ue603\ue60f", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#1b813E"), Config.SKIN_SELECTED_CHANG_PAN){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#1b813E"));
		}
	},
	
	QING_BI("\ue613\ue601", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#007A87"), Config.SKIN_SELECTED_QING_BI){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#007A87"));
		}
	},
	
	CHONG_AO("\ue604\ue600", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#20604F"), Config.SKIN_SELECTED_CHONG_AO){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#20604F"));
		}
	},
	
	HONG_BI("\ue608\ue601", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#7B90D2"), Config.SKIN_SELECTED_HONG_BI){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#7B90D2"));
		}
	},
	
	TENG_SHU("\ue616\ue615", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#6E75A4"), Config.SKIN_SELECTED_TENG_SHU){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#6E75A4"));
		}
	},
	
	LIU_LI("\ue60b\ue60a", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#005caf"), Config.SKIN_SELECTED_LIU_LI){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#005caf"));
		}
	},
	
	GAN_QING("\ue606\ue613", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#113285"), Config.SKIN_SELECTED_GAN_QING){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#113285"));
		}
	},
	
	HEI_XIANG("\ue607\ue617", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#101518"), Config.SKIN_SELECTED_HEI_XIANG){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#101518"));
		}
	},
	
	LIGHT("亮色", R.style.Skin_Light, R.style.Skin_Light_NoShadow, Color.parseColor("#F1F1F1"), Config.SKIN_SELECTED_LIGHT){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#ffffff"));
		}
	},
	
	DARK("暗色", R.style.Skin_Dark, R.style.Skin_Dark_NoShadow, Color.parseColor("#2E3038"), Config.SKIN_SELECTED_DARK){
		@Override
		public Drawable getPreviewDrawable(Context context) {
			return new ColorDrawable(Color.parseColor("#000000"));
		}
	};
	
	private String name;
	private int themeId;
	private int noShadowThemeId;
	private int colorPrimary;
	private String umengLog;
	
	private SkinEnum(String name, int themeId, int noShadowThemeId, int colorPrimary, String umengLog) {
		this.name = name;
		this.themeId = themeId;
		this.noShadowThemeId = noShadowThemeId;
		this.colorPrimary = colorPrimary;
		this.umengLog = umengLog;
	}
	
	public String getEnumName() {
		return this.name();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getThemeId() {
		return themeId;
	}

	@Override
	public int getNoShadowThemeId() {
		return noShadowThemeId;
	}
	
	public String getUmengLog() {
		return umengLog;
	}
	
	public int getColorPrimary() {
		return colorPrimary;
	}
	
	public int getColorPrimaryDark(){
		return ColorUtil.darken(colorPrimary);
	}

	/**
	 * 获取在主题切换页面显示的预览图片
	 * @param context
	 * @return
	 */
	public abstract Drawable getPreviewDrawable(Context context);
}