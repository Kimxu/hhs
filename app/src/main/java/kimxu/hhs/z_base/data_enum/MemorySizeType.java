package kimxu.hhs.z_base.data_enum;

import java.util.Map;

import kimxu.hhs.z_api.Configurable;
import kimxu.hhs.z_volley.structure.CollectionBuilder;


public enum MemorySizeType implements Configurable {
	small(65 * (1 << 20), CollectionBuilder.<String> mapBuilder()
			.put("db_cached_sql_size", "65")
			.put("db_page_size", String.valueOf(1 << 12))
			.put("zip_buffer", String.valueOf(1 << 14))
			.put("requester_count", String.valueOf(2))
			.put("image_cache", String.valueOf(10))
			.put("default_disk_request_buffer_size", "4")
			.put("default_transfer_buffer_size", "8192").getMap()) {
	},
	medium(129 * (1 << 20), CollectionBuilder.<String> mapBuilder()
			.put("db_cached_sql_size", "75")
			.put("db_page_size", String.valueOf(1 << 14))
			.put("zip_buffer", String.valueOf(1 << 14))
			.put("requester_count", String.valueOf(3))
			.put("image_cache", String.valueOf(10))
			.put("default_disk_request_buffer_size", "5")
			.put("default_transfer_buffer_size", "12800").getMap()) {
	},
	large(513 * (1 << 20), CollectionBuilder.<String> mapBuilder()
			.put("db_cached_sql_size", "85")
			.put("db_page_size", String.valueOf(1 << 16))
			.put("zip_buffer", String.valueOf(1 << 15))
			.put("requester_count", String.valueOf(4))
			.put("image_cache", String.valueOf(8))
			.put("default_disk_request_buffer_size", "6")
			.put("default_transfer_buffer_size", "25600").getMap()) {

	},
	huge(1025 * (1 << 20), CollectionBuilder.<String> mapBuilder()
			.put("db_cached_sql_size", "95")
			.put("db_page_size", String.valueOf(1 << 18))
			.put("zip_buffer", String.valueOf(1 << 16))
			.put("requester_count", String.valueOf(4))
			.put("image_cache", String.valueOf(8))
			.put("default_disk_request_buffer_size", "7")
			.put("default_transfer_buffer_size", "51200").getMap());

	private final long threadhold;
	// private int db_page_size;
	// private int db_page_size;
	private final Map<String, String> config;
	private final static Map<String, String> defaultConfig;
	static {
		defaultConfig = CollectionBuilder.<String> mapBuilder().getMap();
	}

	private MemorySizeType(long threadhold, Map<String, String> config) {
		this.threadhold = threadhold;
		this.config = config;

	}

	public long getThreadhold() {
		return threadhold;
	}

	public static MemorySizeType getType(long size) {
		//L.x(size, small.threadhold, medium.threadhold, large.threadhold);
		if (small.threadhold > size) {
			return small;
		} else if (medium.threadhold > size) {
			return medium;
		} else if (large.threadhold > size) {
			return large;
		} else {
			return huge;
		}
	}

	@Override
	public String getConfig(String name) {
		// Map<String, String> map = config == null ? defaultConfig : config;
		String ret = null;
		if (config != null && (ret = config.get(name)) != null) {
			return ret;
		} else {
			return defaultConfig == null ? null : defaultConfig.get(name);
		}
	}
}
