package com.multicus.steamApp.login;

import com.multicus.steamApp.error.SteamException;

/**
 * Exception for when user needs to authenticate via Email Code
 * (aka SteamGuard)
 */
public class SteamGuardException extends SteamException {
    private final String emailSteamId;

    public SteamGuardException(String emailSteamId) {
        super("Login: SteamGuard email code is required");
        this.emailSteamId = emailSteamId;
    }

    public String getEmailSteamId(){
        return emailSteamId;
    }
}
