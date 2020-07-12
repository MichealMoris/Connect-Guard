package com.genius.models;

public class CartModel {

    String product_image;
    String product_name;
    String product_catgory;
    int product_amount;

    public CartModel(String product_image, String product_name, String product_catgory, int product_amount) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_catgory = product_catgory;
        this.product_amount = product_amount;
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
}
