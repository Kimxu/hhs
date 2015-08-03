package kimxu.hhs.z_api.packs;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import kimxu.hhs.basic.SSHApplication;
import kimxu.hhs.z_base.AbstractManager;
import kimxu.hhs.z_api.Api;
import kimxu.hhs.z_api.pref.PreferanceHelper;
import kimxu.hhs.z_base.AppConstant;
import kimxu.hhs.z_base.ConstantValues;
import kimxu.hhs.z_base.data.FileInfo;
import kimxu.hhs.z_volley.base.AppCallback;
import kimxu.hhs.z_volley.structure.CollectionBuilder;
import kimxu.hhs.z_volley.tools.FileHelper;
import kimxu.hhs.z_volley.tools.PackInfoBuilder;
import kimxu.hhs.z_volley.tools.StringHelper;

public class PackManager extends AbstractManager implements PacksHelper {

	protected static volatile PackManager instance;

	protected final PackageManager pm;
	protected final Context context;

	public static PackManager getInstance() {
		if (instance == null) {
			synchronized (PackManager.class) {
				instance = new PackManager();
				// instance.init();
			}
		}
		return instance;
	}

	private PackManager() {
		context = SSHApplication.getInstance().getApplicationContext();
		pm = context.getPackageManager();
	}

	private void init() {
		// System.out.println("do the initing");
		// pm.getLaunchIntentForPackage("");
	}

	public boolean doAppCheck(AppCallback callback) {
		List<PackageInfo> re = pm
				.getInstalledPackages(PackageManager.GET_META_DATA
						| PackageManager.GET_PERMISSIONS
						| PackageManager.GET_SIGNATURES);
		for (PackageInfo ai : re) {
			// String signature = InfoHelper.getPackageHash(ai);
			PackInfo info = getInfoFromeAppInfo(ai);
			callback.onCallBack(info);
		}
		return true;
	}

	public boolean doAppRawCheck(AppCallback callback) {

		List<PackageInfo> re = pm
				.getInstalledPackages(PackageManager.GET_META_DATA);
		for (PackageInfo ai : re) {
			callback.onCallBack(ai);
		}

		return true;
	}

	public PackageInfo checkAppInfo(String packageName) {
		try {
			return pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
//			L.e(e);

		}
		return null;
	}

	public Intent getLaunchIntent(String packName)
			throws PackageManager.NameNotFoundException, IllegalAccessException {
		Intent intent = null;
		try {
			pm.getPackageInfo(packName, PackageManager.GET_META_DATA);
		} catch (PackageManager.NameNotFoundException e) {
			throw e;
		}
		intent = pm.getLaunchIntentForPackage(packName);
		if (intent == null) {
			throw new IllegalAccessException("no start activity");
		} else {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		}
		return intent;
	}

	// public int startInstallPackage(Activity curAc, String name) {
	// // System.out.println("install called");
	// File af = new File(name);
	// if (af.isFile() && af.canRead() && af.exists()) {
	// Intent intent = buildInstallIntent(af);
	// curAc.startActivityForResult(intent, install_code);
	//
	// return 0;
	// } else {
	// return -1;
	// }
	// }

	public PacksHelper.SignatureCheckResult checkSignatureValid(String file,
			String name) {
		// System.out.println("install called");
		File af = new File(name);
		try {
			PackageInfo info = pm.getPackageInfo(name,
					PackageManager.GET_SIGNATURES);
			String pHash = getPackageHash(info);
			String fHash = getApkHash(af);
			return pHash.equals(fHash) ? SignatureCheckResult.same
					: SignatureCheckResult.different;
		} catch (PackageManager.NameNotFoundException e) {
			return SignatureCheckResult.package_not_exist;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return PacksHelper.SignatureCheckResult.file_not_valid;
		}
	}

	@SuppressLint("InlinedApi")
	public Intent buildUnistallIntent(String name) {
		Intent intent = new Intent();
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			intent.setAction(Intent.ACTION_UNINSTALL_PACKAGE);
		else
			intent.setAction(Intent.ACTION_DELETE);
		Uri uri = Uri.fromParts("package", name, null);
		intent.setData(uri);
		return intent;
	}

	// public int startUninstallPackage(Activity curAc, String name) {
	// Intent intent = buildUnistallIntent(name);
	// PackageInfo pInfo = this.checkAppInfo(name);
	// ApplicationInfo info = pInfo.applicationInfo;
	// if (info != null) {
	// if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
	// curAc.startActivityForResult(intent, uninstall_code);
	//
	// return 0;
	// } else {
	// return 1;
	// }
	// }
	// return -1;
	// }

	public int installPackageByRoot(File af) {
		return 0;
	}

	public int uninstallPackageByRoot(String name) {
		return 0;
	}

	public String getPackageHash(String packageName) {
		String pubKey = null;

		try {
			PackageInfo info = pm.getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);
			pubKey = getPackageHash(info);
		} catch (PackageManager.NameNotFoundException e) {
			//L.e(e);
		}
		return pubKey;
	}

	public String getPackageHash(PackageInfo info) {
		String pubKey = null;
		if (info != null && info.signatures != null
				&& info.signatures.length > 0) {
			CertificateFactory certFactory;
			try {
				certFactory = CertificateFactory.getInstance("X.509");

				X509Certificate cert;
				byte[] bs = info.signatures[0].toByteArray();
				cert = (X509Certificate) certFactory
						.generateCertificate(new ByteArrayInputStream(bs));
				if (cert != null) {
					pubKey = cert.getPublicKey().getEncoded().toString();
					pubKey = StringHelper.HashHandler.getHashValue(pubKey,
							StringHelper.HashHandler.HashMethod.sha512);
					pubKey = StringHelper.HashHandler.getHashValue(pubKey,
							StringHelper.HashHandler.HashMethod.sha512);
				}
			} catch (CertificateException e) {
				//L.e(e);
			} catch (Exception ex) {
				//L.e(ex);
			}

		}
		return pubKey;
	}

	// public PackInfo getBriefInfoFromeFile(File af) {
	// if (af != null && af.exists() && af.isFile()) {
	// PackageInfo pInfo = pm.getPackageArchiveInfo(af.getAbsolutePath(),
	// PackageManager.GET_META_DATA
	// | PackageManager.GET_PERMISSIONS
	// | PackageManager.GET_SIGNATURES);
	// if (pInfo != null)
	// return this.getInfoFromeAppInfo(pInfo);
	// }
	// return null;
	// }

	public Drawable getAppIconFromFile(String filePath) {
		File af = new File(filePath);
		Drawable mDrawable = null;
		if (af != null && af.exists() && af.isFile()) {
			try {
				PackageInfo packageInfo = pm.getPackageArchiveInfo(filePath,
						PackageManager.GET_META_DATA);
				if (packageInfo != null) {
					ApplicationInfo applicationInfo = packageInfo.applicationInfo;
					applicationInfo.sourceDir = filePath;
					applicationInfo.publicSourceDir = filePath;
					mDrawable = applicationInfo.loadIcon(pm);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mDrawable;
		}
		return null;
	}

	public PackInfo getInfoFromeFile(File af) {
		if (af != null && af.exists() && af.isFile()) {
			String path = af.getAbsolutePath();
			PackageInfo pInfo = pm.getPackageArchiveInfo(af.getAbsolutePath(),
					PackageManager.GET_META_DATA);
			if (pInfo != null) {
				PackInfoBuilder builder = new PackInfoBuilder();
				ApplicationInfo info = pInfo.applicationInfo;
				info.sourceDir = path;
				info.publicSourceDir = path;
				builder.packageName = info.packageName;
				builder.version = pInfo.versionCode;
				builder.versionName = pInfo.versionName;
				CharSequence an = null;
				try {
					an = pm.getApplicationLabel(info);
				} catch (NullPointerException ex) {

				}
				builder.appName = an == null ? "" : an.toString();
				builder.signature = ""; // getPackageHash(pInfo);
				builder.shortDescription = StringHelper.Cn2py
						.cn2pyf(builder.appName);
				return builder.build();
			}
		}
		return null;
	}

	@SuppressLint("NewApi")
	public PackInfo getInfoFromeAppInfo(PackageInfo pInfo) {
		if (pInfo != null) {
			PackInfoBuilder builder = new PackInfoBuilder();
			ApplicationInfo info = pInfo.applicationInfo;
			builder.packageName = info.packageName;
			builder.version = pInfo.versionCode;
			builder.versionName = pInfo.versionName;
			builder.isSystem = (info.flags & ApplicationInfo.FLAG_SYSTEM) > 0;
			builder.isDebugable = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) > 0;
			builder.isExternalApp = (info.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) > 0;
			builder.installTime = 0;
			builder.updateTime = 0;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				builder.installTime = pInfo.firstInstallTime;
				builder.updateTime = pInfo.lastUpdateTime;
			}
			String[] perms = pInfo.requestedPermissions;
			if (perms != null) {
				for (String p : perms) {
					builder.add(p);
				}
			}
			builder.appName = info.loadLabel(pm).toString();
			builder.signature = getPackageHash(pInfo);
			builder.sourceDir = info.sourceDir;
			builder.shortDescription = StringHelper.Cn2py
					.cn2pyf(builder.appName);
			FileInfo fInfo = FileHelper.getFileInfo(info.sourceDir);
			builder.apkSize = fInfo == null ? 0 : fInfo.size;
			builder.apkFileLastModify = fInfo == null ? 0 : fInfo.lastModified;
			builder.installedLocation = ((info.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) ? PKG_STORED_INTERNAL
					: PKG_STORED_EXTERNAL;
			builder.preferInstallLocation = getPreferInstallLocation(pInfo);
			// builder.preferInstallLocation = -1;
			return builder.build();
		}
		return null;
	}

	@Override
	public PackInfo getInfoFromInstalledPack(String packName) {
		PackageInfo pInfo = this.checkAppInfo(packName);
		if (pInfo != null) {
			return getInfoFromeAppInfo(pInfo);
		}
		return null;
	}

	public int getPreferInstallLocation(PackageInfo info) {
		int preferValue = PKG_STOREPREF_AUTO;
		try {
			Field field = info.getClass().getDeclaredField("installLocation");
			field.setAccessible(true);
			preferValue = field.getInt(info);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return preferValue;
	}

	public String getApkHash(File af) throws FileNotFoundException {
		if (af.exists() && af.isFile() && af.canRead()) {
			String retHash = "";
			try {
				List<Certificate> certs = getJarCerts(af.getName());
				StringBuilder sb = new StringBuilder();
				// if (certs.size() != 1)
				// L.v("cert : ", "Certificate: " + certs.size());

				int certCount = 0;
				for (Certificate cert : certs) {
					byte[] tmp = cert.getPublicKey().getEncoded();
					sb.append(StringHelper.HashHandler.getHashValue(tmp,
							StringHelper.HashHandler.HashMethod.sha512));
					certCount++;
				}

				if (certCount == 0)
					return null;

				retHash = StringHelper.HashHandler.getHashValue(sb.toString(),
						StringHelper.HashHandler.HashMethod.sha512);

			} catch (IOException e) {
				retHash = null;
				//L.e(e);
				// e.printStackTrace();
			} catch (Exception e) {
				retHash = null;
				// e.printStackTrace();
			}
			return retHash;

		} else {
			throw new FileNotFoundException("");
		}
	}

	public ComponentName getCompRunning() {
		Context ac = SSHApplication.getInstance();
		ActivityManager am = (ActivityManager) ac.getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName componentInfo = null;
		try {
			List<ActivityManager.RunningTaskInfo> taskInfo = am
					.getRunningTasks(1);

			componentInfo = taskInfo.get(0).topActivity;
			// L.x("topActivity",
			// "CURRENT Activity ::"
			// ,taskInfo.get(0).topActivity.getClassName());
			//L.x("activity", componentInfo.getClassName(),
			//		componentInfo.getPackageName());
		} catch (SecurityException ex) {
			//L.e(ex);
		}
		return componentInfo;
	}

	public String getAppRunning() {
		ComponentName comp = getCompRunning();
		return comp == null ? null : comp.getPackageName();
	}

	public boolean testCurrentAppIsQuiting() {
		Activity ac = SSHApplication.getCurrentActivity();
		return ac == null;
	}

	private static List<Certificate> getJarCerts(String apkFileName)
			throws IOException {
		JarFile jf = new JarFile(apkFileName, true);
		Enumeration<JarEntry> entries = jf.entries();
		// Vector<JarEntry> entriesVec = new Vector<JarEntry>();

		// while (entries.hasMoreElements()) {
		// JarEntry je = entries.nextElement();
		// entriesVec.addElement(je);
		// String jeName=je.getName();
		// android.util.Log.d("time span", "entry name1:"+jeName);
		// if(!jeName.startsWith("res/")){
		// }
		// }
		// Enumeration<JarEntry> e = entriesVec.elements();
		List<Certificate> certs = new ArrayList<Certificate>();
		Manifest mf = jf.getManifest();
		if (mf != null) {
			Map<String, Attributes> ates = mf.getEntries();
			while (entries.hasMoreElements()) {
				JarEntry je = entries.nextElement();
				if (ates.containsKey(je.getName())) {
					byte[] buffer = new byte[256];
					InputStream is = null;
					is = jf.getInputStream(je);
					if ((is.read(buffer, 0, buffer.length)) != -1)
						;
					if (is != null)
						is.close();

					android.util.Log.d("time span",
							"entry name2:" + je.getName());
					for (Certificate cert : je.getCertificates()) {
						certs.add(cert);
					}
					return certs;
				}
			}
		}
		// while (e.hasMoreElements()) {
		// JarEntry je = e.nextElement();
		// if (jf.getManifest() != null
		// && jf.getManifest().getEntries().containsKey(je.getName())) {
		// for (Certificate cert : je.getCertificates()) {
		// certs.add(cert);
		// }
		// return certs;
		// }
		// }
		jf.close();
		return certs;
	}

	@Override
	public TopActivityState testAppIsRunning() {
		String s = getAppRunning();
		if (s == null) {
			return TopActivityState.unknown;
		} else {
			return s.equals(AppConstant.packName) ? TopActivityState.self
					: TopActivityState.others;
		}
	}

	@Override
	public Drawable getAppIcon(String pack) {
		Drawable mDrawable = null;

		// PackageManager pm = context.getPackageManager();
		ApplicationInfo applicationInfo;

		try {
			applicationInfo = pm.getApplicationInfo(pack, 0);
			mDrawable = applicationInfo.loadIcon(pm);
		} catch (PackageManager.NameNotFoundException e) {
			//L.x(e);
		}

		return mDrawable;
		// return null;
	}

	@Override
	public List<String> getPackageNames() {
		final List<String> packs = CollectionBuilder.newArrayList();
		PackageManager pm = SSHApplication.getAppContext().getPackageManager();
		List<PackageInfo> re = pm
				.getInstalledPackages(PackageManager.GET_META_DATA);
		for (PackageInfo pInfo : re) {
			packs.add(pInfo.packageName);
		}
		// Type tp = new TypeToken<List<String>>() {
		// }.getType();
		// String ret = StringHelper.JsonHelper.gson.toJson(packs, tp);
		return packs;
	}

	@Override
	public boolean checkAppChange() {
		List<String> packs = getPackageNames();
		String allPack = StringHelper.concat(packs);
		String msg = StringHelper.HashHandler.getHashValue(allPack);
		PreferanceHelper ph = (PreferanceHelper) Api.pref.getHandler();
		String oldMsg = ph.getString(ConstantValues.TAG_SYSTEM,
				ConstantValues.KEY_ALL_PACK_HASH, null);
		ph.putObject(ConstantValues.TAG_SYSTEM,
				ConstantValues.KEY_ALL_PACK_HASH, msg);
		//L.w("app change", oldMsg, " ", msg);
		if (oldMsg != null && !oldMsg.equals(msg)) {
			return true;
		}
		return false;
	}

	// @Override
	// public boolean testAppIsQuiting() {
	//
	// return false;
	// }

	// public void onExit() {
	// try {
	// // context.unbindService(iConnection);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// IPackageManager mpm;
	// InstallConnection iConnection;
	//
	// protected volatile boolean installServiceConnected = false;
	// public void onMessage() {
	// final int WAIT_SERVICE_TIMEOUT = 500;
	// try {
	// int count = 0;
	// synchronized (instance) {
	// while (!installServiceConnected && count < 4) {
	// wait(WAIT_SERVICE_TIMEOUT);
	// count++;
	// }
	// }
	// if (mpm == null) {
	// System.out.println("no manager,failed");
	// } else {
	// System.out.println(mpm.isFirstBoot());
	// // mpm.getInstallLocation();
	// mpm.clearApplicationUserData("com.yingyonghui.market",
	// new IPackageDataObserver() {
	//
	// @Override
	// public IBinder asBinder() {
	//
	// return null;
	// }
	//
	// @Override
	// public void onRemoveCompleted(String packageName,
	// boolean succeeded) throws RemoteException {
	// System.out.println("ok," + packageName + " "
	// + succeeded);
	// }
	// });
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// // mpm.installPackage(packageURI, observer, flags,
	// // installerPackageName);
	// }
	// private void getTheIPackManager() {
	// PackageManager pm = context.getPackageManager();
	// try {
	// Class<?> c = Class.forName("android.app.ApplicationPackageManager");
	// Log.w("Package-get", c.getCanonicalName());
	// Log.w("Package-get",
	// "assignable " + c.isAssignableFrom(pm.getClass()));
	//
	// Object obj = c.cast(pm);
	// Field[] ff = c.getDeclaredFields();
	// Field f = null;
	// for (Field af : ff) {
	// Log.w("package-get", af.getType().getCanonicalName() + " : "
	// + af.getName());
	// if (af.getName().equals("mPM")) {
	// f = af;
	// f.setAccessible(true);
	// }
	// }
	// mpm = (IPackageManager) f.get(obj);
	// } catch (ClassNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// private synchronized void connectToService() {
	// Log.w("install_service", "do the connect job");
	// if (!installServiceConnected) {
	// final String DEFAULT_CONTAINER_PACKAGE = "com.android.defcontainer";
	//
	// final ComponentName DEFAULT_CONTAINER_COMPONENT = new ComponentName(
	// DEFAULT_CONTAINER_PACKAGE,
	// "com.android.defcontainer.DefaultContainerService");
	// Intent service = new Intent()
	// .setComponent(DEFAULT_CONTAINER_COMPONENT);
	//
	// boolean connected = context.bindService(service, iConnection,
	// Context.BIND_AUTO_CREATE);
	//
	// Log.w("install_service", "binding service " + connected);
	// }
	//
	// }

	// private class InstallConnection implements ServiceConnection {
	//
	// @Override
	// public void onServiceConnected(ComponentName name, IBinder service) {
	// synchronized (instance) {
	// installServiceConnected = true;
	// mpm = IPackageManager.Stub.asInterface(service);
	// if (mpm != null) {
	// Log.w("install_service", "yes , registered");
	// } else {
	// Log.w("install_service", "no , not registered");
	// }
	// instance.notifyAll();
	// }
	//
	// }
	//
	// @Override
	// public void onServiceDisconnected(ComponentName name) {
	// synchronized (instance) {
	// mpm = null;
	// if (installServiceConnected) {
	// installServiceConnected = false;
	//
	// }
	// Log.w("install_service", "ok , unregistered");
	// instance.notifyAll();
	// }
	//
	// }
	//
	// }
}
