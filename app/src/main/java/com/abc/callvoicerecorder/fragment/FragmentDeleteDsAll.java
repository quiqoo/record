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
import com.abc.callvoicerecorder.adapter.DeleteAllListDicAdapter;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.Record;
import com.abc.callvoicerecorder.converse.DeleteCheckDc;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class FragmentDeleteDsAll extends FragmentBase implements ConstantsColor, DeleteAllListDicAdapter.OnClickListener,
        View.OnClickListener, DeleteCheckDc.OnClickListener, Constants {
    private View rootView;
    private RecyclerView recyclerView;
    private List<Record> callRecordList;
    private DeleteAllListDicAdapter deleteAllListFragmentAdapter;
    private boolean isChecked = true;
    public static Fragment newInstance() {
        return new FragmentDeleteDsAll();
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
        (rootView.findViewById(R.id.btn_delete)).setOnClickListener(FragmentDeleteDsAll.this);
        updateList();
    }

    private void updateList() {
        if (recyclerView != null) {
            try {
                callRecordList = FactoryHelper.getHelper().getDictaphoneRecordDAO().getAllItems();
                LinearLayoutManager layoutManager = new LinearLayoutManager(listenerActivity);
                recyclerView.setLayoutManager(layoutManager);
                deleteAllListFragmentAdapter = new DeleteAllListDicAdapter(listenerActivity, FragmentDeleteDsAll.this);
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
    public void onClick(Record callRecordItem, View view, int position) {
        switch (view.getId()) {
            case R.id.root_item_layout:
                listenerActivity.showFragment(FragmentPlay.newInstance(callRecordItem.getId(), RECORD_PLAY_LAST_FRAGMENT_DELETE));
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                List<Record> itemsListReturned = deleteAllListFragmentAdapter.getCheckedList();
                if (itemsListReturned != null && itemsListReturned.size() != 0)
                    DeleteCheckDc.showDeleteCheckCallConfirmDialog(listenerActivity, itemsListReturned, FragmentDeleteDsAll.this);
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
    public void onClick(List<Record> callRecordItemList) {
        try {
            if (callRecordItemList != null && callRecordItemList.size() != 0) {
                for (Record callRecordItem : callRecordItemList) {
                    FactoryHelper.getHelper().getDictaphoneRecordDAO().deleteItemAndFile(callRecordItem.getId());
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
            callRecordList = FactoryHelper.getHelper().getDictaphoneRecordDAO().getAllItems();
            if (deleteAllListFragmentAdapter == null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(listenerActivity);
                recyclerView.setLayoutManager(layoutManager);
                deleteAllListFragmentAdapter = new DeleteAllListDicAdapter(listenerActivity, FragmentDeleteDsAll.this);
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
