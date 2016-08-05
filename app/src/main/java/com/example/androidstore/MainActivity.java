package com.example.androidstore;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.androidstore.admin.ProductsListActivity;
import com.example.androidstore.fragments.CategoryFragment;
import com.example.androidstore.fragments.ProductFragment;
import com.example.androidstore.helpers.ActivityHelper;
import com.example.androidstore.infrastructure.MyCursorAdapter.OnButtonsClickListener;

public class MainActivity extends ActionBarActivity implements CategoryFragment.OnCategorySelectedListener, OnButtonsClickListener
{	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		TextView homeLink = (TextView)findViewById(R.id.category_home);
		
		if (homeLink != null)
		{
			homeLink.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					onCategorySelected(null);
				}
			});
		}
		
		ProductFragment productFragment = (ProductFragment)getSupportFragmentManager().findFragmentById(R.id.product_fragment);
		
		if (productFragment != null && 
				getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			productFragment.updateContent(null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int itemId = item.getItemId();
		
		if (itemId == R.id.action_checkout)
		{
			ActivityHelper.checkOutButtonClick(this);
		} 
		else if (itemId == R.id.action_admin)
		{
			Intent intent = new Intent(this, ProductsListActivity.class); // change this to login
			startActivity(intent);
		}
		
		return true;
	}
	
	@Override
	public void onCategorySelected(String category)
	{
		ProductFragment productFragment = (ProductFragment)getSupportFragmentManager().findFragmentById(R.id.product_fragment);
		
		if (productFragment != null && 
				getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			productFragment.updateContent(category);
		}
		else
		{
			Intent intent = new Intent(this, ProductActivity.class);
			intent.putExtra("category", category);
			startActivity(intent);
		}
	}

	@Override
	public void onFirstButtonClick(View v)
	{
		ActivityHelper.addButtonClick(this, v);
	}

	@Override
	public void onSecondButtonClick(View v)
	{
		ActivityHelper.detailsButtonClick(this, v);
	}
}
