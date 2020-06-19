package com.multicus.steamApp.marketapi;

import com.multicus.steamApp.error.SteamException;

import java.util.Arrays;

/**
 * When the parameters in a query are invalid
 */
public class WrongQueryException extends SteamException {
    /**
     * Constructor that takes the apiname that created the exception
     * and the query parameters
     * @param apiName
     * @param query
     */
    public WrongQueryException(String apiName, String... query) {
        super("Could not execute api:\t" + apiName + "\twith query:\t" + Arrays.toString(query));
    }
}
