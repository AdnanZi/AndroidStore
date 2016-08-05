package com.example.androidstore.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.example.androidstore.R;
import com.example.androidstore.entities.Cart;
import com.example.androidstore.infrastructure.MyEmailSender;
import javax.mail.MessagingException;

public class SendEmailTask extends AsyncTask<Void, Void, Boolean>
{
    Context context;
    SendEmailTaskListener listener;

    public interface SendEmailTaskListener
    {
        void sendEmailTaskCallback(Boolean result);
    }

    public SendEmailTask(Context context, SendEmailTaskListener listener)
    {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
        try
        {
            SendEmail();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    protected void onPostExecute(Boolean result)
    {
        listener.sendEmailTaskCallback(result);
    }

    private void SendEmail() throws Exception
    {
        String body = createEmailBody();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String address = preferences.getString("emailAddress", null);
        String username = preferences.getString("emailUsername", null);
        String password = preferences.getString("emailPassword", null);

        MyEmailSender sender = new MyEmailSender();

        try
        {
            sender.SendMail(body, address, username, password);
        }
        catch (MessagingException e)
        {
            Log.d("Exception thrown by JavaMail Api", e.getMessage());
            throw e;
        }
        catch (IllegalArgumentException e)
        {
            Log.d("Bad argument", e.getMessage());
            throw e;
        }
    }

    private String createEmailBody()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("New Order\n");
        sb.append("\n");
        sb.append("\nItems:");

        sb.append(Cart.Instance().cartLinesToString());

        sb.append("\n");
        sb.append("\nShip to:");

        sb.append(shippingInfoToString());

        return sb.toString();
    }

    private String shippingInfoToString() {
        StringBuilder sb = new StringBuilder();

        Activity activity = (Activity)context;

        EditText name = (EditText) activity.findViewById(R.id.checkout_name_edit);
        EditText line = (EditText) activity.findViewById(R.id.checkout_line_edit);
        EditText city = (EditText) activity.findViewById(R.id.checkout_city_edit);
        EditText zip = (EditText) activity.findViewById(R.id.checkout_zip_edit);
        EditText state = (EditText) activity.findViewById(R.id.checkout_state_edit);
        TextView location = (TextView) activity.findViewById(R.id.checkout_location_edit);

        if (location != null && location.getText().toString() != context.getResources().getString(R.string.checkout_nolocation) &&
                name != null && !TextUtils.isEmpty(name.getText())) {
            sb.append(String.format("\n%s", name.getText().toString()));
            sb.append(String.format("\n%s", location.getText().toString()));
        } else if (name != null && !TextUtils.isEmpty(name.getText()) &&
                line != null && !TextUtils.isEmpty(line.getText()) &&
                city != null && !TextUtils.isEmpty(city.getText()) &&
                zip != null && !TextUtils.isEmpty(zip.getText()) &&
                state != null && !TextUtils.isEmpty(state.getText())) {
            sb.append(String.format("\n%s", name.getText().toString()));
            sb.append(String.format("\n%s, %s, %s, %s",
                    line.getText().toString(),
                    city.getText().toString(),
                    zip.getText().toString(),
                    state.getText().toString()));
        }

        return sb.toString();
    }

}