package kimxu.hhs.z_api.network;

import java.util.Map;

import kimxu.hhs.Config;
import kimxu.hhs.z_volley.structure.CollectionBuilder;
import kimxu.hhs.z_volley.tools.StringHelper;

public enum Host_URL {
	/**
	 * 
	*/
	upload_image("market/api?api=market.MarketAPI&param={param}", 1),
	/**
	 * 
	*/
	log_api("", 0),
	/**
	 * 
	 */
	resource_api("market/api", 0),
	/**
	 * 
	 */
	sina_api("https://api.weibo.com/2/statuses/upload_url_text.json", 2),
	/**
	 * 
	 */
	download_debug("http://debug.appchina.com/debug/debug/download", 2);
	public static final int
	/** ?a=b&d=c */
	GET_TYPE = 3,
	/** ?param={} */
	GET_PARAM_TYPE = 1,
	/** post */
	POST_TYPE = 0,
	/** host include */
	PURE_TYPE = 2;
	private final String url;
	private final int urlType;

	public static final String HOST = Config.getInstance().get("api.host")
			.toString();

	private Host_URL(String s, int type) {
		url = s;
		urlType = type;

	}

	String getPureUrl() {
		return url;
	}

	String getRawUrl(String host) {
		return StringHelper.genStr(host, url, null);
	}

	public String getUrl(String host, Map<String, Object> param) {
		// String u=null;
		switch (urlType) {
		case POST_TYPE:
			return StringHelper.genStr(host, url, null);
		case GET_TYPE:
			return StringHelper.genStr(host, url, param);
		case GET_PARAM_TYPE:
			return genParamUrl(host, param);
		case PURE_TYPE:
			return url;
		default:
			return null;
		}

	}

	String genParamUrl(String host, Map<String, Object> param) {
		String pstr = StringHelper.JsonHelper.toJson(param);
		pstr = StringHelper.URLGenerator.encodeUrl(pstr);
		Map<String, Object> data = CollectionBuilder.<Object> mapBuilder()
				.put("param", pstr).getMap();
		String u = StringHelper.genStr(host, url, data);
		//L.x("network", "url build ", u);
		return u;
	}
}