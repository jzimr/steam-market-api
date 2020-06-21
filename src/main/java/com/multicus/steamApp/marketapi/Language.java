package com.multicus.steamApp.marketapi;

/**
 * Enum containing all languages supported by Steam
 */
public enum Language {
    ENGLISH("english"),
    NORWEGIAN("norwegian");

    private final String language;

    Language(String language){
        this.language = language;
    }

    public String getLanguage(){
        return this.language;
    }
}
