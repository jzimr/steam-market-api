package com.multicus.steamApp.marketapi;

public enum CountryCode {
    ARABIC("ar"),
    BULGARIAN("bg"),
    CHINESE_SIMPLIFIED("zh-CN"),
    CHINESE_TRADITIONAL("zh-TW"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH("en"),
    FINNISH("fi"),
    FRENCH("fr"),
    GERMAN("de"),
    GREEK("el"),
    HUNGARIAN("hu"),
    ITALIAN("it"),
    JAPANESE("ja"),
    KOREAN("ko"),
    NORWEGIAN("no"),
    POLISH("pl"),
    PORTUGESE("pt"),
    PORTUGESE_BRAZIL("pt-BR"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SPANISH_SPAIN("es"),
    SPANISH_LATIN_AMERICA("es419"),
    SWEDISH("sv"),
    THAI("th"),
    TURKISH("tr"),
    UKRANIAN("uk"),
    VIETNAMESE("vn");

    private final String code;

    CountryCode(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}
