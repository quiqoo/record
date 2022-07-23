package com.abc.callvoicerecorder.fragment;

import android.content.Context;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;


public class FragmentAudioSource extends FragmentBase implements Constants, View.OnClickListener{
    private View rootView;
    private ImageView rbDefaultImg;
    private ImageView rbVCImg;
    private ImageView rbMicImg;
    private ImageView rbVrImg;
    private ImageView rbVCallImg;

    public static Fragment newInstance() {
        return new FragmentAudioSource();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_audio_source, container, false);
        setHasOptionsMenu(true);
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
            listenerActivity.setTitle(listenerActivity.getString(R.string.settings_audio_source));
        }


        (rootView.findViewById(R.id.audio_source_default_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.audio_source_vc_layout)).setOnClickListener(this);;
        (rootView.findViewById(R.id.audio_source_mic_layout)).setOnClickListener(this);;
        (rootView.findViewById(R.id.audio_source_vr_layout)).setOnClickListener(this);;
        (rootView.findViewById(R.id.audio_source_v_call_layout)).setOnClickListener(this);;

        rbDefaultImg = (ImageView) rootView.findViewById(R.id.audio_source_default_ic);
        rbVCImg = (ImageView) rootView.findViewById(R.id.audio_source_vc_ic);
        rbMicImg = (ImageView) rootView.findViewById(R.id.audio_source_mic_ic);
        rbVrImg = (ImageView) rootView.findViewById(R.id.audio_source_vr_ic);
        rbVCallImg = (ImageView) rootView.findViewById(R.id.audio_source_v_call_ic);

        clearAllCheck();

        switch (SharedPreferencesFile.getAudioSource()) {
            case MediaRecorder.AudioSource.DEFAULT:
                rbDefaultImg.setVisibility(View.VISIBLE);
                break;
            case MediaRecorder.AudioSource.VOICE_COMMUNICATION:
                rbVCImg.setVisibility(View.VISIBLE);
                break;
            case MediaRecorder.AudioSource.MIC:
                rbMicImg.setVisibility(View.VISIBLE);
                break;
            case MediaRecorder.AudioSource.VOICE_RECOGNITION:
                rbVrImg.setVisibility(View.VISIBLE);
                break;
            case MediaRecorder.AudioSource.VOICE_CALL:
                rbVCallImg.setVisibility(View.VISIBLE);
                break;
            default:
                rbVCImg.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void clearAllCheck() {
        rbDefaultImg.setVisibility(View.GONE);
        rbVCImg.setVisibility(View.GONE);
        rbMicImg.setVisibility(View.GONE);
        rbVrImg.setVisibility(View.GONE);
        rbVCallImg.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.audio_source_default_layout:
                SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                saveSettings();
                break;
            case R.id.audio_source_vc_layout:
                SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                saveSettings();
                break;
            case R.id.audio_source_mic_layout:
                SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.MIC);
                saveSettings();
                break;
            case R.id.audio_source_vr_layout:
                SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
                saveSettings();
                break;
            case R.id.audio_source_v_call_layout:
                SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                saveSettings();
                break;
            default:
                break;
        }
    }

    private void saveSettings() {
        listenerActivity.showFragment(FragmentOther.newInstance(), ANIM_BACKWARD);
    }

    private void setStyle() {
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.audio_source_default_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.audio_source_vc_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.audio_source_mic_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.audio_source_vr_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.audio_source_v_call_ic, R.drawable.settings_check_ic);

    }
}
