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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.adapter.MainCallAllAdapter;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.converse.Delete;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.helper.NotificationsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

import static com.abc.callvoicerecorder.activity.MainActivity.searchFragmentAdapter;

public class FragmentContactSearch extends FragmentBase implements MainCallAllAdapter.OnClickListener, Constants {
    private View rootView;
    private String searchContactName;
    private RecyclerView recyclerView;
    private List<CallRecord> callRecordList;

    public static Fragment newInstance(String searchContactName) {
        Fragment fragment = new FragmentContactSearch();
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_CONTACT_NAME, searchContactName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(SEARCH_CONTACT_NAME)) {
            searchContactName = bundle.getString(SEARCH_CONTACT_NAME);
        }

        setHasOptionsMenu(true);
        listenerActivity.visibleBackButton();
        initView();

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
        SharedPreferencesFile.setLastSearchName(searchContactName);
        if (listenerActivity != null) {
            listenerActivity.setTitle(getString(R.string.search_title));
        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        updateList();
    }

    private void updateList() {
        if (recyclerView != null) {
            try {
                callRecordList = FactoryHelper.getHelper().getCallRecordDAO().getListByContainsCallNameNoSql(searchContactName);
                LinearLayoutManager layoutManager = new LinearLayoutManager(listenerActivity);
                recyclerView.setLayoutManager(layoutManager);
                searchFragmentAdapter = new MainCallAllAdapter(listenerActivity, FragmentContactSearch.this);
                recyclerView.setAdapter(searchFragmentAdapter);
                searchFragmentAdapter.setLists(callRecordList);
                searchFragmentAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

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

}
