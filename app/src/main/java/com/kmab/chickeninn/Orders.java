package com.kmab.chickeninn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class Orders extends AppCompatActivity {

    TextView tvCharge;
    RecyclerView recyclerView;

    Context context;
    Adapter adapter;

    List listOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        listOrders = MainActivity.listOrders;
        tvCharge = findViewById(R.id.tvCharge);
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter(listOrders);
        recyclerView.setAdapter(adapter);
        calculateCharge();
    }

    public void finish(View view) {
        finish();
    }

    public void selectShop(View view) {
        startActivity(new Intent(context, Shops.class));
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
            int layoutIdForListItem = R.layout.row_orders;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterMeals) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvName, tvPrice, tvNumber;
            ImageView ivRemove;

            Holder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                ivRemove = itemView.findViewById(R.id.ivRemove);
                tvNumber = itemView.findViewById(R.id.tvNumber);
            }

            void bind(final SetterMeals setter) {
                String num = getAdapterPosition() + 1 + ") ";
                tvNumber.setText(num);
                tvName.setText(setter.getName());
                ivRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listAdapter.remove(getAdapterPosition());
                        calculateCharge();
                    }
                });

                String charge = "$" + String.valueOf(setter.getPrice());
                if (charge.length() - charge.indexOf(".") - 1 < 2)
                    charge += "0";
                tvPrice.setText(charge);
            }

        }

    }

    private void calculateCharge() {
        double price = 0;
        for (int x = 0; x < listOrders.size(); x++) {
            SetterMeals setter = (SetterMeals) listOrders.get(x);
            price += setter.getPrice();
        }

        String charge = "$" + String.valueOf(price);
        if (charge.length() - charge.indexOf(".") - 1 < 2)
            charge += "0";

        tvCharge.setText(charge);
    }
}
