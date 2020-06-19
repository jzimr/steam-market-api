package com.multicus.steamApp.marketapi;

import com.google.gson.JsonObject;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONObject;

import java.time.Instant;

/**
 * Class to transform the JSON of current price of an item into an object
 */
public class PriceOverviewJson {

    private boolean success;
    private long lastUpdated;       // not part of json, but wish to include
    private double lowestPrice;
    private int volume;
    private double medianPrice;

    /**
     * Constructor to deserialize the JSON given into the fields of the class
     * @param node JsonNode containing the JSON response to deserialize
     */
    public PriceOverviewJson(JsonNode node){
        JSONObject jsonObj = node.getObject();

        success = jsonObj.getBoolean("success");
        if(!success){
            return;
        }

        lastUpdated = Instant.now().getEpochSecond();

        // get value for lowest price
        String lowestPriceTxt = jsonObj.getString("lowest_price");
        lowestPriceTxt = lowestPriceTxt.replaceAll("[^\\d]", "");
        try{
            lowestPrice = Double.parseDouble(lowestPriceTxt);
            // dividing by 100 because we replaced decimals in the regex above
            lowestPrice /= 100.0;
        } catch(NumberFormatException e){
            e.printStackTrace();
            lowestPrice = -1;
        }

        // get value for volume
        String volumeTxt = jsonObj.getString("volume");
        volumeTxt = volumeTxt.replaceAll("[^\\d]", "");
        try{
            volume = Integer.parseInt(volumeTxt);
        } catch(NumberFormatException e){
            e.printStackTrace();
            volume = -1;
        }

        // get value for median price
        String medianPriceTxt = jsonObj.getString("median_price");
        medianPriceTxt = medianPriceTxt.replaceAll("[^\\d]", "");
        try{
            medianPrice = Double.parseDouble(medianPriceTxt);
            // dividing by 100 because we replaced decimals in the regex above
            medianPrice /= 100.0;
        } catch(NumberFormatException e){
            e.printStackTrace();
            medianPrice = -1;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public int getVolume() {
        return volume;
    }

    public double getMedianPrice() {
        return medianPrice;
    }
}
