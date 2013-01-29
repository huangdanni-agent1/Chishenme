package com.term;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "food.db";
	public static final int DATABASE_VERSION = 1;
	public static final String FOOD_TABLE = "food";
	// 创建数据库
	private static final String DATABASE_CREATE = "CREATE TABLE " + FOOD_TABLE
			+ " (" + FoodColumn._ID + " integer primary key autoincrement,"
			+ FoodColumn.NAME + " text," + FoodColumn.LOCATION + " text,"
			+ FoodColumn.PRICE + " double," + FoodColumn.ASSESSMENT + " double,"
			+ FoodColumn.RECOMMENDATION + " text," + FoodColumn.BUSINESSHOURS
			+ " text," + FoodColumn.REMARKS + " text);";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + FOOD_TABLE);
		onCreate(db);
	}

}
