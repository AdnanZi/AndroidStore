package com.example.androidstore.helpers;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.androidstore.R;
import com.example.androidstore.entities.AndroidStoreContract;

public class  PopulateDbHelper
{
	public static void storeAdminUser(Context context)
	{
		AndroidStoreDbHelper dbHelper = new AndroidStoreDbHelper(context);
		String selectQuery = "SELECT *FROM " + AndroidStoreContract.User.TABLE_NAME;

		SQLiteDatabase database = null;
		Cursor c = null;

		try
		{
			database = dbHelper.getReadableDatabase();

			c = database.rawQuery(selectQuery, null);

			if (!c.moveToFirst()) {
				long created = dbHelper.createUser("Admin", "admin1234");

				if (created == -1) {
					Log.d("Create operation failed.", "User is not created.");
				}
			}
		}
		finally
		{
			if (c != null && !c.isClosed())
			{
				c.close();
			}

			if (database != null)
			{
				database.close();
			}
		}
	}

	public static void populateDatabaseIfEmpty(Context context)
	{
		AndroidStoreDbHelper dbHelper = new AndroidStoreDbHelper(context);
		String selectQuery = "SELECT * FROM " + AndroidStoreContract.Product.TABLE_NAME;

		SQLiteDatabase database = null;
		Cursor c = null;

		try
		{
			database = dbHelper.getWritableDatabase();

			c = database.rawQuery(selectQuery, null);

			if (!c.moveToFirst())
			{
				for (int i = 0; i < 20; i++)
				{
					long rowsAffected = 0;

					String category = i < 5 ? "Books" :
						(i < 10 ? "Audio" :
							(i < 15 ? "Video" :
								"Sport"
									)
								);

					ContentValues values = new ContentValues();
					values.put(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_NAME, "Product" + i);
					values.put(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_DESCRIPTION, "This is our finest product to date.");
					values.put(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_PRICE, "200");
					values.put(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_CATEGORY, category);
					values.put(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_IMAGE, getImageArray(context));

					rowsAffected = database.insert(AndroidStoreContract.Product.TABLE_NAME, null, values);

					if (rowsAffected == -1)
					{
						Log.d("Error while inserting row", "at row" + i );
					}
				}
			}
		}
		finally
		{
			if (c != null && !c.isClosed())
			{
				c.close();
			}

			if (database != null)
			{
				database.close();
			}
		}
	}
	
	public static byte[] getImageArray(Context context)
	{
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		
		return stream.toByteArray();
	}
}
