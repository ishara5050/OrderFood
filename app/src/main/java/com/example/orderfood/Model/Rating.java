package com.example.orderfood.Model;

public class Rating {

    public String userPhone;
    public String foodId;
    public String rateValue;
    public String comment;
    public String date;

    public Rating() {
    }

    public Rating(String userPhone, String foodId, String rateValue, String comment, String date) {
        this.userPhone = userPhone;
        this.foodId = foodId;
        this.rateValue = rateValue;
        this.comment = comment;
        this.date = date;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
