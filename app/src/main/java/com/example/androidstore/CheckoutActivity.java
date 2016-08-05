package com.example.androidstore;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidstore.admin.ProductsListActivity;
import com.example.androidstore.entities.Cart;
import com.example.androidstore.fragments.AddressFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.example.androidstore.tasks.*;

public class CheckoutActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, GetAddressTask.GetAddressTaskListener, SendEmailTask.SendEmailTaskListener
{
	LocationClient locationClient = null;
	ProgressBar progressBar = null;
	AddressFragment addressFragment = new AddressFragment();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);

		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		Button completeOrderButton = (Button)findViewById(R.id.checkout_complete);

		if (completeOrderButton != null)
		{
			completeOrderButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					completeOrderButtonClick();
				}
			});
		}

		CheckBox locationCb = (CheckBox)findViewById(R.id.checkout_location_cb);

		if (locationCb != null)
		{
			locationCb.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (isChecked)
					{
						hideAddressFragment();
					}
					else
					{
						showAddressFragment();
					}
				}
			});
		}

		if (isGooglePlayServicesAvailable())
		{
			locationClient = new LocationClient(this, this, this);
		}
		else
		{
			showAddressFragment();
		}

		progressBar = (ProgressBar)findViewById(R.id.checkout_address_progress);
	}

	@Override
	protected void onStart()
	{
		if (locationClient != null)
		{
			locationClient.connect();
		}

		super.onStart();
	}

	@Override
	protected void onStop()
	{
		if (locationClient != null)
		{
			locationClient.disconnect();
		}

		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.checkout, menu);
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
		else if (itemId == R.id.action_admin)
		{
			Intent intent = new Intent(this, ProductsListActivity.class);
			startActivity(intent);
		}

		return true;
	}

	/*
	 * ConnectionCallbacks implementations
	 */

	@Override
	public void onConnected(Bundle arg0)
	{
		setLocationFields();
	}

	@Override
	public void onDisconnected()
	{
		locationClient.connect();
	}

	/*
	 * ConnectionCallbacks implementations end
	 */

	/*
	 * OnConnectionFailedListener implementation
	 */

	@Override
	public void onConnectionFailed(ConnectionResult arg0)
	{
	}

	/*
	 * OnConnectionFailedListener implementation end
	 */

	/*
	 * Tasks callbacks implementations
	 */

	public void getAddressTaskCallback(String result)
	{
		CheckBox locationCb = (CheckBox) findViewById(R.id.checkout_location_cb);
		TextView locationTw = (TextView) findViewById(R.id.checkout_location_edit);

		if (result != null && locationTw != null && locationCb != null) {
			locationTw.setText(result);

			locationCb.setChecked(true);
			locationCb.setEnabled(true);

			hideAddressFragment();
		} else if (locationTw != null && locationCb != null) {
			locationTw.setText(R.string.checkout_nolocation);

			locationCb.setChecked(false);
			locationCb.setEnabled(false);

			showAddressFragment();
		}

		if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
		}
	}

	public void sendEmailTaskCallback(Boolean result)
	{
		if (result == false)
		{
			Toast.makeText(this, "Unable to complete order. Please check your application settings or internet connection and try again.", Toast.LENGTH_LONG).show();
		}
		else
		{
			Cart.Instance().clearCart();

			Intent intent = new Intent(this, CompleteActivity.class);
			startActivity(intent);
		}

		if (progressBar != null)
		{
			progressBar.setVisibility(View.GONE);
		}
	}

	/*
	 * Tasks callbacks implementations end
	 */

	private Boolean isGooglePlayServicesAvailable()
	{
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (resultCode == ConnectionResult.SUCCESS)
		{
			return true;
		}

		return false;
	}

	private void setLocationFields()
	{
		Location currentLocation = null;

		if (locationClient != null)
		{
			currentLocation = locationClient.getLastLocation();
		}

		// Handle Address lookup on a worker thread
		if (progressBar != null)
		{
			progressBar.setVisibility(View.VISIBLE);
		}

		(new GetAddressTask(this, this)).execute(currentLocation);
	}

	private void hideAddressFragment()
	{
		if (findViewById(R.id.checkout_address_fragment) != null)
		{
			getSupportFragmentManager().beginTransaction().remove(addressFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
		}
	}

	private void showAddressFragment()
	{
		if (findViewById(R.id.checkout_address_fragment) != null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.checkout_address_fragment, addressFragment).commit();
		}
	}

	private void completeOrderButtonClick()
	{
		EditText name = (EditText)findViewById(R.id.checkout_name_edit);
		EditText line = (EditText)findViewById(R.id.checkout_line_edit);
		EditText city = (EditText)findViewById(R.id.checkout_city_edit);
		EditText zip = (EditText)findViewById(R.id.checkout_zip_edit);
		EditText state = (EditText)findViewById(R.id.checkout_state_edit);
		TextView location = (TextView)findViewById(R.id.checkout_location_edit);
		TextView validation = (TextView)findViewById(R.id.checkout_validation);

		if (validation != null && validation.getVisibility() == TextView.VISIBLE)
		{
			validation.setVisibility(TextView.GONE);
		}

		if ((location == null || location.getText().toString() == getResources().getString(R.string.checkout_nolocation) ||
				name == null || TextUtils.isEmpty(name.getText())) &&
				(name == null || TextUtils.isEmpty(name.getText()) ||
				line == null || TextUtils.isEmpty(line.getText()) ||
				city == null || TextUtils.isEmpty(city.getText()) ||
				zip == null || TextUtils.isEmpty(zip.getText()) ||
				state == null || TextUtils.isEmpty(state.getText())))
		{
			if (validation != null)
			{
				validation.setVisibility(TextView.VISIBLE);
				validation.requestFocus();
			}

			return;
		}

		// Send email on a worker thread and do the post processing
		if (progressBar != null)
		{
			progressBar.setVisibility(View.VISIBLE);
		}

		(new SendEmailTask(this, this)).execute();
	}
}
