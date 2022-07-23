package com.abc.callvoicerecorder.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import com.abc.callvoicerecorder.db.Record;
import com.abc.callvoicerecorder.fragment.FragmentList;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

import static com.abc.callvoicerecorder.activity.MainActivity.dictaphoneListFragmentAdapter;


public class FragmentsHelper implements Constants {
    private static List<Record> callRecordList = new ArrayList<>();
    private static List<Record> callRecordFavoriteList = new ArrayList<>();

    public static void updateAllLists(Context context) {
        SharedPreferencesFile.initSharedReferences(context);
        try {
            callRecordList.clear();
            callRecordFavoriteList.clear();
            String sortBy = "";
            boolean typeSort = true;
            if (SharedPreferencesFile.getSortBy() == SORT_BY_DATE) {
                sortBy = Record.NAME_FIELD_ID;
                typeSort = false;
            }
            else if (SharedPreferencesFile.getSortBy() == SORT_BY_NAME) {
                sortBy = Record.NAME_FIELD_RECORD_NAME;
                typeSort = true;
            }

            if (dictaphoneListFragmentAdapter != null) {
                callRecordList = FactoryHelper.getHelper().getDictaphoneRecordDAO().getListSortBy(sortBy, typeSort);
                dictaphoneListFragmentAdapter.setLists(callRecordList);
                dictaphoneListFragmentAdapter.notifyDataSetChanged();

                if (callRecordList.size() != 0)
                    FragmentList.visibleRecycler();
                else
                    FragmentList.visibleTextView();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
