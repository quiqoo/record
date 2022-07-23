package com.abc.callvoicerecorder.fragment;

import android.content.Context;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;


public class FragmentRecordType extends FragmentBase implements Constants, View.OnClickListener{
    private View rootView;
    private ImageView rbAllImg;
    private ImageView rbIncomingImg;
    private ImageView rbOutgoingImg;
    private ImageView rbUnknownImg;
    public static Fragment newInstance() {
        return new FragmentRecordType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_record_type, container, false);
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
            listenerActivity.setTitle(listenerActivity.getString(R.string.settings_record_type));
        }

        (rootView.findViewById(R.id.call_type_record_all_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.call_type_record_incoming_layout)).setOnClickListener(this);;
        (rootView.findViewById(R.id.call_type_record_outgoing_layout)).setOnClickListener(this);;
        (rootView.findViewById(R.id.call_type_record_unknown_layout)).setOnClickListener(this);;

        rbAllImg = (ImageView) rootView.findViewById(R.id.call_type_record_all_ic);
        rbIncomingImg = (ImageView) rootView.findViewById(R.id.call_type_record_incoming_ic);
        rbOutgoingImg = (ImageView) rootView.findViewById(R.id.call_type_record_outgoing_ic);
        rbUnknownImg = (ImageView) rootView.findViewById(R.id.call_type_record_unknown_ic);

        clearAllCheck();
        switch (SharedPreferencesFile.getCallTypeRecord()) {
            case CALL_TYPE_RECORD_ALL:
                rbAllImg.setVisibility(View.VISIBLE);
                break;
            case CALL_TYPE_RECORD_INCOMING:
                rbIncomingImg.setVisibility(View.VISIBLE);
                break;
            case CALL_TYPE_RECORD_OUTGOING:
                rbOutgoingImg.setVisibility(View.VISIBLE);
                break;
            case CALL_TYPE_RECORD_UNKNOWN:
                rbUnknownImg.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void clearAllCheck() {
        rbAllImg.setVisibility(View.GONE);
        rbIncomingImg.setVisibility(View.GONE);
        rbOutgoingImg.setVisibility(View.GONE);
        rbUnknownImg.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call_type_record_all_layout:
                SharedPreferencesFile.setCallTypeRecord(CALL_TYPE_RECORD_ALL);
                saveSettings();
                break;
            case R.id.call_type_record_incoming_layout:
                SharedPreferencesFile.setCallTypeRecord(CALL_TYPE_RECORD_INCOMING);
                saveSettings();
                break;
            case R.id.call_type_record_outgoing_layout:
                SharedPreferencesFile.setCallTypeRecord(CALL_TYPE_RECORD_OUTGOING);
                saveSettings();
                break;
            case R.id.call_type_record_unknown_layout:
                saveSettings();
                SharedPreferencesFile.setCallTypeRecord(CALL_TYPE_RECORD_UNKNOWN);
                break;
            default:
                break;
        }
    }

    private void saveSettings() {
        String type = "";
        switch (SharedPreferencesFile.getCallTypeRecord()) {
            case CALL_TYPE_RECORD_ALL:
                type = listenerActivity.getString(R.string.dialog_call_type_record_all);
                break;
            case CALL_TYPE_RECORD_INCOMING:
                type = listenerActivity.getString(R.string.dialog_call_type_record_incoming);
                break;
            case CALL_TYPE_RECORD_OUTGOING:
                type = listenerActivity.getString(R.string.dialog_call_type_record_outgoing);
                break;
            case CALL_TYPE_RECORD_UNKNOWN:
                type = listenerActivity.getString(R.string.dialog_call_type_record_unknown);
                break;
        }
        Toast.makeText(listenerActivity, getString(R.string.dialog_call_type_record_change) + " " + type,
                Toast.LENGTH_SHORT).show();
        listenerActivity.showFragment(FragmentSettings.newInstance(), ANIM_BACKWARD);
    }

    private void setStyle() {
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.call_type_record_all_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.call_type_record_incoming_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.call_type_record_outgoing_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.call_type_record_unknown_ic, R.drawable.settings_check_ic);
    }
}
