package com.abc.callvoicerecorder.db;

import com.j256.ormlite.field.DatabaseField;

public class IgnoreContact {
    public final static String NAME_FIELD_ID = "id";
    public final static String NAME_FIELD_IGNORE_CALL_NUMBER = "ignore_call_number";

    @DatabaseField(generatedId = true, columnName = NAME_FIELD_ID)
    private int Id;


    @DatabaseField(columnName = NAME_FIELD_IGNORE_CALL_NUMBER)
    private String callNumber;


    public IgnoreContact() {
        this.callNumber = "";
    }

    public IgnoreContact(String callNumber) {
        this.callNumber = callNumber;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

}

