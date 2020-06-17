package com.multicus.steamApp.marketapi;

import com.multicus.steamApp.error.SteamException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

public class WrongQueryException extends SteamException {
    public WrongQueryException(String apiName, String... query) {
        super("Could not execute api:\t" + apiName + "\twith query:\t" + Arrays.toString(query));
    }
}
