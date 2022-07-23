package com.abc.callvoicerecorder.db;

import com.j256.ormlite.field.DatabaseField;

public class Record {
    public final static String NAME_FIELD_ID = "id";
    public final static String NAME_FIELD_RECORD_NAME = "record_name";
    public final static String NAME_FIELD_RECORD_DATE = "record_date";
    public final static String NAME_FIELD_RECORD_TIME = "record_time";
    public final static String NAME_FIELD_PATH = "path";
    public final static String NAME_FIELD_IS_FAVORITE = "is_favorite";
    public final static String NAME_FIELD_DESC = "desc";
    public final static String NAME_FIELD_DESC_TITLE = "desc_title";

    @DatabaseField(generatedId = true, columnName = NAME_FIELD_ID)
    private int Id;

    @DatabaseField(columnName = NAME_FIELD_RECORD_NAME)
    private String recordName;

    @DatabaseField(columnName = NAME_FIELD_RECORD_DATE)
    private long recordDate;

    @DatabaseField(columnName = NAME_FIELD_RECORD_TIME)
    private String recordTime;

    @DatabaseField(columnName = NAME_FIELD_PATH)
    private String path;

    @DatabaseField(columnName = NAME_FIELD_IS_FAVORITE)
    private boolean isFavorite;

    @DatabaseField(columnName = NAME_FIELD_DESC)
    private String desc;

    @DatabaseField(columnName = NAME_FIELD_DESC_TITLE)
    private String descTitle;


    public Record() {
        this.recordName = "";
        this.recordDate = 0L;
        this.path = "";
        this.isFavorite = false;
        this.desc = "";
        this.descTitle = "";
    }

    public Record(String recordName, long recordDate, String recordTime, String path, boolean isFavorite, String desc, String descTitle) {
        this.recordName = recordName;
        this.recordDate = recordDate;
        this.recordTime = recordTime;
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

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public long getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(long recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public void setDescTitle(String descTitle) {
        this.descTitle = descTitle;
    }
}
