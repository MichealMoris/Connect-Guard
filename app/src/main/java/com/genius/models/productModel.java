package com.genius.models;

public class productModel
{

    private  String productImage ;
    private  String productName ;
    private  String standardProductName;
    private  String productDescription ;
    private  String productPrice ;
    private String productStock ;
    private boolean isSoldOut = false;

    public productModel() {
    }

    public productModel(String productImage, String standardProductName,String productName, String productDescription, String productPrice, String productStock) {
        this.productImage = productImage;
        this.standardProductName = standardProductName;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productStock = productStock;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getStandardProductName() {
        return standardProductName;
    }

    public void setStandardProductName(String standardProductName) {
        this.standardProductName = standardProductName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductStock() {
        return productStock;
    }

    public void setProductStock(String productStock) {
        this.productStock = productStock;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }
}
