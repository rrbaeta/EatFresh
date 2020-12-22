package com.example.eatfresh.Model;

import java.sql.Timestamp;

public class ItemData {

    private String itemId;
    private String itemName;
    private long expiryDate;

    public ItemData()
    {}

    public ItemData(String itemName,String itemId, long expiryDate)
    {
        this.setItemName(itemName);
        this.setItemName(itemId);
        this.setExpiryDate(expiryDate);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

}
