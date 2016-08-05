package com.example.androidstore.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidstore.R;
import com.example.androidstore.entities.AndroidStoreContract;
import com.example.androidstore.helpers.AndroidStoreDbHelper;

public class CategoryFragment extends ListFragment
{
	private SQLiteDatabase database;
	private OnCategorySelectedListener categorySelectedCallback;

	public interface OnCategorySelectedListener
	{
		void onCategorySelected(String category);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		try
		{
			categorySelectedCallback = (OnCategorySelectedListener) activity;
		}
		catch (ClassCastException e)
		{
			Log.d("Class cast failed", e.getMessage());
			Toast.makeText(activity, "Error showing products", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{		
		database = new AndroidStoreDbHelper(getActivity()).getReadableDatabase();
		
		Cursor c = database.query(AndroidStoreContract.Product.TABLE_NAME, 
				new String[] 
				{  
					AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_CATEGORY,
					AndroidStoreContract.Product._ID
				}, 
				null, 
				null,
				AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_CATEGORY, 
				null, 
				null);
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, c, 
				new String[] 
				{  
					AndroidStoreContract.Product.COLUMN_NAME_PRODUCT_CATEGORY,
				}, 
				new int[] { android.R.id.text1 }, 0);
		
		setListAdapter(adapter);	
		
		return inflater.inflate(R.layout.fragment_category, container, false);
	}
	
	@Override
	public void onStop()
	{
		if (database != null)
		{
			database.close();
		}
		
		super.onStop();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		String category = null;
		TextView categoryTw = (TextView)v;
		
		if (categoryTw != null)
		{
			category = categoryTw.getText().toString();
		}
		
		categorySelectedCallback.onCategorySelected(category);
	}
}
