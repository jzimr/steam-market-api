package com.multicus.steamApp.marketapi;

public enum AppID {
    COUNTER_STRIKE_GLOBAL_OFFENSIVE(730),
    RUST(252490);

    private final long id;

    AppID(long id){
        this.id = id;
    }

    public long getID(){
        return this.id;
    }
}
