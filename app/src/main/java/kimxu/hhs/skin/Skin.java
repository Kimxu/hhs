package kimxu.hhs.skin;


/**
 * 皮肤接口
 */
public interface Skin {
	
	
	public String getEnumName();
	
	/**
	 * 获取皮肤名称
	 * @return
	 */
	public String getName();

	/**
	 * 获取主题ID
	 * @return
	 */
	public int getThemeId();
	
	/**
	 * 获取没有阴影的主题的ID
	 * @return
	 */
	public int getNoShadowThemeId();
	
	public int getColorPrimary();
	
	public int getColorPrimaryDark();
}
