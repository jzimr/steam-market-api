package com.multicus.steamApp.marketapi;

import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to transform the JSON of market search results into an object
 */
public class MarketSearchJson {
    /**
     * A single item in the search results
     */
    public class Item {
        private String marketHashName;
        private int volume;
        private double sellPrice; // in usd
        private double salePrice; // in usd
        private int appID;
        private String iconURL;
        private String type;

        public String getMarketHashName() {
            return marketHashName;
        }

        public int getVolume() {
            return volume;
        }

        public double getSellPrice() {
            return sellPrice;
        }

        public double getSalePrice() {
            return salePrice;
        }

        public int getAppID() {
            return appID;
        }

        public String getIconURL() {
            return iconURL;
        }

        public String getType() {
            return type;
        }
    }

    private boolean success;
    private int pageStart;
    private int pageSize;
    private int totalCount;
    private long lastUpdated;   // not part of json, but wish to include
    private String currency;    // --------- || ------------
    private List<Item> items = new ArrayList<>();

    /**
     * Constructor to deserialize the JSON given into the fields of the class
     *
     * @param node JsonNode containing the JSON response to deserialize
     */
    public MarketSearchJson(JsonNode node) {
        JSONObject jsonObj = node.getObject();
        Item item;

        success = jsonObj.getBoolean("success");
        pageStart = jsonObj.getInt("start");
        pageSize = jsonObj.getInt("pagesize");
        totalCount = jsonObj.getInt("total_count");
        lastUpdated = Instant.now().getEpochSecond();

        JSONArray rlts = jsonObj.getJSONArray("results");
        for (int i = 0; i < rlts.length(); i++) {
            jsonObj = rlts.getJSONObject(i);
            item = new Item();

            item.marketHashName = jsonObj.getString("hash_name");
            item.volume = jsonObj.getInt("sell_listings");
            item.sellPrice = jsonObj.getInt("sell_price") / 100.0;

            String salePriceTxt = jsonObj.getString("sale_price_text");
            // get the currency identifier of the request
            if (i == 0) {
                currency = salePriceTxt.replaceAll("[\\s\\d.,]", "");
            }
            // convert sale price String to double
            salePriceTxt = salePriceTxt.replaceAll("[^\\d]", "");

            try {
                item.salePrice = Double.parseDouble(salePriceTxt);
                // dividing by 100 because we replaced decimals in the regex above
                item.salePrice /= 100.0;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }

            jsonObj = jsonObj.getJSONObject("asset_description");
            item.appID = jsonObj.getInt("appid");
            item.iconURL = jsonObj.getString("icon_url");

            String type = jsonObj.getString("type");
            for (ItemType itemType : ItemType.values()) {
                if (type.contains(itemType.getType())) {
                    item.type = itemType.getType();
                    break;
                }
            }

            items.add(item);
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public int getPageStart() {
        return pageStart;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public List<Item> getItems() {
        return items;
    }
}
