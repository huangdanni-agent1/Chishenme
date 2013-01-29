package com.term;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class FoodProvider extends ContentProvider {

	private static final String TAG= "ContactsProvider"; 

	private DBHelper dbHelper;
	private SQLiteDatabase foodDB;
	
	public static final String AUTHORITY = "com.term.provider.food";
	public static final String FOOD_TABLE = "food";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/food");
	
	public static final int FOOD = 1;
	public static final int FOOD_ID = 2;
	private static final UriMatcher uriMatcher;	
	
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY,"food",FOOD);
		//单独列
		uriMatcher.addURI(AUTHORITY,"food/#",FOOD_ID);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(getContext());
		foodDB = dbHelper.getWritableDatabase();
		return (foodDB == null)? false : true;
	}
	// 删除指定数据列
	@Override
	public int delete(Uri uri, String where, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count;
		switch (uriMatcher.match(uri)) {
			case FOOD:
				count = foodDB.delete(FOOD_TABLE, where, selectionArgs);
				break;
			case FOOD_ID:
				String foodID = uri.getPathSegments().get(1);
				count = foodDB.delete(FOOD_TABLE, 
						FoodColumn._ID+ "="+foodID
						+ (!TextUtils.isEmpty(where) ? " AND ("+ where + ")" : ""),
						selectionArgs);
				break;
			default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	// URI类型转换
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
			case FOOD:
			return "vnd.android.cursor.dir/vnd.term";
			case FOOD_ID:
			return "vnd.android.cursor.item/vnd.term";
			default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
	// 插入数据
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		
		if (uriMatcher.match(uri) != FOOD) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
			Log.e(TAG+"insert","initialValues is not null");
		} else {
			values = new ContentValues();
		}
		// 设置默认值
		
		
		if (values.containsKey(FoodColumn.NAME) == false) {
			values.put(FoodColumn.NAME, "");
			Log.e(TAG+"insert","NAME is null");
		}
		if (values.containsKey(FoodColumn.LOCATION) == false) {
			values.put(FoodColumn.LOCATION, "");
		}
		if (values.containsKey(FoodColumn.PRICE) == false) {
			values.put(FoodColumn.PRICE, "");
		}
		if (values.containsKey(FoodColumn.ASSESSMENT) == false) {
			values.put(FoodColumn.ASSESSMENT, "");
		}
		if (values.containsKey(FoodColumn.RECOMMENDATION) == false) {
			values.put(FoodColumn.RECOMMENDATION, "");
		}
		if (values.containsKey(FoodColumn.BUSINESSHOURS) == false) {
			values.put(FoodColumn.BUSINESSHOURS, "");
		}
		if (values.containsKey(FoodColumn.REMARKS) == false) {
			values.put(FoodColumn.REMARKS, "");
		}
		
		Log.e(TAG+"insert",values.toString());
		long rowId = foodDB.insert(FOOD_TABLE, null, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(CONTENT_URI,rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			Log.e(TAG+"insert",noteUri.toString());
			return noteUri;
		}

		throw new SQLException("Failed to insert row into " + uri);

	}
	// 查询数据
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.e(TAG+":query"," in Query");
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(FOOD_TABLE);
		
		switch (uriMatcher.match(uri)) {
			case FOOD_ID:
				qb.appendWhere(FoodColumn._ID + "=" + uri.getPathSegments().get(1));
				break;
			default: break;
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = FoodColumn._ID;
		} else {
			orderBy = sortOrder;
		}
		Cursor c = qb.query(foodDB,projection,
							selection, selectionArgs,
							null, null,orderBy);

		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
		
	}
	// 更新数据库
	@Override
	public int update(Uri uri, ContentValues values, String where, String[] selectionArgs) {

		int count;
		Log.e(TAG+"update",values.toString());
		Log.e(TAG+"update",uri.toString());
		Log.e(TAG+"update :match",""+uriMatcher.match(uri));
		switch (uriMatcher.match(uri)) {
			case FOOD:
				Log.e(TAG+"update",FOOD+"");
				count = foodDB.update(FOOD_TABLE, values, where, selectionArgs);
				break;
			case FOOD_ID:
				String foodID = uri.getPathSegments().get(1);
				Log.e(TAG+"update",foodID+"");
				count = foodDB.update(FOOD_TABLE,values,
						FoodColumn._ID + "=" + foodID
						+ (!TextUtils.isEmpty(where) ? " AND ("+ where + ")" : ""), 
						selectionArgs);
				break;
			default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
