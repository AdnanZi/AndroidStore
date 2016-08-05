package com.example.androidstore.infrastructure;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidstore.R;

/* 
 * Extends SimpleCursorAdapter 
 * with two button click handlers
 * for list elements
 * 
 * USAGE:
 * 		Implement OnButtonsClickListener interface in the context activity
 * 			that will handle button clicks
 * 		Set list element's identity tag id 'list_id'
 * 		Set first button's id 'button_first'
 * 		Set second button's id 'button_second'
 * 
 * PARAMS:
 * 		context: The context activity
 * 		viewBinder: 'null' if no custom binder should be used
 * 		(See SimpleCursorAdapter documentation for more)
 * */
public class MyCursorAdapter extends SimpleCursorAdapter
{
	private OnButtonsClickListener buttonsClickCallback;
	
	public interface OnButtonsClickListener
	{
		public void onFirstButtonClick(View v);
		public void onSecondButtonClick(View v);
	}

	public MyCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, ViewBinder viewBinder)
	{
		super(context, layout, c, from, to, flags);
		
		if (viewBinder != null)
		{
			this.setViewBinder(viewBinder);
		}
		
		buttonsClickCallback = (OnButtonsClickListener)context;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		view = super.getView(position, view, parent);
		
		setButtonClickListeners(view);
		
		return view;
	}

	private void setButtonClickListeners(View view)
	{
		String productId = null;
		
		TextView idText = (TextView)view.findViewById(R.id.list_id);
		
		if (idText != null)
		{
			productId = idText.getText().toString();
		}
		
		Button firstButton = (Button)view.findViewById(R.id.button_first);
		
		if (firstButton != null)
		{
			firstButton.setTag(productId);
			
			firstButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					buttonsClickCallback.onFirstButtonClick(v);
				}
			});
		}
		
		Button secondButton = (Button)view.findViewById(R.id.button_second);
		
		if (secondButton != null)
		{
			secondButton.setTag(productId);
			
			secondButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{						
					buttonsClickCallback.onSecondButtonClick(v);
				}
			});
		}
	}
}
