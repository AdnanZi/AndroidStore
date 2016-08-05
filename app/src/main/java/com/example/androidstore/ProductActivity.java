package com.example.androidstore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.androidstore.admin.ProductsListActivity;
import com.example.androidstore.fragments.ProductFragment;
import com.example.androidstore.helpers.ActivityHelper;
import com.example.androidstore.infrastructure.MyCursorAdapter.OnButtonsClickListener;

public class ProductActivity extends ActionBarActivity implements OnButtonsClickListener
{
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			finish();
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		
		ActionBar actionBar = getSupportActionBar();
		
		if (actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		Intent intent = getIntent();
		String category = intent.getStringExtra("category");
		ProductFragment productFragment = (ProductFragment)getSupportFragmentManager().findFragmentById(R.id.product_fragment);
		
		if (productFragment != null)
		{
			productFragment.updateContent(category);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int itemId = item.getItemId();
		
		if (itemId == android.R.id.home)
		{
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
		} 
		else if (itemId == R.id.action_checkout)
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
