package com.zemliak.simplewebshop.models.good;

import com.zemliak.simplewebshop.core.Core;

import java.time.LocalDateTime;

public class Good {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String creationDate;
    private String updateDate;
    private String pictureSource;

    public Good(
            long id,
            String name,
            int price,
            String description,
            String pictureSource,
            String creationDate,
            String updateDate
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.creationDate = Core.convertToFormattedString(LocalDateTime.now());
        this.pictureSource = pictureSource;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public Good(
            String name,
            int price,
            String description,
            String pictureSource
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.creationDate = Core.convertToFormattedString(LocalDateTime.now());
        this.pictureSource = pictureSource;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creation_date) {
        this.creationDate = creation_date;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String update_date) {
        this.updateDate = update_date;
    }

    public String getPictureSource() {
        return pictureSource;
    }

    public void setPictureSource(String pic_src) {
        this.pictureSource = pic_src;
    }
}