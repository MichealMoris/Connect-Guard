package com.genius.models;

public class OrderModel {

    String orderUserImage;
    String orderUserName;
    String orderUserEmail;
    String orderUserAddress;
    String orderUserPhoneNumber;
    String orderProductNumber;
    String orderProductName;
    String orderProductModel;
    String orderProductAmount;
    String orderProductPrice;
    String orderProductTotalPrice;
    String orderProductStep;

    public OrderModel(String orderUserImage, String orderUserName, String orderUserEmail, String orderUserAddress, String orderUserPhoneNumber, String orderProductNumber, String orderProductName, String orderProductModel, String orderProductAmount, String orderProductPrice, String orderProductTotalPrice, String orderProductStep) {
        this.orderUserImage = orderUserImage;
        this.orderUserName = orderUserName;
        this.orderUserEmail = orderUserEmail;
        this.orderUserAddress = orderUserAddress;
        this.orderUserPhoneNumber = orderUserPhoneNumber;
        this.orderProductNumber = orderProductNumber;
        this.orderProductName = orderProductName;
        this.orderProductModel = orderProductModel;
        this.orderProductAmount = orderProductAmount;
        this.orderProductPrice = orderProductPrice;
        this.orderProductTotalPrice = orderProductTotalPrice;
        this.orderProductStep = orderProductStep;
    }

    public OrderModel() {
    }

    public String getOrderUserImage() {
        return orderUserImage;
    }

    public void setOrderUserImage(String orderUserImage) {
        this.orderUserImage = orderUserImage;
    }

    public String getOrderUserName() {
        return orderUserName;
    }

    public void setOrderUserName(String orderUserName) {
        this.orderUserName = orderUserName;
    }

    public String getOrderUserEmail() {
        return orderUserEmail;
    }

    public void setOrderUserEmail(String orderUserEmail) {
        this.orderUserEmail = orderUserEmail;
    }

    public String getOrderUserAddress() {
        return orderUserAddress;
    }

    public void setOrderUserAddress(String orderUserAddress) {
        this.orderUserAddress = orderUserAddress;
    }

    public String getOrderUserPhoneNumber() {
        return orderUserPhoneNumber;
    }

    public void setOrderUserPhoneNumber(String orderUserPhoneNumber) {
        this.orderUserPhoneNumber = orderUserPhoneNumber;
    }

    public String getOrderProductNumber() {
        return orderProductNumber;
    }

    public void setOrderProductNumber(String orderProductNumber) {
        this.orderProductNumber = orderProductNumber;
    }

    public String getOrderProductName() {
        return orderProductName;
    }

    public void setOrderProductName(String orderProductName) {
        this.orderProductName = orderProductName;
    }

    public String getOrderProductAmount() {
        return orderProductAmount;
    }

    public void setOrderProductAmount(String orderProductAmount) {
        this.orderProductAmount = orderProductAmount;
    }

    public String getOrderProductPrice() {
        return orderProductPrice;
    }

    public void setOrderProductPrice(String orderProductPrice) {
        this.orderProductPrice = orderProductPrice;
    }

    public String getOrderProductTotalPrice() {
        return orderProductTotalPrice;
    }

    public void setOrderProductTotalPrice(String orderProductTotalPrice) {
        this.orderProductTotalPrice = orderProductTotalPrice;
    }

    public String getOrderProductModel() {
        return orderProductModel;
    }

    public void setOrderProductModel(String orderProductModel) {
        this.orderProductModel = orderProductModel;
    }

    public String getOrderProductStep() {
        return orderProductStep;
    }

    public void setOrderProductStep(String orderProductStep) {
        this.orderProductStep = orderProductStep;
    }
}
