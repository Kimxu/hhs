package kimxu.hhs.z_api.pref;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kimxu.hhs.basic.SSHApplication;
import kimxu.hhs.z_base.AbstractManager;
import kimxu.hhs.z_volley.structure.CollectionBuilder;

public class PreferanceManager extends AbstractManager implements
        PreferanceHelper {

    private static volatile PreferanceHelper instance;

    public static PreferanceHelper getInstance() {
        if (instance == null) {
            synchronized (PreferanceManager.class) {
                instance = new PreferanceManager();
            }
        }
        return instance;
    }

    private PreferanceManager() {
        prefs = CollectionBuilder.newHashMap();
    }

    private Map<String, SharedPreferences> prefs;

    @Override
    public SharedPreferences getPreferance(String name) {
        if (name == null) {
            name = SSHApplication.getInstance().getPackageName() + "_preferences";
        }
        if (!prefs.containsKey(name)) {
            SharedPreferences pref = SSHApplication.getInstance()
                    .getSharedPreferences(name, Context.MODE_PRIVATE);
            prefs.put(name, pref);
        }
        return prefs.get(name);
    }

    @Override
    public boolean testPreferanceExist(String name) {
        return true;
    }

    public synchronized int putIntIncreasing(String name, String k) {
        SharedPreferences pref = this.getPreferance(name);
        Editor edit = pref.edit();
        int v = pref.getInt(k, -1);
        if (v >= 0) {
            ++v;
            edit.putInt(k, v);
        } else {
            edit.putInt(k, 1);
        }
        int r = edit.commit() ? 0 : -1;
        return r;
    }

    @Override
    public synchronized boolean putToggledBoolean(String name, String k) {
        SharedPreferences pref = this.getPreferance(name);
        Editor edit = pref.edit();
        boolean f = pref.getBoolean(k, false);
        f = !f;
        edit.putBoolean(k, f);
        return edit.commit();
    }

    @Override
    public synchronized int putObject(String name, String k, Object ret) {
        //L.x("prefer", "add preferance;", name, k, ret);
        SharedPreferences pref = this.getPreferance(name);
        Editor edit = pref.edit();
        putValueToEdit(ret, edit, k);
        int r = edit.commit() ? 0 : -1;
        return r;
    }

    private void putValueToEdit(Object ret, Editor edit, String k) {
        Class<?> returnType = ret.getClass();
        if (returnType.isAssignableFrom(int.class)
                || returnType.isAssignableFrom(Integer.class)) {
            edit.putInt(k, ret == null ? 0 : Integer.parseInt(ret.toString()));
        } else if (returnType.isAssignableFrom(boolean.class)
                || returnType.isAssignableFrom(Boolean.class)) {
            edit.putBoolean(k,
                    ret == null ? false : Boolean.parseBoolean(ret.toString()));
        } else if (returnType.isAssignableFrom(long.class)
                || returnType.isAssignableFrom(Long.class)) {
            edit.putLong(k, ret == null ? 0L : Long.parseLong(ret.toString()));
        } else if (returnType.isAssignableFrom(double.class)
                || returnType.isAssignableFrom(Double.class)) {
            edit.putLong(
                    k,
                    ret == null ? Double.doubleToRawLongBits(0.0) : Double
                            .doubleToRawLongBits(Double.parseDouble(ret
                                    .toString())));
        } else if (returnType.isAssignableFrom(float.class)
                || returnType.isAssignableFrom(Float.class)) {
            edit.putFloat(k,
                    ret == null ? 0f : Float.parseFloat(ret.toString()));
        } else if (returnType.isAssignableFrom(byte.class)
                || returnType.isAssignableFrom(Byte.class)) {
            edit.putInt(k, ret == null ? 0 : Byte.parseByte(ret.toString()));
        } else if (returnType.isAssignableFrom(short.class)
                || returnType.isAssignableFrom(Short.class)) {
            edit.putInt(k, ret == null ? 0 : Short.parseShort(ret.toString()));
        } else {
            edit.putString(k, ret.toString());
        }
    }

    @SuppressLint("NewApi")
    @Override
    public int putAllObject(String name, String k, Collection<?> v,
                            Class<?> returnType) {
        SharedPreferences pref = this.getPreferance(name);
        Editor edit = pref.edit();
        Set<String> vs = CollectionBuilder.newHashSet();
        edit.putStringSet(k, vs);
        int r = edit.commit() ? 0 : -1;
        return r;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getObject(String name, String k, Class<T> returnType) {

        SharedPreferences pref = this.getPreferance(name);
        if (returnType.isAssignableFrom(int.class)
                || returnType.isAssignableFrom(Integer.class)) {
            return (T) Integer.valueOf(pref.getInt(k, 0));
        } else if (returnType.isAssignableFrom(boolean.class)
                || returnType.isAssignableFrom(Boolean.class)) {
            return (T) Boolean.valueOf(pref.getBoolean(k, false));
        } else if (returnType.isAssignableFrom(long.class)
                || returnType.isAssignableFrom(Long.class)) {
            return (T) Long.valueOf(pref.getLong(k, 0));
        } else if (returnType.isAssignableFrom(double.class)
                || returnType.isAssignableFrom(Double.class)) {
            return (T) Double.valueOf(Double.longBitsToDouble(pref
                    .getLong(k, 0)));
        } else if (returnType.isAssignableFrom(float.class)
                || returnType.isAssignableFrom(Float.class)) {
            return (T) Float.valueOf(pref.getFloat(k, 0));
        } else if (returnType.isAssignableFrom(byte.class)
                || returnType.isAssignableFrom(Byte.class)) {
            return (T) Byte.valueOf((byte) pref.getInt(k, 0));
        } else if (returnType.isAssignableFrom(short.class)
                || returnType.isAssignableFrom(Short.class)) {
            return (T) Short.valueOf((short) pref.getInt(k, 0));
        } else {
            return (T) pref.getString(k, "");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getObject(String name, String k, T defaultValue,
                           Class<T> returnType) {
        SharedPreferences pref = this.getPreferance(name);
        if (returnType.isAssignableFrom(int.class)
                || returnType.isAssignableFrom(Integer.class)) {
            return (T) Integer.valueOf(pref.getInt(k, (Integer) defaultValue));
        } else if (returnType.isAssignableFrom(boolean.class)
                || returnType.isAssignableFrom(Boolean.class)) {
            return (T) Boolean.valueOf(pref.getBoolean(k,
                    (Boolean) defaultValue));
        } else if (returnType.isAssignableFrom(long.class)
                || returnType.isAssignableFrom(Long.class)) {
            return (T) Long.valueOf(pref.getLong(k, (Long) defaultValue));
        } else if (returnType.isAssignableFrom(double.class)
                || returnType.isAssignableFrom(Double.class)) {
            return (T) Double.valueOf(Double.longBitsToDouble(pref.getLong(k,
                    (Long) defaultValue)));
        } else if (returnType.isAssignableFrom(float.class)
                || returnType.isAssignableFrom(Float.class)) {
            return (T) Float.valueOf(pref.getFloat(k, (Float) defaultValue));
        } else if (returnType.isAssignableFrom(byte.class)
                || returnType.isAssignableFrom(Byte.class)) {
            return (T) Byte.valueOf((byte) pref.getInt(k,
                    (Integer) defaultValue));
        } else if (returnType.isAssignableFrom(short.class)
                || returnType.isAssignableFrom(Short.class)) {
            return (T) Short.valueOf((short) pref.getInt(k,
                    (Integer) defaultValue));
        } else {
            return (T) pref.getString(k, defaultValue == null ? null
                    : defaultValue.toString());
        }
    }

    @Override
    public String getString(String name, String k, String defaultValue) {
        SharedPreferences pref = this.getPreferance(name);
        return pref.getString(k, defaultValue);
    }

    @Override
    public int getInt(String name, String k, int defaultValue) {
        SharedPreferences pref = this.getPreferance(name);
        return pref.getInt(k, defaultValue);
    }

    @Override
    public boolean getBoolean(String name, String k, boolean defaultValue) {
        SharedPreferences pref = this.getPreferance(name);
        return pref.getBoolean(k, defaultValue);
    }

    public Map<String, ?> getAllValues(String name) {
        SharedPreferences pref = this.getPreferance(name);
        return pref.getAll();
    }

    @Override
    public <T> List<T> getObjects(String name, String k) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<T> getObjects(String name, String k, T defaultValue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putBoolean(String name, String k, boolean b) {
        SharedPreferences pref = this.getPreferance(name);
        Editor edit = pref.edit();
        edit.putBoolean(k, b);
        edit.apply();

    }
}
