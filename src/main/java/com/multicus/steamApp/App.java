package com.multicus.steamApp;

import com.multicus.steamApp.login.RSA;
import com.multicus.steamApp.login.SteamLogin;
import com.multicus.steamApp.marketapi.*;
import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        Unirest.config()
                .enableCookieManagement(true);

        SteamLogin sl = new SteamLogin();
        
    }
}
