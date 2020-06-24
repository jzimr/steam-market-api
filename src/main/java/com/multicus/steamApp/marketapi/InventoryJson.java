package com.multicus.steamApp.marketapi;

import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to transform the JSON of inventory of a user into an object
 */
public class InventoryJson {
    /**
     * A unique item in the inventory
     */
    public class Item {
        private String marketHashName;
        private String type;            // (only for CSGO) e.g. "Container", "Rifle", ...
        private List<String> classIds = new ArrayList<>();      // items can have same name but different classIds
        private int amount;
        private String iconURL;
        private boolean marketable;

        public String getMarketHashName() {
            return marketHashName;
        }

        public String getType() {
            return type;
        }

        public int getAmount() {
            return amount;
        }

        public String getIconURL() {
            return iconURL;
        }

        public boolean isMarketable() {
            return marketable;
        }

        public List<String> getClassIds() {
            return classIds;
        }
    }

    private boolean success;
    private int totalInventoryCount;
    private int currency;
    private long lastUpdated;
    private List<Item> items = new ArrayList<>();

    /**
     * Constructor to deserialize the JSON given into the fields of the class
     * @param node JsonNode containing the JSON response to deserialize
     */
    public InventoryJson(JsonNode node) {
        // invalid query or server error
        if(node == null){
            success = false;
            return;
        }

        JSONObject jsonObj = node.getObject();

        // todo check these
        if (jsonObj.has("error")  || (jsonObj.has("success") && jsonObj.getInt("success") == 0)) {
            success = false;
            return;
        }


        success = jsonObj.getInt("success") == 1;
        totalInventoryCount = jsonObj.getInt("total_inventory_count");
        lastUpdated = Instant.now().getEpochSecond();

        // no items available on this inventory
        if (totalInventoryCount == 0) {
            return;
        }

        JSONArray descriptions = jsonObj.getJSONArray("descriptions");
        JSONArray assets = jsonObj.getJSONArray("assets");

        JSONObject itemObj;
        JSONArray tags;
        // first register all unique items in the inventory
        for (int i = 0; i < descriptions.length(); i++) {
            itemObj = descriptions.getJSONObject(i);
            tags = itemObj.getJSONArray("tags");
            if (i == 0) {
                currency = itemObj.getInt("currency");
            }

            String marketHashName = itemObj.getString("market_hash_name");
            String classId = itemObj.getString("classid");
            Item item;
            int j;
            // try to find existing item with market_hash_name
            for(j = 0; j < items.size(); j++) {
                item = items.get(j);
                if(item.marketHashName.equals(marketHashName)) {
                    item.classIds.add(classId);
                    break;
                }
            }
            // if no item found, add new to list
            if(j == items.size()) {
                item = new Item();
                item.marketHashName = marketHashName;
                item.marketable = itemObj.getInt("marketable") == 1;
                item.amount = 0;
                item.iconURL = itemObj.getString("icon_url");
                item.classIds.add(classId);

                // only CSGO is currently supported for getting "type"
                if(itemObj.getInt("appid") == AppID.COUNTER_STRIKE_GLOBAL_OFFENSIVE.getID()){
                    for (int k = 0; k < tags.length(); k++) {
                        if (tags.getJSONObject(k).getString("category").equals("Type")) {
                            item.type = tags.getJSONObject(k).getString("localized_tag_name");
                        }
                    }
                } else {
                    item.type = ItemType.UNCATEGORIZED.getType();
                }
                items.add(item);
            }
        }

        // then register the amount
        int amount;
        for (int i = 0; i < assets.length(); i++) {
            itemObj = assets.getJSONObject(i);
            String classid = itemObj.getString("classid");

            // get value for amount
            String amountTxt = itemObj.getString("amount");
            amountTxt = amountTxt.replaceAll("[^\\d]", "");
            try {
                amount = Integer.parseInt(amountTxt);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                amount = 1;
            }
            final int amountFinal = amount;

            items.stream()
                    .filter(item -> item.classIds.stream().anyMatch(id -> id.equals(classid)))
                    .findAny()
                    .ifPresent(item -> item.amount += amountFinal);
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public int getTotalInventoryCount() {
        return totalInventoryCount;
    }

    public int getCurrency() {
        return currency;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public List<Item> getItems() {
        return items;
    }
}
