package com.multicus.steamApp.marketapi;

import kong.unirest.Cookie;

public class APIHandler {
    private static Cookie sessionCookie = new Cookie("", "");

    public APIHandler(Cookie sessionCookie) {
        if(this.sessionCookie.getName() == ""){
            this.sessionCookie = sessionCookie;
        }
    }

    protected static Cookie getSessionCookie() {
        return sessionCookie;
    }

    public static void updateSessionCookie(Cookie sessionCookie) {
        APIHandler.sessionCookie = sessionCookie;
    }
}
