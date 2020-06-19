package com.multicus.steamApp.marketapi;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

/**
 * Class to retrieve the current pricing of an item
 */
public class PriceOverview {
    public static final String baseURL = "https://steamcommunity.com/market/priceoverview/";

    /**
     * Call the API that returns the current price of an item
     * @param appId The Steam App ID of the game to retrieve item from
     * @param marketHashName The market_hash_name of the item to retrieve pricing from
     * @param currency Which currency to get prices to
     * @return The currenct price, volume and median price
     * @throws WrongQueryException Thrown if the parameters in the query are incorrect or other errors
     */
    public PriceOverviewJson callAPI(AppID appId, String marketHashName, CurrencyCode currency) throws WrongQueryException{
        HttpResponse<JsonNode> priceOverview = Unirest.get(baseURL)
                .queryString("appid", appId.getID())
                .queryString("market_hash_name", marketHashName)
                .queryString("currency", currency.getCode())
                .asJson()
                .ifFailure(r -> System.out.println("Could not do priceoverview request: " + r.getStatusText()));

        PriceOverviewJson priceOverviewData = new PriceOverviewJson(priceOverview.getBody());

        if(!priceOverviewData.isSuccess()){
            throw new WrongQueryException("priceoverview",
                    "appid: " + appId.getID(),
                    "market_hash_name: " + marketHashName,
                    "currency: " + currency.getCode());
        }

        return priceOverviewData;
    }
}
