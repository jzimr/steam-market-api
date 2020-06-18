package com.multicus.steamApp.marketapi;

import kong.unirest.Cookie;

/**
 * Base class for API calls that require user to be logged in
 */
public class APIHandler {
    private static Cookie sessionCookie = new Cookie("", "");

    /**
     * Singleton (ish) constructor
     * @param sessionCookie A Cookie containing the steamLoginSecure with value
     */
    public APIHandler(Cookie sessionCookie) {
        if(this.sessionCookie.getName() == ""){
            this.sessionCookie = sessionCookie;
        }
    }

    protected static Cookie getSessionCookie() {
        return sessionCookie;
    }

    /**
     * Replace the current session cookie
     * @param sessionCookie A cookie containing the steamLoginSecure with value
     */
    public static void updateSessionCookie(Cookie sessionCookie) {
        APIHandler.sessionCookie = sessionCookie;
    }
}
