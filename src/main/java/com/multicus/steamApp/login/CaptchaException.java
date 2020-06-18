package com.multicus.steamApp.login;

import com.multicus.steamApp.error.SteamException;

/**
 * Exception when user needs to solve a captcha on login
 */
public class CaptchaException extends SteamException {
    private final String captchaUrl;
    private final String captchaGid;

    public CaptchaException(String captchaUrl, String captchaGid) {
        super("Login: Captcha verification is required");
        this.captchaUrl = captchaUrl;
        this.captchaGid = captchaGid;
    }

    public String getCaptchaUrl() {
        return captchaUrl;
    }

    public String getCaptchaGid() {
        return captchaGid;
    }
}
