package com.multicus.steamApp.marketapi;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class MarketHistory extends APIHandler /*implements APIInterface*/ {
    private static final String baseURL = "https://steamcommunity.com/market/pricehistory/";

    public MarketHistory(Cookie sessionCookie) {
        super(sessionCookie);
    }

    public MarketHistoryJson callAPI(CountryCode countryCode, CurrencyCode currency, AppID appId, String marketHashName) throws NotLoggedInException, WrongQueryException {
        HttpResponse<JsonNode> priceHistory = Unirest.get(baseURL)
                .queryString("country", countryCode.getCode())
                .queryString("currency", currency.getCode())
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
                    "country: " + countryCode.getCode(),
                    "currency: " + currency.getCode(),
                    "appid: " + appId.getID(),
                    "market_hash_name: " + marketHashName);
        }

        return marketData;
    }
}
