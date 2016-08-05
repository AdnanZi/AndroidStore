package com.example.androidstore.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.androidstore.MainActivity;
import com.example.androidstore.R;
import com.example.androidstore.helpers.ActivityHelper;
import com.example.androidstore.infrastructure.MyCursorAdapter.OnButtonsClickListener;

public class ProductsListActivity extends ActionBarActivity implements OnButtonsClickListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		// If user is not logged in, redirect to LoginActivity
		if(preferences.getBoolean("LoggedIn", false) == false)
		{
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products_list);
		
		Button addButton = (Button)findViewById(R.id.admin_add_product);
		
		if (addButton != null)
		{
			addButton.setOnClickListener(new OnClickListener()
			{		
				@Override
				public void onClick(View v)
				{
					ActivityHelper.editButtonClick(v.getContext(), v);
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.products_list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int itemId = item.getItemId();
		
		if (itemId == R.id.action_main)
		{
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
		} 
		else if (itemId == R.id.action_settings)
		{
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
		
		return true;
	}

	@Override
	public void onFirstButtonClick(View v)
	{
		ActivityHelper.editButtonClick(this, v);
	}

	@Override
	public void onSecondButtonClick(View v)
	{
		ActivityHelper.deleteButtonClick(this, v);
	}
}
