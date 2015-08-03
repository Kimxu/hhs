package kimxu.hhs.z_volley.http;

import java.util.Map;

import kimxu.hhs.z_api.Api;
import kimxu.hhs.z_api.device.DeviceHelper;
import kimxu.hhs.z_volley.structure.CollectionBuilder;

public final class CommonParams {
	private final Map<String, String> params;

	private static CommonParams ins;

	private CommonParams() {
		DeviceHelper dh;
		dh = (DeviceHelper) Api.device.getHandler();
		params = CollectionBuilder.<String> mapBuilder()
				.put("api", "market.MarketAPI").put("key", "")
				.put("deviceId", dh.getSerialNumber())
				.put("referer", dh.getUUID()).getMap();
	}

	public static void addCommonParams(Map<String, String> mp) {
		if (ins == null) {
			synchronized (CommonParams.class) {
				ins = new CommonParams();
			}
		}
		mp.putAll(ins.params);
	}

}
