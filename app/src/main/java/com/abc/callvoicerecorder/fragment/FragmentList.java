package com.abc.callvoicerecorder.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.adapter.ListAdapter;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.Record;
import com.abc.callvoicerecorder.converse.DeleteDc;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

import static com.abc.callvoicerecorder.activity.MainActivity.dictaphoneListFragmentAdapter;

public class FragmentList extends FragmentBase implements ListAdapter.OnClickListener,
        DeleteDc.OnClickListener, Constants {
    private View rootView;
    private static RecyclerView recyclerView;
    private static RelativeLayout noRecordLayout;
    private List<Record> dictaphoneRecordList = new ArrayList<>();
    private  String sortBy = "";
    private boolean typeSort = true;

    public static Fragment newInstance() {
        return new FragmentList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_list, container, false);
        initView();
        setStyle();
        return rootView;
    }

    private void initView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        noRecordLayout = (RelativeLayout) rootView.findViewById(R.id.no_record_call_layout);
        noRecordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerActivity.showFragment(FragmentRecord.newInstance());
            }
        });
        updateList();

    }

    private void updateList() {
        if (recyclerView != null) {
            try {
                if (SharedPreferencesFile.getSortBy() == SORT_BY_DATE) {
                    sortBy = Record.NAME_FIELD_ID;
                    typeSort = false;
                }
                else if (SharedPreferencesFile.getSortBy() == SORT_BY_NAME) {
                    sortBy = Record.NAME_FIELD_RECORD_NAME;
                    typeSort = true;
                }
                dictaphoneRecordList = FactoryHelper.getHelper().getDictaphoneRecordDAO().getListSortBy(sortBy, typeSort);
                LinearLayoutManager layoutManager = new LinearLayoutManager(listenerActivity);
                recyclerView.setLayoutManager(layoutManager);
                dictaphoneListFragmentAdapter = new ListAdapter(listenerActivity, FragmentList.this);
                recyclerView.setAdapter(dictaphoneListFragmentAdapter);
                dictaphoneListFragmentAdapter.setLists(dictaphoneRecordList);
                dictaphoneListFragmentAdapter.notifyDataSetChanged();
                if (dictaphoneRecordList.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    (rootView.findViewById(R.id.no_record_call_layout)).setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    (rootView.findViewById(R.id.no_record_call_layout)).setVisibility(View.GONE);
                }
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
    public void onClick(Record dictaphoneRecordItem, View view, int position) {
        switch (view.getId()) {
            case R.id.root_item_layout:
                Intent intentPlay = new Intent();
                intentPlay.setAction(Intent.ACTION_VIEW);
                File filePlay = new File(dictaphoneRecordItem.getPath());
                if (filePlay.exists()) {
                    listenerActivity.showFragment(FragmentPlay.newInstance(dictaphoneRecordItem.getId(), RECORD_PLAY_LAST_FRAGMENT_MAIN));
                }
                break;
        }
    }

    private void updateAdapter() {
        try {
            dictaphoneRecordList.clear();
            dictaphoneRecordList = FactoryHelper.getHelper().getDictaphoneRecordDAO().getListSortBy(sortBy, typeSort);
            if (dictaphoneListFragmentAdapter == null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(listenerActivity);
                recyclerView.setLayoutManager(layoutManager);
                dictaphoneListFragmentAdapter = new ListAdapter(listenerActivity, FragmentList.this);
                recyclerView.setAdapter(dictaphoneListFragmentAdapter);
            }
            dictaphoneListFragmentAdapter.setLists(dictaphoneRecordList);
            dictaphoneListFragmentAdapter.notifyDataSetChanged();
            if (dictaphoneRecordList.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                (rootView.findViewById(R.id.no_record_call_layout)).setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                (rootView.findViewById(R.id.no_record_call_layout)).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(Record dictaphoneRecordItem) {
        try {
            FactoryHelper.getHelper().getDictaphoneRecordDAO().deleteItemAndFile(dictaphoneRecordItem.getId());
            updateAdapter();
        } catch (Exception e) {
            e.printStackTrace();
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
