package com.zemliak.simplewebshop.models.good;

import com.zemliak.simplewebshop.models.LocalizedObject;

public class GoodsPriceLocalization extends LocalizedObject {
    private int itemPrice;

    public GoodsPriceLocalization(
            int itemPrice,
            String languageTag
    ) {
        this.itemPrice = itemPrice;
        this.setLanguageTag(languageTag);
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
}
