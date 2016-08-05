package com.example.androidstore.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.androidstore.CartActivity;
import com.example.androidstore.DetailsActivity;
import com.example.androidstore.admin.EditProductActivity;
import com.example.androidstore.admin.ProductsListActivity;
import com.example.androidstore.entities.Cart;
import com.example.androidstore.entities.Product;

public class ActivityHelper
{	
	public static void detailsButtonClick(Context context, View v)
	{
		Intent intent = new Intent(context, DetailsActivity.class);
		intent.putExtra("productId", v.getTag().toString());
		
		context.startActivity(intent);
	}
	
	public static void addButtonClick(Context context, View v)
	{
		String productId = v.getTag().toString();
		Product product = getProduct(context, productId);
		
		Cart.Instance().addToCart(product);
	}
	
	public static void checkOutButtonClick(Context context)
	{
		/*try
		{
			Cart.Instance().saveCartData(context);
		}
		catch (IOException e)
		{
			Log.d("Error while saving cart", e.getMessage());
			Toast.makeText(context, "Error while saving cart", Toast.LENGTH_SHORT).show();
		}*/
		
		if (Cart.Instance().getCart().isEmpty())
		{
			Toast.makeText(context, "Cart is empty. Please add some items prior to Checkout", Toast.LENGTH_LONG).show();
			return;
		}
		
		Intent intent = new Intent(context, CartActivity.class);
		
		context.startActivity(intent);
	}
	
	public static void editButtonClick(Context context, View v)
	{
		Intent intent = new Intent(context, EditProductActivity.class);
		
		if (v.getTag() != null)
		{
			intent.putExtra("productId", v.getTag().toString());
		}
		
		context.startActivity(intent);
	}
	
	public static void deleteButtonClick(Context context, View v)
	{	
		int deleted = new AndroidStoreDbHelper(context).deleteProduct(v.getTag().toString());
		
		if (deleted >= 1)
		{
			FragmentActivity currentActivity = (FragmentActivity)context;
			currentActivity.recreate();
		}
		else
		{
			Log.d("Delete operation failed.", "Product is not deleted.");
		}
	}
	
	public static Product getProduct(Context context, String productId)
	{
		return new AndroidStoreDbHelper(context).getProduct(productId);
	}

	public static void saveProduct(Context context, Product product)
	{
		if (product.getId() == 0)
		{
			createProduct(context, product);
		}
		else
		{
			updateProduct(context, product);
		}
	}

	private static void updateProduct(Context context, Product product)
	{
		int updated = new AndroidStoreDbHelper(context).updateProduct(product);
		
		if (updated < 1)
		{
			Log.d("Update operation failed.", "Product is not updated.");
		}
		
		Intent intent = new Intent(context, ProductsListActivity.class);
		context.startActivity(intent);
	}

	private static void createProduct(Context context, Product product)
	{		
		long created = new AndroidStoreDbHelper(context).createProduct(product);
		
		if (created == -1)
		{
			Log.d("Create operation failed.", "Product is not created.");
		}

		Intent intent = new Intent(context, ProductsListActivity.class);
		context.startActivity(intent);
	}
}
