package com.abc.callvoicerecorder.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.adapter.DeleteAllListAdapter;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.converse.DeleteCheck;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public class FragmentDeleteAll extends FragmentBase implements ConstantsColor, DeleteAllListAdapter.OnClickListener, View.OnClickListener, DeleteCheck.OnClickListener, Constants {
    private View rootView;
    private RecyclerView recyclerView;
    private List<CallRecord> callRecordList;
    private DeleteAllListAdapter deleteAllListFragmentAdapter;
    private boolean isChecked = true;
    public static Fragment newInstance() {
        return new FragmentDeleteAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_delete_all, container, false);
        setHasOptionsMenu(true);
        listenerActivity.visibleBackButton();
        initView();
        setStyle();
        return rootView;
    }

    private void initView() {
        if (listenerActivity != null) {
            listenerActivity.setTitle(getString(R.string.delete_header));
        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        (rootView.findViewById(R.id.btn_delete)).setOnClickListener(FragmentDeleteAll.this);
        updateList();
    }

    private void updateList() {
        if (recyclerView != null) {
            try {
                callRecordList = FactoryHelper.getHelper().getCallRecordDAO().getAllItems();
                LinearLayoutManager layoutManager = new LinearLayoutManager(listenerActivity);
                recyclerView.setLayoutManager(layoutManager);
                deleteAllListFragmentAdapter = new DeleteAllListAdapter(listenerActivity, FragmentDeleteAll.this);
                recyclerView.setAdapter(deleteAllListFragmentAdapter);
                deleteAllListFragmentAdapter.setLists(callRecordList);
                deleteAllListFragmentAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.delete_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_check_all:
                deleteAllListFragmentAdapter.setCheckAllCheckBox(isChecked);
                isChecked = !isChecked;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(CallRecord callRecordItem, View view, int position) {
        switch (view.getId()) {
            case R.id.root_item_layout:
                listenerActivity.showFragment(FragmentRecordPlay.newInstance(callRecordItem.getId(), RECORD_PLAY_LAST_FRAGMENT_DELETE));
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                List<CallRecord> itemsListReturned = deleteAllListFragmentAdapter.getCheckedList();
                if (itemsListReturned != null && itemsListReturned.size() != 0)
                    DeleteCheck.showDeleteCheckCallConfirmDialog(listenerActivity, itemsListReturned, FragmentDeleteAll.this);
                final InterstitialAd mInterstitial = new InterstitialAd(getActivity());
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
                });

                break;
        }
    }

    @Override
    public void onClick(List<CallRecord> callRecordItemList) {
        try {
            if (callRecordItemList != null && callRecordItemList.size() != 0) {
                for (CallRecord callRecordItem : callRecordItemList) {
                    FactoryHelper.getHelper().getCallRecordDAO().deleteItemAndFile(callRecordItem.getId());
                }

                updateAdapter();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAdapter() {
        try {
            callRecordList.clear();
            callRecordList = FactoryHelper.getHelper().getCallRecordDAO().getAllItems();
            if (deleteAllListFragmentAdapter == null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(listenerActivity);
                recyclerView.setLayoutManager(layoutManager);
                deleteAllListFragmentAdapter = new DeleteAllListAdapter(listenerActivity, FragmentDeleteAll.this);
                recyclerView.setAdapter(deleteAllListFragmentAdapter);
            }
            deleteAllListFragmentAdapter.setLists(callRecordList);
            deleteAllListFragmentAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStyle() {
        (rootView.findViewById(R.id.btn_delete)).setBackgroundResource(BUTTON_BG[SharedPreferencesFile.getThemeNumber()]);
    }
}
