package com.abc.callvoicerecorder.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.fragment.FragmentFavoriteCall;
import com.abc.callvoicerecorder.fragment.FragmentIncomingCall;
import com.abc.callvoicerecorder.fragment.FragmentOutgoingCall;
import com.abc.callvoicerecorder.fragment.FragmentCall;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

import static com.abc.callvoicerecorder.activity.MainActivity.mainFragmentAllAdapter;
import static com.abc.callvoicerecorder.activity.MainActivity.mainFragmentFavoriteAdapter;
import static com.abc.callvoicerecorder.activity.MainActivity.mainFragmentIncomingAdapter;
import static com.abc.callvoicerecorder.activity.MainActivity.mainFragmentOutgoingAdapter;
import static com.abc.callvoicerecorder.activity.MainActivity.searchFragmentAdapter;


public class FragmentsCallHelper implements Constants {
    private static List<CallRecord> callRecordList = new ArrayList<>();
    private static List<CallRecord> callRecordIncomingList = new ArrayList<>();
    private static List<CallRecord> callRecordOutgoingList = new ArrayList<>();
    private static List<CallRecord> callRecordFavoriteList = new ArrayList<>();

    private static List<CallRecord> searchList = new ArrayList<>();

    public static void updateAllLists(Context context) {
        SharedPreferencesFile.initSharedReferences(context);
        try {
            callRecordList.clear();
            callRecordIncomingList.clear();
            callRecordOutgoingList.clear();
            callRecordFavoriteList.clear();
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
            if (mainFragmentAllAdapter != null) {
//            callRecordList = HelperFactory.getHelper().getCallRecordDAO().getAllItems();
                // сортируем по убыванию
                callRecordList = FactoryHelper.getHelper().getCallRecordDAO().getListSortBy(sortBy, typeSort);
                addDateFields(callRecordList);
                mainFragmentAllAdapter.setLists(callRecordList);
                mainFragmentAllAdapter.notifyDataSetChanged();

                if (callRecordList.size() != 0)
                    FragmentCall.visibleRecycler();
                else
                    FragmentCall.visibleTextView();
            }
            if (mainFragmentIncomingAdapter != null) {
//            for (CallRecordItem callRecordItem : HelperFactory.getHelper().getCallRecordDAO().getAllItems()) {
                for (CallRecord callRecordItem : FactoryHelper.getHelper().getCallRecordDAO().getListSortBy(sortBy, typeSort)) {
                    if (callRecordItem.getCallType() == CALL_TYPE_INCOMING)
                        callRecordIncomingList.add(callRecordItem);
                }
                addDateFields(callRecordIncomingList);
                mainFragmentIncomingAdapter.setLists(callRecordIncomingList);
                mainFragmentIncomingAdapter.notifyDataSetChanged();

                if (callRecordList.size() != 0)
                    FragmentIncomingCall.visibleRecycler();
                else
                    FragmentIncomingCall.visibleTextView();
            }
            if (mainFragmentOutgoingAdapter != null) {
//            for (CallRecordItem callRecordItem : HelperFactory.getHelper().getCallRecordDAO().getAllItems()) {
                for (CallRecord callRecordItem : FactoryHelper.getHelper().getCallRecordDAO().getListSortBy(sortBy, typeSort)) {
                    if (callRecordItem.getCallType() == CALL_TYPE_OUTGOING)
                        callRecordOutgoingList.add(callRecordItem);
                }
                addDateFields(callRecordOutgoingList);
                mainFragmentOutgoingAdapter.setLists(callRecordOutgoingList);
                mainFragmentOutgoingAdapter.notifyDataSetChanged();

                if (callRecordList.size() != 0)
                    FragmentOutgoingCall.visibleRecycler();
                else
                    FragmentOutgoingCall.visibleTextView();
            }
            if (mainFragmentFavoriteAdapter != null) {
                for (CallRecord callRecordItem : FactoryHelper.getHelper().getCallRecordDAO().getListSortBy(sortBy, typeSort)) {
                    if (callRecordItem.isFavorite())
                        callRecordFavoriteList.add(callRecordItem);
                }
                addDateFields(callRecordFavoriteList);
                mainFragmentFavoriteAdapter.setLists(callRecordFavoriteList);
                mainFragmentFavoriteAdapter.notifyDataSetChanged();

                if (callRecordList.size() != 0)
                    FragmentFavoriteCall.visibleRecycler();
                else
                    FragmentFavoriteCall.visibleTextView();
            }
            if (searchFragmentAdapter != null) {
                searchList.clear();
//                searchList = HelperFactory.getHelper().getCallRecordDAO().getListByContainsCallName(SharedPreferencesFile.getLastSearchName());
                searchList = FactoryHelper.getHelper().getCallRecordDAO().getListByContainsCallNameNoSql(SharedPreferencesFile.getLastSearchName());
                searchFragmentAdapter.setLists(searchList);
                searchFragmentAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addDateFields(List<CallRecord> callRecordList) {
        if (callRecordList.size() != 0 && SharedPreferencesFile.getSortBy() == SORT_BY_DATE) {
            Calendar timeC = Calendar.getInstance();
            timeC.set(Calendar.HOUR_OF_DAY, 0);
            timeC.set(Calendar.MINUTE, 0);
            timeC.set(Calendar.SECOND, 0);
            timeC.set(Calendar.MILLISECOND, 0);

            while (callRecordList.get(0).getCallDate() < timeC.getTimeInMillis())
                timeC.add(Calendar.DAY_OF_MONTH, -1);
            CallRecord callRecordItem = new CallRecord(null, null, timeC.getTimeInMillis(), "", 999, "", false, "", "");
            callRecordList.add(0, callRecordItem);

            try {
                for (int i = 1; i < callRecordList.size(); i++) {
                    long date = timeC.getTimeInMillis();
                    if (callRecordList.get(i).getCallDate() != date) {
                        while (callRecordList.get(i).getCallDate() != timeC.getTimeInMillis()
                                && callRecordList.get(callRecordList.size() - 1).getCallDate() < timeC.getTimeInMillis())
                            timeC.add(Calendar.DAY_OF_MONTH, -1);
                        if (callRecordList.get(i).getCallDate() == timeC.getTimeInMillis()) {
                            CallRecord callRecordItem1 = new CallRecord(null, null, timeC.getTimeInMillis(), "", 999, "", false, "", "");
                            callRecordList.add(i, callRecordItem1);
                            i++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
