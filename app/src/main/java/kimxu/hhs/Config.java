package kimxu.hhs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by xuzhiguo on 15/7/29.
 */
public final class Config {
    private static volatile Config config;
    private final Properties p;
    public static Config getInstance() {
        if (config == null) {
            synchronized (Config.class) {
                config = new Config();
            }
        }
        return config;
    }

    private Config() {
        p = new Properties();
        // try {
        //
        // p.load(Config.class.getClassLoader().getResourceAsStream(
        // "com/app/china/base/config.properties"));
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

    public Object get(String key) {
        Object ret = p.get(key);
        return ret == null ? "" : ret;
    }

    public Object set(String key, Object value) {
        return p.put(key, value);
    }

    public void loadResource(InputStream is) throws IOException {
        p.load(is);
    }



    public static final String PREFERENCE_ID = "preference_id";



    public static final String UUID = "uuid";

    public static final String ADD_PARAM = "add_param";




    /** banner */
    public static final int SHOW_PLACE_BANNER = 0000000;




    // 皮肤选中 - 默认的
    public static final String SKIN_SELECTED_DEFAULT = "skin_selected_default";

    // 皮肤选中 - 都市黑
    public static final String SKIN_SELECTED_BLACK = "skin_selected_black";

    // 皮肤选中 - 锈桔梗
    public static final String SKIN_SELECTED_DARK_BLUE = "skin_selected_dark_blue";

    // 皮肤选中 - 竹叶绿
    public static final String SKIN_SELECTED_BAMBOO_LEAF_GREEN = "skin_selected_bamboo_leaf_green";

    // 皮肤选中 - 高贵紫
    public static final String SKIN_SELECTED_PURPLE = "skin_selected_purple";

    // 皮肤选中 - 深海蓝
    public static final String SKIN_SELECTED_MARINE_BLUE = "skin_selected_marine_blue";

    // 皮肤选中 - 草莓红
    public static final String SKIN_SELECTED_STRAWBERRY_RED = "skin_selected_strawberry_red";

    // 皮肤选中 - 珊瑚红
    public static final String SKIN_SELECTED_CORAL_RED = "skin_selected_coral_red";

    // 皮肤选中 - 霓虹紫
    public static final String SKIN_SELECTED_NEON_PURPLE = "skin_selected_neon_purple";

    // 皮肤选中 - 汇汇黄
    public static final String SKIN_SELECTED_HUI_HUI_YELLOW = "skin_selected_hui_hui_yellow";

    // 皮肤选中 - 炫星空
    public static final String SKIN_SELECTED_STARRY_SKY = "skin_selected_starry_sky";

    // 皮肤选中 - 沙滩
    public static final String SKIN_SELECTED_SANDY_BEACH = "skin_selected_sandy_beach";

    // 皮肤选中 - 黄蘖
    public static final String SKIN_SELECTED_HUANG_NIE = "skin_selected_huang_nie";

    // 皮肤选中 - 中红
    public static final String SKIN_SELECTED_ZHONG_HONG = "skin_selected_zhong_hong";

    // 皮肤选中 - 银朱
    public static final String SKIN_SELECTED_YIN_ZHU = "skin_selected_yin_zhu";

    // 皮肤选中 - 葡萄
    public static final String SKIN_SELECTED_PU_TAO = "skin_selected_pu_tao";

    // 皮肤选中 - 浅葱
    public static final String SKIN_SELECTED_QIAN_CONG = "skin_selected_qian_cong";

    // 皮肤选中 - 露草
    public static final String SKIN_SELECTED_LU_CAO = "skin_selected_lu_cao";

    // 皮肤选中 - 常磐
    public static final String SKIN_SELECTED_CHANG_PAN = "skin_selected_chang_pan";

    // 皮肤选中 - 青碧
    public static final String SKIN_SELECTED_QING_BI = "skin_selected_qing_bi";

    // 皮肤选中 - 虫奥
    public static final String SKIN_SELECTED_CHONG_AO = "skin_selected_chong_ao";

    // 皮肤选中 - 红碧
    public static final String SKIN_SELECTED_HONG_BI = "skin_selected_hong_bi";

    // 皮肤选中 - 藤鼠
    public static final String SKIN_SELECTED_TENG_SHU = "skin_selected_teng_shu";

    // 皮肤选中 - 琉璃
    public static final String SKIN_SELECTED_LIU_LI = "skin_selected_liu_li";

    // 皮肤选中 - 绀青
    public static final String SKIN_SELECTED_GAN_QING = "skin_selected_gan_qing";

    // 皮肤选中 - 黑橡
    public static final String SKIN_SELECTED_HEI_XIANG = "skin_selected_hei_xiang";

    // 皮肤选中 - 亮色
    public static final String SKIN_SELECTED_LIGHT = "skin_selected_light";

    // 皮肤选中 - 暗色
    public static final String SKIN_SELECTED_DARK = "skin_selected_dark";
}
