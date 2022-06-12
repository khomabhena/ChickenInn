package com.kmab.chickeninn;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Payments extends AppCompatActivity {

    List listOrders;
    TextView tvCharge, tvShop, tvAmount;
    static int position = 0;
    SetterShops setterShops;

    private static final int REQUEST_READ_SMS = 1884;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        position = getIntent().getExtras().getInt("position");
        setterShops = (SetterShops) Shops.list.get(position);

        listOrders = MainActivity.listOrders;
        tvCharge = findViewById(R.id.tvCharge);
        tvShop = findViewById(R.id.tvShop);
        tvAmount = findViewById(R.id.tvAmount);

        String shop = setterShops.getCity() + ", " + setterShops.getAddress();
        tvShop.setText(shop);

        tvCharge.setText(calculateCharge());
        String payment = "Pay " + calculateCharge() + " via Ecocash";
        tvAmount.setText(payment);
    }

    public void dialer(View view) {
        String harsh = Uri.encode("#");
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + setterShops.getPrefix() + setterShops.getMerchant() + harsh));
        startActivity(intent);
    }

    public void pickMessage(View view) {
        Intent intent = new Intent(context, ConfirmationMessage.class);
        intent.putExtra("position", position);
        if (Build.VERSION.SDK_INT >= 23) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_SMS},
                        REQUEST_READ_SMS);
            } else {
                startActivity(intent);
            }
        } else {
            startActivity(intent);
        }
    }

    public void finish(View view) {
        finish();
    }

    private String calculateCharge() {
        double price = 0;
        for (int x = 0; x < listOrders.size(); x++) {
            SetterMeals setter = (SetterMeals) listOrders.get(x);
            price += setter.getPrice();
        }

        String charge = "$" + String.valueOf(price);
        if (charge.length() - charge.indexOf(".") - 1 < 2)
            charge += "0";

        return charge;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(context, ConfirmationMessage.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Allow app to read the EcoCash message", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
