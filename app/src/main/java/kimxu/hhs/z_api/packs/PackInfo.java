package kimxu.hhs.z_api.packs;


import java.util.Set;

import kimxu.hhs.z_base.data.GenericData;
import kimxu.hhs.z_volley.tools.PackInfoBuilder;
import kimxu.hhs.z_volley.tools.StringHelper;

public class PackInfo implements GenericData {
	public final String packageName;
	public final int version;
	public final String versionName;
	public final boolean isSystem, isDebugable, isExternalApp;
	public final long updateTime, installTime;
	public final String signature;
	public final Set<String> permissions;

	/**/
	public final String appName;  //Ӧ����
	public final String sourceDir;//apk·��
	public final long apkSize;	//apk��С
	public final long apkFileLastModify;//apk�ļ����ϴ��޸�ʱ��
	public final String shortDescription;//����תӢ��
	public final int installedLocation;//��װ·��,Ӧ�ð������
	public final int preferInstallLocation;//AndroidManifest.xml��preferInstallLocation
	
	public PackInfo(PackInfoBuilder builder) {
		this.packageName = builder.packageName;
		this.version = builder.version;
		this.versionName = builder.versionName;
		this.isSystem = builder.isSystem;
		this.isDebugable = builder.isDebugable;
		this.isExternalApp = builder.isExternalApp;
		this.updateTime = builder.updateTime;
		this.installTime = builder.installTime;
		signature = builder.signature;
		if (builder.permissions.size() > 0){
			permissions = builder.permissions;
		}else{
			permissions = null;
	}
		this.sourceDir = builder.sourceDir;
		this.apkSize = builder.apkSize;
		this.apkFileLastModify = builder.apkFileLastModify;
		this.shortDescription = builder.shortDescription;
		this.installedLocation = builder.installedLocation;
		this.preferInstallLocation = builder.preferInstallLocation;
		this.appName = builder.appName;
	}

	@Override
	public String toString() {
		return StringHelper.concat(" ", new Object[]{this.packageName,
				this.versionName, this.version, this.signature,
				this.permissions, this.isDebugable, this.isExternalApp,
				this.isSystem, this.installTime, this.updateTime});
	}
	
	@Override
	public String getIdentifyer() {
		// TODO Auto-generated method stub
		return "PackInfo";
	}

}