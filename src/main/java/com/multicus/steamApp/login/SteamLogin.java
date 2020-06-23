package com.multicus.steamApp.login;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to login into Steam
 */
public class SteamLogin {

    /**
     * Login to steam providing username and password.
     * @param user The username of the Steam account
     * @param password The password of the Steam account
     * @return Cookie needed for various other API requests that require user to be logged into Steam
     * @throws SteamGuardException If SteamGuard authentication is required
     * @throws CaptchaException If Captcha solve is required
     * @throws VerificationException If username of password is incorrect
     */
    public Cookie login(String user, String password) throws SteamGuardException, CaptchaException, VerificationException{
        return login(user, password, "", "", "", "");
    }

    /**
     * Login to steam providing a number of information. At least username and password of account required.
     * @param user The username of the Steam account
     * @param password The password of the Steam account
     * @param captchaText The solved captcha code (if requested to solve Captcha)
     * @param captchaGid The Gid of the captcha (if requested to solve Captcha)
     * @param emailSteamId The Steam ID of account that needs SteamGuard authentication (if requested to SteamGuard)
     * @param emailCode The code that is given on email (if requested to SteamGuard)
     * @return Cookie needed for various other API requests that require user to be logged into Steam
     * @throws SteamGuardException If SteamGuard authentication is required
     * @throws CaptchaException If Captcha solve is required
     * @throws VerificationException If username of password is incorrect
     */
    public Cookie login(String user, String password, String captchaText, String captchaGid, String emailSteamId, String emailCode)
            throws SteamGuardException, CaptchaException, VerificationException {
        // don't save cookies
        Unirest.config()
                .enableCookieManagement(false);

        try {
            // first get RSA key
            RSAJson rsaJson = getRSAKey(user);

            // Encrypt password with the hash from RSAJson
            RSAEncryption crypto = new RSAEncryption(rsaJson.getPublicKeyMod(), rsaJson.getPublicKeyExp());
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

    /**
     * Login to steam providing username, password and the captcha solve.
     * @param user The username of the Steam account
     * @param password The password of the Steam account
     * @param captchaText The solved captcha code (if requested to solve Captcha)
     * @param captchaGid The Gid of the captcha (if requested to solve Captcha)
     * @return Cookie needed for various other API requests that require user to be logged into Steam
     * @throws SteamGuardException If SteamGuard authentication is required
     * @throws CaptchaException If Captcha solve is required
     * @throws VerificationException If username of password is incorrect
     */
    public Cookie loginCaptcha(String user, String password, String captchaText, String captchaGid) throws SteamGuardException, CaptchaException, VerificationException{
        return login(user, password, captchaText, captchaGid, "", "");
    }

    /**
     * Login to Steam providing username, password and SteamGuard authentication
     * @param user The username of the Steam account
     * @param password The password of the Steam account
     * @param emailSteamId The Steam ID of account that needs SteamGuard authentication (if requested to SteamGuard)
     * @param emailCode The code that is given on email (if requested to SteamGuard)
     * @return Cookie needed for various other API requests that require user to be logged into Steam
     * @throws SteamGuardException If SteamGuard authentication is required
     * @throws CaptchaException If Captcha solve is required
     * @throws VerificationException If username of password is incorrect
     */
    public Cookie loginSteamGuard(String user, String password, String emailSteamId, String emailCode) throws SteamGuardException, CaptchaException, VerificationException{
        return login(user, password, "", "", emailSteamId, emailCode);
    }

    /**
     * Perform the steam login
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

    /**
     * Return the RSA key response containing information to encrypt password and login.
     * @param user The username of the account to login with
     * @return Class contaning response info
     * @throws VerificationException If username or password are incorrect
     */
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
