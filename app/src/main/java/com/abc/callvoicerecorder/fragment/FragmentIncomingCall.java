package com.abc.callvoicerecorder.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.adapter.MainCallAllAdapter;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.converse.Delete;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.helper.FragmentsCallHelper;
import com.abc.callvoicerecorder.helper.NotificationsHelper;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

import static com.abc.callvoicerecorder.activity.MainActivity.mainFragmentIncomingAdapter;

public class FragmentIncomingCall extends FragmentBase implements Constants, MainCallAllAdapter.OnClickListener {
    private View rootView;
    private static RecyclerView recyclerView;
    private static RelativeLayout noRecordLayout;
    private List<CallRecord>  callRecordList = new ArrayList<>();
    public static Fragment newInstance() {
        return new FragmentIncomingCall();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_callother, container, false);
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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        noRecordLayout = (RelativeLayout) rootView.findViewById(R.id.no_record_call_layout);
        updateList();
    }

    private void updateList() {
        if (recyclerView != null) {
            listenerActivity.setTitle(getString(R.string.main_screen_call_incoming_fragment));
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
                for (CallRecord callRecordItem :  FactoryHelper.getHelper().getCallRecordDAO().getListSortBy(sortBy, typeSort)) {
                    if (callRecordItem.getCallType() == CALL_TYPE_INCOMING)
                        callRecordList.add(callRecordItem);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(listenerActivity);
                recyclerView.setLayoutManager(layoutManager);
                FragmentsCallHelper.addDateFields(callRecordList);
                mainFragmentIncomingAdapter = new MainCallAllAdapter(listenerActivity, FragmentIncomingCall.this);
                recyclerView.setAdapter(mainFragmentIncomingAdapter);
                mainFragmentIncomingAdapter.setLists(callRecordList);
                mainFragmentIncomingAdapter.notifyDataSetChanged();
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
    }
}
