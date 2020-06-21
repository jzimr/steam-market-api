package com.multicus.steamApp.marketapi;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

/**
 * Class to retrieve the inventory data of a user
 */
public class Inventory {
    private static final String baseURL = "https://steamcommunity.com/inventory/";

    /**
     * Call the API that returns the inventory of a user
     * @param steamUserId The Steam ID to get inventory from
     * @param appId The Steam App ID of the game to retrieve items from
     * @param language The language to get item name, description, ... in
     * @param count Max items to retrieve
     * @return All inventory items
     * @throws WrongQueryException Thrown if the parameters in the query are incorrect
     * @throws InventoryEmptyException Thrown if user has an empty inventory in particular game
     */
    public InventoryJson callAPI(String steamUserId, AppID appId, Language language, int count) throws WrongQueryException, InventoryEmptyException{
        String newURL = baseURL + steamUserId + "/" + appId.getID() + "/2";

        HttpResponse<JsonNode> inventory = Unirest.get(newURL)
                .queryString("l", language.getLanguage())
                .queryString("count", count)
                .asJson()
                .ifFailure(r -> System.out.println("Could not do getInventory request: " + r.getStatusText()));

        InventoryJson inventoryData = new InventoryJson(inventory.getBody());

        if(!inventoryData.isSuccess()){
            throw new WrongQueryException("getinventory",
                    inventory.getBody() == null ? "null" : inventory.getBody().toString(),
                    "steamuserid: " + steamUserId,
                    "appid: " + appId.getID(),
                    "language: " + language.getLanguage(),
                    "count: " + count);
        }
        if(inventoryData.getTotalInventoryCount() == 0){
            throw new InventoryEmptyException(steamUserId, appId);
        }

        return inventoryData;
    }
}
