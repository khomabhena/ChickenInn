package com.kmab.chickeninn;

import android.content.Context;

import java.util.Calendar;
import java.util.Random;

public class XOrderNumber {

    XOrderNumber() {
    }

    public String getOrderNumber() {
        String[] alpha = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        Random random = new Random();
        String a = alpha[checkNum(random.nextInt(26))];
        String b = alpha[checkNum(random.nextInt(26))];
        int c = random.nextInt(9);
        int d = random.nextInt(9);
        int e = random.nextInt(9);
        int f = random.nextInt(9);
        String g = alpha[checkNum(random.nextInt(26))];
        String h = alpha[checkNum(random.nextInt(26))];

        return a + b + g + h + "-" + getDate(false) + "-" + getDate(true);
    }

    private int checkNum(int a) {
        if (a == 0 || a == 27) {
            if (a == 0) {
                a += 1;
            }
            if (a == 27) {
                a -= 1;
            }
        }

        return a;
    }

    String getDate(boolean b) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.HOUR_OF_DAY, 0);
        //calendar.set(Calendar.MINUTE, 0);

        String day = getTheValue(calendar.get(Calendar.DAY_OF_MONTH));
        String month = getTheValue(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
        String hour = getTheValue(calendar.get(Calendar.HOUR_OF_DAY));
        String min = getTheValue(calendar.get(Calendar.MINUTE));

        if (b)
            return year + month + day;
        else
            return hour + min;
    }

    private String getTheValue(int value){
        String theValue = String.valueOf(value);
        if (theValue.length() == 1){
            return "0"+theValue;
        } else {
            return theValue;
        }
    }

}
