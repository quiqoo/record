package com.abc.callvoicerecorder;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.abc.callvoicerecorder.receiver.MediaRecorderService;

//import static com.kv.callrecorder.Utility.Utils.hasPermissions;
//import static com.kv.callrecorder.Utility.Utils.log;

public class PhoneStateListener extends BroadcastReceiver {
    static boolean incoming_flag;

    @Override
    public void onReceive(Context ctx, Intent intent) {

        String event = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (event.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            incoming_flag = true;
        } else if (event.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            startService(ctx, intent);
        } else if (event.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            ctx.stopService(new Intent(ctx, MediaRecorderService.class));
        }
    }

    private void startService(Context context, Intent intent) {
        intent.setClass(context, MediaRecorderService.class);
        intent.putExtra("incoming_flag", incoming_flag);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

    }
}
