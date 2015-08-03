package kimxu.hhs.z_api.packs;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import kimxu.hhs.z_volley.base.AppCallback;
import kimxu.hhs.z_volley.base.GenericHelper;

public interface PacksHelper extends GenericHelper {
	public static final int PKG_STORED_INTERNAL = 0;
	public static final int PKG_STORED_EXTERNAL = 1;
	public static final int PKG_STOREPREF_UNSPECIFIED = -1;
	public static final int PKG_STOREPREF_AUTO = 0; // auto
	public static final int PKG_STOREPREF_INT = 1; // internalOnly
	public static final int PKG_STOREPREF_EXT = 2; // preferExternal

	// public static final int install_code = 0x10001;
	// public static final int uninstall_code = 0x10002;

	public static enum SignatureCheckResult {
		same, different, package_not_exist, file_not_valid
	}

	public static enum TopActivityState {
		self, others, unknown
	}

	public boolean doAppCheck(AppCallback callback);

	public boolean doAppRawCheck(AppCallback callback);

	public PackageInfo checkAppInfo(String packageName);

	public Intent getLaunchIntent(String packName)
			throws PackageManager.NameNotFoundException, IllegalAccessException;

	// public int startInstallPackage(Activity curAc, String name);

	// public int startUninstallPackage(Activity curAc, String name);

	// public int installPackageByRoot(File af);

	// public int uninstallPackageByRoot(String name);

	public String getApkHash(File af) throws FileNotFoundException;

	public String getPackageHash(String packageName);

	public PackInfo getInfoFromeAppInfo(PackageInfo pInfo);

	public PackInfo getInfoFromeFile(File af);

	public Drawable getAppIconFromFile(String af);

	public Drawable getAppIcon(String pack);

	public String getAppRunning();

	public boolean testCurrentAppIsQuiting();

	public TopActivityState testAppIsRunning();

	public abstract PackInfo getInfoFromInstalledPack(String packName);

	public boolean checkAppChange();

	// public boolean testAppIsQuiting();

	public abstract List<String> getPackageNames();

}