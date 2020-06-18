package com.multicus.steamApp.login;

import kong.unirest.JsonNode;
import kong.unirest.json.JSONObject;

/**
 * Class that contains the response fields when logging into Steam
 */
public class SteamLoginJson {
    private final boolean success;
    private String message;
    private boolean emailAuthNeeded;
    private String emailDomain;
    private String emailSteamId;
    private boolean captchaNeeded;
    private String captchaGid;

    /**
     * Constructor to deserialize the JSON given into the fields of the class
     * @param node JsonNode containing the JSON response to deserialize
     */
    public SteamLoginJson(JsonNode node){
        JSONObject nodeObj = node.getObject();

        success = nodeObj.getBoolean("success");
        if (nodeObj.has("message")) {
            message = nodeObj.getString("message");
        }
        if (nodeObj.has("captcha_needed")) {
            captchaNeeded = nodeObj.getBoolean("captcha_needed");
            captchaGid = nodeObj.getString("captcha_gid");
        }
        if (nodeObj.has("emailauth_needed")) {
            emailAuthNeeded = nodeObj.getBoolean("emailauth_needed");
            emailDomain = nodeObj.getString("emaildomain");
            emailSteamId = nodeObj.getString("emailsteamid");
        }
        // todo check for too many failed login attempts
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmailAuthNeeded() {
        return emailAuthNeeded;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public String getEmailSteamId() {
        return emailSteamId;
    }

    public boolean isCaptchaNeeded() {
        return captchaNeeded;
    }

    public String getCaptchaGid() {
        return captchaGid;
    }
}
