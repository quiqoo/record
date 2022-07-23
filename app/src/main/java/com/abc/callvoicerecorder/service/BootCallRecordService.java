package com.abc.callvoicerecorder.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.activity.MainActivity;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.receiver.CallRecordReceiver;
import com.abc.callvoicerecorder.utils.CallRecord;
import com.abc.callvoicerecorder.helper.NotificationsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;



public class BootCallRecordService extends Service implements Constants {
    private CallRecord callRecord;
    private CallRecordReceiver mCallRecordReceiver;
    public static String PACKAGE_NAME;
    MyBinder binder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent arg0) {
        Log.d("WTF_S", "bind");
        return binder;
    }

    public class MyBinder extends Binder {
        public BootCallRecordService getService() {
            return BootCallRecordService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesFile.initSharedReferences(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (SharedPreferencesFile.isMainNotificationActive())
                startMyOwnForeground();
            else
                stopMyOwnForeground();

        } else {
            if (SharedPreferencesFile.isMainNotificationActive())
                startCallRecordClick();
            else
                stopAutoCallRecordClick();
        }


    }
    private void startMyOwnForeground()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SharedPreferencesFile.setIsMainNotificationActive(true);
            Log.d("WTF_SS", "Start_Receiver");
            callRecordSettings();
            int iconSmallId = 0;
            if (SharedPreferencesFile.isMainNotificationActive())
                iconSmallId = R.mipmap.ic_status_bar;
            else
                iconSmallId = R.mipmap.ic_status_bar_disable;

            String NOTIFICATION_CHANNEL_ID = getApplicationContext().getPackageName();
            String channelName = getResources().getString(R.string.app_name);
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            Intent appIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, MAIN_ACTIVITY_ID, appIntent, 0);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder

                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setSmallIcon(iconSmallId)
                    .setContentText(getResources().getString(R.string.notification_enable_text))
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(2, notification);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(CallRecordReceiver.ACTION_IN);
            intentFilter.addAction(CallRecordReceiver.ACTION_OUT);
            registerReceiver(mCallRecordReceiver, intentFilter);
        }
    }

    public void stopMyOwnForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SharedPreferencesFile.setIsMainNotificationActive(false);
            Log.d("WTF_SS", "Start_Receiver");
            callRecordSettings();
            int iconSmallId = 0;
            if (SharedPreferencesFile.isMainNotificationActive())
                iconSmallId = R.mipmap.ic_status_bar;
            else
                iconSmallId = R.mipmap.ic_status_bar_disable;

            String NOTIFICATION_CHANNEL_ID = getApplicationContext().getPackageName();
            String channelName = getResources().getString(R.string.app_name);
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            Intent appIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, MAIN_ACTIVITY_ID, appIntent, 0);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setSmallIcon(iconSmallId)
                    .setContentText(getResources().getString(R.string.notification_disable_text))
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(2, notification);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(CallRecordReceiver.ACTION_IN);
            intentFilter.addAction(CallRecordReceiver.ACTION_OUT);
            registerReceiver(mCallRecordReceiver, intentFilter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        } else {
            stopForeground(true);
        }

    }

    public void startCallRecordClick() {

        SharedPreferencesFile.setIsMainNotificationActive(true);
        Log.d("WTF_SS", "Start_Receiver");
        callRecordSettings();
        startForeground(MAIN_NOTIFICATION_ID, NotificationsHelper.getNotification(this, getResources().getString(R.string.app_name), getResources().getString(R.string.notification_enable_text), -1, MainActivity.class, false));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CallRecordReceiver.ACTION_IN);
        intentFilter.addAction(CallRecordReceiver.ACTION_OUT);
        registerReceiver(mCallRecordReceiver, intentFilter);


    }


    public void stopAutoCallRecordClick() {
        SharedPreferencesFile.setIsMainNotificationActive(false);
        Log.d("WTF_SS", "Start_Receiver");
        callRecordSettings();
        startForeground(MAIN_NOTIFICATION_ID, NotificationsHelper.getNotification(this, getResources().getString(R.string.app_name), getResources().getString(R.string.notification_disable_text), -1, MainActivity.class, false));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CallRecordReceiver.ACTION_IN);
        intentFilter.addAction(CallRecordReceiver.ACTION_OUT);
        registerReceiver(mCallRecordReceiver, intentFilter);
    }

    public void callRecordSettings() {
        SharedPreferencesFile.initSharedReferences(this);
        String absDirPath = SharedPreferencesFile.getDirPathAbs();
        if (absDirPath.equals("")) {
            absDirPath = Environment.getExternalStorageDirectory().getPath() + "/CallRecord";
            SharedPreferencesFile.setDirPathAbs(absDirPath);
        }
        callRecord = new CallRecord.Builder(this)
                .setRecordFileName("Record_" + new SimpleDateFormat("ddMMyyyyHHmmss", Locale.US).format(new Date()))
                .setRecordDirName("CallRecord")
                .setRecordDirPath(Environment.getExternalStorageDirectory().getPath())
                .setRecordDirPathAbs(absDirPath)
                .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
                .setOutputFormat(SharedPreferencesFile.getOutputFormat())
                .setAudioSource(SharedPreferencesFile.getAudioSource())
                .setShowPhoneNumber(true)
                .setShowSeed(true)
                .build();

        mCallRecordReceiver = new CallRecordReceiver(callRecord);
    }

}
