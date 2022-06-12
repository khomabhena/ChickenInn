package com.kmab.chickeninn;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Shops extends AppCompatActivity {

    DBOperations dbOperations;
    SQLiteDatabase db;
    Context context;

    ProgressBar progressBar;
    RecyclerView recyclerView;
    static List list, listKeys;
    String databaseRoot = "shops";
    Adapter adapter;
    boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context= this;
        list = new ArrayList();
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        listKeys = dbOperations.getShopKeys(db);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar  = findViewById(R.id.progressBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        new BackTask().execute();
    }

    public void finish(View view) {
        finish();
    }

    private class BackTask extends AsyncTask<Void, SetterShops, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Cursor cursor = dbOperations.getShops(db);
            int count = cursor.getCount();
            list = new ArrayList();

            String uniqueKey, city, address, prefix;
            long merchant, whatsApp;

            while (cursor.moveToNext()) {
                uniqueKey = cursor.getString(cursor.getColumnIndex(DBContract.Shops.UNIQUE_KEY));
                city = cursor.getString(cursor.getColumnIndex(DBContract.Shops.CITY));
                address = cursor.getString(cursor.getColumnIndex(DBContract.Shops.ADDRESS));
                merchant = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBContract.Shops.MERCHANT)));
                prefix = cursor.getString(cursor.getColumnIndex(DBContract.Shops.PREFIX));
                whatsApp = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBContract.Shops.WHATSAPP)));

                SetterShops setter = new SetterShops(uniqueKey, city, address, merchant, prefix, whatsApp);

                publishProgress(setter);
            }
            cursor.close();

            return count;
        }

        @Override
        protected void onProgressUpdate(SetterShops... values) {
            list.add(values[0]);
        }

        @Override
        protected void onPostExecute(Integer count) {
            progressBar.setVisibility(View.GONE);
            if (count != 0) {
                adapter = new Adapter(list);
                recyclerView.setAdapter(adapter);
            }
            if (!isLoaded)
                firebaseLoad();
        }
    }

    private void firebaseLoad() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(databaseRoot)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            SetterShops setter = snapshot.getValue(SetterShops.class);

                            new InsertShops(context, listKeys).execute(setter);
                        }

                        isLoaded = true;
                        new BackTask().execute();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(context, "Error: " +databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List listAdapter;
        Toast toast;

        public Adapter(List listAdapter) {
            this.listAdapter = listAdapter;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_shop;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterShops) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvNumber, tvCity, tvStreet, tvMerchant;
            View viewRow;

            Holder(View itemView) {
                super(itemView);
                tvStreet = itemView.findViewById(R.id.tvStreet);
                tvCity = itemView.findViewById(R.id.tvCity);
                tvNumber = itemView.findViewById(R.id.tvNumber);
                viewRow = itemView.findViewById(R.id.viewRow);
                tvMerchant = itemView.findViewById(R.id.tvMerchant);
            }

            void bind(final SetterShops setter) {
                String num = getAdapterPosition() + 1 + ") ";
                String merchant = "Merchant code: " + setter.getMerchant();
                tvNumber.setText(num);
                tvCity.setText(setter.getCity());
                tvStreet.setText(setter.getAddress());
                tvMerchant.setText(merchant);

                viewRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, Payments.class);
                        intent.putExtra("position", getAdapterPosition());
                        startActivity(intent);
                    }
                });
            }

        }

    }

}
