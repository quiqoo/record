package com.abc.callvoicerecorder;

import android.content.Context;
import android.content.Intent;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.service.BootCallRecordService;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;
import com.onesignal.OneSignal;


public class Base extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .autoPromptLocation(true)

                .init();

        try {
            Class.forName("android.os.AsyncTask");
        }
        catch(Throwable ignore) {
        }
        SharedPreferencesFile.initSharedReferences(this);
        FactoryHelper.setHelper(getApplicationContext());
        //startService(new Intent(getApplicationContext(), BootCallRecordService.class));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
