package com.example.orderfood.Model;

public class EmployeeRating {


    public String userPhone;
    public String employeeId;
    public String rateValue;
    public String date;

    public EmployeeRating() {
    }

    public EmployeeRating(String userPhone, String employeeId, String rateValue, String date) {
        this.userPhone = userPhone;
        this.employeeId = employeeId;
        this.rateValue = rateValue;
        this.date = date;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
