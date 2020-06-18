package com.multicus.steamApp.marketapi;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

/**
 * Class to retrieve the price history of an item
 */
public class MarketHistory extends APIHandler {
    private static final String baseURL = "https://steamcommunity.com/market/pricehistory/";

    /**
     * Normal constructor to register the session
     * @param sessionCookie Cookie that contains the steamLoginSecure session value
     */
    public MarketHistory(Cookie sessionCookie) {
        super(sessionCookie);
    }

    /**
     * Call the API that returns the price history of an item
     * @param appId The Steam App ID of the game to retrieve items from
     * @param marketHashName The market_hash_name of the item to retrieve price history from
     * @return All price history entries recorded
     * @throws NotLoggedInException Thrown if user not logged in or session cookie has expired
     * @throws WrongQueryException Thrown if the parameters in the query are wrong
     */
    public MarketHistoryJson callAPI(AppID appId, String marketHashName) throws NotLoggedInException, WrongQueryException {
        HttpResponse<JsonNode> priceHistory = Unirest.get(baseURL)
                .queryString("appid", appId.getID())
                .queryString("market_hash_name", marketHashName)
                .cookie(APIHandler.getSessionCookie())
                .asJson()
                .ifFailure(r -> System.out.println("Could not do pricehistory request: " + r.getStatusText()));

        MarketHistoryJson marketData = new MarketHistoryJson(priceHistory.getBody());

        if(marketData.isEmpty()){
            throw new NotLoggedInException("pricehistory");
        }
        if(!marketData.isSuccess()){
            throw new WrongQueryException("pricehistory",
                    "appid: " + appId.getID(),
                    "market_hash_name: " + marketHashName);
        }

        return marketData;
    }
}
