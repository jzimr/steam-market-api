package com.multicus.steamApp.marketapi;

import com.multicus.steamApp.error.SteamException;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class MarketSearch {
    private static final String baseURL = "https://steamcommunity.com/market/search/render/";

    public MarketSearchJson callAPI(AppID appId, int start, int count) throws SteamException {
        HttpResponse<JsonNode> marketSearch = Unirest.get(baseURL)
                .queryString("norender", 1)
                .queryString("appid", appId.getID())
                .queryString("start", start)
                .queryString("count", count)
                .asJson()
                .ifFailure(r -> System.out.println("Could not do marketsearch request: " + r.getStatusText()));

        MarketSearchJson marketData = new MarketSearchJson(marketSearch.getBody());

        if(!marketData.isSuccess()) {
            throw new SteamException("!Success. Response: " + marketSearch.getBody().toString());
        }
        if(marketData.getItems().isEmpty()){
            throw new SteamException("marketsearch item result is empty! Maybe wrong query?\n Response: " + marketSearch.getBody().toString());
        }

        return marketData;
    }

    public MarketSearchJson callAPI(AppID appId, int start) throws SteamException {
        return callAPI(appId, start, 100);
    }
}
