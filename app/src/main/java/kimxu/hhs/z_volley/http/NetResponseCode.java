package kimxu.hhs.z_volley.http;

import java.util.Map;

import kimxu.hhs.z_volley.structure.CollectionBuilder;


public enum NetResponseCode {

	no_reponse(0, 0, "网络无响应"),
	/*
	 * 
	 */
	processing(100, 1, "请求继续进行中"),
	/*
	 * 
	 */
	success(200, 2, "请求成功"),
	/*
		 * 
		 */
	redirect(300, 3, "请求重定向"),
	/*
			 * 
			 */
	found(302, "请求位置已经找到，正在转到位置"),
	/*
				 * 
				 */
	see_other(303, "请求位置已经转移，正在转到位置"),
	/*
					 * 
					 */
	temperory_redirect(307, "请求位置暂时转移，正在转到位置"),
	/*
			 * 
			 */
	not_modified(304, "请求无变化"),
	/*
				 * 
				 */
	bad_request(400, 4, "请求有误，无法执行"),
	/*
	 * 
	 */
	unauthorized(401, "请求需要权限认证"),
	/*
	 * 
	 */
	forbidden(403, "请求了无权限访问的位置"),
	/*
			 * 
			 */
	not_found(404, "请求了无效位置"),
	/*
 * 
 */
	not_accepted(406, "请求的参数不合法"),
	/*
	 * 
	 */
	request_timeout(408, "客户端请求发送超时"),
	/*
			 * 
			 */
	entity_too_large(413, "请求的数据太大"),
	/*
 * 
 */
	uri_too_long(414, "请求的地址长度超标"),
	/*
 * 
 */
	unsupported_media_type(415, "请求了不支持的文件类型"),
	/*
			 * 
			 */
	range_not_satisfiable(416, "请求的文件内部的字节位置无法找到"),
	/*
 * 
 */
	server_error(500, 5, "服务器内部错误"),
	/*
	 * 
	 */
	internal_server_error(500, "服务器内部错误"),
	/*
			 * 
			 */
	bad_gateway(502, "网关出错"),
	/*
 * 
 */
	gateway_timeout(504, "网关超时"),
	/*
 * 
 */
	time_out(600, 6, "请求超时，服务器无应答"),
	/*
			 * 
			 */
	too_much_processing(601, "请求处理步骤过多"),
	/*
 * 
 */
	unable_to_resolve(700, 7, "请求得到的数据无法解析"),
	/*
			 * 
			 */
	undefined(-1, ""), ;
	public static final int SUCCESS_FAMILY = 2;
	public static final int REDIRECT_FAMILY = 3;
	public static final int WRONG_REQUEST_FAMILY = 4;
	public static final int SERVER_ERROR_FAMILY = 5;
	public static final int TIME_OUT_FAMILY = 6;
	public static final int UNKNOWN_FAMILY = 7;
	private int status = -1;
	private int start, codeFamily;
	private boolean genericType;
	private static final Map<Integer, NetResponseCode> statusMap = CollectionBuilder
			.newHashMap();
	private static final Map<Integer, NetResponseCode> statusesMap = CollectionBuilder
			.newHashMap();
	private static volatile boolean inited = false;
	private final String msg;

	private NetResponseCode(int status, String msg) {
		this.status = status;
		genericType = false;
		this.codeFamily = (int) Math.floor(status / 100);
		this.msg = msg;
		// statusMap.put(status, this);
	}

	private NetResponseCode(int statusStart, int codeFamily, String msg) {
		this.start = statusStart;
		this.codeFamily = codeFamily;
		genericType = true;
		this.status=statusStart;
		this.msg = msg;
	}

	public int getCodeFamily() {
		return codeFamily;
	}

	public int getStatus() {
		return status;
	}

	public boolean isGenericType() {
		return genericType;
	}

	public static NetResponseCode toStatus(int status) {
		if (!inited) {
			buildMap();
		}
		if (statusMap.containsKey(status)) {
			return statusMap.get(status);
		} else {
			int nn = (int) Math.floor(status / 100);
			if (statusesMap.containsKey(nn)) {
				return statusesMap.get(nn);
			}
		}
		return undefined;
	}

	private synchronized static void buildMap() {
		NetResponseCode[] vs = NetResponseCode.values();
		for (NetResponseCode v : vs) {
			int n = v.getStatus();
			if (n >= 0) {
				statusMap.put(n, v);
			} else {
				int nn = v.getCodeFamily();
				statusesMap.put(nn, v);
			}
		}
		inited = true;
	}

	public String getMsg() {
		return msg;
	}
}
