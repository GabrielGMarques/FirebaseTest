package com.gabrielgomarques.firebasetest.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Gabriel on 08/01/2017.
 */

public class Consts {

    public static final String REQUEST_TOKEN_ID = "06876530579-r3g5cormkpuln3nmljo7o88qaii3nid1.apps.googleusercontent.com";
    public static final SimpleDateFormat SP_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    public static String getTimeDiference(final Date date) {
        long diff = new Date().getTime() - date.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return (diffDays > 0 ? diffDays + " " + (diffDays > 1 ? "days" : "day") : diffHours > 0 ? diffHours + " " + (diffHours > 1 ? "hours" : "hour") : diffMinutes > 0 ? diffMinutes + " " + (diffMinutes > 1 ? "minutes" : "minute") : diffSeconds + " " + (diffSeconds > 1 ? "seconds" : "second")) + " ago";
    }

}
