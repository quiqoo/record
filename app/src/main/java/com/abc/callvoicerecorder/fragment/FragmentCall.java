package com.abc.callvoicerecorder.fragment;

import android.content.Intent;
import android.graphics.PointF;
import android.media.MediaRecorder;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.adapter.MainCallAllAdapter;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.converse.Delete;
import com.abc.callvoicerecorder.converse.StartRecord;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.helper.FragmentsCallHelper;
import com.abc.callvoicerecorder.utils.HidingScrollListener;
import com.abc.callvoicerecorder.helper.NotificationsHelper;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

import static com.abc.callvoicerecorder.activity.MainActivity.mainFragmentAllAdapter;

public class FragmentCall extends FragmentBase implements MainCallAllAdapter.OnClickListener, Constants, ConstantsColor {
    private View rootView;
    private static RecyclerView recyclerView;
    private static RelativeLayout noRecordLayout;
    private List<CallRecord>  callRecordList = new ArrayList<>();
    public LinearLayout scrollLayout;
    private SwitchButton switchButton;
    private TextView mainButtonText;
    public static Fragment newInstance() {
        return new FragmentCall();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferencesFile.initSharedReferences(listenerActivity);
        rootView = inflater.inflate(R.layout.fragment_call, container, false);
        initView();
        setStyle();
        return rootView;
    }

    private void initView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        noRecordLayout = (RelativeLayout) rootView.findViewById(R.id.no_record_call_layout);
        recyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
            }
            @Override
            public void onShow() {
            }
        });
        scrollLayout = (LinearLayout) rootView.findViewById(R.id.scroll_layout);
        mainButtonText = (TextView) rootView.findViewById(R.id.main_button_text);
        switchButton = (SwitchButton) rootView.findViewById(R.id.sb_main);
        PointF p = new PointF(100, 100);
        float size = 24 * getResources().getDisplayMetrics().density;
        switchButton.setThumbSize(new PointF(size, size));
        switchButton.setThumbColorRes(R.color.color_switch_button);
        switchButton.setBackColorRes(SWITCH_BTN_MAIN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        switchButton.setThumbRadius(100);
        switchButton.setBackRadius(100);

        switchButton.setChecked(SharedPreferencesFile.isMainNotificationActive());
        if (SharedPreferencesFile.isMainNotificationActive()) {
            mainButtonText.setText(getString(R.string.record_enable));
            switchButton.setBackColorRes(SWITCH_BTN_MAIN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        }
        else {
            mainButtonText.setText(getString(R.string.record_disable));
            switchButton.setBackColorRes(SWITCH_BTN_MAIN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
        }
        if (!SharedPreferencesFile.isEncoderTypeCrash())
            SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    activeRecorder();
                } else {
                    deactivateRecorder();
                }

            }
        });

        scrollLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchButton.setChecked(!switchButton.isChecked());
            }
        });
        updateList();
    }

    private void updateList() {
        if (recyclerView != null) {
            try {
                String sortBy = "";
                boolean typeSort = true;

                if (SharedPreferencesFile.getSortBy() == SORT_BY_DATE) {
                    sortBy = CallRecord.NAME_FIELD_ID;
                    typeSort = false;
                }
                else if (SharedPreferencesFile.getSortBy() == SORT_BY_NAME) {
                    sortBy = CallRecord.NAME_FIELD_CALL_NAME;
                    typeSort = true;
                }
                callRecordList = FactoryHelper.getHelper().getCallRecordDAO().getListSortBy(sortBy, typeSort);
                FragmentsCallHelper.addDateFields(callRecordList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(listenerActivity);
                recyclerView.setLayoutManager(layoutManager);
                mainFragmentAllAdapter = new MainCallAllAdapter(listenerActivity, FragmentCall.this);
                recyclerView.setAdapter(mainFragmentAllAdapter);
                mainFragmentAllAdapter.setLists(callRecordList);
                mainFragmentAllAdapter.notifyDataSetChanged();
                if (callRecordList.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    (rootView.findViewById(R.id.no_record_call_layout)).setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(CallRecord callRecordItem, View view, int position) {
        switch (view.getId()) {
            case R.id.dots_layout:
                NotificationsHelper.showDotsPopupMenu(view, listenerActivity, callRecordItem, DELETE_AUDIO_STATUS_RIGHT, callRecordItem.isFavorite());
                break;
            case R.id.root_item_layout:
                try {
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    File file = new File(callRecordItem.getPath());
                    if (file.exists()) {
                        listenerActivity.showFragment(FragmentRecordPlay.newInstance(callRecordItem.getId(), RECORD_PLAY_LAST_FRAGMENT_MAIN));
                    } else {
                        Delete.showDeleteCallConfirmDialog(listenerActivity, callRecordItem, DELETE_AUDIO_STATUS_ERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void visibleRecycler() {
        recyclerView.setVisibility(View.VISIBLE);
        noRecordLayout.setVisibility(View.GONE);
    }
    public static void visibleTextView() {
        recyclerView.setVisibility(View.GONE);
        noRecordLayout.setVisibility(View.VISIBLE);
    }

    private void setStyle() {
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.main_screen_folder_ic, R.drawable.main_screen_folder_ic);
        (rootView.findViewById(R.id.scroll_layout)).setBackground(getResources().getDrawable(BUTTON_BG_MAIN[SharedPreferencesFile.getThemeNumber()]));
    }

    private void activeRecorder() {
        try {
            mainButtonText.setText(getString(R.string.record_enable));
            switchButton.setBackColorRes(SWITCH_BTN_MAIN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
            listenerActivity.myService.startCallRecordClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deactivateRecorder() {
        try {
            if (!SharedPreferencesFile.isWidgetAttentionShowed()) {
                StartRecord.showStartAttentionDialog(listenerActivity, getString(R.string.dialog_attention_label), getString(R.string.dialog_widget_body));
                SharedPreferencesFile.setIsWidgetAttentionShowed(true);
            }

            mainButtonText.setText(getString(R.string.record_disable));
            switchButton.setBackColorRes(SWITCH_BTN_MAIN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
            listenerActivity.myService.stopAutoCallRecordClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
