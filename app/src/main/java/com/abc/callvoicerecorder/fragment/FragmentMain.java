package com.abc.callvoicerecorder.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.adapter.MainCallPagerAdapter;
import com.abc.callvoicerecorder.converse.StartAlertWindow;
import com.abc.callvoicerecorder.converse.StartRecord;
import com.abc.callvoicerecorder.helper.CustomTabLinearHelper;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.helper.FragmentsCallHelper;
import com.abc.callvoicerecorder.utils.PermissionHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

public class FragmentMain extends FragmentBase implements Constants, ConstantsColor {
    private View rootView;
    private MainCallPagerAdapter mainFragmentCallPagerAdapter;
    private int PICK_REQUEST_CODE = 0;
    public static Fragment newInstance() {
        return new FragmentMain();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        SharedPreferencesFile.initSharedReferences(listenerActivity);
        setHasOptionsMenu(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_theme1));
        }

        initView();
        if (!SharedPreferencesFile.isSystemAlertWindowShowed()) {
            if (!PermissionHelper.isSystemAlertPermissionGranted(listenerActivity)) {
                StartAlertWindow.showStartAttentionDialog(listenerActivity, getString(R.string.dialog_attention_label), getString(R.string.dialog_system_alert_window));
            }
            SharedPreferencesFile.setIsSystemAlertWindowShowed(true);

        } else if (!SharedPreferencesFile.isStartAttentionShowed()) {
            StartRecord.showStartAttentionDialog(listenerActivity, getString(R.string.dialog_attention_label), getString(R.string.dialog_attention));
            SharedPreferencesFile.setIsStartAttentionShowed(true);
        }

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

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Drawable homeButton = getResources().getDrawable(R.drawable.ic_navigation_menu);
            if (homeButton != null) {
                homeButton.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
            }
            ((AppCompatActivity) getActivity()). getSupportActionBar().setHomeAsUpIndicator(homeButton);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_theme1));
        }

        return rootView;
    }


    private void initView() {
        if (listenerActivity != null) {
            listenerActivity.disableBackButton();
            listenerActivity.setTitle(getString(R.string.app_name_label));
        }

        final ViewPager callPager = (ViewPager) rootView.findViewById(R.id.call_pager);
        mainFragmentCallPagerAdapter = new MainCallPagerAdapter((FragmentMain.this).getFragmentManager(), listenerActivity);
        callPager.setAdapter(mainFragmentCallPagerAdapter);
        callPager.setOffscreenPageLimit(2);

        final RelativeLayout customTab = (RelativeLayout) rootView.findViewById(R.id.custom_tab);
        CustomTabLinearHelper.initCustomTabLinearHelper(listenerActivity, customTab, -1, callPager);

        callPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CustomTabLinearHelper.initCustomTabLinearHelper(listenerActivity, customTab, position, callPager);
                Log.d("RRR", String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.searchview_in_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager manager = (SearchManager) listenerActivity.getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) MenuItemCompat.getActionView(searchItem);
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemsVisibility(menu, searchItem, false);
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setItemsVisibility(menu, searchItem, true);
                return false;
            }
        });
        search.setQueryHint(getString(R.string.search_title));
        search.setSearchableInfo(manager.getSearchableInfo(listenerActivity.getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listenerActivity.showFragment(FragmentContactSearch.newInstance(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }
        });
    }

    private void setItemsVisibility(final Menu menu, final MenuItem exception,
                                    final boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception)
                item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_contacts:
                listenerActivity.showFragment(FragmentDeleteContact.newInstance());
                return true;
            case R.id.action_delete_all:
                listenerActivity.showFragment(FragmentDeleteAll.newInstance());
                return true;
            case R.id.action_delete_dictaphone:
                listenerActivity.showFragment(FragmentDeleteDsAll.newInstance());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void onResume() {
        super.onResume();
        mainFragmentCallPagerAdapter.notifyDataSetChanged();
        FragmentsCallHelper.updateAllLists(listenerActivity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
