package com.abc.callvoicerecorder.dao;

import com.abc.callvoicerecorder.db.IgnoreContact;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class IgnoreContactDAO extends BaseDaoImpl<IgnoreContact, Integer> {
    public IgnoreContactDAO(ConnectionSource connectionSource, Class<IgnoreContact> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<IgnoreContact> getAllItems() throws SQLException {
        return this.queryForAll();
    }


    public IgnoreContact getItem(Integer id) throws SQLException {
        return this.queryForId(id);
    }

    public void deleteItem(int id) throws SQLException {
        DeleteBuilder<IgnoreContact, Integer> deleteBuilder = this.deleteBuilder();
        deleteBuilder.where().eq(IgnoreContact.NAME_FIELD_ID, id);
        deleteBuilder.delete();
    }

    public List<IgnoreContact> getItemByContactNumber(String name) throws SQLException {
        QueryBuilder<IgnoreContact, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(IgnoreContact.NAME_FIELD_IGNORE_CALL_NUMBER, name);
        PreparedQuery<IgnoreContact> preparedQuery = queryBuilder.prepare();
        List<IgnoreContact> itemList =query(preparedQuery);
        return itemList;
    }

    public List<IgnoreContact> getListByContainsNumber(String name) throws SQLException {
        QueryBuilder<IgnoreContact, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().like(IgnoreContact.NAME_FIELD_IGNORE_CALL_NUMBER.toLowerCase(), "%" + name.toLowerCase() + "%");
        PreparedQuery<IgnoreContact> preparedQuery = queryBuilder.prepare();
        List<IgnoreContact> itemList = query(preparedQuery);
        return itemList;
    }

    public boolean isNumberExist(String name) throws SQLException {
        QueryBuilder<IgnoreContact, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(IgnoreContact.NAME_FIELD_IGNORE_CALL_NUMBER, name);
        PreparedQuery<IgnoreContact> preparedQuery = queryBuilder.prepare();
        List<IgnoreContact> itemList = query(preparedQuery);
        if (itemList != null && itemList.size() != 0)
            return true;
        else
            return false;
    }

    public List<IgnoreContact> getListSortBy(String fieldName, boolean type) throws SQLException {
        QueryBuilder<IgnoreContact, Integer> queryBuilder = queryBuilder();
        queryBuilder.orderBy(fieldName, type);
        PreparedQuery<IgnoreContact> preparedQuery = queryBuilder.prepare();
        List<IgnoreContact> itemList = query(preparedQuery);
        return itemList;
    }

    public  List<IgnoreContact> getLastItem() throws SQLException {
        QueryBuilder<IgnoreContact, Integer> queryBuilder = queryBuilder();
        queryBuilder.limit(1);
        queryBuilder.orderBy(IgnoreContact.NAME_FIELD_ID, false);
        PreparedQuery<IgnoreContact> preparedQuery = queryBuilder.prepare();
        List<IgnoreContact> itemList = query(preparedQuery);
        return itemList;
    }
}