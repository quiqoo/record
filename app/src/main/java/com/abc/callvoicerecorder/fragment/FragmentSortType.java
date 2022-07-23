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


public class FragmentSortType extends FragmentBase implements Constants, View.OnClickListener{
    private View rootView;
    private ImageView rbDateImg;
    private ImageView rbNameImg;
    public static Fragment newInstance() {
        return new FragmentSortType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sort_type, container, false);
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
            listenerActivity.setTitle(listenerActivity.getString(R.string.settings_sort));
        }


        (rootView.findViewById(R.id.sort_by_date_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.sort_by_name_layout)).setOnClickListener(this);

        rbDateImg = (ImageView) rootView.findViewById(R.id.sort_by_date_ic);
        rbNameImg = (ImageView) rootView.findViewById(R.id.sort_by_name_ic);

        clearAllCheck();
        switch (SharedPreferencesFile.getSortBy()) {
            case SORT_BY_DATE:
                rbDateImg.setVisibility(View.VISIBLE);
                break;
            case SORT_BY_NAME:
                rbNameImg.setVisibility(View.VISIBLE);
                break;
        }


    }

    private void clearAllCheck() {
        rbDateImg.setVisibility(View.GONE);
        rbNameImg.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sort_by_date_layout:
                SharedPreferencesFile.setSortBy(SORT_BY_DATE);
                saveSettings();
                break;
            case R.id.sort_by_name_layout:
                SharedPreferencesFile.setSortBy(SORT_BY_NAME);
                saveSettings();
                break;
            default:
                break;
        }
    }

    private void saveSettings() {
        String type = "";
        switch (SharedPreferencesFile.getSortBy()) {
            case SORT_BY_DATE:
                type = getString(R.string.sort_by_date);
                break;
            case SORT_BY_NAME:
                type = getString(R.string.sort_by_name);
                break;
        }
        Toast.makeText(listenerActivity, getString(R.string.dialog_call_sort_change) + " " + type,
                Toast.LENGTH_SHORT).show();
        listenerActivity.showFragment(FragmentOther.newInstance(), ANIM_BACKWARD);
    }

    private void setStyle() {
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.sort_by_date_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.sort_by_name_ic, R.drawable.settings_check_ic);
    }
}
