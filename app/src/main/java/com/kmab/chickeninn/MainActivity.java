package com.kmab.chickeninn;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements FragmentChicken.HandleOrders {

    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;

    TextView tvCharge, tvOutlet, tvOrders;
    ImageView ivLeft, ivRight;

    static List listOrders;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (FirebaseAuth.getInstance() == null)
            FirebaseAuth.getInstance().signInAnonymously();
        context = MainActivity.this;
        listOrders = new ArrayList();
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
        sectionsPagerAdapter.addFragments(new FragmentChicken(), "Chicken Inn");
        sectionsPagerAdapter.addFragments(new FragmentPizza(), "Pizza Inn");
        sectionsPagerAdapter.addFragments(new FragmentCreamy(), "Creamy Inn");
        sectionsPagerAdapter.addFragments(new FragmentBakers(), "Bakers Inn");
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(0, true);

        tvOrders = findViewById(R.id.tvOrders);
        tvCharge = findViewById(R.id.tvCharge);
        tvOutlet = findViewById(R.id.tvOutlet);
        ivLeft = findViewById(R.id.ivLeft);
        ivRight = findViewById(R.id.ivRight);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvOutlet.setText(sectionsPagerAdapter.getPageTitle(position));
                if (position == 0) {
                    ivLeft.setVisibility(View.GONE);
                    ivRight.setVisibility(View.VISIBLE);
                } else if (position == sectionsPagerAdapter.getCount() - 1) {
                    ivRight.setVisibility(View.GONE);
                    ivLeft.setVisibility(View.VISIBLE);
                } else {
                    ivLeft.setVisibility(View.VISIBLE);
                    ivRight.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public String checkCount(SetterMeals setterMeals) {
        int count = 0;
        for (int x = 0; x < listOrders.size(); x++) {
            SetterMeals setter = (SetterMeals) listOrders.get(x);
            if (setter.getType().equals(setterMeals.getType()) && setter.getId() == setterMeals.getId())
                count++;
        }

        if (count == 0)
            return "";
        else
            return "x" + count;
    }

    @Override
    public void addOrder(SetterMeals setterMeals) {
        listOrders.add(setterMeals);
        calculateCharge();
    }

    @Override
    public boolean removeOrder(SetterMeals setterMeals) {
        for (int x = 0; x < listOrders.size(); x++) {
            SetterMeals setter = (SetterMeals) listOrders.get(x);
            if (setter.getType().equals(setterMeals.getType()) && setter.getId() == setterMeals.getId()) {
                listOrders.remove(x);
                break;
            }
        }
        calculateCharge();

        return isOrderContained(setterMeals);
    }

    private boolean isOrderContained(SetterMeals setterMeals) {
        for (int x = 0; x < listOrders.size(); x++) {
            SetterMeals setter = (SetterMeals) listOrders.get(x);
            if (setter.getType().equals(setterMeals.getType()) && setter.getId() == setterMeals.getId())
                return true;
        }

        return false;
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
        String items;
        if (listOrders.size() == 1)
            items = "Order " + listOrders.size() + " item";
        else
            items = "Order " + listOrders.size() + " items";
        tvOrders.setText(items);
    }

    public void viewOrders(View view) {
        startActivity(new Intent(context, Orders.class));
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

            ArrayList<Fragment> fragments = new ArrayList<>();
            ArrayList<String> tabTitles = new ArrayList<>();

            public void addFragments(Fragment fragments, String titles){
                this.fragments.add(fragments);
                this.tabTitles.add(titles);
            }

            public SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {

                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles.get(position);
            }
        }

}
