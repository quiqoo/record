package com.abc.callvoicerecorder.constant;


public interface Constants {

    //Privacy Policy
    String PRIVACY_POLICY = "http://pharidali.com.np/privacy-policy/"; //Policy Url

    //Ads 1
    String Image1 = "https://i.imgur.com/w6VhceX.png"; //Image Url
    String AdsPACKAGE1 = "com.abc.callrecorder"; // Ads Package Id

    //Ads 2
    String Image2 = "https://i.imgur.com/h3wYEsI.png"; //Image Url
    String AdsPACKAGE2 = "com.abc.cryptomarket"; // Ads Package Id

    //Ads 3
    String Image3 = "https://i.imgur.com/xOkNFHg.png"; //Image Url
    String AdsPACKAGE3 = "com.abc.qhospitalmap"; // Ads Package Id



    String PUSH_ID = "PUSH_ID";
    int MAIN_NOTIFICATION_ID = 417;
    int SECOND_NOTIFICATION_ID = 419;
    int ERROR_NOTIFICATION_ID = 420;

    int CALL_TYPE_INCOMING = 0;
    int CALL_TYPE_OUTGOING = 1;

    int DELETE_AUDIO_STATUS_RIGHT = 0;
    int DELETE_AUDIO_STATUS_ERROR = 1;
    int DELETE_AUDIO_STATUS_RIGHT_CLOSE = 2;

    int EVENT_TYPE_VOICE_CALL = 0;
    int EVENT_TYPE_DEFAULT = 1;
    int EVENT_TYPE_VOICE_COMMUNICATION = 2;

    String EVENT_TITLE_NAME = "event_name";
    String EVENT_FUNCTION_RECORD = "function_record";
    String EVENT_FUNCTION_RECORD_TYPE = "type";
    String EVENT_FUNCTION_RECORD_TYPE_GROUP[] = {"function_record.VOICE_CALL", "function_record.DEFAULT", "function_record.VOICE_COMMUNICATION"};

    String SEARCH_CONTACT_NAME = "SEARCH_CONTACT_NAME";
    String RECORD_ID = "RECORD_ID";
    String RECORD_PLAY_LAST_FRAGMENT= "RECORD_PLAY_LAST_FRAGMENT";
    int RECORD_PLAY_LAST_FRAGMENT_MAIN = 0;
    int RECORD_PLAY_LAST_FRAGMENT_DELETE = 1;

    int SORT_BY_DATE = 0;
    int SORT_BY_NAME = 1;

    int ACTION_AFTER_CALL_NOTIFICATION = 0;
    int ACTION_AFTER_CALL_OPEN = 1;
    int ACTION_AFTER_CALL_NOTHING = 2;

    int CALL_TYPE_RECORD_ALL = 0;
    int CALL_TYPE_RECORD_INCOMING = 1;
    int CALL_TYPE_RECORD_OUTGOING = 2;
    int CALL_TYPE_RECORD_UNKNOWN = 3;

    String TYPE_PIN = "pin_type";
    int TYPE_PIN_SET = 0;
    int TYPE_PIN_CONFIRM = 1;

    int MAIN_ACTIVITY_ID = -1;

    int ANIM_FORWARD = 0;
    int ANIM_BACKWARD = 1;

}
