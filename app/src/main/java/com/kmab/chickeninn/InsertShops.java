package com.kmab.chickeninn;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

public class InsertShops extends AsyncTask<SetterShops, Void, Void> {

    private Context context;
    private List listKeys;
    DBOperations dbOperations;
    SQLiteDatabase db;

    public InsertShops(Context context, List listKeys) {
        this.context = context;
        this.listKeys = listKeys;

        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
    }

    @Override
    protected Void doInBackground(SetterShops... params) {
        SetterShops setter = params[0];

        ContentValues values = new ContentValues();
        values.put(DBContract.Shops.UNIQUE_KEY, setter.getUniqueKey());
        values.put(DBContract.Shops.CITY, setter.getCity());
        values.put(DBContract.Shops.ADDRESS, setter.getAddress());
        values.put(DBContract.Shops.MERCHANT, setter.getMerchant());
        values.put(DBContract.Shops.PREFIX, setter.getPrefix());
        values.put(DBContract.Shops.WHATSAPP, setter.getWhatsApp());

        if (!listKeys.contains(setter.getUniqueKey()))
            db.insert(DBContract.Shops.TABLE_NAME, null, values);
        else
            db.update(DBContract.Shops.TABLE_NAME, values,
                    DBContract.Shops.UNIQUE_KEY + "='" + setter.getUniqueKey() + "'", null);

        return null;
    }

}
