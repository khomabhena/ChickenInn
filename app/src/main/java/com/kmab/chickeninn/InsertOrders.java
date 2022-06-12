package com.kmab.chickeninn;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

public class InsertOrders extends AsyncTask<SetterOrders, Void, Void> {

    private Context context;
    private List listKeys;
    DBOperations dbOperations;
    SQLiteDatabase db;

    public InsertOrders(Context context, List listKeys) {
        this.context = context;
        this.listKeys = listKeys;

        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
    }

    @Override
    protected Void doInBackground(SetterOrders... params) {
        SetterOrders setter = params[0];

        ContentValues values = new ContentValues();
        values.put(DBContract.Orders.TYPE, setter.getType());
        values.put(DBContract.Orders.ORDER_ID, setter.getId());
        values.put(DBContract.Orders.NAME, setter.getName());
        values.put(DBContract.Orders.PRICE, setter.getPrice());
        values.put(DBContract.Orders.TIMESTAMP, setter.getTimestamp());
        values.put(DBContract.Orders.MESSAGE, setter.getMessage());
        values.put(DBContract.Orders.ORDER_NUMBER, setter.getOrderNumber());

        if (!listKeys.contains(setter.getMessage()))
            db.insert(DBContract.Orders.TABLE_NAME, null, values);
        /*else
            db.update(DBContract.Orders.TABLE_NAME, values,
                    DBContract.Orders.UNIQUE_KEY + "='" + setter.getUniqueKey() + "'", null);*/

        return null;
    }

}
