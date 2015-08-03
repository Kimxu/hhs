package kimxu.hhs.z_base;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import java.util.Set;

import kimxu.hhs.basic.SSHApplication;
import kimxu.hhs.z_api.Api;
import kimxu.hhs.z_api.pref.PreferanceHelper;
import kimxu.hhs.z_volley.structure.CollectionBuilder;
//import com.app.china.base.Config;
//import com.app.china.base.context.Application;
//import com.app.china.base.tools.PackInfoHelper;
//import com.game.playbook.ConstValue;

//import com.yingyonghui.market.MarketApplication;

public class AppConstant {
	public static final ApplicationInfo info = SSHApplication.getInstance()
			.getApplicationInfo();
	// public static final PackageInfo pInfo;
	public static final String packName;
	public static final int versionCode;
	public static final String versionName;
	public static final String signatureHash;
	public static final String channel;
	// public static final int versionCode =info.
	public static final boolean isDebug = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) > 0;
	public static final int INSTALL_REQUEST_CODE = 0x1000;
	public static final int UNINSTALL_REQUEST_CODE = 0x1001;
	public static final int CURRENT_MAIN_PROCESS_ID = android.os.Process
			.myPid();
	public static final int CURRENT_USER_ID = android.os.Process.myUid();
	public static final String SRC_DIST = info.sourceDir;
	private static PreferanceHelper pref = (PreferanceHelper) Api.pref
			.getHandler();
	public static final Set<String> ACTIVITIES = CollectionBuilder.newHashSet();
	public static final String launchActivity;
	static {
		PackageInfo p = null;
		int vc = -1;
		String vn = "";
		String sh = "";
		String pn = "";
		String ch = "";
		String la = "";
		try {
			PackageManager pm = SSHApplication.getInstance().getPackageManager();
			pn = SSHApplication.getInstance().getPackageName();
			p = pm.getPackageInfo(pn, PackageManager.GET_RECEIVERS
					| PackageManager.GET_META_DATA
					| PackageManager.GET_SIGNATURES
					| PackageManager.GET_ACTIVITIES);
			vc = p.versionCode;
			vn = p.versionName;
			ch = "";

			try {
				ch = pref.getString(null, ConstantValues.KEY_CHANNEL, "");

				if (ch.equals("")) {
					Bundle bundle = p.applicationInfo.metaData;
					if (bundle != null) {
						ch = bundle.getString(ConstantValues.KEY_CHANNEL);
						if (ch == null) {
							ch = "none";
						}
						pref.putObject(ConstantValues.TAG_SYSTEM,
								ConstantValues.KEY_CHANNEL, ch);
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}

			// sh = PackInfoHelper.getPackageHash(p);
			ActivityInfo[] infos = p.receivers;
			if (infos != null) {
				for (ActivityInfo info : infos) {
					//L.x("reciever info : ", info == null ? "" : info.name);
				}
				if (infos != null) {
					//L.x("receiver count", infos.length);
					// Application.config.set(Config.ReceiverCount,
					// infos.length);
				}
			}
			ActivityInfo[] acInfo = p.activities;
			if (acInfo != null) {
				//L.x("activity count", acInfo.length);
				for (ActivityInfo i : acInfo) {
					ACTIVITIES.add(i.name);

					//L.x("activity ", i.name);
				}
			}
			Intent i = pm.getLaunchIntentForPackage(pn);
			ComponentName c = i.getComponent();
			la = (c == null ? "" : c.getClassName());
		} catch (NameNotFoundException e) {
			// TODO: fuck , why this package not installed
			e.printStackTrace();
		} finally {
			// pInfo = p;
			versionCode = vc;
			versionName = vn;
			signatureHash = sh;
			packName = pn;
			channel = ch;
			launchActivity = la;
		}
	}
}