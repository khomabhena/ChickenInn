package com.kmab.chickeninn;

import java.util.ArrayList;
import java.util.List;

public class XMeals {
    private List list;
    private final static String CHICKEN = "chicken", PIZZA = "pizza", CREAMY = "creamy", BARKERS = "barker";

    XMeals() {
        list = new ArrayList();
    }

    public List chickenInnList() {
        addMeal(CHICKEN,"Two Piecer", R.drawable.meal_two_piecer, 3.50);
        addMeal(CHICKEN, "Rotisserie Chicken", R.drawable.meal_rotisserie_chicken, 3);

        return list;
    }

    private void addMeal(String type, String name, int imageResource, double price) {
        SetterMeals setter = new SetterMeals(type, list.size(), name, imageResource, price);
        list.add(setter);
    }

}
