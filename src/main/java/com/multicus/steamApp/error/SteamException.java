package com.multicus.steamApp.error;

public class SteamException extends RuntimeException {
    public SteamException(String message){
        super(message);
    }

    public SteamException(String message, Throwable throwable){
        super(message, throwable);
    }
}
