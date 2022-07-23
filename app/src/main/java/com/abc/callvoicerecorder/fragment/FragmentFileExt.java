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

public class FragmentFileExt extends FragmentBase implements Constants, View.OnClickListener{
    private View rootView;
    private ImageView amrImg;
    private ImageView gpImg;
    private ImageView fourImg;

    public static Fragment newInstance() {
        return new FragmentFileExt();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_file_ext, container, false);
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
            listenerActivity.setTitle(listenerActivity.getString(R.string.settings_file_extension));
        }


        (rootView.findViewById(R.id.file_ext_amr_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.file_ext_three_gp_layout)).setOnClickListener(this);;
        (rootView.findViewById(R.id.file_ext_mp_four_layout)).setOnClickListener(this);;

        amrImg = (ImageView) rootView.findViewById(R.id.file_ext_amr_ic);
        gpImg = (ImageView) rootView.findViewById(R.id.file_ext_three_gp_ic);
        fourImg = (ImageView) rootView.findViewById(R.id.file_ext_mp_four_ic);

        clearAllCheck();

        switch (SharedPreferencesFile.getOutputFormat()) {
            case MediaRecorder.OutputFormat.AMR_NB:
                amrImg.setVisibility(View.VISIBLE);
                break;
            case MediaRecorder.OutputFormat.AMR_WB:
                amrImg.setVisibility(View.VISIBLE);
                break;
            case MediaRecorder.OutputFormat.THREE_GPP:
                gpImg.setVisibility(View.VISIBLE);
                break;
            case MediaRecorder.OutputFormat.MPEG_4:
                fourImg.setVisibility(View.VISIBLE);
                break;
            default:
                amrImg.setVisibility(View.VISIBLE);
                break;
        }


    }

    private void clearAllCheck() {
        amrImg.setVisibility(View.GONE);
        gpImg.setVisibility(View.GONE);
        fourImg.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.file_ext_amr_layout:
                SharedPreferencesFile.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
                saveSettings();
                break;
            case R.id.file_ext_three_gp_layout:
                SharedPreferencesFile.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                saveSettings();
                break;
            case R.id.file_ext_mp_four_layout:
                SharedPreferencesFile.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
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
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.file_ext_amr_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.file_ext_three_gp_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.file_ext_mp_four_ic, R.drawable.settings_check_ic);
    }
}
