package com.example.androidstore.helpers;

import com.example.androidstore.entities.AndroidStoreContract;
import com.example.androidstore.entities.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AndroidStoreDbHelper extends SQLiteOpenHelper
{
	private static final String SQL_CREATE_PRODUCTS_TABLE =
		    "CREATE TABLE " + AndroidStoreContract.Product.TABLE_NAME + " (" +
		    		AndroidStoreContract.Product._ID + " INTEGER PRIMARY KEY," +
		    		AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_NAME + " TEXT," +
		    		AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_DESCRIPTION + " TEXT," +
		    		AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_PRICE + " REAL," +
		    		AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_CATEGORY + " TEXT," +
		    		AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_IMAGE + " BLOB" +
		    " )";
	
	private static final String SQL_CREATE_USERS_TABLE = 
			"CREATE TABLE " + AndroidStoreContract.User.TABLE_NAME + " (" + 
					AndroidStoreContract.User._ID + " INTEGER PRIMARY KEY," +
					AndroidStoreContract.User.COLUMN_NAME_USER_NAME + " TEXT," +
					AndroidStoreContract.User.COLUMN_NAME_PASSWORD + " TEXT" +
			" )";
	
	private static final String SQL_DROP_PRODUCTS_TABLE = 
			"DROP TABLE IF EXISTS " + AndroidStoreContract.Product.TABLE_NAME;
	
	private static final String SQL_DROP_USERS_TABLE =
			"DROP TABLE IF EXISTS " + AndroidStoreContract.User.TABLE_NAME;
	
	public static final String DB_NAME = "AndroidStore.db";
	public static final int DB_VERSION = 5;
	
	public AndroidStoreDbHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
		db.execSQL(SQL_CREATE_USERS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL(SQL_DROP_PRODUCTS_TABLE);
		db.execSQL(SQL_DROP_USERS_TABLE);
		onCreate(db);
	}
	
	/*
	 * Products CRUD operations
	 */
	
	public Product getProduct(String productId)
	{
		SQLiteDatabase database = null;
		Cursor c = null;

		try
		{
			database = this.getReadableDatabase();
			
			c = database.query(AndroidStoreContract.Product.TABLE_NAME,
					new String[] 
					{  
						AndroidStoreContract.Product._ID,
						AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_NAME,
						AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_PRICE,
						AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_DESCRIPTION,
						AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_CATEGORY,
						AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_IMAGE
					}, 
					AndroidStoreContract.Product._ID + "=?",
					new String[]{ productId }, 
					null, null, null);
			
			Product product = new Product();
			
			if (c.moveToFirst())
			{
				product.setId(c.getInt(c.getColumnIndex(AndroidStoreContract.Product._ID)));
				product.setProductName(c.getString(c.getColumnIndex(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_NAME)));
				product.setDescription(c.getString(c.getColumnIndex(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_DESCRIPTION)));
				product.setCategory(c.getString(c.getColumnIndex(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_CATEGORY)));
				product.setPrice(c.getDouble(c.getColumnIndex(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_PRICE)));
				product.setImage(c.getBlob(c.getColumnIndex(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_IMAGE)));
			}
			
			return product;
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
	
	public long createProduct(Product product)
	{
		SQLiteDatabase database = null;
		
		try
		{
			database = this.getWritableDatabase();
			
			ContentValues values = getProductValues(product);
			
			return database.insert(AndroidStoreContract.Product.TABLE_NAME, null, values);
		}
		finally
		{
			if (database != null)
			{
				database.close();
			}
		}
	}
	
	public int updateProduct(Product product)
	{
		SQLiteDatabase database = null;
		
		try
		{
			database = this.getWritableDatabase();
			
			ContentValues values = getProductValues(product);
			
			return database.update(AndroidStoreContract.Product.TABLE_NAME, values, AndroidStoreContract.Product._ID + " = ?", 
								new String[] { String.valueOf(product.getId()) });
		}
		finally
		{
			if (database != null)
			{
				database.close();
			}
		}
	}
	
	public int deleteProduct(String productId)
	{
		SQLiteDatabase database = null;
		
		try
		{
			database = this.getWritableDatabase();
			return database.delete(AndroidStoreContract.Product.TABLE_NAME, AndroidStoreContract.Product._ID + " = ?", 
								new String[] { productId });
		}
		finally
		{
			if (database != null)
			{
				database.close();
			}
		}
	}
	
	/*
	 * Users operations
	 */
	
	public Boolean authenticate(String userName, String password)
	{
		SQLiteDatabase database = null;
		
		try
		{
			database = this.getReadableDatabase();
			
			Cursor c = database.query(AndroidStoreContract.User.TABLE_NAME,
									new String[] 
									{
										AndroidStoreContract.User._ID,
										AndroidStoreContract.User.COLUMN_NAME_USER_NAME,
										AndroidStoreContract.User.COLUMN_NAME_PASSWORD
									},
									AndroidStoreContract.User.COLUMN_NAME_USER_NAME + "=?",
									new String[] { userName }, 
									null, null, null);
			
			if (c.moveToFirst())
			{
				String dbPassword = c.getString(c.getColumnIndex(AndroidStoreContract.User.COLUMN_NAME_PASSWORD));
				
				if (password.equals(dbPassword))
				{
					return true;
				}
			}
			
			return false;
		}
		finally
		{
			if (database != null)
			{
				database.close();
			}
		}
	}
	
	public long createUser(String userName, String password)
	{
		SQLiteDatabase database = null;
		
		try
		{
			database = this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(AndroidStoreContract.User.COLUMN_NAME_USER_NAME, userName);
			values.put(AndroidStoreContract.User.COLUMN_NAME_PASSWORD, password);
			
			return database.insert(AndroidStoreContract.User.TABLE_NAME, null, values);
		}
		finally
		{
			if (database != null)
			{
				database.close();
			}
		}
	}
	
	/*
	 * Private methods
	 */
	
	private ContentValues getProductValues(Product product)
	{
		ContentValues values = new ContentValues();
		values.put(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_NAME, product.getProductName());
		values.put(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_DESCRIPTION, product.getDescription());
		values.put(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_CATEGORY, product.getCategory());
		values.put(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_PRICE, product.getPrice());
		values.put(AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_IMAGE, product.getImage());
		
		return values;
	}
}
