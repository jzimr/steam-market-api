package com.multicus.steamApp.marketapi;

import kong.unirest.JsonNode;

import java.util.HashMap;

public interface APIInterface {

    void callAPI() throws NotLoggedInException;

    Object getData();
}
