package com.zemliak.simplewebshop.models.good;

import com.zemliak.simplewebshop.core.Core;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "goods_mydaos1")
public class GoodDao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "description")
    private String description;
    @Lob
    @Column(name = "price")
    private String price;
    @Column(name = "creation_date")
    private String creationDate;
    @Column(name = "update_date")
    private String updateDate;
    @Column(name = "pic_src")
    private String pictureSource;

    public GoodDao(){}

    //name, price, description stored in serialized objects
    public GoodDao(
            String name,
            String price,
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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