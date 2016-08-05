package com.example.androidstore.entities;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint("UseSparseArrays")
public class Cart
{
	private static Cart cart;
	private Map<Integer, CartLine> lines;
	private final String fileName = "cartData";
	
	private Cart() 
	{
		lines = new HashMap<Integer, CartLine>();
	}
	
	public static synchronized Cart Instance()
	{
		if (cart == null)
		{
			cart = new Cart();
		}
		
		return cart;
	}
	
	public void addToCart(Product product)
	{
		if (lines.containsKey(product.getId()))
		{
			lines.get(product.getId()).quantity++;
		}
		else
		{
			lines.put(product.getId(), new CartLine(product, 1));
		}
	}
	
	public void removeFromCart(Product product)
	{
		if (lines.containsKey(product.getId()))
		{
			lines.remove(product.getId());
		}
	}
	
	public ArrayList<CartLine> getCart()
	{
		return new ArrayList<CartLine>(lines.values());
		
		/*DataInputStream dataStream = null;
		ArrayList<CartLine> lines = new ArrayList<Cart.CartLine>();
		
		try
		{
			dataStream = new DataInputStream(new BufferedInputStream(context.openFileInput(fileName)));
			
			try 
			{
				while (true)
				{
					Product product = new Product();
					product.productName = dataStream.readUTF();
					product.price = dataStream.readDouble();
					
					lines.add(new CartLine(product, dataStream.readInt()));
				}
			}
			catch (EOFException e) {}
			
			return lines;
		}
		finally
		{
			if (dataStream != null)
			{
				dataStream.close();
			}
		}*/
	}
	
	// Obsolete/unused
	public void saveCartData(Context context) throws IOException
	{
		DataOutputStream out = null;
		Iterator<Entry<Integer, CartLine>> iterator = lines.entrySet().iterator();
		
		try
		{
			out = new DataOutputStream(new BufferedOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE)));
			
			while (iterator.hasNext())
			{
				CartLine line = iterator.next().getValue();
				
				out.writeUTF(line.product.getProductName());
				out.writeDouble(line.product.getPrice());
				out.writeInt(line.getQuantity());
			}
			
		}
		finally
		{
			if (out != null)
			{
				out.close();
			}
		}
	}
	
	public void clearCart()
	{
		if (lines != null)
		{
			lines.clear();
		}
		
		/*if (context.getFileStreamPath(fileName) != null)
		{
			context.deleteFile(fileName);
		}*/
	}
	
	public double ComputeTotalValue()
	{
		double value = 0;
		Iterator<Entry<Integer, CartLine>> iterator = lines.entrySet().iterator();
		
		while (iterator.hasNext())
		{
			CartLine line = iterator.next().getValue();
			
			value += line.getProduct().getPrice() * line.getQuantity();
		}
		
		return value;
	}
	
	public String cartLinesToString()
	{
		StringBuilder sb = new StringBuilder();
		
		Iterator<CartLine> iterator = lines.values().iterator();
		
		while (iterator.hasNext())
		{
			CartLine line = iterator.next();
			
			sb.append(String.format("\n %s (%s) \t %s",
					line.getProduct().getProductName(), 
					line.getQuantity(),
					String.valueOf(line.getQuantity() * line.getProduct().getPrice())));
		}
		
		sb.append("\n");
		sb.append(String.format("\nTotal order value: %s", String.valueOf(ComputeTotalValue())));
		
		return sb.toString();
	}
	
	public class CartLine
	{
		private Product product;
		private int quantity;
		
		public CartLine(Product product, int quantity)
		{
			this.product = product;
			this.quantity = quantity;
		}
		
		public Product getProduct()
		{
			return product;
		}
		
		public int getQuantity()
		{
			return quantity;
		}
	}
}
