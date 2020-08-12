package com.genius.models;

public class CategoryModel {

    String CategoryImage;
    String CategoryName;
    String CategoryContentDescription;

    public CategoryModel(String categoryImage, String categoryName, String categoryContentDescription) {
        CategoryImage = categoryImage;
        CategoryName = categoryName;
        CategoryContentDescription = categoryContentDescription;
    }

    public CategoryModel() {
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryContentDescription() {
        return CategoryContentDescription;
    }

    public void setCategoryContentDescription(String categoryContentDescription) {
        CategoryContentDescription = categoryContentDescription;
    }
}
