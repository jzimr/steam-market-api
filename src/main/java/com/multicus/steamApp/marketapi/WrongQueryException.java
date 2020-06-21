package com.multicus.steamApp.marketapi;

import com.multicus.steamApp.error.SteamException;
import kong.unirest.JsonNode;

import java.util.Arrays;

/**
 * When the parameters in a query are invalid
 */
public class WrongQueryException extends SteamException {
    /**
     * Constructor that takes the apiname that created the exception
     * and the query parameters
     * @param apiName Name of the api that threw this exception
     * @param response The request response that we got
     * @param query The input parameters of the URL
     */
    public WrongQueryException(String apiName, String response, String... query) {
        super("Could not execute api:\t" + apiName + "\twith query:\t" + Arrays.toString(query)
        + "\nResponse: " + response);
    }
}
