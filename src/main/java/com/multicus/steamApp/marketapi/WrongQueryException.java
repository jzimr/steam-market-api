package com.multicus.steamApp.marketapi;

import com.multicus.steamApp.error.SteamException;

import java.util.Arrays;

/**
 * When the parameters in a query are invalid
 */
public class WrongQueryException extends SteamException {
    public WrongQueryException(String apiName, String... query) {
        super("Could not execute api:\t" + apiName + "\twith query:\t" + Arrays.toString(query));
    }
}
