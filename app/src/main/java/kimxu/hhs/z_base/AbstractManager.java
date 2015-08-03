package kimxu.hhs.z_base;

import android.content.Context;

import kimxu.hhs.basic.SSHApplication;
import kimxu.hhs.z_volley.base.GenericHelper;


public abstract class AbstractManager implements GenericHelper {
	volatile Context c;

	@Override
	public Context getContext() {
		if (c == null) {
			c = SSHApplication.getAppContext();
		}
		return c;
	}

	public void setC(Context c) {
		this.c = c;
	}

}
