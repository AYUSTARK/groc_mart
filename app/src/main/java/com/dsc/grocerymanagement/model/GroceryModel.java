package com.dsc.grocerymanagement.model;


import androidx.annotation.Nullable;

public class GroceryModel {
    String name;
    String save;
    String price;
    String img;
    String price0;
    Boolean isAdded = false;

    public GroceryModel() {

    }

    public GroceryModel(String name, String save, String price, String image, String price0) {
        this.name = name;
        this.save = save;
        this.price = price;
        this.img = image;
        this.price0 = price0;
        this.isAdded = false;
    }

    public String getPrice0() {
        return price0;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSave() {
        return save;
    }

    public String getPrice() {
        return price;
    }

    public Boolean getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(Boolean isAdded) {
        this.isAdded = isAdded;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        GroceryModel item = (GroceryModel)obj;
        return name.equals(item.name) && save.equals(item.save) && price.equals(item.price) && img.equals(item.img) && price0.equals(item.price0);
    }
}