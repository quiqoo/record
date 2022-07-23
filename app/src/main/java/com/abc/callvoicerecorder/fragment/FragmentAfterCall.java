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


public class FragmentAfterCall extends FragmentBase implements Constants, View.OnClickListener{
    private View rootView;
    private ImageView rbNotificationImg;
    private ImageView rbOpenImg;
    private ImageView rbNothingImg;

    public static Fragment newInstance() {
        return new FragmentAfterCall();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_after_call, container, false);
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
            listenerActivity.setTitle(listenerActivity.getString(R.string.settings_action_after_call));
        }


        (rootView.findViewById(R.id.action_after_call_notification_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.action_after_call_open_layout)).setOnClickListener(this);;
        (rootView.findViewById(R.id.action_after_call_nothing_layout)).setOnClickListener(this);;

        rbNotificationImg = (ImageView) rootView.findViewById(R.id.action_after_call_notification_ic);
        rbOpenImg = (ImageView) rootView.findViewById(R.id.action_after_call_open_ic);
        rbNothingImg = (ImageView) rootView.findViewById(R.id.action_after_call_nothing_ic);

        clearAllCheck();

        switch (SharedPreferencesFile.getActionAfterCall()) {
            case ACTION_AFTER_CALL_NOTIFICATION:
                rbNotificationImg.setVisibility(View.VISIBLE);
                break;
            case ACTION_AFTER_CALL_OPEN:
                rbOpenImg.setVisibility(View.VISIBLE);
                break;
            case ACTION_AFTER_CALL_NOTHING:
                rbNothingImg.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void clearAllCheck() {
        rbNotificationImg.setVisibility(View.GONE);
        rbOpenImg.setVisibility(View.GONE);
        rbNothingImg.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_after_call_notification_layout:
                SharedPreferencesFile.setActionAfterCall(ACTION_AFTER_CALL_NOTIFICATION);
                saveSettings();
                break;
            case R.id.action_after_call_open_layout:
                SharedPreferencesFile.setActionAfterCall(ACTION_AFTER_CALL_OPEN);
                saveSettings();
                break;
            case R.id.action_after_call_nothing_layout:
                SharedPreferencesFile.setActionAfterCall(ACTION_AFTER_CALL_NOTHING);
                saveSettings();
                break;
            default:
                break;
        }
    }

    private void saveSettings() {
        String type = "";
        switch (SharedPreferencesFile.getActionAfterCall()) {
            case ACTION_AFTER_CALL_NOTIFICATION:
                type = getString(R.string.dialog_action_after_call_notification);
                break;
            case ACTION_AFTER_CALL_OPEN:
                type = getString(R.string.dialog_action_after_call_open);
                break;
            case ACTION_AFTER_CALL_NOTHING:
                type = getString(R.string.dialog_action_after_call_nothing);
                break;
        }
        Toast.makeText(listenerActivity, getString(R.string.dialog_action_after_call_change) + " " + type,
                Toast.LENGTH_SHORT).show();
        listenerActivity.showFragment(FragmentOther.newInstance(), ANIM_BACKWARD);
    }

    private void setStyle() {
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.action_after_call_notification_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.action_after_call_open_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.action_after_call_nothing_ic, R.drawable.settings_check_ic);
    }
}
