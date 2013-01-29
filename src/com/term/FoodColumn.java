package com.term;

import android.provider.BaseColumns;

public class FoodColumn implements BaseColumns {

	public FoodColumn() {
	}

	// 列名

	public static final String NAME = "name";//	名称
	public static final String LOCATION = "location";//	位置
	public static final String PRICE = "price";//	价格
	public static final String ASSESSMENT = "assessment";//	评价
	public static final String RECOMMENDATION = "recommendation";//	推荐搭配
	public static final String BUSINESSHOURS = "businesshours";//	营业时间
	public static final String REMARKS = "remarks";//	备注
	// 列 索引值
	public static final int _ID_COLUMN = 0;
	public static final int NAME_COLUMN = 1;
	public static final int LOCATION_COLUMN = 2;
	public static final int PRICE_COLUMN = 3;
	public static final int ASSESSMENT_COLUMN = 4;
	public static final int RECOMMENDATION_COLUMN = 5;
	public static final int BUSINESSHOURS_COLUMN = 6;
	public static final int REMARKS_COLUMN = 7;
	// 查询结果
	public static final String[] PROJECTION = {
			_ID,// 0
			NAME,// 1
			LOCATION,// 2
			PRICE,//3
			ASSESSMENT,//4
			RECOMMENDATION,//5
			BUSINESSHOURS,
			REMARKS
	};
}
