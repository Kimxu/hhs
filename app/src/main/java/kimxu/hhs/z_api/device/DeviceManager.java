package kimxu.hhs.z_api.device;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import kimxu.hhs.basic.SSHApplication;
import kimxu.hhs.z_base.AbstractManager;
import kimxu.hhs.z_api.Api;
import kimxu.hhs.z_api.pref.PreferanceHelper;
import kimxu.hhs.z_base.ConstantValues;

public final class DeviceManager extends AbstractManager implements
		DeviceHelper {
	private static volatile DeviceHelper instance;

	public static DeviceHelper getInstance() {
		if (instance == null) {
			synchronized (DeviceManager.class) {
				instance = new DeviceManager();
			}
		}
		return instance;
	}

	private WifiManager wm;
	private PreferanceHelper ph;
	private ConnectivityManager cm;
	private Runtime runtime;
	private String macAddress;
	private String mUUIDString;
	private String curNetType;
	private List<String> allNetType;

	private DeviceManager() {
		wm = (WifiManager) SSHApplication.getInstance().getSystemService(
				Context.WIFI_SERVICE);
		ph = (PreferanceHelper) Api.pref.getHandler();
		cm = (ConnectivityManager) SSHApplication.getAppContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		runtime = Runtime.getRuntime();
	}

	@Override
	public String getMacAddress() {
		if (macAddress == null) {
			WifiInfo info = wm.getConnectionInfo();
			if (info != null) {
				macAddress = info.getMacAddress();
			}
			// return null;
		}
		return macAddress;

	}

	@Override
	public String getSerialNumber() {
		String serialNumber = "";

		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class);
			serialNumber = (String) get.invoke(c, "ro.serialno");

			if (serialNumber.equals("")) {
				serialNumber = "?";
			}
		} catch (Exception e) {
			if (serialNumber.equals("")) {
				serialNumber = "?";
			}
		}

		return serialNumber;
	}

	@Override
	public String getAndroidId() {
		return Secure.getString(SSHApplication.getAppContext()
				.getContentResolver(), Secure.ANDROID_ID);
	}

	@Override
	public String getUUID() {
		if (mUUIDString != null) {
			return mUUIDString;
		}

		String uuidString = ph.getString(null, ConstantValues.UUID, null);
		if (uuidString != null) {
			mUUIDString = uuidString;
		} else {
			String mac = this.getMacAddress();
			UUID uuid;
			if (TextUtils.isEmpty(mac)) {
				uuid = UUID.randomUUID();
			} else {
				uuid = UUID.nameUUIDFromBytes(mac.getBytes());
			}
			mUUIDString = uuid.toString();
			ph.putObject(null, ConstantValues.UUID, mUUIDString);
		}

		return mUUIDString;
	}

	@Override
	public String getCurNetType(boolean refresh) {
		if (refresh || curNetType == null) {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isConnectedOrConnecting()) {
				curNetType = info.getTypeName();
			} else {
				curNetType = "!None!";
			}
		}
		return curNetType;
	}

	@Override
	public String getNetMsg() {
		// NetworkInfo info = cm.getActiveNetworkInfo();
		StringBuilder builder = new StringBuilder();
		// if (info != null) {
		// if (info.isConnectedOrConnecting()) {

		builder.append("active").append(":").append(this.getCurNetType(true))
				.append(",all:");
		NetworkInfo[] infos = cm.getAllNetworkInfo();
		for (NetworkInfo info : infos) {
			// if (info.is) {
			builder.append(info.getTypeName()).append("/");
			// }
		}
		// }
		// }
		return builder.toString();
	}

	@Override
	public long getCurMemoryAvailable() {
		return runtime.freeMemory();
	}

	@Override
	public long getMemoryAll() {
		return runtime.totalMemory();
	}

	@Override
	public String[] getDNSAddrs() {
		try {
			ContentResolver cr = SSHApplication.getAppContext()
					.getContentResolver();
			String dns1 = android.provider.Settings.System.getString(cr,
					android.provider.Settings.System.WIFI_STATIC_DNS1);
			String dns2 = android.provider.Settings.System.getString(cr,
					android.provider.Settings.System.WIFI_STATIC_DNS2);
			return new String[] { dns1, dns2 };
		} catch (Exception e) {
			//L.e(e);
		}
		return null;
	}

}
