package com.abc.callvoicerecorder.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.activity.MainActivity;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.db.IgnoreContact;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.helper.ContactsHelper;
import com.abc.callvoicerecorder.helper.NotificationsHelper;
import com.abc.callvoicerecorder.utils.PermissionHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;


public class CallRecordReceiver extends PhoneCallReceiver implements Constants {

    public static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    public static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    public static final String EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";

    protected com.abc.callvoicerecorder.utils.CallRecord callRecord;
    private static MediaRecorder recorder;
    private File audiofile;
    private boolean isRecordStarted = false;
    private static String file_name;
    private int output_format;
    private int audio_source;
    private int audio_encoder;
    private  static File sampleDir;
    private static String suffix = "";
    private String contactName = "";
    private String contactSeed = "";
    private boolean isCanRecord = false;

    private boolean running;
    private LinearLayout testLayout;
    private ImageView touchLayout;
    private ImageView recImg;
    private boolean isRecWidgetRec = false;
    private WindowManager wm;
    private int originalXPos;
    private int originalYPos;
    private boolean isRecordDelayStarted = false;

    public CallRecordReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    public CallRecordReceiver(com.abc.callvoicerecorder.utils.CallRecord callRecord) {
        this.callRecord = callRecord;
    }

    @Override
    protected void onIncomingCallReceived(Context context, String number, Date start) {

    }

    @Override
    protected void onIncomingCallAnswered(Context context, String number, Date start) {
        startRecord(context, "incoming", number);
    }

    @Override
    protected void onIncomingCallEnded(Context context, String number, Date start, Date end) {
        isRecordDelayStarted = true;
        stopWidgetRecord();
        setImgStart();
        stopRecord(context);
    }

    @Override
    protected void onOutgoingCallStarted(Context context, String number, Date start) {
        startRecord(context, "outgoing", number);
    }

    @Override
    protected void onOutgoingCallEnded(Context context, String number, Date start, Date end) {
        isRecordDelayStarted = true;
        stopWidgetRecord();
        setImgStart();
        stopRecord(context);
    }

    @Override
    protected void onMissedCall(Context context, String number, Date start) {

    }

    protected void onRecordingStarted(Context context, com.abc.callvoicerecorder.utils.CallRecord callRecord, File audioFile) {
    }

    protected void onRecordingFinished(Context context, com.abc.callvoicerecorder.utils.CallRecord callRecord, File audioFile) {
    }


    private void setMaxValue(Context context) {
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, maxVolume, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startRecord(final Context context, final String seed, String phoneNumber) {

        try {
            isRecordDelayStarted = false;
            FactoryHelper.setHelper(context.getApplicationContext());

            startRecWidget(context, seed);
            setImgStart();

            SharedPreferencesFile.initSharedReferences(context);
            boolean isSaveFile = SharedPreferencesFile.isFileSave();

            if (SharedPreferencesFile.isSettingsMaxCallVolume())
                setMaxValue(context);

            if (!isSaveFile) {
                return;
            }

            contactSeed = seed;
            setCallInfo(context, seed);

            List<IgnoreContact> ignoreList = FactoryHelper.getHelper().getIgnoreContactDAO().getAllItems();
            for (IgnoreContact ignoreItem : ignoreList) {
                if (ignoreItem.getCallNumber().equals(savedNumber)) {
                    return;
                }
            }

            switch (SharedPreferencesFile.getCallTypeRecord()) {
                case 0:
                    isCanRecord = true;
                    break;
                case 1:
                    if (contactSeed.equalsIgnoreCase("incoming"))
                        isCanRecord = true;
                    break;
                case 2:
                    if (contactSeed.equalsIgnoreCase("outgoing"))
                        isCanRecord = true;
                    break;
                case 3:
                    if (ContactsHelper.getContactName(savedNumber, context).equals(""))
                        isCanRecord = true;
                    break;
            }

            if (isCanRecord && SharedPreferencesFile.isMainNotificationActive()) {
                Handler changeFragmentHandler = new Handler();
                Runnable changeFragmentRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (!isRecordDelayStarted) {
                            isRecordDelayStarted = true;
                            startMainRecord(context, seed);
                        }
                    }
                };
                int timeDelay = SharedPreferencesFile.getPostCallTime();
                if (timeDelay == 0)
                    timeDelay = 100;
                changeFragmentHandler.postDelayed(changeFragmentRunnable, timeDelay);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecord(Context context) {
        try {
            if (recorder != null && isRecordStarted) {

                isRecordDelayStarted = false;
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;

                isRecordStarted = false;
                onRecordingFinished(context, callRecord, audiofile);

                if (contactName.equals(""))
                    contactName = savedNumber;
                int dbSeed = 0;
                if (contactSeed.equalsIgnoreCase("outgoing"))
                    dbSeed = CALL_TYPE_OUTGOING;
                else
                    dbSeed = CALL_TYPE_INCOMING;

                FactoryHelper.getHelper().addCallRecordItemDB(contactName, savedNumber, dbSeed, audiofile.getAbsolutePath());

                if (!SharedPreferencesFile.isEncoderTypeCheck()) {
                    SharedPreferencesFile.setIsEncoderTypeCheck(true);
                }

                sendNotification(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                isRecWidgetRec = false;
                stopWidgetRecord();
                isRecordStarted = false;
                onRecordingFinished(context, callRecord, audiofile);

                if (contactName.equals(""))
                    contactName = savedNumber;
                int dbSeed = 0;
                if (contactSeed.equalsIgnoreCase("outgoing"))
                    dbSeed = CALL_TYPE_OUTGOING;
                else
                    dbSeed = CALL_TYPE_INCOMING;
                FactoryHelper.getHelper().addCallRecordItemDB(contactName, savedNumber, dbSeed, audiofile.getAbsolutePath());

                if (!SharedPreferencesFile.isEncoderTypeCheck()) {
                    SharedPreferencesFile.setIsEncoderTypeCheck(true);
                }
                sendNotification(context);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    private void startMainRecord(Context context, String seed) {
        try {
            isRecWidgetRec = true;
            setImgStop();
            setRecorderInfo();
            recorder.prepare();
            recorder.start();

            isRecordStarted = true;
            onRecordingStarted(context, callRecord, audiofile);
        } catch (Exception e) {
            e.printStackTrace();

            if (!SharedPreferencesFile.isAudioSourceAuto()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    int iconSmallId = 0;
                    if (SharedPreferencesFile.isMainNotificationActive())
                        iconSmallId = R.mipmap.ic_status_bar;
                    else
                        iconSmallId = R.mipmap.ic_status_bar_disable;

                    String NOTIFICATION_CHANNEL_ID = context.getPackageName();
                    String channelName = context.getString(R.string.app_name);
                    NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    assert manager != null;
                    manager.createNotificationChannel(chan);
                    Intent appIntent = new Intent(context, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, MAIN_ACTIVITY_ID, appIntent, 0);

                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
                    Notification notification = notificationBuilder

                            .setContentTitle(context.getString(R.string.app_name))
                            .setSmallIcon(iconSmallId)
                            .setContentText(context.getString(R.string.audio_source_error))
                            .setPriority(NotificationManager.IMPORTANCE_MIN)
                            .setCategory(Notification.CATEGORY_SERVICE)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                            .setAutoCancel(true)
                            .setOngoing(true)
                            .setContentIntent(pendingIntent)
                            .build();
                    NotificationManagerCompat.from(context).notify(SECOND_NOTIFICATION_ID, notification);
                    isRecordStarted = false;

                } else {
                    NotificationManagerCompat.from(context).notify(SECOND_NOTIFICATION_ID, NotificationsHelper.getNotificationError(context, context.getResources().getString(R.string.app_name), context.getString(R.string.audio_source_error), MainActivity.class));
                    isRecordStarted = false;
                }




            } else {
               if (SharedPreferencesFile.getAudioSource() == MediaRecorder.AudioSource.VOICE_CALL) {
                    SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                    setCallInfo(context, seed);
                    setRecorderInfo();
                    try {
                        SharedPreferencesFile.setIsEncoderTypeCrash(true);
                        recorder.prepare();
                        recorder.start();
                        isRecordStarted = true;
                        onRecordingStarted(context, callRecord, audiofile);
                      } catch (Exception e1) {
                        e1.printStackTrace();
                       if (SharedPreferencesFile.getAudioSource() == MediaRecorder.AudioSource.DEFAULT) {
                            SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                            setCallInfo(context, seed);
                            setRecorderInfo();
                            try {
                                SharedPreferencesFile.setIsEncoderTypeCrash(true);
                                recorder.prepare();
                                recorder.start();
                                isRecordStarted = true;
                                onRecordingStarted(context, callRecord, audiofile);

                            } catch (Exception e2) {
                                e2.printStackTrace();
                                }
                        }
                    }
                } else if (SharedPreferencesFile.getAudioSource() == MediaRecorder.AudioSource.DEFAULT) {
                    SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                    setCallInfo(context, seed);
                    setRecorderInfo();
                    try {
                        SharedPreferencesFile.setIsEncoderTypeCrash(true);
                        recorder.prepare();
                        recorder.start();
                        isRecordStarted = true;
                        onRecordingStarted(context, callRecord, audiofile);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        }
                }
            }
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
                    String notificationText = context.getResources().getString(R.string.call_recorded) + " " + savedNumber;
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

    private void setRecorderInfo() {
        try {
            audiofile = File.createTempFile(file_name, suffix, sampleDir);
            recorder = new MediaRecorder();
            recorder.setAudioSource(audio_source);
            recorder.setOutputFormat(output_format);
            recorder.setAudioEncoder(audio_encoder);
            recorder.setOutputFile(audiofile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCallInfo(Context context, String seed) {
        file_name = SharedPreferencesFile.getFileName();
        String dir_path = SharedPreferencesFile.getDirPath();
        String dir_name = SharedPreferencesFile.getDirName();
        String dir_name_abs = SharedPreferencesFile.getDirPathAbs();
        boolean show_seed = SharedPreferencesFile.isShowSeed();
        boolean show_phone_number = SharedPreferencesFile.isShowNumber();
        output_format = SharedPreferencesFile.getOutputFormat();
        audio_source = SharedPreferencesFile.getAudioSource();
        audio_encoder = SharedPreferencesFile.getAudioEncoder();

        if (dir_name_abs.equals(""))
            dir_name_abs = dir_path + "/" + dir_name;
        sampleDir = new File(dir_name_abs);


        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }

        StringBuilder fileNameBuilder = new StringBuilder();
        contactName = ContactsHelper.getContactName(savedNumber, context);
        fileNameBuilder.append(new SimpleDateFormat("ddMMyyyyHHmmss", Locale.US).format(new Date()));
        fileNameBuilder.append("_");

        if (show_seed) {
            fileNameBuilder.append(seed);
            fileNameBuilder.append("_");
        }

        if (show_phone_number) {
            fileNameBuilder.append(savedNumber);
            if (savedNumber != null) {
                Log.d("WTF_RECEIVER", savedNumber);
            } else
                Log.d("WTF_RECEIVER", "Error");

        }


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
    }


    public boolean startWidgetRecord(Context context, String seed) {
        Log.d("QQQ", "Init Widget");
        if (running) {
            return false;
        }
        initOverlayView(context, seed);
        running = true;
        return true;
    }

    public boolean stopWidgetRecord() {
        Log.d("QQQ", "Stop Widget");
        if (!running) {
            return false;
        }
        removeOverlayView();
        running = false;

        return true;
    }

    private void recWidget(Context context, String seed) {
        Log.d("QQQ", "widget Rec Button Click");
        isRecWidgetRec = !isRecWidgetRec;
        if (isRecWidgetRec) {
            setImgStop();
            startMainRecord(context, seed);
        } else {
            setImgStart();
            stopRecord(context);
        }
    }

    private void setImgStart() {
        if (recImg != null)
            recImg.setImageResource(R.drawable.widget_start);
    }

    private void setImgStop() {
        if (recImg != null)
            recImg.setImageResource(R.drawable.widget_stop);
    }

    private void initOverlayView(final Context context, final String seed) {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater inflate = LayoutInflater.from(context);
        testLayout = (LinearLayout) inflate.inflate(R.layout.record_widget_layout, null);
        recImg = (ImageView) testLayout.findViewById(R.id.rec_ic);
        touchLayout = (ImageView) testLayout.findViewById(R.id.touch_layout);
        testLayout.setVisibility(View.GONE);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        if (SharedPreferencesFile.getButtonGravity() == 0)
            params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        else params.gravity = SharedPreferencesFile.getButtonGravity();
        params.x = (int) SharedPreferencesFile.getLastButtonPositionX();
        params.y = (int) SharedPreferencesFile.getLastButtonPositionY();

        recImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recWidget(context, seed);
            }
        });

        touchLayout.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        originalXPos = params.x;
                        originalYPos = params.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:

                        SharedPreferencesFile.setLastButtonPositionX(params.x);
                        SharedPreferencesFile.setLastButtonPositionY(params.y);
                        SharedPreferencesFile.setButtonGravity(params.gravity);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        try {
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            if (wm != null)
                                wm.updateViewLayout(testLayout, params);
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return true;
                        }
                }
                return false;
            }
        });

        wm.addView(testLayout, params);
    }

    private void showButtonStop() {
        testLayout.setVisibility(View.VISIBLE);
    }

    private void removeOverlayView() {
        if (testLayout != null) {
            wm.removeView(testLayout);
            testLayout = null;
        }
    }

    public void startRecWidget(Context context, String seed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && SharedPreferencesFile.isWidgetActive() && PermissionHelper.isSystemAlertPermissionGranted(context)) {
            startWidgetRecord(context, seed);
            showButtonStop();
        }
    }

}
