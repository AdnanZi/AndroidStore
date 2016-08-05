package com.example.androidstore.entities;

import android.provider.BaseColumns;

public class AndroidStoreContract
{
	public AndroidStoreContract()
	{
	}

	public static abstract class Product implements BaseColumns
	{
		public static final String TABLE_NAME = "Products";
		public static final String COLUMN_NAME_PRODUCT_NAME= "Name";
		public static final String COLUMN_NAME_PRODUCT_DESCRIPTION = "Description";
		public static final String COLUMN_NAME_PRODUCT_PRICE = "Price";
		public static final String COLUMN_NAME_PRODUCT_CATEGORY = "Category";
		public static final String COLUMN_NAME_PRODUCT_IMAGE = "Image";
	}
	
	public static abstract class User implements BaseColumns
	{
		public static final String TABLE_NAME = "Users";
		public static final String COLUMN_NAME_USER_NAME = "UserName";
		public static final String COLUMN_NAME_PASSWORD = "Password";
	}
}
