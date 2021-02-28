package com.zemliak.simplewebshop.models.good;

import com.zemliak.simplewebshop.models.LocalizedObject;

public class GoodsNameLocalization extends LocalizedObject {
    private String itemName;

    public GoodsNameLocalization(
            String itemName,
            String languageTag
    ) {
        this.itemName = itemName;
        this.setLanguageTag(languageTag);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
