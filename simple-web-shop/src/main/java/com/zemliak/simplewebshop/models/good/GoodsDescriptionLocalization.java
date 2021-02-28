package com.zemliak.simplewebshop.models.good;

import com.zemliak.simplewebshop.models.LocalizedObject;

public class GoodsDescriptionLocalization extends LocalizedObject{
    private String itemDescription;

    public GoodsDescriptionLocalization(
            String itemDescription,
            String languageTag
    ) {
        this.itemDescription = itemDescription;
        this.setLanguageTag(languageTag);
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}