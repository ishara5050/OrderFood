package com.example.orderfood.Model;

public class Food {

    private String Name,Image,Price,Rating,Rating_cnt,Status,Default,Size,Description,MenuId,Discount;


    public Food() {
    }

    public Food(String name, String image, String price, String rating, String rating_cnt, String status, String aDefault, String size, String description, String menuId, String discount) {
        Name = name;
        Image = image;
        Price = price;
        Rating = rating;
        Rating_cnt = rating_cnt;
        Status = status;
        Default = aDefault;
        Size = size;
        Description = description;
        MenuId = menuId;
        Discount = discount;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getRating_cnt() {
        return Rating_cnt;
    }

    public void setRating_cnt(String rating_cnt) {
        Rating_cnt = rating_cnt;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDefault() {
        return Default;
    }

    public void setDefault(String aDefault) {
        Default = aDefault;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
