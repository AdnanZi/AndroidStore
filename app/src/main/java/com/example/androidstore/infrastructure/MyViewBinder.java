package com.example.androidstore.infrastructure;

import com.example.androidstore.R;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewBinder implements ViewBinder
{
	@Override
	public boolean setViewValue(View v, Cursor c, int columnIndex)
	{
		try
		{
			if (v.getId() == R.id.product_thumbnail)
			{
				ImageView image = (ImageView)v;
				byte[] imageBytes = c.getBlob(columnIndex);
				
				if (image != null && imageBytes != null)
					image.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
				
				return true;
			}
			else if (v.getId() == R.id.product_name || v.getId() == R.id.product_price || 
					v.getId() == R.id.product_category)
			{
				TextView text = (TextView)v;
				
				if (text != null)
					text.setText(c.getString(columnIndex));
				
				return true;
			}
			
			return false;
		}
		catch (Exception e)
		{
			Log.d("Error while binding view", e.getMessage());
			return false;
		}
	}

}
