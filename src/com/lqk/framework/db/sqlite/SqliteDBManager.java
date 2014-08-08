package com.lqk.framework.db.sqlite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lqk.framework.db.annotation.Column;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * 数据库操作类
 * @author longqiankun
 *2014-06-29
 */

public class SqliteDBManager {
	private final int BUFFER_SIZE = 400000;
	public static final String DB_NAME = "lqk.db"; // 保存的数据库文件名
	private static String MASTER = "sqlite_master";
	// 数据库版本
	private static final int DB_VERSION = 1;

	// 执行open()打开数据库时，保存返回的数据库对象
	public SQLiteDatabase mSQLiteDatabase = null;

	// 由SQLiteOpenHelper继承过来
	private DatabaseHelper mDatabaseHelper = null;

	// 本地Context对象
	private Context mContext = null;

	private static SqliteDBManager dbConn = null;

	// 查询游标对象
	private Cursor cursor;

	/**
	 * SQLiteOpenHelper内部类
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		private static DatabaseHelper mHelper;

		private DatabaseHelper(Context context) {
			// 当调用getWritableDatabase()或 getReadableDatabase()方法时,创建一个数据库
			super(context, DB_NAME, null, DB_VERSION);
		}

		public synchronized static DatabaseHelper getInstance(Context context) {
			if (mHelper == null) {
				mHelper = new DatabaseHelper(context);
			}
			return mHelper;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * 构造函数
	 * 
	 * @param mContext
	 */
	private SqliteDBManager(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public static SqliteDBManager getInstance(Context mContext) {
		if (null == dbConn) {
			dbConn = new SqliteDBManager(mContext);
		}
		return dbConn;
	}

	/**
	 * 打开数据库
	 */
	public void open() {
		mSQLiteDatabase = DatabaseHelper.getInstance(mContext)
				.getWritableDatabase();
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
		return db;

	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		if (null != mDatabaseHelper) {
			mDatabaseHelper.close();
		}
		if (null != cursor) {
			cursor.close();
		}
	}

	/**
	 * 插入数据
	 * 
	 * @param tableName
	 *            表名
	 * @param nullColumn
	 *            null
	 * @param contentValues
	 *            名值对
	 * @return 新插入数据的ID，错误返回-1
	 * @throws Exception
	 */

	private long insert(String tableName, String nullColumn,
			ContentValues contentValues) {
		return mSQLiteDatabase.insert(tableName, nullColumn, contentValues);
	}

	private <T> long insert(T t, String tableName, String nullColumn,
			ContentValues contentValues) {
		long pos = mSQLiteDatabase.insert(tableName, nullColumn, contentValues);
		/*
		 * if(mUpdateListener!=null){ mUpdateListener.onUpdateTable(tableName);
		 * }
		 */
		return pos;
	}

	/**
	 * @description 在数据库中存储集合数据 思路： 0.开启事务 1.遍历容器中的对象 2.通过对象得到该对象的类
	 *              3.在该类中获取所有的字段 4.创建一个存储字段值的容器 5.获取每个字段的值 6.将每个字段值存储在容器中
	 *              7.类名就作为表名，容器中的数据就是要向数据库中插入的数据。 8.调用数据库的插入方法，将表名和容器作为参数传进去。
	 *              9.关闭事务
	 * @param collection
	 *            集合容器
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	public synchronized <T> void insert(Collection<T> collection)
			throws IllegalArgumentException, IllegalAccessException {
		if (collection != null && collection.size() > 0) {
			Iterator<T> iterator = collection.iterator();
			mSQLiteDatabase.beginTransaction();
			String className = null;
			try {
				while (iterator.hasNext()) {
					ContentValues contentValues = new ContentValues();
					T t = iterator.next();
					createTable(t);
					Class clazz = t.getClass();
					className = clazz.getName();
					className = className
							.substring(className.lastIndexOf(".") + 1);
					Field[] fields = clazz.getDeclaredFields();
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Column.class))
							continue;
						field.setAccessible(true);
						Class<?> type = field.getType();

						// String simpleName=type.getSimpleName();
						String fieldName = field.getName();
						// String value=(String) field.get(t);
						Object obj = field.get(t);
						if (obj != null) {
							contentValues.put(fieldName, obj.toString());
						} else {
							contentValues.put(fieldName, "");
						}
					}
					insert(t, className, null, contentValues);
				}
				if (mUpdateListener != null && className != null) {
					mUpdateListener.onUpdateTable(className);
				}
				mSQLiteDatabase.setTransactionSuccessful();
			} finally {
				mSQLiteDatabase.endTransaction();
			}
		}
	}
/**
 * 
* @Title: insert
* @Description:插入数据到数据库
* @param @param t
* @param @param nullColume
* @param @throws IllegalArgumentException
* @param @throws IllegalAccessException
* @return void
* @throws
 */
	public synchronized <T> void insert(T t, String nullColume)
			throws IllegalArgumentException, IllegalAccessException {
		mSQLiteDatabase.beginTransaction();
		createTable(t);
		try {
			ContentValues contentValues = new ContentValues();
			Class clazz = t.getClass();
			String className = clazz.getName();
			className = className.substring(className.lastIndexOf(".") + 1);
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (!field.isAnnotationPresent(Column.class))
					continue;
				field.setAccessible(true);
				Class<?> type = field.getType();
				// String simpleName=type.getSimpleName();
				String fieldName = field.getName();
				// String value=(String) field.get(t);
				nullColume = fieldName;
				Object obj = field.get(t);
				if (obj != null) {
					contentValues.put(fieldName, obj.toString());
				} else {
					contentValues.put(fieldName, "");
				}
			}
			insert(t, className, nullColume, contentValues);
			if (mUpdateListener != null && className != null) {
				mUpdateListener.onUpdateTable(className);
			}
			mSQLiteDatabase.setTransactionSuccessful();
		} finally {
			mSQLiteDatabase.endTransaction();
		}

	}

	/**
	 * 
	 * 描述:根据key先查询看是否有该条信息，如果有就更新，没有就插入
	 * 
	 * @param collection
	 *            插入或更新的数据
	 * @param key
	 *            判断的关键字，列名
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 *             思路： 1.遍历传入的容器，获取容器中的每个对象。 2.使用反射技术获取对象中的属性和值
	 *             3.获取传入的key（列名），判断是否包含当前的属性，如果包含，则获取当前的值放在一个数组中。
	 *             4.获取属性和对应的值，使用多条件查询，查询表中是否存在该条信息，如果存在，使用更新操作，否则进行插入操作。
	 */
	public synchronized <T> void insert(Collection<T> collection, String[] key)
			throws IllegalArgumentException, IllegalAccessException {
		if (collection != null && collection.size() > 0) {
			String value = "";
			String[] vaules = null;
			Iterator<T> iterator = collection.iterator();
			if (key != null && key.length > 0) {
				vaules = new String[key.length];
			}
			mSQLiteDatabase.beginTransaction();
			String className = null;
			try {
				while (iterator.hasNext()) {
					ContentValues contentValues = new ContentValues();
					T t = iterator.next();
					createTable(t);
					Class clazz = t.getClass();
					className = clazz.getName();
					className = className
							.substring(className.lastIndexOf(".") + 1);
					Field[] fields = clazz.getDeclaredFields();
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Column.class))
							continue;
						field.setAccessible(true);
						Class<?> type = field.getType();
						String fieldName = field.getName();
						Object obj = field.get(t);
						if (obj != null) {
							contentValues.put(fieldName, obj.toString());
						} else {
							contentValues.put(fieldName, "");
						}
						if (key != null && key.length > 0) {
							for (int i = 0; i < key.length; i++) {
								String keyObj = key[i];
								if (fieldName.equals(keyObj)) {
									if (obj != null) {
										vaules[i] = obj.toString();
									} else {
										vaules[i] = "";
									}

								}
							}
						}
					}
					List<T> findBy = findBy(t, key, vaules);
					if (findBy.size() > 0) {
						udpate(className, key, vaules, contentValues);
					} else {
						insert(t, className, null, contentValues);
					}

				}
				if (mUpdateListener != null && className != null) {
					mUpdateListener.onUpdateTable(className);
				}
				mSQLiteDatabase.setTransactionSuccessful();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mSQLiteDatabase.endTransaction();
			}
		}
	}

	/**
	 * 
	 * 描述:根据key先查询看是否有该条信息，如果有就更新，没有就插入
	 * 
	 * @param collection
	 *            插入或更新的数据
	 * @param key
	 *            判断的关键字
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public synchronized <T> void insert(Collection<T> collection, String key)
			throws IllegalArgumentException, IllegalAccessException {
		if (collection != null && collection.size() > 0) {
			boolean ishas = false;
			String value = "";
			Iterator<T> iterator = collection.iterator();
			mSQLiteDatabase.beginTransaction();
			String className = null;
			try {
				while (iterator.hasNext()) {
					ContentValues contentValues = new ContentValues();
					T t = iterator.next();
					createTable(t);// 创建
					Class clazz = t.getClass();
					className = clazz.getName();
					className = className
							.substring(className.lastIndexOf(".") + 1);
					Field[] fields = clazz.getDeclaredFields();
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Column.class))
							continue;
						field.setAccessible(true);
						Class<?> type = field.getType();
						// String simpleName=type.getSimpleName();
						String fieldName = field.getName();
						// String value=(String) field.get(t);
						Object obj = field.get(t);
						if (obj != null) {
							contentValues.put(fieldName, obj.toString());
							if (fieldName.equals(key)) {
								value = obj.toString();
							}
						} else {
							contentValues.put(fieldName, "");
						}
					}
					List<T> findById = findById(t, key, value);
					if (findById.size() > 0) {
						udpate(className, new String[] { key },
								new String[] { value }, contentValues);
					} else {
						insert(t, className, null, contentValues);
					}
				}
				if (mUpdateListener != null && className != null) {
					mUpdateListener.onUpdateTable(className);
				}
				mSQLiteDatabase.setTransactionSuccessful();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				mSQLiteDatabase.endTransaction();
			}
		}
	}

	/**
	 * 
	 * 描述:根据key先查询看是否有该条信息，如果有就更新，没有就插入
	 * 
	 * @param collection
	 *            插入或更新的数据
	 * @param key
	 *            判断的关键字
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public synchronized <T> void insertMore(Collection<T> collection, String key)
			throws IllegalArgumentException, IllegalAccessException {
		if (collection != null && collection.size() > 0) {
			boolean ishas = false;
			String value = "";
			Iterator<T> iterator = collection.iterator();
			String className = null;
			try {
				while (iterator.hasNext()) {
					ContentValues contentValues = new ContentValues();
					T t = iterator.next();
					createTable(t);
					Class clazz = t.getClass();
					className = clazz.getName();
					className = className
							.substring(className.lastIndexOf(".") + 1);
					Field[] fields = clazz.getDeclaredFields();
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Column.class))
							continue;
						field.setAccessible(true);
						Class<?> type = field.getType();
						// String simpleName=type.getSimpleName();
						String fieldName = field.getName();
						// String value=(String) field.get(t);
						Object obj = field.get(t);
						if (obj != null) {
							contentValues.put(fieldName, obj.toString());
							if (fieldName.equals(key)) {
								value = obj.toString();
							}
						} else {
							contentValues.put(fieldName, "");
						}
					}
					List<T> findById = findById(t, key, value);
					if (findById.size() > 0) {
						udpate(className, new String[] { key },
								new String[] { value }, contentValues);
					} else {
						insert(t, className, null, contentValues);
					}
				}
				if (mUpdateListener != null && className != null) {
					mUpdateListener.onUpdateTable(className);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			}
		}
	}

	/**
	 * @description 通过主键ID删除数据
	 * @param tableName
	 *            表名
	 * @param key
	 *            主键名
	 * @param id
	 *            主键值
	 * @return 受影响的记录数
	 * @throws Exception
	 */
	private long delete(String tableName, String key, String id)
			throws Exception {
		try {
			return mSQLiteDatabase.delete(tableName, key + " = " + id, null);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @description 根据关键字删除数据库信息指定信息
	 * @param t
	 *            指对象，可以根据这个对象来获取对应的表
	 * @param key
	 *            表中的关键列名
	 * @param id
	 *            条件的值
	 */
	public <T> void deleteByKey(T t, String key, String id) {
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		// if(tabbleIsExist(className)){
		mSQLiteDatabase.delete(className, key + " = ?", new String[] { id });
		// }
	}
/**
 * 
* @Title: delete
* @Description: 删除表中所有数据
* @param @param t
* @param @throws Exception
* @return void
* @throws
 */
	public <T> void delete(T t) throws Exception {
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		// if(tabbleIsExist(className)){
		mSQLiteDatabase.delete(className, null, null);
		// }
	}
/**
 * 
* @Title: delete
* @Description:根据条件删除指定数据
* @param @param t 实体映射对应的表名
* @param @param names 字段名
* @param @param whereArgs 字段条件值
* @param @throws Exception
* @return void
* @throws
 */
	public <T> void delete(T t, String[] names, String[] whereArgs)
			throws Exception {
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		StringBuffer selection = new StringBuffer();
		if (names != null && names.length > 0) {
			for (int i = 0; i < names.length; i++) {
				selection.append(names[i]);
				selection.append(" = ?");
				if (i != names.length - 1) {
					selection.append(" and ");
				}
			}
		}
		mSQLiteDatabase.delete(className, selection.toString(), whereArgs);
	}

	/**
	 * 查找表的所有数据
	 * 
	 * @param tableName
	 *            表名
	 * @param columns
	 *            如果返回所有列，则填null
	 * @return
	 * @throws Exception
	 */
	private Cursor findAll(String tableName, String[] columns) throws Exception {
		try {
			cursor = mSQLiteDatabase.query(tableName, columns, null, null,
					null, null, null);
			// cursor.moveToFirst();
			return cursor;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @descritpion 获取表中的所有数据
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public synchronized <T> List<T> findAll(T t) throws Exception {
		List<T> colls = new ArrayList<T>();
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		Cursor cursor = findAll(className, null);
		
		Field[] fields = clazz.getDeclaredFields();
		if (cursor != null) {
			int count = cursor.getCount();
			mSQLiteDatabase.beginTransaction();
			try {
				while (cursor.moveToNext()) {
					T t2 = (T) clazz.newInstance();
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Column.class))
							continue;
						field.setAccessible(true);
						Class<?> type = field.getType();
						String fieldName = field.getName();
						String value = cursor.getString(cursor
								.getColumnIndex(fieldName));
						if (!TextUtils.isEmpty(value)) {
							field.set(t2, value);
						} else {
							field.set(t2, "");
						}
					}
					colls.add(t2);
				}
				mSQLiteDatabase.setTransactionSuccessful();
			} finally {
				mSQLiteDatabase.endTransaction();
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return colls;
	}

	/**
	 * 根据主键查找数据
	 * 
	 * @param tableName
	 *            表名
	 * @param key
	 *            主键名
	 * @param id
	 *            主键值
	 * @param columns
	 *            如果返回所有列，则填null
	 * @return Cursor游标
	 * @throws Exception
	 */
	private Cursor findById(String tableName, String key, String id,
			String[] columns) {
		return mSQLiteDatabase.query(tableName, columns, key + " =?",
				new String[] { id }, null, null, null);
	}

	/**
	 * 根据主键查找数据
	 * 
	 * @param tableName
	 *            表名
	 * @param key
	 *            主键名
	 * @param id
	 *            主键值
	 * @param columns
	 *            如果返回所有列，则填null
	 * @return Cursor游标
	 * @throws Exception
	 */
	private Cursor findLike(String tableName, String key, String id,
			String[] columns) {
		return mSQLiteDatabase.query(tableName, columns, key + " like?",
				new String[] { "%" + id + "%" }, null, null, null);
	}

	/**
	 * 
	 * 描述: 多条件查询，模糊查询的组合
	 * 
	 * @param tableName
	 *            表名
	 * @param names
	 *            条件的列名
	 * @param values
	 *            列名对应的值
	 * @param likekey
	 *            模糊查询的列
	 * @param likevalue
	 *            模糊查询的值
	 * @param columns
	 *            返回的列
	 * @return
	 */
	private Cursor findLike(String tableName, String[] names, String[] values,
			String likekey, String likevalue, String[] columns) {
		StringBuffer selection = new StringBuffer();
		String[] value = null;
		if (names != null && values != null && names.length > 0) {
			value = new String[names.length + 1];
			for (int i = 0; i < names.length; i++) {
				selection.append(names[i]);
				selection.append(" = ?");
				if (i != names.length - 1) {
					selection.append(" and ");
				}
				value[i] = values[i];
			}
			value[names.length] = "%" + likevalue + "%";
			;
			selection.append(" and " + likekey + " like?");
		} else {
			value = new String[1];
			selection.append(likekey + " like?");
			value[0] = "%" + likevalue + "%";
		}
		return mSQLiteDatabase.query(tableName, columns, selection.toString(),
				value, null, null, null);
	}

	/**
	 * @descritpion 获取表中的所有数据
	 * @param t
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws Exception
	 */
	public synchronized <T> List<T> findById(T t, String key, String value)
			throws InstantiationException, IllegalAccessException {
		List<T> colls = new ArrayList<T>();
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		Cursor cursor = findById(className, key, value, null);
		Field[] fields = clazz.getDeclaredFields();
		if (cursor != null) {
			int count = cursor.getCount();
			mSQLiteDatabase.beginTransaction();
			try {
				while (cursor.moveToNext()) {
					T t2 = (T) clazz.newInstance();
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Column.class))
							continue;
						field.setAccessible(true);
						Class<?> type = field.getType();
						String fieldName = field.getName();
						String valuee = cursor.getString(cursor
								.getColumnIndex(fieldName));

						if (!TextUtils.isEmpty(valuee)) {
							field.set(t2, valuee);
						} else {
							field.set(t2, "");
						}
					}
					colls.add(t2);
				}
				mSQLiteDatabase.setTransactionSuccessful();
			} finally {
				mSQLiteDatabase.endTransaction();
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return colls;
	}

	/**
	 * 
	 * 描述:多条件查询，返回所有列
	 * 
	 * @param t
	 *            表名
	 * @param key
	 *            列名
	 * @param value
	 *            值
	 * @return
	 * @throws Exception
	 */
	public synchronized <T> List<T> findBy(T t, String[] key, String[] value)
			throws Exception {
		List<T> colls = new ArrayList<T>();
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		if (!tabbleIsExist(className)){
			createTable(t);
			return colls;
		}
		Cursor cursor = find(className, key, value, null, null, null, null);
		Field[] fields = clazz.getDeclaredFields();
		if (cursor != null) {
			int count = cursor.getCount();
			mSQLiteDatabase.beginTransaction();
			try {
				while (cursor.moveToNext()) {
					T t2 = (T) clazz.newInstance();
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Column.class))
							continue;
						field.setAccessible(true);
						Class<?> type = field.getType();
						String fieldName = field.getName();
						String valuee = cursor.getString(cursor
								.getColumnIndex(fieldName));

						if (!TextUtils.isEmpty(valuee)) {
							field.set(t2, valuee);
						} else {
							field.set(t2, "");
						}
					}
					colls.add(t2);
				}
				mSQLiteDatabase.setTransactionSuccessful();
			} finally {
				mSQLiteDatabase.endTransaction();
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return colls;
	}

	/**
	 * 
	 * 描述:多条件查询，返回指定列
	 * 
	 * @param t
	 *            表名
	 * @param key
	 *            列名
	 * @param value
	 *            值
	 * @param colums
	 *            列名
	 * @return
	 * @throws Exception
	 */
	public synchronized <T> List<T> findBy(T t, String[] key, String[] value,
			String[] colums) throws Exception {
		List<T> colls = new ArrayList<T>();
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		Cursor cursor = find(className, key, value, colums, null, null, null);
		Field[] fields = clazz.getDeclaredFields();
		if (cursor != null) {
			int count = cursor.getCount();
			mSQLiteDatabase.beginTransaction();
			try {
				while (cursor.moveToNext()) {
					T t2 = (T) clazz.newInstance();
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Column.class))
							continue;
						field.setAccessible(true);
						Class<?> type = field.getType();
						String fieldName = field.getName();
						// String
						// valuee=cursor.getString(cursor.getColumnIndex(fieldName));

						for (int i = 0; i < colums.length; i++) {
							if (colums[i].equals(fieldName)) {
								String valuee = cursor.getString(cursor
										.getColumnIndex(fieldName));
								if (!TextUtils.isEmpty(valuee)) {
									field.set(t2, valuee);
								} else {
									field.set(t2, "");
								}
								break;
							}
						}

					}
					colls.add(t2);
				}
				mSQLiteDatabase.setTransactionSuccessful();
			} finally {
				mSQLiteDatabase.endTransaction();
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return colls;
	}

	/**
	 * 
	 * 描述:获取表中数据的数量
	 * 
	 * @param t
	 *            表名
	 * @param key
	 *            列名
	 * @param value
	 *            值
	 * @return
	 * @throws Exception
	 */
	public <T> int getCount(T t, String[] names, String[] value)
			throws Exception {
		List<T> colls = new ArrayList<T>();
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		StringBuffer selection = new StringBuffer();
		if (names != null && names.length > 0) {
			selection.append(" where ");
			for (int i = 0; i < names.length; i++) {
				selection.append(names[i]);
				selection.append(" = ");
				selection.append("'" + value[i] + "'");
				if (i != names.length - 1) {
					selection.append(" and ");
				}
			}
		}
		long count = 0;
		Cursor cursor = mSQLiteDatabase.rawQuery("Select  count(*) from "
				+ className + selection.toString() + ";", null);
		if (cursor != null) {

			cursor.moveToFirst();
			// 获取数据中的LONG类型数据
			count = cursor.getLong(0);
			cursor.close();
		}
		return (int) count;

	}

	/**
	 * 
	 * 描述: 多条件查询，模糊查询的组合
	 * 
	 * @param tableName
	 *            表名
	 * @param names
	 *            条件的列名
	 * @param values
	 *            列名对应的值
	 * @param likekey
	 *            模糊查询的列
	 * @param likevalue
	 *            模糊查询的值
	 * @param columns
	 *            返回的列
	 * @return
	 */
	public synchronized <T> List<T> findByLike(T t, String[] names,
			String[] values, String key, String value) throws Exception {
		List<T> colls = new ArrayList<T>();
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		Cursor cursor = findLike(className, names, values, key, value, null);
		Field[] fields = clazz.getDeclaredFields();
		if (cursor != null) {
			int count = cursor.getCount();
			mSQLiteDatabase.beginTransaction();
			try {
				while (cursor.moveToNext()) {
					T t2 = (T) clazz.newInstance();
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Column.class))
							continue;
						field.setAccessible(true);
						Class<?> type = field.getType();
						String fieldName = field.getName();
						String valuee = cursor.getString(cursor
								.getColumnIndex(fieldName));
						if (!TextUtils.isEmpty(valuee)) {
							field.set(t2, valuee);
						} else {
							field.set(t2, "");
						}
					}
					colls.add(t2);
				}
				mSQLiteDatabase.setTransactionSuccessful();
			} finally {
				mSQLiteDatabase.endTransaction();
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return colls;
	}

	/**
	 * @descritpion 获取表中的所有数据
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public synchronized <T> List<T> findByLike(T t, String key, String value)
			throws Exception {
		List<T> colls = new ArrayList<T>();
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		Cursor cursor = findLike(className, key, value, null);
		Field[] fields = clazz.getDeclaredFields();
		if (cursor != null) {
			int count = cursor.getCount();
			mSQLiteDatabase.beginTransaction();
			try {
				while (cursor.moveToNext()) {
					T t2 = (T) clazz.newInstance();
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Column.class))
							continue;
						field.setAccessible(true);
						Class<?> type = field.getType();
						String fieldName = field.getName();
						String valuee = cursor.getString(cursor
								.getColumnIndex(fieldName));
						if (!TextUtils.isEmpty(valuee)) {
							field.set(t2, valuee);
						} else {
							field.set(t2, "");
						}
					}
					colls.add(t2);
				}
				mSQLiteDatabase.setTransactionSuccessful();
			} finally {
				mSQLiteDatabase.endTransaction();
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return colls;
	}

	/**
	 * 根据条件查询数据
	 * 
	 * @param tableName
	 *            表名
	 * @param names
	 *            查询条件
	 * @param values
	 *            查询条件值
	 * @param columns
	 *            如果返回所有列，则填null
	 * @param orderColumn
	 *            排序的列 ASC表示升序排序，DESC表示降序排序。
	 * @param limit
	 *            限制返回数
	 * @return Cursor游标
	 * @throws Exception
	 */
	private synchronized Cursor find(String tableName, String[] names,
			String[] values, String[] columns, String orderColumn,
			String minid, String limit) throws Exception {
		try {
			StringBuffer selection = new StringBuffer();
			if (names != null && names.length > 0) {
				for (int i = 0; i < names.length; i++) {
					selection.append(names[i]);
					selection.append(" = ?");
					if (i != names.length - 1) {
						selection.append(" and ");
					}
				}
			}
			String lim;
			if (limit == null || minid == null) {
				lim = null;
			} else {
				String mid = (Integer.valueOf(minid))
						* (Integer.valueOf(limit)) + "";
				lim = mid + "," + limit;
			}
			cursor = mSQLiteDatabase.query(true, tableName, columns,
					selection.toString(), values, null, null, orderColumn, lim);
			return cursor;
		} catch (Exception e) {
			throw e;
		}
	}

	public synchronized Cursor sqlQuery(String sql) {
		Cursor rawQuery = mSQLiteDatabase.rawQuery(sql, new String[] {});
		return rawQuery;
	}

	/**
	 * @descritpion 获取表中的所有数据
	 * @param t
	 * @param names
	 *            查询条件
	 * @param values
	 *            查询条件值
	 * @param columns
	 *            如果返回所有列，则填null
	 * @param orderColumn
	 *            排序的列 ASC表示升序排序，DESC表示降序排序。
	 * @param limit
	 *            限制返回数
	 * @return Cursor游标
	 * @throws Exception
	 * @return
	 * @throws Exception
	 */
	public synchronized <T> List<T> findLimit(T t, String[] names,
			String[] values, String[] columns, String orderColumn,
			String minid, String limit) throws Exception {
		List<T> colls = new ArrayList<T>();
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		Cursor cursor = find(className, names, values, columns, orderColumn,
				minid, limit);
		Field[] fields = clazz.getDeclaredFields();
		if (cursor != null) {
			int count = cursor.getCount();
			mSQLiteDatabase.beginTransaction();
			try {
				while (cursor.moveToNext()) {
					T t2 = (T) clazz.newInstance();
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Column.class))
							continue;
						field.setAccessible(true);
						Class<?> type = field.getType();
						String fieldName = field.getName();
						String value = cursor.getString(cursor
								.getColumnIndex(fieldName));
						if (!TextUtils.isEmpty(value)) {
							field.set(t2, value);
						} else {
							field.set(t2, "");
						}
					}
					colls.add(t2);
				}
				mSQLiteDatabase.setTransactionSuccessful();
			} finally {
				mSQLiteDatabase.endTransaction();
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return colls;
	}

	/**
	 * @param tableName
	 *            表名
	 * @param names
	 *            查询条件
	 * @param values
	 *            查询条件值
	 * @param args
	 *            更新列-值对
	 * @return true或false
	 * @throws Exception
	 */
	private synchronized boolean udpate(String tableName, String[] names,
			String[] values, ContentValues args) throws Exception {

		try {
			StringBuffer selection = new StringBuffer("");
			if (names != null && names.length > 0) {
				for (int i = 0; i < names.length; i++) {
					selection.append(names[i]);
					selection.append(" = ?");
					if (i != names.length - 1) {
						selection.append(" and ");
					}
				}
			}
			return mSQLiteDatabase.update(tableName, args,
					selection.toString(), values) > 0;
		} catch (Exception e) {
			throw e;
		}
	}

	public <T> boolean udpate(T t, String[] names, String[] values)
			throws Exception {
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		ContentValues args = new ContentValues();
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(Column.class))
				continue;
			field.setAccessible(true);
			Class<?> type = field.getType();
			String fieldName = field.getName();
			String value = (String) field.get(t);
			args.put(fieldName, value);
		}
		boolean isOk = udpate(className, names, values, args);
		if (mUpdateListener != null) {
			mUpdateListener.onUpdateTable(className);
		}
		return isOk;
	}

	/**
	 * @param t
	 *            通过t获取表名 表名
	 * @param names
	 *            查询条件
	 * @param values
	 *            查询条件值
	 * @param args
	 *            更新列-值对
	 * @return true或false
	 * @throws Exception
	 */
	public <T> boolean udpate(T t, String[] names, String[] values,
			ContentValues args) throws Exception {
		Class clazz = t.getClass();
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		boolean isOk = udpate(className, names, values, args);
		if (mUpdateListener != null) {
			mUpdateListener.onUpdateTable(className);
		}
		return isOk;
	}

	/**
	 * 执行sql语句，包括创建表、删除、插入
	 * 
	 * @param sql
	 */
	public synchronized void executeSql(String sql) {
		mSQLiteDatabase.execSQL(sql);
	}

	/**
	 * 判断表是否存在
	 * 
	 * @param tablename
	 * @return
	 */
	public boolean isTableExits(String tablename) {
		boolean result = false;// 表示不存在
		String str = "select count(*) xcount  from sqlite_master where table ='"
				+ tablename + "' ";
		Cursor c = mSQLiteDatabase.rawQuery(str, null);
		int xcount = c.getColumnIndex("xcount");
		if (xcount != 0) {
			result = true; // 表存在
		}
		return result;
	}
/**
 * 根据表名查看是否存在
 * @param tableName 表名
 * @return
 */
	public boolean tabbleIsExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		SQLiteDatabase db = mSQLiteDatabase;
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from " + MASTER
					+ " where type ='table' and name ='" + tableName.trim()
					+ "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	/**
	 * 根据实体对象判断对应表是否存在
	 * @param t 实体对象
	 * @return
	 */
	public <T> boolean tabbleIsExist(T t) {
		Class clazz = t.getClass();
		String tableName = clazz.getName();
		tableName = tableName.substring(tableName.lastIndexOf(".") + 1);
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		SQLiteDatabase db = mSQLiteDatabase;
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from " + MASTER
					+ " where type ='table' and name ='" + tableName.trim()
					+ "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
		}
		return result;
	}
	/**
	 * 创建数据库表
	 */
	public <T> void createTable(T t) {
		Class<? extends Object> class1 = t.getClass();
		String tableName = class1.getName();
		tableName = tableName.substring(tableName.lastIndexOf(".") + 1);
		StringBuilder sb = new StringBuilder();// 生产sql语句
		// 判断表是否被创建，如果没创建则创建
		// if(isTableExits(tableName)){
		if (tabbleIsExist(tableName)) {
			Field[] fields = class1.getFields();
			Cursor query = mSQLiteDatabase.query(tableName, null, null, null,
					null, null, null);
			String[] columnNames = query.getColumnNames();
			// 判断列数了是否有变化
			// if(fields.length!=columnNames.length){
			for (int i = 0; i < fields.length; i++) {
				if (!fields[i].isAnnotationPresent(Column.class))
					continue;
				String filed = fields[i].getName();
				// 方法1
				/*
				 * boolean flag=true;//表示表中是否包含 for (int j = 0; j <
				 * columnNames.length; j++) { String columeName=columnNames[j];
				 * if(filed.equals(columeName)){//如果 flag=false; } }
				 * if(flag){//表中不包含该字段 StringBuffer sql=new StringBuffer();
				 * sql.append("alter table "); sql.append(tableName);
				 * sql.append(" add "); sql.append(filed); sql.append("text;");
				 * mSQLiteDatabase.execSQL(sql.toString());// 在表中添加新字段 }
				 */

				// 方法2
				if (!checkColumnExist1(mSQLiteDatabase, tableName, filed)) {
					StringBuffer sql = new StringBuffer();
					sql.append("alter table ");
					sql.append(tableName);
					sql.append(" add ");
					sql.append(filed);
					sql.append(" text;");
					mSQLiteDatabase.execSQL(sql.toString());// 在表中添加新字段
				}
			}
			// }
		} else {
			sb.append("create table ");
			sb.append(tableName);
			sb.append("(");
			Field[] fields = class1.getFields();

			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (!field.isAnnotationPresent(Column.class))
					continue;
				String _id = field.getName();
				if ("_id".equals(_id)) {// 判断是否是自增编号
					sb.append(_id);// 字段
					if (i != fields.length - 1) {// 判断是否是最后一个字段，如果是，不加后面的逗号，否则加上后面逗号
						sb.append(" integer primary key autoincrement, ");// 字段类型
					} else {
						sb.append(" integer primary key autoincrement ");// 字段类型
					}
				} else {
					sb.append(_id);// 字段
					if (i != fields.length - 1) {// 判断是否是最后一个字段，如果是，不加后面的逗号，否则加上后面逗号
						sb.append(" text, ");// 字段类型
					} else {
						sb.append(" text ");// 字段类型
					}
				}
			}
			
			String execSQL=sb.toString();
			if(execSQL.contains("text")&&execSQL.substring(execSQL.lastIndexOf("text"), execSQL.length()).contains(", ")){
			sb.deleteCharAt(execSQL.lastIndexOf(","));
			}
			sb.append(");");
			if (mSQLiteDatabase != null) {
				mSQLiteDatabase.execSQL(sb.toString());
			}
		}
	}

	/**
	 * 方法1：检查某表列是否存在
	 * 
	 * @param db
	 * @param tableName
	 *            表名
	 * @param columnName
	 *            列名
	 * @return
	 */
	private boolean checkColumnExist1(SQLiteDatabase db, String tableName,
			String columnName) {
		boolean result = false;
		Cursor cursor = null;
		try {
			// 查询一行
			cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0",
					null);
			result = cursor != null && cursor.getColumnIndex(columnName) != -1;
		} catch (Exception e) {
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}

	/**
	 * 方法2：检查表中某列是否存在
	 * 
	 * @param db
	 * @param tableName
	 *            表名
	 * @param columnName
	 *            列名
	 * @return
	 */
	private boolean checkColumnExists2(SQLiteDatabase db, String tableName,
			String columnName) {
		boolean result = false;
		Cursor cursor = null;
		try {
			cursor = db
					.rawQuery(
							"select * from sqlite_master where name = ? and sql like ?",
							new String[] { tableName, "%" + columnName + "%" });
			result = null != cursor && cursor.moveToFirst();
		} catch (Exception e) {
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}

	/**
	 * 获取所有的表名 CREATE TABLE sqlite_master ( type TEXT, name TEXT, tbl_name TEXT,
	 * rootpage INTEGER, sql TEXT );
	 * 
	 * @return
	 */
	private List<String> getAllTableNames(SQLiteDatabase db) {
		List<String> tables = new ArrayList<String>();
		Cursor cursor = null;
		try {
			cursor = db
					.rawQuery(
							"select name,tbl_name from sqlite_master where type='table' order by name",
							null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					String tName = cursor.getString(cursor
							.getColumnIndex("tbl_name"));
					tables.add(tName);
				}
			}
		} catch (Exception e) {
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return tables;
	}

	/**
	 * 删除所有的表数据
	 */
	public void delAllTablesData() {
		List<String> allTableNames = getAllTableNames(mSQLiteDatabase);
		for (int i = 0; i < allTableNames.size(); i++) {
			String tName = allTableNames.get(i);
			mSQLiteDatabase.delete(tName, null, null);
		}
	}

	// 监听表的跟新
	private OnTableUpdateListener mUpdateListener;

	public void setOnTableUpdateListener(OnTableUpdateListener mUpdateListener) {
		this.mUpdateListener = mUpdateListener;
	}

	public interface OnTableUpdateListener {
		void onUpdateTable(String tableName);
	}
}
