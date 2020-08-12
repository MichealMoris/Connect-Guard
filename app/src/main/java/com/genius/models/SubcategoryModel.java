package com.genius.models;

public class SubcategoryModel {

    String SubcategoryImage;
    String SubcategoryName;

    public SubcategoryModel(String subcategoryImage, String subcategoryName) {
        SubcategoryImage = subcategoryImage;
        SubcategoryName = subcategoryName;
    }

    public SubcategoryModel() {

    }

    public String getSubcategoryImage() {
        return SubcategoryImage;
    }

    public void setSubcategoryImage(String subcategoryImage) {
        SubcategoryImage = subcategoryImage;
    }

    public String getSubcategoryName() {
        return SubcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        SubcategoryName = subcategoryName;
    }
}
