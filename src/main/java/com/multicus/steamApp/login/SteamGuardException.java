package com.multicus.steamApp.login;

import com.multicus.steamApp.error.SteamException;

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
