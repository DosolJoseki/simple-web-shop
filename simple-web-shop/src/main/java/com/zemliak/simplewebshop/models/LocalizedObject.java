package com.zemliak.simplewebshop.models;

//object to extends language tag to all localized properties
public abstract class LocalizedObject {
    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }

    private String languageTag;
}
