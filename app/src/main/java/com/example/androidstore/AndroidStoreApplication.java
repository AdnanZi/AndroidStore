package com.example.androidstore;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.example.androidstore.entities.Cart;
import com.example.androidstore.helpers.PopulateDbHelper;

public class AndroidStoreApplication extends Application
{
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		
		// Reset LoggedIn value whenever application starts
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = preferences.edit();
		editor.putBoolean("LoggedIn", false);
		editor.commit();
		
		PopulateDbHelper.storeAdminUser(this);
		PopulateDbHelper.populateDatabaseIfEmpty(this);
		
		Cart.Instance().clearCart();
	}
}
