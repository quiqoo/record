package com.abc.callvoicerecorder.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Surface;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.activity.MainActivity;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.helper.ContactsHelper;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.helper.NotificationsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.abc.callvoicerecorder.constant.Constants.ACTION_AFTER_CALL_NOTHING;
import static com.abc.callvoicerecorder.constant.Constants.ACTION_AFTER_CALL_NOTIFICATION;
import static com.abc.callvoicerecorder.constant.Constants.ACTION_AFTER_CALL_OPEN;
import static com.abc.callvoicerecorder.constant.Constants.CALL_TYPE_INCOMING;
import static com.abc.callvoicerecorder.constant.Constants.CALL_TYPE_OUTGOING;
import static com.abc.callvoicerecorder.constant.Constants.PUSH_ID;
import static com.abc.callvoicerecorder.constant.Constants.SECOND_NOTIFICATION_ID;


public class MediaRecorderService extends Service {
    private MediaRecorder recorder;
    NotificationManagerCompat notificationManager;
    private boolean incoming_flag;
    private String number;
    private int output_format;
    private int audio_source;
    private int audio_encoder;
    private static String suffix = "";
    private static String file_name;
    private String contactName = "";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            incoming_flag = intent.getBooleanExtra("incoming_flag", false);
            startRecording();
        }
        return START_STICKY;
    }




    private void startRecording() {


     //   SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // default value is 0 for call recording so as to record call by default




      //  file_name = SharedPreferencesFile.getFileName();
        String dir_path = SharedPreferencesFile.getDirPath();
        String dir_name = SharedPreferencesFile.getDirName();
        String dir_name_abs = SharedPreferencesFile.getDirPathAbs();
        boolean show_seed = SharedPreferencesFile.isShowSeed();
        boolean show_phone_number = SharedPreferencesFile.isShowNumber();
        output_format = SharedPreferencesFile.getOutputFormat();
        audio_source = SharedPreferencesFile.getAudioSource();
        audio_encoder = SharedPreferencesFile.getAudioEncoder();

        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        recorder.setOutputFile(getFilename());
        recorder.setOrientationHint(Surface.ROTATION_0);
        try {

            recorder.prepare();
            recorder.start();
            notificationBuilder();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notificationBuilder() {
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Recording")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOngoing(true);
            notificationManager = NotificationManagerCompat.from(this);

            notificationManager.notify(1, builder.build());
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {

            //    if (incoming_flag) {
                 //   state = "incoming";
              //  }
                sendNotification(getApplicationContext());


                recorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            recorder.reset();
            recorder.release();
            recorder = null;
        }



        if (Build.VERSION.SDK_INT >= 26) {
            stopForeground(true);
        } else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
        }
    }


    private void sendNotification(Context context) {
        try {
            int callId = -1;
            List<CallRecord> callRecordList = FactoryHelper.getHelper().getCallRecordDAO().getAllItems();
            if (callRecordList != null && callRecordList.size() != 0 && callRecordList.get(callRecordList.size() - 1) != null)
                callId = callRecordList.get(callRecordList.size() - 1).getId();

            switch (SharedPreferencesFile.getActionAfterCall()) {
                case ACTION_AFTER_CALL_NOTIFICATION:
                    String notificationText = context.getResources().getString(R.string.call_recorded) + " " + number;
                    NotificationManagerCompat.from(context).notify(SECOND_NOTIFICATION_ID, NotificationsHelper.getNotificationSecondary(context, context.getResources().getString(R.string.app_name), notificationText, callId, MainActivity.class, true));
                    break;
                case ACTION_AFTER_CALL_OPEN:
                    Intent appIntent = new Intent(context, MainActivity.class);
                    appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    appIntent.putExtra(PUSH_ID, callId);
                    context.startActivity(appIntent);
                    break;
                case ACTION_AFTER_CALL_NOTHING:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }

    private String getFilename() {
        boolean show_seed = SharedPreferencesFile.isShowSeed();
        boolean show_phone_number = SharedPreferencesFile.isShowNumber();

      //  formated_number = StringUtils.prepareContacts(getApplicationContext(), number);
        String state = "outgoing";
       // String ext = ".3gp";
       String filepath = Environment.getExternalStorageDirectory().getPath();
       filepath += "/CallRecord/";

        File file = new File(filepath);

        if (!file.exists()) {
            file.mkdirs();
            createNomedia(file.getAbsolutePath());
        }
        if (incoming_flag) {
            state = "incoming";
        }
        if (number == null){
            number = "Unknown";
        }

        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(new SimpleDateFormat("ddMMyyyyHHmmss", Locale.US).format(new Date()));
        fileNameBuilder.append("_");



        if (show_seed) {
            fileNameBuilder.append(state);
            fileNameBuilder.append("_");
        }

            fileNameBuilder.append(number);
            if (number != null) {
                Log.d("WTF_RECEIVER", number);
            } else
                Log.d("WTF_RECEIVER", "Error");


        file_name = fileNameBuilder.toString();
        suffix = "";
        switch (output_format) {
            case MediaRecorder.OutputFormat.AMR_NB: {
                suffix = ".amr";
                break;
            }
            case MediaRecorder.OutputFormat.AMR_WB: {
                suffix = ".amr";
                break;
            }
            case MediaRecorder.OutputFormat.MPEG_4: {
                suffix = ".mp4";
                break;
            }
            case MediaRecorder.OutputFormat.THREE_GPP: {
                suffix = ".3gp";
                break;
            }
            default: {
                suffix = ".amr";
                break;
            }
        }


        contactName = ContactsHelper.getContactName(number, getApplicationContext());
        if (contactName.equals(""))
            contactName = number;

        int dbSeed = 0;

        if (incoming_flag) {
            dbSeed = CALL_TYPE_INCOMING;
        } else {
            dbSeed = CALL_TYPE_OUTGOING;
        }

        //   if (contactSeed.equalsIgnoreCase("outgoing"))
        //   dbSeed = CALL_TYPE_OUTGOING;
        // else
        //   dbSeed = CALL_TYPE_INCOMING;

       // String filepath = Environment.getExternalStorageDirectory().getPath();
       // filepath += "/CallRecord/";
       // File file = new File(filepath);

        FactoryHelper.getHelper().addCallRecordItemDB(contactName, number, dbSeed, file.getAbsolutePath() + "/" + file_name +  suffix);

        if (!SharedPreferencesFile.isEncoderTypeCheck()) {
            SharedPreferencesFile.setIsEncoderTypeCheck(true);
        }


        return (file.getAbsolutePath() + "/" + file_name +  suffix);
    }


    private void createNomedia(String absolutePath) {
        File file = new File(absolutePath + "/" + ".nomedia");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}