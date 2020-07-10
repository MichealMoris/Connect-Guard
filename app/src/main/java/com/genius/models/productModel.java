package com.genius.models;

import android.widget.EditText;

public class productModel
{

    private  String productName ;
    private  String productModel ;
    private  String productDiscreption ;
    private  String productPrice ;


    public productModel(String productName, String productModel, String productDiscreption, String productPrice) {
        this.productName = productName;
        this.productModel = productModel;
        this.productDiscreption = productDiscreption;
        this.productPrice = productPrice;
    }

    public productModel() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductDiscreption() {
        return productDiscreption;
    }

    public void setProductDiscreption(String productDiscreption) {
        this.productDiscreption = productDiscreption;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }


}
