package com.abc.callvoicerecorder.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtils {

    public static int[] getTimeFromMillis(long millis) {
        int minutes = (int)(millis / (1000 * 60)) % 60;
        int seconds = (int)(millis / (1000)) % 60;
        return new int[]{minutes, seconds};
    }

    public static String getTimeToString(int millis) {
        Calendar calendar = Calendar.getInstance();
        int[] time = {0, 0};
        if(millis > 0) {
            time = getTimeFromMillis(millis);
        }
        calendar.set(Calendar.MINUTE, time[0]);
        calendar.set(Calendar.SECOND, time[1]);
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(calendar.getTime());
    }


}
