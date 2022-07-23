package com.abc.callvoicerecorder.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.abc.callvoicerecorder.activity.MainActivity;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;


public class FragmentSkins extends FragmentBase implements Constants, View.OnClickListener{
    private View rootView;
    private ImageView redImg;
    private ImageView blueImg;
    private ImageView greenImg;

    public static Fragment newInstance() {
        return new FragmentSkins();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_skins, container, false);
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
            listenerActivity.setTitle(listenerActivity.getString(R.string.skins));
        }


        (rootView.findViewById(R.id.theme_red_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.theme_blue_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.theme_green_layout)).setOnClickListener(this);

        redImg = (ImageView) rootView.findViewById(R.id.theme_red_ic);
        blueImg = (ImageView) rootView.findViewById(R.id.theme_blue_ic);
        greenImg = (ImageView) rootView.findViewById(R.id.theme_green_ic);

        clearAllCheck();
        switch (SharedPreferencesFile.getThemeNumber()) {
            case 1:
                redImg.setVisibility(View.VISIBLE);
                break;
            case 0:
                blueImg.setVisibility(View.VISIBLE);
                break;
            case 2:
                greenImg.setVisibility(View.VISIBLE);
                break;
        }


    }

    private void clearAllCheck() {
        redImg.setVisibility(View.GONE);
        blueImg.setVisibility(View.GONE);
        greenImg.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.theme_red_layout:
                SharedPreferencesFile.setThemeNumber(1);
                saveSettings();
                break;
            case R.id.theme_blue_layout:
                SharedPreferencesFile.setThemeNumber(0);
                saveSettings();
                break;
            case R.id.theme_green_layout:
                SharedPreferencesFile.setThemeNumber(2);
                saveSettings();
                break;
            default:
                break;
        }
    }

    private void saveSettings() {
        Intent appIntent = new Intent(listenerActivity, MainActivity.class);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        appIntent.putExtra(PUSH_ID, MAIN_ACTIVITY_ID);
        ((MainActivity) listenerActivity).finish();
        listenerActivity.startActivity(appIntent);
    }

    private void setStyle() {
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.theme_red_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.theme_blue_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.theme_green_ic, R.drawable.settings_check_ic);

        ((ImageView) rootView.findViewById(R.id.theme_red_img)).setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.skin_icon_theme2, R.color.color_icons_theme1));
        ((ImageView) rootView.findViewById(R.id.theme_blue_img)).setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.skin_icon_theme1, R.color.color_icons_theme1));
        ((ImageView) rootView.findViewById(R.id.theme_green_img)).setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.skin_icon_theme3, R.color.color_icons_theme1));
    }
}
