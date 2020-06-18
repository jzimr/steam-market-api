package com.multicus.steamApp.marketapi;

import com.multicus.steamApp.error.SteamException;

/**
 * Exception for when user is not logged in when required
 */
public class NotLoggedInException extends SteamException {
    public NotLoggedInException(String apiName) {
        super("Need to be logged in to call this API: " + apiName);
    }
}
