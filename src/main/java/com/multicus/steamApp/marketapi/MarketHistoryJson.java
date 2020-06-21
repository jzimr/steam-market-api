package com.multicus.steamApp.marketapi;

import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class to transform the JSON of price history of an item into an object
 */
public class MarketHistoryJson {
    /**
     * A single item price history entry
     */
    public class Record {
        private final Date date;
        private final double price;
        private final int amountSold;

        public Record(Date date, double price, int amountSold){
            this.date = date;
            this.price = price;
            this.amountSold = amountSold;
        }
        public Date getDate() {
            return date;
        }
        public double getPrice() {
            return price;
        }
        public int getAmountSold() {
            return amountSold;
        }
    }

    private boolean isEmpty;
    private boolean success;
    private String pricePrefix;
    private String priceSuffix;
    private List<Record> records = new ArrayList<>();
    private final static SimpleDateFormat isoFormat = new SimpleDateFormat("MMM dd yyyy HH", Locale.ENGLISH);

    /**
     * Constructor to deserialize the JSON given into the fields of the class
     * @param node JsonNode containing the JSON response to deserialize
     */
    public MarketHistoryJson(JsonNode node) {
        if(node.isArray() && node.getArray().isEmpty()){
            isEmpty = true;
            return;
        }

        isEmpty = false;
        success = node.getObject().getBoolean("success");
        if(success == false){
            return;
        }
        pricePrefix = node.getObject().getString("price_prefix");
        priceSuffix = node.getObject().getString("price_suffix");

        if(node.getObject().get("prices") instanceof Boolean){
            return;
        } else if (node.getObject().getJSONArray("prices") != null) {
            JSONArray prices = node.getObject().getJSONArray("prices");
            Date date;
            double price;
            int amountSold;

            // for each price entry
            for(int i = 0; i < prices.length(); i++){
                JSONArray entry = prices.getJSONArray(i);

                if(entry.length() == 3){
                    try{
                        date = isoFormat.parse(entry.getString(0));
                        price = entry.getDouble(1);
                        amountSold = Integer.parseInt(entry.getString(2));
                    } catch(ParseException e){
                        System.out.println("parse error: " + e.getMessage());
                        continue;
                    }
                    records.add(new Record(date, price, amountSold));
                }
            }
        }
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getPricePrefix() {
        return pricePrefix;
    }

    public String getPriceSuffix() {
        return priceSuffix;
    }

    public List<Record> getRecords() {
        return records;
    }
}
