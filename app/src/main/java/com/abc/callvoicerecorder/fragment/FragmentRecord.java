package com.abc.callvoicerecorder.fragment;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.helper.FragmentsHelper;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

public class FragmentRecord extends FragmentBase implements ConstantsColor {
    private View rootView;
    private MediaRecorder recorder;
    private File audioFile;
    private boolean isRecordStarted = false;
    private String fileName;
    private TextView timerTV;
    private int timerSeconds = -1;
    private int timerMinutes = 0;
    private int timerHours = 0;
    private boolean isEnd = false;
    private boolean isStopBtn = false;
    private String DICTAPHONE_DIR = "/DictaphoneRecords";
    private LinearLayout stopLayout;
    private LinearLayout deleteLayout;
    private int toolbarHeight = 0;
    private boolean isDelete = false;

    public static Fragment newInstance() {
        return new FragmentRecord();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_record, container, false);
        setHasOptionsMenu(true);
        listenerActivity.visibleBackButton();
        initView();
        return rootView;
    }

    private void initView() {
        if (listenerActivity != null) {
            listenerActivity.setTitle(getString(R.string.dictaphone_label));
            if (listenerActivity.getSupportActionBar() != null) {
                toolbarHeight = listenerActivity.getSupportActionBar().getHeight();
            }
        }

        timerTV = (TextView) rootView.findViewById(R.id.timer_tv);
        stopLayout = (LinearLayout) rootView.findViewById(R.id.fab_menu);
        deleteLayout = (LinearLayout) rootView.findViewById(R.id.fab_delete);
        stopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRecordStarted) {
                    startRecord();
                } else {
                    isStopBtn = true;
                    stopRecord();
                }
            }
        });
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecordStarted) {
                    isStopBtn = true;
                    isDelete = true;
                    stopRecord();
                }
            }
        });
        ((ImageView)rootView.findViewById(R.id.icon_menu)).setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.fab_stop_ic, COLOR_ICONS_DICTAPHONE[SharedPreferencesFile.getThemeNumber()]));
        ((ImageView)rootView.findViewById(R.id.icon_delete)).setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.fab_icon_delete, COLOR_ICONS_DICTAPHONE[SharedPreferencesFile.getThemeNumber()]));
        stopLayout.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                stopLayout.getLocationInWindow(location);
                int x = location[0] + stopLayout.getWidth() / 2;
                int y = location[1]  - toolbarHeight;
                listenerActivity.setBtnPosX(x);
                listenerActivity.setBtnPosY(y);
            }
        });

        (rootView.findViewById(R.id.root)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setStyle();
        startRecord();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void startRecord() {
        isEnd = false;
        isStopBtn = false;
        try {
            String path = Environment.getExternalStorageDirectory().getPath()+"/CallRecord" + DICTAPHONE_DIR;
            File sampleDir = new File(path);
            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }
            fileName = "Record_" + new SimpleDateFormat("ddMMyyyyHHmmss", Locale.US).format(new Date());
            String suffix = ".amr";
            audioFile = File.createTempFile(fileName, suffix, sampleDir);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int audioSource = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
        int outputFormat = MediaRecorder.AudioEncoder.AMR_WB;
        int audioEncoder = MediaRecorder.AudioEncoder.AMR_WB;

        recorder = new MediaRecorder();
        recorder.setAudioSource(audioSource);
        recorder.setOutputFormat(outputFormat);
        recorder.setAudioEncoder(audioEncoder);
        recorder.setOutputFile(audioFile.getAbsolutePath());

        try {
            recorder.prepare();
            recorder.start();
            startTimerListener();
            isRecordStarted = true;
            Toast.makeText(listenerActivity, getString(R.string.dictaphone_toast_start_record),
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(listenerActivity, getString(R.string.dictaphone_toast_stop_error),
                    Toast.LENGTH_SHORT).show();
        }


    }

    private void stopRecord() {
        try {
            isEnd = true;
            if (recorder != null && isRecordStarted) {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;

                isRecordStarted = false;

                String recordDbName = "Record_0";
                FactoryHelper.getHelper().getDictaphoneRecordDAO().getAllItems();
                if (FactoryHelper.getHelper().getDictaphoneRecordDAO().getAllItems().size() != 0) {
                    recordDbName = "Record_" + String.valueOf(FactoryHelper.getHelper().getDictaphoneRecordDAO().getLastItem().get(0).getId());
                }
                FactoryHelper.getHelper().addDictaphoneRecordItemDB(recordDbName, audioFile.getAbsolutePath());

                if (isDelete) {
                    FactoryHelper.getHelper().getDictaphoneRecordDAO().deleteItemAndFile(FactoryHelper.getHelper().getDictaphoneRecordDAO().getLastItem().get(0).getId());
                }

                setZeroTime();

                Toast.makeText(listenerActivity, getString(R.string.dictaphone_toast_stop_record),
                        Toast.LENGTH_SHORT).show();
                if (isStopBtn)
                    listenerActivity.onBackPressed();

                FragmentsHelper.updateAllLists(listenerActivity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(listenerActivity, getString(R.string.dictaphone_toast_stop_error),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecord();
    }

    private void startTimerListener() {
        final Handler mHandler = new Handler();
        listenerActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (timerTV != null && !isEnd) {
                    if (timerSeconds < 59) {
                        timerSeconds++;
                    } else if (timerMinutes < 59) {
                        timerMinutes++;
                        timerSeconds = 0;
                    } else {
                        timerHours++;
                        timerSeconds = 0;
                        timerMinutes = 0;
                    }

                    String hours = "";
                    String minutes = "";
                    String seconds = "";

                    if (timerMinutes < 10) {
                        minutes = "0" + String.valueOf(timerMinutes);
                    } else {
                        minutes = String.valueOf(timerMinutes);
                    }

                    if (timerSeconds < 10) {
                        seconds = "0" + String.valueOf(timerSeconds);
                    } else {
                        seconds = String.valueOf(timerSeconds);
                    }
                    hours = String.valueOf(timerHours);

                    String timerText = hours + ":" + minutes + ":" + seconds;
                    timerTV.setText(timerText);

                    mHandler.postDelayed(this, 1000);
                }
            }
        });
    }

    private void setZeroTime() {
        timerTV.setText("0:00:00");
        timerSeconds = -1;
        timerMinutes = 0;
        timerHours = 0;
    }

    private void setStyle() {
    }
}
