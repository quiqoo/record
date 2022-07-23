package com.abc.callvoicerecorder.db;

import com.j256.ormlite.field.DatabaseField;

public class CallRecord {
    public final static String NAME_FIELD_ID = "id";
    public final static String NAME_FIELD_CALL_NAME = "call_name";
    public final static String NAME_FIELD_CALL_NUMBER = "call_number";
    public final static String NAME_FIELD_CALL_DATE = "call_date";
    public final static String NAME_FIELD_CALL_TIME = "call_time";
    public final static String NAME_FIELD_CALL_TYPE = "call_type";
    public final static String NAME_FIELD_PATH = "path";
    public final static String NAME_FIELD_IS_FAVORITE = "is_favorite";
    public final static String NAME_FIELD_DESC = "desc";
    public final static String NAME_FIELD_DESC_TITLE = "desc_title";

    @DatabaseField(generatedId = true, columnName = NAME_FIELD_ID)
    private int Id;

    @DatabaseField(columnName = NAME_FIELD_CALL_NAME)
    private String callName;

    @DatabaseField(columnName = NAME_FIELD_CALL_NUMBER)
    private String callNumber;

    @DatabaseField(columnName = NAME_FIELD_CALL_DATE)
    private long callDate;

    @DatabaseField(columnName = NAME_FIELD_CALL_TIME)
    private String callTime;

    @DatabaseField(columnName = NAME_FIELD_CALL_TYPE)
    private int callType;

    @DatabaseField(columnName = NAME_FIELD_PATH)
    private String path;

    @DatabaseField(columnName = NAME_FIELD_IS_FAVORITE)
    private boolean isFavorite;

    @DatabaseField(columnName = NAME_FIELD_DESC)
    private String desc;

    @DatabaseField(columnName = NAME_FIELD_DESC_TITLE)
    private String descTitle;


    public CallRecord() {
        this.callName = "";
        this.callNumber = "";
        this.callDate = 0L;
        this.callType = 0;
        this.path = "";
        this.isFavorite = false;
        this.desc = "";
        this.descTitle = "";
    }

    public CallRecord(String callName, String callNumber, long callDate, String callTime, int callType, String path, boolean isFavorite, String desc, String descTitle) {
        this.callName = callName;
        this.callNumber = callNumber;
        this.callDate = callDate;
        this.callTime = callTime;
        this.callType = callType;
        this.path = path;
        this.isFavorite = isFavorite;
        this.desc = desc;
        this.descTitle = descTitle;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }


    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public long getCallDate() {
        return callDate;
    }

    public void setCallDate(long callDate) {
        this.callDate = callDate;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDescTitle() {
        return descTitle;
    }

    public void setDescTitle(String desc_title) {
        this.descTitle = desc_title;
    }
}