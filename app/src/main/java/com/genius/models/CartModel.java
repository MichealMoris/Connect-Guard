package com.genius.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CartModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "product_image")
    private String product_image;
    @ColumnInfo(name = "product_name")
    private String product_name;
    @ColumnInfo(name = "product_catgory")
    private String product_catgory;
    @ColumnInfo(name = "product_amount")
    private int product_amount;
    @ColumnInfo(name = "product_price")
    private int product_price;

    public CartModel(String product_image, String product_name, String product_catgory, int product_amount, int product_price) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_catgory = product_catgory;
        this.product_amount = product_amount;
        this.product_price = product_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_catgory() {
        return product_catgory;
    }

    public void setProduct_catgory(String product_catgory) {
        this.product_catgory = product_catgory;
    }

    public int getProduct_amount() {
        return product_amount;
    }

    public void setProduct_amount(int product_amount) {
        this.product_amount = product_amount;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }
}
