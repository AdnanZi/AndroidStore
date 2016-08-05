package com.example.androidstore.fragments.admin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidstore.R;
import com.example.androidstore.entities.AndroidStoreContract;
import com.example.androidstore.helpers.AndroidStoreDbHelper;
import com.example.androidstore.infrastructure.MyCursorAdapter;
import com.example.androidstore.infrastructure.MyViewBinder;

public class ProductsListFragment extends ListFragment
{
	private SQLiteDatabase database;
	private SimpleCursorAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		String[] from = new String[] 
				{  
					AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_NAME,
					AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_CATEGORY,
					AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_IMAGE,
					AndroidStoreContract.Product._ID
				};
		
		int[] to = new int[] 
				{
					R.id.product_name,
					R.id.product_category,
					R.id.product_thumbnail,
					R.id.list_id
				};
		
		database = new AndroidStoreDbHelper(getActivity()).getReadableDatabase();
		
		Cursor c = database.query(AndroidStoreContract.Product.TABLE_NAME, 
				new String[] 
				{  
					AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_NAME,
					AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_CATEGORY,
					AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_IMAGE,
					AndroidStoreContract.Product._ID
				}, 
				null, null, null, null, null);
		
		adapter = new MyCursorAdapter(getActivity(), R.layout.list_product_admin, c, from, to, 0, new MyViewBinder());
		
		setListAdapter(adapter);	
		
		return inflater.inflate(R.layout.fragment_product, container, false);
	}
	
	@Override
	public void onStop()
	{
		if (database != null)
			database.close();
		
		super.onStop();
	}
}
