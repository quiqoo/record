package com.abc.callvoicerecorder.dao;

import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CallRecordDAO extends BaseDaoImpl<CallRecord, Integer> {

    private List<CallRecord> contactCallRecordList = new ArrayList<>();

    public CallRecordDAO(ConnectionSource connectionSource, Class<CallRecord> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<CallRecord> getAllItems() throws SQLException {
        return this.queryForAll();
    }

    public List<CallRecord> getAllContacts() throws SQLException {
        List<CallRecord> callRecordList = this.queryForAll();
        contactCallRecordList.clear();

        for (CallRecord mainItem: callRecordList) {
            checkNumberEquals(mainItem);
        }

        return contactCallRecordList;
    }

    private void checkNumberEquals(CallRecord callItem) {
        for (CallRecord secondItem: contactCallRecordList) {
            if (callItem.getCallNumber().equals(secondItem.getCallNumber())) {
                return;
            }
        }
        contactCallRecordList.add(callItem);
    }

    public List<CallRecord> getListByContainsCallNameNoSql(String name) throws SQLException {
        List<CallRecord> callRecordList = FactoryHelper.getHelper().getCallRecordDAO().getAllItems();
        List<CallRecord> callRecordListNew = new ArrayList<>();
        for (CallRecord item: callRecordList) {
            if (item.getCallName().toLowerCase().contains(name.toLowerCase()) || item.getCallNumber().toLowerCase().contains(name.toLowerCase()))
                callRecordListNew.add(item);
        }
        return callRecordListNew;
    }


    public CallRecord getItem(Integer id) throws SQLException {
        return this.queryForId(id);
    }

    public void deleteItemAndFile(int id) throws SQLException {
        File file = new File(getItem(id).getPath());
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted)
                file.delete();
        }
        deleteItem(id);
    }

    public void deleteItem(int id) throws SQLException {
        DeleteBuilder<CallRecord, Integer> deleteBuilder = this.deleteBuilder();
        deleteBuilder.where().eq(CallRecord.NAME_FIELD_ID, id);
        deleteBuilder.delete();
    }

    public List<CallRecord> getItemByCallName(String name) throws SQLException {
        QueryBuilder<CallRecord, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(CallRecord.NAME_FIELD_CALL_NAME, name);
        PreparedQuery<CallRecord> preparedQuery = queryBuilder.prepare();
        List<CallRecord> itemList =query(preparedQuery);
        return itemList;
    }

    public List<CallRecord> getListByContainsCallName(String name) throws SQLException {
        QueryBuilder<CallRecord, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().like(CallRecord.NAME_FIELD_CALL_NAME.toLowerCase(), "%" + name.toLowerCase() + "%").or().like(CallRecord.NAME_FIELD_CALL_NUMBER.toLowerCase(), "%" + name.toLowerCase() + "%");
        PreparedQuery<CallRecord> preparedQuery = queryBuilder.prepare();
        List<CallRecord> itemList = query(preparedQuery);
        return itemList;
    }

    public boolean isPackageNameExist(String name) throws SQLException {
        QueryBuilder<CallRecord, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(CallRecord.NAME_FIELD_CALL_NAME, name);
        PreparedQuery<CallRecord> preparedQuery = queryBuilder.prepare();
        List<CallRecord> itemList = query(preparedQuery);
        if (itemList != null && itemList.size() != 0)
            return true;
        else
            return false;
    }

    public List<CallRecord> getListSortBy(String fieldName, boolean type) throws SQLException {
        QueryBuilder<CallRecord, Integer> queryBuilder = queryBuilder();
        queryBuilder.orderBy(fieldName, type);
        PreparedQuery<CallRecord> preparedQuery = queryBuilder.prepare();
        List<CallRecord> itemList = query(preparedQuery);
        return itemList;
    }

    public  List<CallRecord> getLastItem() throws SQLException {
        QueryBuilder<CallRecord, Integer> queryBuilder = queryBuilder();
        queryBuilder.limit(1);
        queryBuilder.orderBy(CallRecord.NAME_FIELD_ID, false);
        PreparedQuery<CallRecord> preparedQuery = queryBuilder.prepare();
        List<CallRecord> itemList = query(preparedQuery);
        return itemList;
    }
}