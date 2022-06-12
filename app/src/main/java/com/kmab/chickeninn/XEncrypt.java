package com.kmab.chickeninn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class XEncrypt {

    Context context;
    List listKeys;
    DBOperations dbOperations;
    SQLiteDatabase db;

    public XEncrypt(Context context) {
        this.context = context;

        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();

        listKeys = dbOperations.getOrderKeys(db);
    }

    private String[] originalLetters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private String[] encrypt =        {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m", "M", "N", "B", "V", "C", "X", "Z", "L", "K", "J", "H", "G", "F", "D", "S", "A", "P", "O", "I", "U", "Y", "T", "R", "E", "W", "Q"};

    public JSONArray putNewsOrders(List list, String message) {
        JSONArray jsonArrayStory = new JSONArray();

        String orderNumber = new XOrderNumber().getOrderNumber();
        ConfirmationMessage.orderNumber = orderNumber;

        for (int x = 0; x < list.size(); x++) {
            SetterMeals setter = (SetterMeals) list.get(x);
            SetterOrders setterOrders = new SetterOrders(setter.getType(), setter.getId(),
                    setter.getName(), setter.getPrice(), System.currentTimeMillis(), message, orderNumber);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", setterOrders.getType());
                jsonObject.put("id", setterOrders.getId());
                jsonObject.put("name", setterOrders.getName());
                jsonObject.put("price", setterOrders.getPrice());
                jsonObject.put("timestamp", System.currentTimeMillis());
                jsonObject.put("message", message);
                jsonObject.put("orderNumber", setterOrders.getOrderNumber());
            } catch (JSONException e) {
                //
            }
            jsonArrayStory.put(jsonObject);
            new InsertOrders(context, listKeys).execute(setterOrders);
        }

        return jsonArrayStory;
    }

    public String encryptString(String sample) {
        List<String> listOG = new LinkedList<>(Arrays.asList(originalLetters));
        StringBuilder newString = new StringBuilder();
        char[] charArray = sample.toCharArray();
        for (char aCharArray : charArray) {
            String sam = String.valueOf(aCharArray);
            int index = listOG.indexOf(sam);
            if (index == -1)
                newString.append(aCharArray);
            else
                newString.append(encrypt[index]);
        }

        return newString.toString();
    }



    private boolean getOrderObjects(String decrypted, List listKeys) {
        try {
            JSONObject jsonObjectDecrypted = new JSONObject(decrypted);
            JSONArray jsonArrayStories = jsonObjectDecrypted.getJSONArray("timelyNewsStories");

            int countStory = 0;
            for (int x = 0;  x < jsonArrayStories.length(); x++) {
                JSONObject jo = jsonArrayStories.getJSONObject(countStory);
                SetterOrders setter = new SetterOrders(
                        jo.getString("type"),
                        jo.getInt("id"),
                        jo.getString("name"),
                        jo.getDouble("price"),
                        jo.getLong("timestamp"),
                        jo.getString("message"),
                        jo.getString("orderNumber"));

                if (listKeys.contains(setter.getMessage()))
                    continue;
                else
                    new InsertOrders(context, listKeys).execute(setter);

                countStory++;
            }

            if (countStory == 0)
                Toast.makeText(context, "Order already exists", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "Orders added: " + countStory, Toast.LENGTH_LONG).show();

            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private String decryptString(String sample) {
        List<String> listOG = new LinkedList<>(Arrays.asList(encrypt));
        StringBuilder newString = new StringBuilder();
        char[] charArray = sample.toCharArray();
        for (char aCharArray : charArray) {
            String sam = String.valueOf(aCharArray);
            int index = listOG.indexOf(sam);
            if (index == -1)
                newString.append(aCharArray);
            else
                newString.append(originalLetters[index]);
        }

        return newString.toString();
    }
}
