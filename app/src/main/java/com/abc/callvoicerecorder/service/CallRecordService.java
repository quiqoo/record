package com.abc.callvoicerecorder.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

import com.abc.callvoicerecorder.utils.CallRecord;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;


public class CallRecordService extends Service {

    private static final String TAG = CallRecordService.class.getSimpleName();

    protected CallRecord mCallRecord;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand()");

        String file_name = SharedPreferencesFile.getFileName();
        String dir_path = SharedPreferencesFile.getDirPath();
        String dir_name = SharedPreferencesFile.getDirName();
        boolean show_seed = SharedPreferencesFile.isShowSeed();
        boolean show_phone_number = SharedPreferencesFile.isShowNumber();
        int output_format = SharedPreferencesFile.getOutputFormat();
        int audio_source = SharedPreferencesFile.getAudioSource();
        int audio_encoder = SharedPreferencesFile.getAudioEncoder();

        mCallRecord = new CallRecord.Builder(this)
                .setRecordFileName(file_name)
                .setRecordDirName(dir_name)
                .setRecordDirPath(dir_path)
                .setAudioEncoder(audio_encoder)
                .setAudioSource(audio_source)
                .setOutputFormat(output_format)
                .setShowSeed(show_seed)
                .setShowPhoneNumber(show_phone_number)
                .build();

        Log.i(TAG, "mCallRecord.startCallReceiver()");
        mCallRecord.startCallReceiver();

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCallRecord.stopCallReceiver();

        Log.i(TAG, "onDestroy()");
    }
}
