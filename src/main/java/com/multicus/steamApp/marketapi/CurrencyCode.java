package com.multicus.steamApp.marketapi;

/**
 * Enum containing the currencies of countries currently supported by this API
 */
public enum CurrencyCode {
    USD(1),
    EURO(3),
    NOK(9);     // todo: add rest of currencies

    private final int id;

    CurrencyCode(int id){
        this.id = id;
    }

    public int getCode(){
        return this.id;
    }
}
