package kimxu.hhs.z_api;

public interface Configurable {
	public static final String DBCachedSqlSize= "DbCachedSqlSize";
	public static final String DbPageSize= "DbPageSize";
	public static final String zip_buffer= "zip_buffer";
	public static final String requester_count= "requester_count";
	public static final String image_cache= "image_cache";
	public static final String default_transfer_buffer_size= "default_transfer_buffer_size";
	public String getConfig(String name);
}
