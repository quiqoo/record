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
import com.abc.callvoicerecorder.transalation.LocaleTranslation;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;


public class FragmentTranslation extends FragmentBase implements Constants, View.OnClickListener{
    private View rootView;
    private ImageView rbDefaultImg;
    private ImageView rbEnImg;
    private ImageView rbRuImg;
    private ImageView rbDeImg;
    private ImageView rbEsImg;
    private ImageView rbFrImg;
    private ImageView rbJaImg;
    private ImageView rbKoImg;
    private ImageView rbPtImg;
    private ImageView rbZhImg;
    private ImageView rbZhRtwImg;
    private String langReturn = "default";

    public static Fragment newInstance() {
        return new FragmentTranslation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_translate, container, false);
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
            listenerActivity.setTitle(listenerActivity.getString(R.string.settings_lang));
        }


        (rootView.findViewById(R.id.lang_default_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.lang_en_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.lang_ru_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.lang_de_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.lang_es_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.lang_fr_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.lang_ja_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.lang_ko_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.lang_pt_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.lang_zh_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.lang_zh_rtw_layout)).setOnClickListener(this);

        rbDefaultImg = (ImageView) rootView.findViewById(R.id.lang_default_ic);
        rbEnImg = (ImageView) rootView.findViewById(R.id.lang_en_ic);
        rbRuImg = (ImageView) rootView.findViewById(R.id.lang_ru_ic);
        rbDeImg = (ImageView) rootView.findViewById(R.id.lang_de_ic);
        rbEsImg = (ImageView) rootView.findViewById(R.id.lang_es_ic);
        rbFrImg = (ImageView) rootView.findViewById(R.id.lang_fr_ic);
        rbJaImg = (ImageView) rootView.findViewById(R.id.lang_ja_ic);
        rbKoImg = (ImageView) rootView.findViewById(R.id.lang_ko_ic);
        rbPtImg = (ImageView) rootView.findViewById(R.id.lang_pt_ic);
        rbZhImg = (ImageView) rootView.findViewById(R.id.lang_zh_ic);
        rbZhRtwImg = (ImageView) rootView.findViewById(R.id.lang_zh_rtw_ic);

        clearAllCheck();

        LocaleTranslation localeEnum = LocaleTranslation.LOCALE_DEFAULT;
        for (LocaleTranslation enam : LocaleTranslation.values()){
            if (enam.getMessage().equals(SharedPreferencesFile.getLangType()))
                localeEnum = enam;
        }

        switch (localeEnum) {
            case LOCALE_DEFAULT:
                rbDefaultImg.setVisibility(View.VISIBLE);
                break;
            case LOCALE_EN:
                rbEnImg.setVisibility(View.VISIBLE);
                break;
            case LOCALE_RU:
                rbRuImg.setVisibility(View.VISIBLE);
                break;
            case LOCALE_DE:
                rbDeImg.setVisibility(View.VISIBLE);
                break;
            case LOCALE_ES:
                rbEsImg.setVisibility(View.VISIBLE);
                break;
            case LOCALE_FR:
                rbFrImg.setVisibility(View.VISIBLE);
                break;
            case LOCALE_JA:
                rbJaImg.setVisibility(View.VISIBLE);
                break;
            case LOCALE_KO:
                rbKoImg.setVisibility(View.VISIBLE);
                break;
            case LOCALE_PT:
                rbPtImg.setVisibility(View.VISIBLE);
                break;
            case LOCALE_ZH:
                rbZhImg.setVisibility(View.VISIBLE);
                break;
            case LOCALE_ZH_RTW:
                rbZhRtwImg.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void clearAllCheck() {
        rbDefaultImg.setVisibility(View.GONE);
        rbEnImg.setVisibility(View.GONE);
        rbRuImg.setVisibility(View.GONE);
        rbDeImg.setVisibility(View.GONE);
        rbEsImg.setVisibility(View.GONE);
        rbFrImg.setVisibility(View.GONE);
        rbJaImg.setVisibility(View.GONE);
        rbJaImg.setVisibility(View.GONE);
        rbKoImg.setVisibility(View.GONE);
        rbKoImg.setVisibility(View.GONE);
        rbPtImg.setVisibility(View.GONE);
        rbZhImg.setVisibility(View.GONE);
        rbZhRtwImg.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lang_default_layout:
                langReturn = LocaleTranslation.LOCALE_DEFAULT.getMessage();
                saveSettings();
                break;
            case R.id.lang_en_layout:
                langReturn = LocaleTranslation.LOCALE_EN.getMessage();
                saveSettings();
                break;
            case R.id.lang_ru_layout:
                langReturn = LocaleTranslation.LOCALE_RU.getMessage();
                saveSettings();
                break;
            case R.id.lang_de_layout:
                langReturn = LocaleTranslation.LOCALE_DE.getMessage();
                saveSettings();
                break;
            case R.id.lang_es_layout:
                langReturn = LocaleTranslation.LOCALE_ES.getMessage();
                saveSettings();
                break;
            case R.id.lang_fr_layout:
                langReturn = LocaleTranslation.LOCALE_FR.getMessage();
                saveSettings();
                break;
            case R.id.lang_ja_layout:
                langReturn = LocaleTranslation.LOCALE_JA.getMessage();
                saveSettings();
                break;
            case R.id.lang_ko_layout:
                langReturn = LocaleTranslation.LOCALE_KO.getMessage();
                saveSettings();
                break;
            case R.id.lang_pt_layout:
                langReturn = LocaleTranslation.LOCALE_PT.getMessage();
                saveSettings();
                break;
            case R.id.lang_zh_layout:
                langReturn = LocaleTranslation.LOCALE_ZH.getMessage();
                saveSettings();
                break;
            case R.id.lang_zh_rtw_layout:
                langReturn = LocaleTranslation.LOCALE_ZH_RTW.getMessage();
                saveSettings();
                break;
            default:
                break;
        }
    }


    private void saveSettings() {
        SharedPreferencesFile.setLangType(langReturn);
        Intent appIntent = new Intent(listenerActivity, MainActivity.class);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        appIntent.putExtra(PUSH_ID, MAIN_ACTIVITY_ID);
        ((MainActivity) listenerActivity).finish();
        listenerActivity.startActivity(appIntent);
    }

    private void setStyle() {
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.lang_default_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.lang_en_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.lang_ru_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.lang_de_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.lang_es_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.lang_fr_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.lang_ja_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.lang_ko_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.lang_pt_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.lang_zh_ic, R.drawable.settings_check_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.lang_zh_rtw_ic, R.drawable.settings_check_ic);
    }
}
