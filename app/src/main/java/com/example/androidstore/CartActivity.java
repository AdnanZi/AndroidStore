package com.example.androidstore;

import java.util.ArrayList;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidstore.admin.ProductsListActivity;
import com.example.androidstore.entities.Cart;
import com.example.androidstore.entities.Cart.CartLine;
import com.example.androidstore.entities.Product;

public class CartActivity extends ActionBarActivity
{

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
		
		ActionBar actionBar = getSupportActionBar();
		
		if (actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		createItemsView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cart, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		
		int itemId = item.getItemId();
		
		if (itemId == android.R.id.home)
		{
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
		} 
		else if (itemId == R.id.action_continue_shopping)
		{
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		} 
		else if (itemId == R.id.action_checkout_now)
		{
			if (Cart.Instance().getCart().isEmpty())
			{
				intent = new Intent(this, MainActivity.class);
				startActivity(intent);

				Toast.makeText(this, "Your cart is empty. Please add some items.", Toast.LENGTH_LONG).show();

				return true;
			}

			intent = new Intent(this, CheckoutActivity.class);
			startActivity(intent);
		} 
		else if (itemId == R.id.action_admin)
		{
			intent = new Intent(this, ProductsListActivity.class);
			startActivity(intent);
		} 
		
		return true;
	}
	
	private void createItemsView() 
	{
		ViewGroup listView = (ViewGroup)findViewById(R.id.cart_list);
		TextView totalTw = (TextView)findViewById(R.id.cart_total_value);

		ArrayList<CartLine> lines = Cart.Instance().getCart();
		Iterator<CartLine> iterator = lines.iterator();
		
		while (iterator.hasNext())
		{
			CartLine line = iterator.next();
			
			View lineLayout = getLayoutInflater().inflate(R.layout.list_cart, null);
			
			TextView item = (TextView)lineLayout.findViewById(R.id.cart_item);
			TextView quantity = (TextView)lineLayout.findViewById(R.id.cart_quantity);
			TextView price = (TextView)lineLayout.findViewById(R.id.cart_price);
			Button removeButton = (Button)lineLayout.findViewById(R.id.cart_remove);
			
			if (item != null)
			{
				if (quantity == null)
				{
					item.setText(String.format("%s (%s)", line.getProduct().getProductName(), line.getQuantity()));
				}
				else
				{
					item.setText(line.getProduct().getProductName());
					quantity.setText(String.valueOf(line.getQuantity()));
				}
			}
			
			if (price != null)
			{
				price.setText(String.valueOf(line.getProduct().getPrice() * line.getQuantity()));
			}
			
			if (removeButton != null)
			{
				removeButton.setId(removeButton.getId() + lines.indexOf(line));
				removeButton.setTag(line.getProduct());
				removeButton.setOnClickListener(new OnClickListener()
				{
					@SuppressLint("NewApi")
					@Override
					public void onClick(View v)
					{
						Cart.Instance().removeFromCart((Product)v.getTag());
						
						FragmentActivity activity = (FragmentActivity)v.getContext();
						activity.recreate();
					}
				});
			}
			
			listView.addView(lineLayout);
		}
		
		if (totalTw != null)
		{
			totalTw.setText(String.valueOf(Cart.Instance().ComputeTotalValue()));
		}
	}
	
	
}
