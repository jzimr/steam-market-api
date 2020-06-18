package com.multicus.steamApp.login;

import com.multicus.steamApp.error.SteamException;

/**
 * Exception for when username or password is incorrect
 */
public class VerificationException extends SteamException {
    public VerificationException(String message) {
        super(message);
    }
}
