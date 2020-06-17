package com.multicus.steamApp.marketapi;

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
