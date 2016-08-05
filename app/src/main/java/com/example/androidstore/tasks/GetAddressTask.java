package com.example.androidstore.tasks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class GetAddressTask extends AsyncTask<Location, Void, String>
{
    Context context;
    GetAddressTaskListener listener;

    public interface GetAddressTaskListener
    {
        void getAddressTaskCallback(String result);
    }

    public GetAddressTask(Context context, GetAddressTaskListener listener)
    {
        super();

        this.context = context;
        this.listener = listener;
    }

    /*
     * Called on a worker thread
     */
    @Override
    protected String doInBackground(Location... params)
    {
        Geocoder geo = new Geocoder(context, Locale.getDefault());
        Location location = params[0];
        List<Address> addresses = null;

        try
        {
            if (location == null)
            {
                return null;
            }

            addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        }
        catch (Exception e)
        {
            Log.d("Error while getting address", e.getMessage());
        }

        if (addresses != null && addresses.size() > 0)
        {
            Address address = addresses.get(0);

            return String.format("%s, %s, %s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getLocality() != null ? address.getLocality() : "",
                    address.getPostalCode() != null ? address.getPostalCode() : "",
                    address.getCountryName() != null ? address.getCountryName() : "");
        }
        else
        {
            return null;
        }
    }

    /*
     * 	Called on UI thread when doInBackground returns
     */
    protected void onPostExecute(String result)
    {
        listener.getAddressTaskCallback(result);
    }
}