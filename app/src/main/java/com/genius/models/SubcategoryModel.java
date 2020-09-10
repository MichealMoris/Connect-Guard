package com.genius.models;

public class SubcategoryModel {

    String SubcategoryImage;
    String SubcategoryName;
    String MainCategoryName;

    public SubcategoryModel(String subcategoryImage, String subcategoryName, String mainCategoryName) {
        SubcategoryImage = subcategoryImage;
        SubcategoryName = subcategoryName;
        MainCategoryName = mainCategoryName;
    }

    public SubcategoryModel() {

    }

    public String getMainCategoryName() {
        return MainCategoryName;
    }

    public void setMainCategoryName(String mainCategoryName) {
        MainCategoryName = mainCategoryName;
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
