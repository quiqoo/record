package com.abc.callvoicerecorder.transalation;

public enum LocaleTranslation {
    LOCALE_DEFAULT("default"),
    LOCALE_EN("en"),
    LOCALE_RU("ru"),
    LOCALE_DE("de"),
    LOCALE_ES("es"),
    LOCALE_FR("fr"),
    LOCALE_JA("ja"),
    LOCALE_KO("ko"),
    LOCALE_PT("pt"),
    LOCALE_ZH("zh"),
    LOCALE_ZH_RTW("zh-rTW");

    private  String mMessage;

    LocaleTranslation(String message) {
        this.mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
