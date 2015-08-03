package kimxu.hhs.z_volley.tools;


import java.util.HashSet;
import java.util.Set;

import kimxu.hhs.z_api.packs.PackInfo;

public class PackInfoBuilder {
	public String packageName;
	public int version;
	public String versionName;
	public boolean isSystem, isDebugable, isExternalApp;
	public long updateTime, installTime;
	public String signature;
	public Set<String> permissions;
	/**/
	public  String appName;//应用名
	public  String sourceDir;//apk路径
	public  long apkSize;//apk大小
	public  long apkFileLastModify;//apk文件的上次修改时间
	public  String shortDescription;//中文转英文
	public  int installedLocation;//安装路径,应用搬家有用
	public  int preferInstallLocation;//AndroidManifest.xml的preferInstallLocation
	
	public PackInfoBuilder() {
		this.versionName = "";
		this.permissions = new HashSet<String>();
	}

	public PackInfoBuilder setPkgName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public PackInfoBuilder setVersion(int version) {
		this.version = version;
		return this;
	}

	public PackInfoBuilder setVersionName(String versionName) {
		this.versionName = versionName;
		return this;
	}

	public PackInfoBuilder setSystem(boolean isSystem) {
		this.isSystem = isSystem;
		return this;
	}

	public PackInfoBuilder setDebugable(boolean isDebugable) {
		this.isDebugable = isDebugable;
		return this;
	}

	public PackInfoBuilder setExternalApp(boolean isExternalApp) {
		this.isExternalApp = isExternalApp;
		return this;
	}

	public PackInfoBuilder setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public PackInfoBuilder setInstallTime(long installTime) {
		this.installTime = installTime;
		return this;
	}

	public PackInfoBuilder setSignature(String signature) {
		this.signature = signature;
		return this;
	}

	public PackInfoBuilder add(String e) {
		permissions.add(e);
		return this;
	}
	
	public PackInfoBuilder setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
		return this;
	}

	public PackInfoBuilder setApkSize(long apkSize) {
		this.apkSize = apkSize;
		return this;
	}

	public PackInfoBuilder setApkFileLastModify(long apkFileLastModify) {
		this.apkFileLastModify = apkFileLastModify;
		return this;
	}

	public PackInfoBuilder setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
		return this;
	}

	public PackInfoBuilder setInstalledLocation(int installedLocation) {
		this.installedLocation = installedLocation;
		return this;
	}

	public PackInfoBuilder setPreferInstallLocation(int preferInstallLocation) {
		this.preferInstallLocation = preferInstallLocation;
		return this;
	}
	
	public PackInfoBuilder setAppName(String appName) {
		this.appName = appName;
		return this;
	}

	public PackInfo build() {
		return new PackInfo(this);
	}
}