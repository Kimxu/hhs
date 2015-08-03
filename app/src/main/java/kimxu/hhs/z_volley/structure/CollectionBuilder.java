package kimxu.hhs.z_volley.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
//import com.app.china.framework.api._base.Stringifiable;
//import com.google.gson.Gson;

public abstract class CollectionBuilder<T> {
	

	private transient final int type;

	public static <E> ArrayList<E> newArrayList() {
		return new ArrayList<E>();
	}
	
	public static <E> ArrayList<E> newArrayList(int size) {
		return new ArrayList<E>(size);
	}

	public static <E> ArrayList<E> newArrayList(E... elements) {
		int capacity = (elements.length * 110) / 100 + 5;
		ArrayList<E> list = new ArrayList<E>(capacity);
		Collections.addAll(list, elements);
		return list;
	}

	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	public static <K> HashSet<K> newHashSet() {
		return new HashSet<K>();
	}

	public static <E> HashSet<E> newHashSet(E... elements) {
		int capacity = elements.length * 4 / 3 + 1;
		HashSet<E> set = new HashSet<E>(capacity);
		Collections.addAll(set, elements);
		return set;
	}

	public static <E> SortedSet<E> newSortedSet() {
		return new TreeSet<E>();
	}

	public static <E> SortedSet<E> newSortedSet(E... elements) {
		SortedSet<E> set = new TreeSet<E>();
		Collections.addAll(set, elements);
		return set;
	}

	public static Map<String, Object> arrayToMap(Object... params) {
		Map<String, Object> ret = newHashMap();
		if (params.length % 2 == 0) {
			for (int i = 0, n = params.length; i < n; i += 2) {
				if (params[i] instanceof String) {
					ret.put(params[i].toString(), params[i + 1]);
				}
			}
		} else {
			throw new IllegalArgumentException(
					"parameters number should be even");
		}
		return ret;
	}

	public static Map<String, Object> arrayToMap(String[] keys, String[] values) {
		Map<String, Object> ret = newHashMap();
		int n = (keys.length == values.length ? keys.length : 0);
		if (n > 0) {
			for (int i = 0; i < n; ++i) {
				ret.put(keys[i], values[i]);
			}
		} else {
			throw new IllegalArgumentException(
					"keys length don't match values length");
		}
		return ret;
	}

	public static <T> MapBuilder<T> mapBuilder() {
		return new MapBuilder<T>(0);
	}

	public static <T> SetBuilder<T> setBuilder() {
		return new SetBuilder<T>(1);
	}

	public static <T> ListBuilder<T> listBuilder() {
		return new ListBuilder<T>(2);
	}

	protected CollectionBuilder(int type) {
		this.type = type;
	}

	// static abstract class BaseCollectionBuilder<T> extends
	// CollectionBuilder<T> {
	//
	// protected BaseCollectionBuilder(int type) {
	// super(type);
	// }
	//
	// @Override
	// public CollectionBuilder<T> put(String key, T value) {
	// return null;
	// }
	//
	// @Override
	// public CollectionBuilder<T> add(T l) {
	// return null;
	// }
	//
	// @Override
	// public List<T> getList() {
	// return null;
	// }
	//
	// @Override
	// public Map<String, T> getMap() {
	// return null;
	// }
	//
	// @Override
	// public Set<T> getSet() {
	// return null;
	// }
	//
	// }

	public static final class MapBuilder<T> extends CollectionBuilder<T> {

		private final Map<String, T> map;

		private MapBuilder(int type) {
			super(type);
			map = CollectionBuilder.newHashMap();
		}

//		@Override
//		public Stringifiable getImmutableSrc() {
//			String s = gson.toJson(map);
//			return new CommonStringify(s);
//		}

//		@Override
//		public String getJson() {
//			return gson.toJson(map);
//		}

		public Map<String, T> getMap() {
			return map;
		}

		public MapBuilder<T> put(String key, T value) {
			map.put(key, value);
			return this;
		}

	}

	public static final class ListBuilder<T> extends CollectionBuilder<T> {

		private final List<T> list;

		private ListBuilder(int type) {
			super(type);
			list = CollectionBuilder.newArrayList();
		}

//		@Override
//		public Stringifiable getImmutableSrc() {
//			String s = gson.toJson(list);
//			return new CommonStringify(s);
//		}

//		@Override
//		public String getJson() {
//			return gson.toJson(list);
//		}

		public ListBuilder<T> add(T l) {
			list.add(l);
			return this;
		}

		public List<T> getList() {
			return list;
		}

	}

	public static final class SetBuilder<T> extends CollectionBuilder<T> {

		private final Set<T> set;

		private SetBuilder(int type) {
			super(type);
			set = CollectionBuilder.newHashSet();
		}

//		@Override
//		public Stringifiable getImmutableSrc() {
//			String s = gson.toJson(set);
//			return new CommonStringify(s);
//		}

//		@Override
//		public String getJson() {
//			return gson.toJson(set);
//		}

		public SetBuilder<T> add(T l) {
			this.set.add(l);
			return this;
		}

		public Set<T> getSet() {
			return set;
		}

	}
	
	

//	public abstract Stringifiable getImmutableSrc();

//	public abstract String getJson();

	// public abstract CollectionBuilder<T> put(String key, T value);
	//
	// public abstract CollectionBuilder<T> add(T l);
	//
	// public abstract List<T> getList();
	//
	// public abstract Map<String, T> getMap();
	//
	// public abstract Set<T> getSet();

	// public ContentValues getContentValues(){
	// return new ContentValues(map);
	// }

	// public CollectionBuilder<T> put(String key, T value) {
	// if (map != null)
	// map.put(key, value);
	// return this;
	// }
	//
	// public CollectionBuilder<T> putAll(Map<? extends String, ? extends T> m)
	// {
	// if (map != null)
	// map.putAll(m);
	// return this;
	// }
	//
	// public CollectionBuilder<T> setAll(Collection<? extends T> c) {
	// if (set != null)
	// set.addAll(c);
	// return this;
	// }
	//
	// public CollectionBuilder<T> set(T e) {
	// if (set != null)
	// set.add(e);
	// return this;
	// }
	//
	// public CollectionBuilder<T> addAll(int index, Collection<? extends T> c)
	// {
	// if (list != null)
	// list.addAll(index, c);
	// return this;
	// }
	//
	// public CollectionBuilder<T> add(T l) {
	// if (list != null)
	// list.add(l);
	// return this;
	// }

	// public CollectionBuilder add(int index, Object element) {
	// list.add(index, element);
	// return this;
	// }

}
