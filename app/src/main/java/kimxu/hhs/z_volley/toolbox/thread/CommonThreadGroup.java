package kimxu.hhs.z_volley.toolbox.thread;

public class CommonThreadGroup extends ThreadGroup {

	public CommonThreadGroup(String name) {
		super(name);
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {

		super.uncaughtException(t, e);
	}

}
