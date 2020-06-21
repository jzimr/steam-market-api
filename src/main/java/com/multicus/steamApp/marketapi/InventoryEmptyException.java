package com.multicus.steamApp.marketapi;

import com.multicus.steamApp.error.SteamException;

/**
 * Exception for when user does not have any items in the inventory
 * of a particular game
 */
public class InventoryEmptyException extends SteamException {

    /**
     * Constructor that takes user id and app id
     * @param steamUserId The Steam user ID of user with empty inventory
     * @param appId The Steam App ID of game
     */
    public InventoryEmptyException(String steamUserId, AppID appId) {
        super("User " + steamUserId + " has no items in inventory on game " + appId.getID());
    }
}
