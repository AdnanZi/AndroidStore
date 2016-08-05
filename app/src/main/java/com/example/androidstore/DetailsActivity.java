package com.example.androidstore;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidstore.entities.Product;
import com.example.androidstore.helpers.ActivityHelper;

public class DetailsActivity extends FragmentActivity
{	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		Intent intent = getIntent();
		String productId = intent.getStringExtra("productId");
		
		Button backButton = (Button)findViewById(R.id.product_details_back);
		
		if (backButton != null)
		{
			backButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					FragmentActivity activity = (FragmentActivity)v.getContext();
					activity.finish();
				}
			});
		}
		
		Button addButton = (Button)findViewById(R.id.product_details_add);
		
		if (addButton != null)
		{	
			addButton.setTag(productId);
			addButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ActivityHelper.addButtonClick(v.getContext(), v);
				}
			});
		}
		
		createDetailsView(productId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}
	
	private void createDetailsView(String productId)
	{
		//TextView id = (TextView)findViewById(R.id.product_details_id);
		TextView name = (TextView)findViewById(R.id.product_details_name);
		TextView description = (TextView)findViewById(R.id.product_details_description);
		TextView price = (TextView)findViewById(R.id.product_details_price);
		ImageView image = (ImageView)findViewById(R.id.product_details_image);
		
		Product product = ActivityHelper.getProduct(this, productId);
		
		if (product != null)
		{
			//if (id != null)
			//{
			//	id.setText(String.valueOf(product.id));
			//}
			
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
	
			if (image != null && product.getImage() != null)
			{
				image.setImageBitmap(BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length));
			}
		}
	}
}
