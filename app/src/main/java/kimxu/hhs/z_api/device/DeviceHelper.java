package kimxu.hhs.z_api.device;


import kimxu.hhs.z_volley.base.GenericHelper;

public interface DeviceHelper extends GenericHelper {
	public String getMacAddress();
	public abstract String getSerialNumber();
	public abstract String getAndroidId();
	public abstract String getUUID();
	public abstract String getCurNetType(boolean refresh);
	public abstract String getNetMsg();
	public abstract long getMemoryAll();
	public abstract long getCurMemoryAvailable();
	public abstract String[] getDNSAddrs();
}
