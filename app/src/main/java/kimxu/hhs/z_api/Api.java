/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kimxu.hhs.z_api;

import android.content.Context;

import java.util.Map;

import kimxu.hhs.z_api.device.DeviceHelper;
import kimxu.hhs.z_api.device.DeviceManager;
import kimxu.hhs.z_api.device.DisplayHelper;
import kimxu.hhs.z_api.device.DisplayManager;
import kimxu.hhs.z_api.http.HttpManager;
import kimxu.hhs.z_api.network.NetworkInfoHelper;
import kimxu.hhs.z_api.network.NetworkManager;
import kimxu.hhs.z_api.packs.PackManager;
import kimxu.hhs.z_api.packs.PacksHelper;
import kimxu.hhs.z_api.pref.PreferanceHelper;
import kimxu.hhs.z_api.pref.PreferanceManager;
import kimxu.hhs.z_api.zip.ZipHelper;
import kimxu.hhs.z_api.zip.ZipManager;
import kimxu.hhs.z_volley.base.GenericHelper;
import kimxu.hhs.z_volley.http.HttpRequestHelper;


public enum Api {


	http() {
		@Override
		public HttpRequestHelper getHandler() {
			return HttpManager.getInstance();
		}
	},

	device() {

		public DeviceHelper getHandler() {
			return DeviceManager.getInstance();
		}

	},

	display() {

		public DisplayHelper getHandler() {
			// TODO Auto-generated method stub
			return DisplayManager.getInstance();
		}

		public GenericHelper getHandler(int type, Map<String, Object> args) {
			if (args != null && args.size() > 0) {
				Object obj = args.get("context");
				if (obj != null && obj instanceof Context) {
					Context c = (Context) obj;
					return DisplayManager.getInstance(c);
				}
			}
			return null;
		}

	},
	netInfo() {

		@Override
		public NetworkInfoHelper getHandler() {

			return NetworkManager.getInstance();
		}

	},

	pref() {

		public PreferanceHelper getHandler() {
			return PreferanceManager.getInstance();
		}

	},
	pack() {

		@Override
		public PacksHelper getHandler() {
			return PackManager.getInstance();
		}

	},

	zip() {

		@Override
		public ZipHelper getHandler() {
			// TODO Auto-generated method stub
			return ZipManager.getInstance();
		}

	}


	;

public abstract GenericHelper getHandler();

	public GenericHelper getHandler(int type, Map<String, Object> args) {
		return null;
	}
}
