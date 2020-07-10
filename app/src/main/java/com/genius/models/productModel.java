package com.genius.models;

import android.net.Uri;
import android.widget.EditText;

public class productModel
{

    private  String productName ;
    private  String productModel ;
    private  String productDiscreption ;
    private  String productPrice ;
    private  String productImage ;
    private  int type ;


    public productModel(String productName, String productModel, String productDiscreption, String productPrice, String productImage, int type) {
        this.productName = productName;
        this.productModel = productModel;
        this.productDiscreption = productDiscreption;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.type = type;
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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
