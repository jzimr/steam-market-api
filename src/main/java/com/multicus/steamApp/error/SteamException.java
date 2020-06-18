package com.multicus.steamApp.error;

/**
 * Base class for all Steam exceptions in this project
 */
public class SteamException extends RuntimeException {
    public SteamException(String message){
        super(message);
    }

    public SteamException(String message, Throwable throwable){
        super(message, throwable);
    }
}
