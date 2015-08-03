package kimxu.hhs.z_api.pref;

import android.content.SharedPreferences;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import kimxu.hhs.z_volley.base.GenericHelper;


public interface PreferanceHelper extends GenericHelper {

	public SharedPreferences getPreferance(String name);

	public boolean testPreferanceExist(String name);

	public int putObject(String name, String k, Object v);

	public int putAllObject(String name, String k, Collection<?> v,
							Class<?> type);

	public <T> T getObject(String name, String k, Class<T> returnType);

	public <T> T getObject(String name, String k, T defaultValue,
						   Class<T> returnType);

	public <T> List<T> getObjects(String name, String k);

	public <T> List<T> getObjects(String name, String k, T defaultValue);

	public Map<String, ?> getAllValues(String name);

	public int putIntIncreasing(String name, String k);

	public abstract boolean getBoolean(String name, String k,
									   boolean defaultValue);

	public abstract int getInt(String name, String k, int defaultValue);

	public abstract String getString(String name, String k, String defaultValue);

	public boolean putToggledBoolean(String name, String k);

	public void putBoolean(String name, String k,boolean b);
}
