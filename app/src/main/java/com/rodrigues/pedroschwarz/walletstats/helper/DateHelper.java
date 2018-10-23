package com.rodrigues.pedroschwarz.walletstats.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateHelper {

    private static Long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(getCurrentDate());
    }

    public static String getDateKey(String dateKey) {
        String[] date = dateKey.split("/");
        String formatedDate = date[1] + date[2];
        return formatedDate;
    }
}
