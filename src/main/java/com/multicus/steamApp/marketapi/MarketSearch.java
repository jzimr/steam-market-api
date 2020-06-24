package com.multicus.steamApp.marketapi;

import com.multicus.steamApp.error.SteamException;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

/**
 * Class to retrieve market results using search.
 */
public class MarketSearch {
    public enum SortBy{
        DEFAULT("default"),
        NAME("name"),
        PRICE("price"),
        QUANTITY("quantity");

        private final String sortby;
        SortBy(String sortby){
            this.sortby = sortby;
        }
        public String getSortby(){
            return this.sortby;
        }
    }

    private static final String baseURL = "https://steamcommunity.com/market/search/render/";

    /**
     * Call the API that returns the search results of the query
     * @param appId The Steam App ID of the game to retrieve items from
     * @param query [Optional] The search query to search items after
     * @param start Offset of search result
     * @param count How many items to retrieve? MAX 100
     * @param sortBy What to order the results by. DEFAULT = No sorting
     * @param ascending true if results should be in ascending order, false if descending order
     * @return All search results
     * @throws SteamException General exception if no results or other errors
     */
    public MarketSearchJson callAPI(AppID appId, String query, int start, int count, SortBy sortBy, boolean ascending) throws SteamException {
        HttpResponse<JsonNode> marketSearch = Unirest.get(baseURL)
                .queryString("norender", 1)
                .queryString("appid", appId.getID())
                .queryString("query", query)
                .queryString("start", start)
                .queryString("count", count)
                .queryString("sort_column", sortBy == SortBy.DEFAULT ? "" : sortBy.getSortby())
                .queryString("sort_dir", ascending ? "asc" : "desc")
                .asJson()
                .ifFailure(r -> System.out.println("Could not do marketsearch request: " + r.getStatusText()));

        MarketSearchJson marketData = new MarketSearchJson(marketSearch.getBody());

        if(!marketData.isSuccess()) {
            throw new SteamException("Unsuccesfull! Response: " + marketSearch.getBody().toString());
        }
        if(marketData.getItems().isEmpty()){
            throw new SteamException("marketsearch item result is empty! Maybe wrong query?\n Response: " + marketSearch.getBody().toString());
        }

        return marketData;
    }

    /**
     * Call the API that returns the search results of the query
     * @param appId The Steam App ID of the game to retrieve items from
     * @param start Offset of search result
     * @return All search results
     * @throws SteamException General exception if no results or other errors
     */
    public MarketSearchJson callAPI(AppID appId, int start) throws SteamException {
        return callAPI(appId, "", start, 100, SortBy.DEFAULT, false);
    }

    /**
     * Call the API that returns the search results of the query
     * @param appId The Steam App ID of the game to retrieve items from
     * @param start Offset of search result
     * @param sortBy What to order the results by. DEFAULT = No sorting
     * @param ascending true if results should be in ascending order, false if descending order
     * @return All search results
     * @throws SteamException General exception if no results or other errors
     */
    public MarketSearchJson callAPI(AppID appId, int start, SortBy sortBy, boolean ascending) throws SteamException{
        return callAPI(appId, "", start, 100, sortBy, ascending);
    }

    /**
     * Call the API that returns the search results of the query
     * @param appId The Steam App ID of the game to retrieve items from
     * @param start Offset of search result
     * @param count How many items to retrieve? MAX 100
     * @return All search results
     * @throws SteamException General exception if no results or other errors
     */
    public MarketSearchJson callAPI(AppID appId, int start, int count) throws SteamException {
        return callAPI(appId, "", start, count, SortBy.DEFAULT, false);
    }

    /**
     * Call the API that returns the search results of the query
     * @param appId The Steam App ID of the game to retrieve items from
     * @param query [Optional] The search query to search items after
     * @return All search results
     * @throws SteamException General exception if no results or other errors
     */
    public MarketSearchJson callAPI(AppID appId, String query) throws SteamException {
        return callAPI(appId, query, 0, 100, SortBy.DEFAULT, false);
    }

    /**
     * Call the API that returns the search results of the query
     * @param query [Optional] The search query to search items after
     * @param start Offset of search result
     * @return All search results
     * @throws SteamException General exception if no results or other errors
     */
    public MarketSearchJson callAPI(AppID appID, String query, int start) throws SteamException {
        return callAPI(appID, query, start, 100, SortBy.DEFAULT, false);
    }
}
