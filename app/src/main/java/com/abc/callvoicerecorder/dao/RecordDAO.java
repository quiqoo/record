package com.abc.callvoicerecorder.dao;

import com.abc.callvoicerecorder.db.Record;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecordDAO extends BaseDaoImpl<Record, Integer> {

    private List<Record> contactCallRecordList = new ArrayList<>();

    public RecordDAO(ConnectionSource connectionSource, Class<Record> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Record> getAllItems() throws SQLException {
        return this.queryForAll();
    }

    public Record getItem(Integer id) throws SQLException {
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
        DeleteBuilder<Record, Integer> deleteBuilder = this.deleteBuilder();
        deleteBuilder.where().eq(Record.NAME_FIELD_ID, id);
        deleteBuilder.delete();
    }

    public List<Record> getItemByCallName(String name) throws SQLException {
        QueryBuilder<Record, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Record.NAME_FIELD_RECORD_NAME, name);
        PreparedQuery<Record> preparedQuery = queryBuilder.prepare();
        List<Record> itemList =query(preparedQuery);
        return itemList;
    }

    public List<Record> getListByContainsCallName(String name) throws SQLException {
        QueryBuilder<Record, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().like(Record.NAME_FIELD_RECORD_NAME.toLowerCase(), "%" + name.toLowerCase() + "%");
        PreparedQuery<Record> preparedQuery = queryBuilder.prepare();
        List<Record> itemList = query(preparedQuery);
        return itemList;
    }

    public boolean isPackageNameExist(String name) throws SQLException {
        QueryBuilder<Record, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Record.NAME_FIELD_RECORD_NAME, name);
        PreparedQuery<Record> preparedQuery = queryBuilder.prepare();
        List<Record> itemList = query(preparedQuery);
        if (itemList != null && itemList.size() != 0)
            return true;
        else
            return false;
    }

    public List<Record> getListSortBy(String fieldName, boolean type) throws SQLException {
        QueryBuilder<Record, Integer> queryBuilder = queryBuilder();
        queryBuilder.orderBy(fieldName, type);
        PreparedQuery<Record> preparedQuery = queryBuilder.prepare();
        List<Record> itemList = query(preparedQuery);
        return itemList;
    }

    public  List<Record> getLastItem() throws SQLException {
        QueryBuilder<Record, Integer> queryBuilder = queryBuilder();
        queryBuilder.limit(1);
        queryBuilder.orderBy(Record.NAME_FIELD_ID, false);
        PreparedQuery<Record> preparedQuery = queryBuilder.prepare();
        List<Record> itemList = query(preparedQuery);
        return itemList;
    }
}