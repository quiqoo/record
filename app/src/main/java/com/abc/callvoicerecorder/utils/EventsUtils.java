package com.abc.callvoicerecorder.utils;

import android.content.Context;


import java.util.HashMap;
import java.util.Map;

import com.abc.callvoicerecorder.constant.Constants;


public class EventsUtils implements Constants {
    public static String AUTO = "auto";


    public static void sendEncoderTypeEvent(Context context, int positionGroup) {
        Map<String, String> map = new HashMap<>();
        map.put(EVENT_TITLE_NAME, EVENT_FUNCTION_RECORD);
        map.put(EVENT_FUNCTION_RECORD_TYPE, EVENT_FUNCTION_RECORD_TYPE_GROUP[positionGroup]);
    }
}

