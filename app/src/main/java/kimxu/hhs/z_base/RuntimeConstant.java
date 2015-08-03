package kimxu.hhs.z_base;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

import kimxu.hhs.basic.SSHApplication;
import kimxu.hhs.z_base.data_enum.MemorySizeType;

//import com.app.china.base.context.Application;
//import com.app.china.framework.data.enums.MemorySizeType;
//import com.yingyonghui.market.MarketApplication;

public class RuntimeConstant {
	public static final Runtime CURRENT_RUNTIME = Runtime.getRuntime();
	public static final int CONCURRENCY_ABILITY = CURRENT_RUNTIME
			.availableProcessors();
	public static final long MEMORY_AVAILABLE = CURRENT_RUNTIME.freeMemory();
	public static final long MEMORY_MAX = CURRENT_RUNTIME.maxMemory();
	public static final long MEMORY_TOTAL = CURRENT_RUNTIME.totalMemory();
	public static final MemorySizeType MEMORY_TYPE = MemorySizeType
			.getType(MEMORY_MAX);
	public static final int memoryClass;
	
	static {
		ActivityManager am = (ActivityManager) SSHApplication.getInstance()
				.getSystemService(Context.ACTIVITY_SERVICE);
		memoryClass = am.getMemoryClass();
		MemoryInfo info = new MemoryInfo();
		am.getMemoryInfo(info);
		//L.x("memory",info.availMem);
		//L.x("memory",info.threshold);
		
	}
}