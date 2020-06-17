package com.multicus.steamApp.login;

import com.multicus.steamApp.error.SteamException;

public class VerificationException extends SteamException {
    public VerificationException(String message) {
        super(message);
    }
}
