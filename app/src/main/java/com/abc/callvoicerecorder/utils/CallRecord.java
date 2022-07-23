package com.abc.callvoicerecorder.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.abc.callvoicerecorder.receiver.CallRecordReceiver;
import com.abc.callvoicerecorder.service.CallRecordService;


public class CallRecord {

    private static final String TAG = CallRecord.class.getSimpleName();


    private Context mContext;
    private CallRecordReceiver mCallRecordReceiver;

    private CallRecord(Context context) {
        this.mContext = context;
        SharedPreferencesFile.initSharedReferences(context);
    }

    public static CallRecord initReceiver(Context context) {
        CallRecord callRecord = new Builder(context).build();
        callRecord.startCallReceiver();
        return callRecord;
    }

    public static CallRecord initService(Context context) {
        CallRecord callRecord = new Builder(context).build();
        callRecord.startCallRecordService();
        return callRecord;
    }

    public void startCallReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CallRecordReceiver.ACTION_IN);
        intentFilter.addAction(CallRecordReceiver.ACTION_OUT);

        if (mCallRecordReceiver == null) {
            mCallRecordReceiver = new CallRecordReceiver(this);
        }
        mContext.registerReceiver(mCallRecordReceiver, intentFilter);
    }

    public void stopCallReceiver() {
        try {
            mContext.unregisterReceiver(mCallRecordReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startCallRecordService() {
        Intent intent = new Intent();
        intent.setClass(mContext, CallRecordService.class);

      //  mContext.startService(intent);
        Log.i(TAG, "startService()");
    }

    public void enableSaveFile() {
        SharedPreferencesFile.setIsFileSave(true);
        Log.i("CallRecord", "Save file enabled");
    }


    public static class Builder {
        private Context mContext;
        public Builder(Context context) {
            this.mContext = context;

            SharedPreferencesFile.setFileName("Record");
            SharedPreferencesFile.setDirName("CallRecord");
            SharedPreferencesFile.setDirPath(Environment.getExternalStorageDirectory().getPath());
            SharedPreferencesFile.setDirPathAbs(SharedPreferencesFile.getDirPathAbs());
            SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            SharedPreferencesFile.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            SharedPreferencesFile.setOutputFormat(SharedPreferencesFile.getOutputFormat());
            SharedPreferencesFile.setIsShowSeed(true);
            SharedPreferencesFile.setIsShowNumber(true);
        }
        public CallRecord build() {
            CallRecord callRecord = new CallRecord(mContext);
            callRecord.enableSaveFile();
            return callRecord;
        }

        public Builder setRecordFileName(String recordFileName) {
            SharedPreferencesFile.setFileName(recordFileName);
            return this;
        }


        public Builder setRecordDirName(String recordDirName) {
            SharedPreferencesFile.setDirName(recordDirName);
            return this;
        }


        public Builder setAudioSource(int audioSource) {
            SharedPreferencesFile.setAudioSource(audioSource);
            return this;
        }

        public Builder setAudioEncoder(int audioEncoder) {
            SharedPreferencesFile.setAudioEncoder(audioEncoder);
            return this;
        }

        public Builder setOutputFormat(int outputFormat) {
            SharedPreferencesFile.setOutputFormat(outputFormat);
            return this;
        }

        public Builder setShowSeed(boolean showSeed) {
            SharedPreferencesFile.setIsShowSeed(showSeed);
            return this;
        }


        public Builder setShowPhoneNumber(boolean showNumber) {
            SharedPreferencesFile.setIsShowNumber(showNumber);
            return this;
        }

        public Builder setRecordDirPath(String recordDirPath) {
            SharedPreferencesFile.setDirPath(recordDirPath);
            return this;
        }

        public Builder setRecordDirPathAbs(String recordDirPath) {
            SharedPreferencesFile.setDirPathAbs(recordDirPath);
            return this;
        }
    }
}
