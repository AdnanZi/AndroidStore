package com.example.androidstore.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidstore.MainActivity;
import com.example.androidstore.R;
import com.example.androidstore.entities.Product;
import com.example.androidstore.helpers.ActivityHelper;
import com.example.androidstore.helpers.AndroidStoreDbHelper;

public class EditProductActivity extends ActionBarActivity
{
	private final int PICK_FROM_FILE = 1;
	
	private TextView id;
	private EditText name;
	private EditText description;
	private EditText price;
	private EditText category;
	private ImageView image;
	private TextView validation;
	
	@SuppressLint("NewApi")
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
		setContentView(R.layout.activity_edit_product);
		
		ActionBar actionBar = getSupportActionBar();
		
		if (actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		id = (TextView)findViewById(R.id.edit_product_id);
		name = (EditText)findViewById(R.id.edit_product_name);
		description = (EditText)findViewById(R.id.edit_product_description);
		price = (EditText)findViewById(R.id.edit_product_price);
		category = (EditText)findViewById(R.id.edit_product_category);
		image = (ImageView)findViewById(R.id.edit_product_image);
		validation = (TextView)findViewById(R.id.edit_product_validation);
		
		String productId = getIntent().getStringExtra("productId");
		
		if (productId != null)
		{
			if (id != null)
			{
				id.setText(productId);
			}
			
			setProductView(productId);
		}
		
		TextView headerText = (TextView)findViewById(R.id.edit_product_header);
		
		if (headerText != null)
		{
			if (productId == null)
			{
				headerText.setText("Add product");
			}
			else
			{
				headerText.setText("Edit product");
			}
		}
		
		Button saveButton = (Button)findViewById(R.id.edit_product_save);
		
		if (saveButton != null)
		{
			saveButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					saveProductButtonClick(v);
				}
			});
		}
		
		Button insertImageButton = (Button)findViewById(R.id.edit_product_insert_image);
		
		if (insertImageButton != null)
		{
			insertImageButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					InsertNewImage();
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_product, menu);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if (requestCode == PICK_FROM_FILE && resultCode == RESULT_OK)
		{
			ImageView image = (ImageView)findViewById(R.id.edit_product_image);
			Uri uri = intent.getData();
			byte[] data = null;
			
			try
			{
				data = getImageBytesFromUri(uri);
			}
			catch (IOException e)
			{
				Log.d("An exception has occurred during IO operation", e.getMessage());
			}
			
			if (data != null && data.length > 0)
			{
				image.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
			}
		}
	}

	private void InsertNewImage()
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		startActivityForResult(Intent.createChooser(intent, "Get data with"), PICK_FROM_FILE);
	}

	private byte[] getImageBytesFromUri(Uri uri) throws IOException
	{
		InputStream dataStream = null;	
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int c;
		
		try
		{
			dataStream = getContentResolver().openInputStream(uri);
			
			if (dataStream != null)
			{
				while((c = dataStream.read()) != -1)
				{
					byteStream.write(c);
				}
				
				return byteStream.toByteArray();
			}
		}
		finally
		{
			if (dataStream != null)
			{
				dataStream.close();
			}
		}
		
		return null;
	}
	
	private void setProductView(String productId)
	{
		Product product = new AndroidStoreDbHelper(this).getProduct(productId);
		
		if (product == null)
		{
			return;
		}
		
		if (name != null)
		{
			name.setText(product.getProductName());
		}
		
		if (description != null)
		{
			description.setText(product.getDescription());
		}
		
		if (price != null)
		{
			price.setText(String.valueOf(product.getPrice()));
		}
		
		if (category != null)
		{
			category.setText(product.getCategory());
		}
		
		if (image != null)
		{
			byte[] data = product.getImage();
			
			if (data != null)
			{
				image.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
			}
		}
	}

	private void saveProductButtonClick(View v)
	{	
		if (validation != null && validation.getVisibility() == TextView.VISIBLE)
		{
			validation.setVisibility(TextView.GONE);
		}
		
		if (name == null || TextUtils.isEmpty(name.getText()) ||
				description == null || TextUtils.isEmpty(description.getText()) ||
				price == null || TextUtils.isEmpty(price.getText()) ||
				category == null || TextUtils.isEmpty(category.getText()))
		{
			if (validation != null)
			{
				validation.setVisibility(TextView.VISIBLE);
				validation.requestFocus();
			}
			
			return;
		}
		
		Product product = new Product();
		
		if (id != null && id.getText().toString() != "")
		{
			product.setId(Integer.valueOf(id.getText().toString()));
		}
		
		product.setProductName(name.getText().toString());
		product.setDescription(description.getText().toString());
		product.setPrice(Double.valueOf(price.getText().toString()));
		product.setCategory(category.getText().toString());
		
		if (image != null && image.getDrawable() != null)
		{
			Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			
			product.setImage(stream.toByteArray());
		}
		
		ActivityHelper.saveProduct(this, product);
	}
}
