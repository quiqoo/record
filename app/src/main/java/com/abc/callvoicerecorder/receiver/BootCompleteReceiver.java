package com.abc.callvoicerecorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.abc.callvoicerecorder.service.BootCallRecordService;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         //   context.startForegroundService(new Intent(context.getApplicationContext(), BootCallRecordService.class));
       // } else {
      //      context.startService(new Intent(context.getApplicationContext(), BootCallRecordService.class));
      //  }
    }

}
