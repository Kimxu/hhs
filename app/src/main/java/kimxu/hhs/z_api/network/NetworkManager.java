package kimxu.hhs.z_api.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Set;

import kimxu.hhs.basic.SSHApplication;
import kimxu.hhs.z_base.AbstractManager;

public class NetworkManager extends AbstractManager implements
		NetworkInfoHelper {

	private ConnectivityManager cm;

	private static volatile NetworkManager instance;

	public static NetworkManager getInstance() {
		if (instance == null) {
			synchronized (NetworkManager.class) {
				instance = new NetworkManager();
			}
		}
		return instance;
	}

	private NetworkManager() {
		cm = ((ConnectivityManager) SSHApplication.getAppContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE));
	}

	public byte[] checkFinalIp(URI uri) {
		if (uri != null) {
			try {
				InetAddress ip = InetAddress.getByName(uri.getHost());
				ip.getCanonicalHostName();
				return ip.getAddress();
			} catch (UnknownHostException e) {
				//L.e(e);
				return new byte[] {};
			}
		} else {
			return new byte[] {};
		}
		// this.finalIp = finalIp;
	}

	@Override
	public boolean requestRouteToHost(int networkType, int hostAddress) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Integer> getAllActiveNetworkType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkConnectivity(int networkType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWifi() {
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}

		if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}
}
