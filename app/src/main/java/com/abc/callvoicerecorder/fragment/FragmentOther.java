package com.abc.callvoicerecorder.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.callvoicerecorder.converse.ActionClick;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kyleduo.switchbutton.SwitchButton;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.converse.PostCallTime;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;


public class FragmentOther extends FragmentBase implements Constants, ConstantsColor, View.OnClickListener, ActionClick.OnClickListener, PostCallTime.OnClickListener {
    private View rootView;
    private SwitchButton switchButtonAudio;
    private ImageView audioSourceImg;
    private TextView audioSourceSelectTitleTv;
    private TextView audioSourceSelectDescTv;
    private int PICK_REQUEST_CODE = 0;
    private String oldPass = "";

    public static Fragment newInstance() {
        return new FragmentOther();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferencesFile.initSharedReferences(listenerActivity);
        rootView = inflater.inflate(R.layout.fragment_other, container, false);
        setHasOptionsMenu(true);
        listenerActivity.visibleBackButton();
        initView();
        setStyle();

        ConnectivityManager conMgr  = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();

        if(info != null && info.isConnected())
        {
            // internet is there.
        }
        else
        {
            AdView mAdMobAdView = (AdView) rootView.findViewById(R.id.admob_adview);
            mAdMobAdView.setVisibility(View.GONE);
            // internet is not there.
        }
        AdView mAdMobAdView = (AdView) rootView.findViewById(R.id.admob_adview);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdMobAdView.loadAd(adRequest);
   /*  final InterstitialAd mInterstitial = new InterstitialAd(getActivity());
        mInterstitial.setAdUnitId(getString(R.string.interstitial_ad_unit));
        mInterstitial.loadAd(new AdRequest.Builder().build());

        mInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub
                super.onAdLoaded();
                if (mInterstitial.isLoaded()) {
                    mInterstitial.show();
                }
            }
        }); */

        return rootView;
    }

    private void initView() {
        if (listenerActivity != null) {
            listenerActivity.setTitle(listenerActivity.getString(R.string.settings_other));
        }


        switchButtonAudio = (SwitchButton) rootView.findViewById(R.id.sb_audio_source);
        PointF p = new PointF(100, 100);
        float size = 18 * getResources().getDisplayMetrics().density;
        switchButtonAudio.setThumbSize(new PointF(size, size));
        switchButtonAudio.setThumbColorRes(R.color.color_switch_button);
        switchButtonAudio.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        switchButtonAudio.setThumbRadius(100);
        switchButtonAudio.setBackRadius(100);

        audioSourceImg = (ImageView) rootView.findViewById(R.id.settings_audio_source_img);
        audioSourceSelectTitleTv = (TextView) rootView.findViewById(R.id.settings_audio_source_title_tv);
        audioSourceSelectDescTv = (TextView) rootView.findViewById(R.id.settings_audio_source_desc_tv);


        oldPass = SharedPreferencesFile.getAppPassword();


        if (SharedPreferencesFile.isAudioSourceAuto()) {
            switchButtonAudio.setChecked(false);
            switchButtonAudio.setBackColorRes(SWITCH_BTN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
            disableHandAudioSource();
        } else {
            switchButtonAudio.setChecked(true);
            switchButtonAudio.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
            activeHandAudioSource();;
        }


        switchButtonAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    activeHandAudioSource();
                } else {
                    disableHandAudioSource();
                }

            }
        });

        ((TextView) rootView.findViewById(R.id.settings_post_call_time_desc_tv)).setText(SharedPreferencesFile.getPostCallTime()/1000 + getString(R.string.sec));
        (rootView.findViewById(R.id.settings_sort_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_action_after_call_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_file_extension_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_audio_source_label_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_audio_source_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_post_call_time_layout)).setOnClickListener(this);

        setDescText();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_sort_layout:
                listenerActivity.showFragment(FragmentSortType.newInstance(), ANIM_FORWARD);
                break;
            case R.id.settings_action_after_call_layout:
                listenerActivity.showFragment(FragmentAfterCall.newInstance(), ANIM_FORWARD);
                break;
            case R.id.settings_file_extension_layout:
                listenerActivity.showFragment(FragmentFileExt.newInstance(), ANIM_FORWARD);
                break;
            case R.id.settings_audio_source_label_layout:
                switchButtonAudio.setChecked(!switchButtonAudio.isChecked());
                break;
            case R.id.settings_audio_source_layout:
                if (switchButtonAudio.isChecked())
                    listenerActivity.showFragment(FragmentAudioSource.newInstance(), ANIM_FORWARD);
                break;
            case R.id.settings_post_call_time_layout:
                PostCallTime.showPostCallTimeDialog(listenerActivity, FragmentOther.this);
                break;
            default:
                break;
        }
    }

    private void activeHandAudioSource() {
        switchButtonAudio.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        audioSourceImg.setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.settings_audio_source_select_ic, COLOR_ICONS[SharedPreferencesFile.getThemeNumber()]));
        audioSourceSelectTitleTv.setTextColor(listenerActivity.getResources().getColor(R.color.color_edit_record_text));
        audioSourceSelectDescTv.setTextColor(listenerActivity.getResources().getColor(R.color.color_settings_pin_desc));
        SharedPreferencesFile.setIsAudioSourceAuto(false);
    }

    private void disableHandAudioSource() {
        switchButtonAudio.setBackColorRes(SWITCH_BTN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
        audioSourceImg.setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.settings_audio_source_select_ic, R.color.color_settings_pin_unselected));
        audioSourceSelectDescTv.setTextColor(listenerActivity.getResources().getColor(R.color.color_settings_pin_unselected));
        SharedPreferencesFile.setIsAudioSourceAuto(true);
        SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            List<Uri> files = Utils.getSelectedFilesFromResult(data);
            for (Uri uri: files) {
                File file = Utils.getFileForUri(uri);
                SharedPreferencesFile.setDirPathAbs(file.getAbsolutePath());
                Toast.makeText(listenerActivity, getString(R.string.change_save_folder_to) + " " + file.getAbsolutePath(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setStyle() {
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_sort_ic, R.drawable.settings_sort_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_action_after_call_ic, R.drawable.settings_action_after_call_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_file_ext_ic, R.drawable.settings_file_ext_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_audio_source_ic, R.drawable.settings_audio_source_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_post_call_time_img, R.drawable.settings_post_call_time_ic);
    }

    private void setDescText() {
        TextView settingsSortDescTv = (TextView) rootView.findViewById(R.id.settings_sort_desc_tv);
        switch (SharedPreferencesFile.getSortBy()) {
            case SORT_BY_DATE:
                settingsSortDescTv.setText(getString(R.string.sort_by_date));
                break;
            case SORT_BY_NAME:
                settingsSortDescTv.setText(getString(R.string.sort_by_name));
                break;
        }

        TextView settingsActionAfterCallDescTv = (TextView) rootView.findViewById(R.id.settings_action_after_call_desc_tv);
        switch (SharedPreferencesFile.getActionAfterCall()) {
            case ACTION_AFTER_CALL_NOTIFICATION:
                settingsActionAfterCallDescTv.setText(getString(R.string.dialog_action_after_call_notification));
                break;
            case ACTION_AFTER_CALL_OPEN:
                settingsActionAfterCallDescTv.setText(getString(R.string.dialog_action_after_call_open));
                break;
            case ACTION_AFTER_CALL_NOTHING:
                settingsActionAfterCallDescTv.setText(getString(R.string.dialog_action_after_call_nothing));
                break;
        }

        TextView settingsFileExtensionDescTv = (TextView) rootView.findViewById(R.id.settings_file_extension_desc_tv);
        switch (SharedPreferencesFile.getOutputFormat()) {
            case MediaRecorder.OutputFormat.AMR_WB:
                settingsFileExtensionDescTv.setText(getString(R.string.dialog_file_ext_amr));
                break;
            case MediaRecorder.OutputFormat.THREE_GPP:
                settingsFileExtensionDescTv.setText(getString(R.string.dialog_file_ext_three_gp));
                break;
            case MediaRecorder.OutputFormat.MPEG_4:
                settingsFileExtensionDescTv.setText(getString(R.string.dialog_file_ext_mp_four));
                break;
            default:
                settingsFileExtensionDescTv.setText(getString(R.string.dialog_file_ext_amr));
                break;
        }
    }

    @Override
   public void onClick() {
        setDescText();
    }

    @Override
    public void onClick(int size) {
        ((TextView) rootView.findViewById(R.id.settings_post_call_time_desc_tv)).setText(String.valueOf(size) + getString(R.string.sec));
    }
}
