package com.multicus.steamApp.login;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.HashMap;
import java.util.Map;


public class SteamLogin {

    public Cookie login(String user, String password) {
        return login(user, password, "", "", "", "");
    }

    public Cookie login(String user, String password, String captchaText, String captchaGid, String emailSteamId, String emailCode)
            throws SteamGuardException, CaptchaException, VerificationException {
        try {
            // first get RSA key
            RSAJson rsaJson = getRSAKey(user);

            // Encrypt password with the hash from RSAJson
            RSA crypto = new RSA(rsaJson.getPublicKeyMod(), rsaJson.getPublicKeyExp());
            String encryptedPassword = crypto.encrypt(password);

            // Make the login by sending request with necessary data
            Map<String, Object> params = new HashMap<>();
            params.put("username", user);
            params.put("password", encryptedPassword);
            params.put("twofactorcode", "");
            params.put("emailauth", emailCode);
            params.put("emailsteamid", emailSteamId);
            params.put("loginfriendlyname", "");
            params.put("captchagid", captchaGid);
            params.put("captcha_text", captchaText);
            params.put("rsatimestamp", Long.toString(rsaJson.getTimestamp()));
            params.put("remember_login", "true");
            params.put("donotcache", Long.toString(System.currentTimeMillis()));

            return doLogin(params);

        } catch (VerificationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cookie loginCaptcha(String user, String password, String captchaText, String captchaGid) {
        return login(user, password, captchaText, captchaGid, "", "");
    }

    public Cookie loginSteamGuard(String user, String password, String emailSteamId, String emailCode) {
        return login(user, password, "", "", emailSteamId, emailCode);
    }

    /**
     * Perform the steam login
     *
     * @param params Parameters to be input into the URL
     * @return The secure login token to perform requests to various Steam API's
     */
    private Cookie doLogin(Map<String, Object> params) throws SteamGuardException, CaptchaException, VerificationException {
        final String loginURL = "https://steamcommunity.com/login/dologin/";

        HttpResponse<JsonNode> response = Unirest.get(loginURL)
                .queryString(params)
                .asJson()
                .ifFailure(r -> System.out.println("Could not do doLogin request: " + r.getStatusText()));

        SteamLoginJson loginJson = new SteamLoginJson(response.getBody());

        if (loginJson.isSuccess()) {
            // logged in
            //System.out.println(response.getBody().toString());
            //System.out.println(Arrays.toString(response.getCookies().toArray()));
            System.out.println("Successfully logged in");
            return response.getCookies().getNamed("steamLoginSecure");
        }

        if (loginJson.isEmailAuthNeeded()) {
            throw new SteamGuardException(loginJson.getEmailSteamId());
        }
        if (loginJson.isCaptchaNeeded()) {
            String captchaG = loginJson.getCaptchaGid();
            throw new CaptchaException("https://steamcommunity.com/public/captcha.php?gid=" + captchaG, captchaG);
        }

        throw new VerificationException(loginJson.getMessage());

    }

    private RSAJson getRSAKey(String user) throws VerificationException {
        final String rsaURL = "https://store.steampowered.com/login/getrsakey/";

        HttpResponse<JsonNode> response = Unirest.get(rsaURL)
                .queryString("username", user)
                .queryString("donotcache", Long.toString(System.currentTimeMillis()))
                .asJson()
                .ifFailure(r -> System.out.println("Could not do getRSA key request: " + r.getStatusText()));

        RSAJson rsaJson = new RSAJson(response.getBody());

        if (rsaJson.isSuccess()) {
            System.out.println("Successfully retrieved RSA Key");
            return rsaJson;
        } else {
            throw new VerificationException("Request failed for some reason. Response: " + response.getBody().toString());
        }
    }
}
