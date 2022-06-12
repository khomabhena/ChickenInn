package com.kmab.chickeninn;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ConfirmationMessage extends AppCompatActivity {

    ListView listView;
    ArrayList<String> smsList;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    DBOperations dbOperations;
    SQLiteDatabase db;
    List<String> messageKeys;

    static int position = 0;
    SetterShops setterShops;
    List listOrders;
    Context context;
    static String message = "", orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        position = getIntent().getExtras().getInt("position");
        setterShops = (SetterShops) Shops.list.get(position);
        listOrders = MainActivity.listOrders;

        dbOperations = new DBOperations(ConfirmationMessage.this);
        db = dbOperations.getWritableDatabase();
        messageKeys = dbOperations.getConfirmationKeys(db);
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        listView= findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sendOrder();
            }
        });

        showMessages();
    }

    private void sendOrder() {
        JSONArray jsonArray = new XEncrypt(context).putNewsOrders(listOrders, message);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("timelyNewsStories", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //ToDO Generate order number, Insert orders in DB when generating JSON Objects
        String details = "_Chicken Inn Orders_\n\nOrder Number: " + orderNumber + "\n";
        String message = details + "\n\n" + new XEncrypt(context).encryptString(jsonObject.toString());

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + setterShops.getWhatsApp() + "&text=" + urlEncode(message)));
        startActivity(intent);

        finish();
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private String calculateCharge() {
        double price = 0;
        for (int x = 0; x < listOrders.size(); x++) {
            SetterMeals setter = (SetterMeals) listOrders.get(x);
            price += setter.getPrice();
        }

        String charge = String.valueOf(price);
        if (charge.length() - charge.indexOf(".") - 1 < 2)
            charge += "0";

        return charge;
    }

    private void showMessages() {
        Uri inboxURI = Uri.parse("content://sms/inbox");
        smsList = new ArrayList<>();
        String merchant = String.valueOf(setterShops.getMerchant());
        String price = calculateCharge();
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(inboxURI, null, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                if (c.getPosition() < 10) {
                    String number = c.getString(c.getColumnIndexOrThrow("address"));
                    String body = c.getString(c.getColumnIndexOrThrow("body"));

                    if (number.contains("+263164")/* && body.toLowerCase().contains(merchant)
                            && body.contains(price) && (body.contains(getDate()) || body.contains(getDateNext()))*/) {
                        //body = body.substring(0, body.indexOf("New wallet"));
                        message = body;
                        smsList.add("\nNumber: " + number + "\nMessage: " + body + "\n");

                        if (!messageKeys.contains(body))
                            new Insert(ConfirmationMessage.this).execute(body);
                    }
                }
            }
            c.close();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsList);
            listView.setAdapter(adapter);
        }
    }

    String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        String day = getTheValue(calendar.get(Calendar.DAY_OF_MONTH));
        String month = getTheValue(calendar.get(Calendar.MONTH) +1);
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);

        return  year + month + day;
    }

    String getDateNext() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        String day = getTheValue(calendar.get(Calendar.DAY_OF_MONTH) + 1);
        String month = getTheValue(calendar.get(Calendar.MONTH) +1);
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);

        return  year + month + day;
    }

    public String getTheValue(int value){
        String theValue = String.valueOf(value);
        if (theValue.length() == 1){
            return "0"+theValue;
        } else {
            return theValue;
        }
    }

    private class Insert extends AsyncTask<String, Void, Void> {

        Context context;

        Insert(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            String message = params[0];

            if (!messageKeys.contains(message)) {
                //makeAdImpression(setter);
                ContentValues values = new ContentValues();
                values.put(DBContract.ConfirmationMessage.MESSAGE, message);

                db.insert(DBContract.ConfirmationMessage.TABLE_NAME, null, values);
            }

            messageKeys = dbOperations.getConfirmationKeys(db);

            return null;
        }

    }

}
