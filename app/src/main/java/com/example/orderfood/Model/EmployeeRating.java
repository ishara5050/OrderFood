package com.example.orderfood.Model;

public class EmployeeRating {


    public String userPhone;
    public String employeeId;
    public String rateValue;

    public EmployeeRating() {
    }

    public EmployeeRating(String userPhone, String employeeId, String rateValue) {
        this.userPhone = userPhone;
        this.employeeId = employeeId;
        this.rateValue = rateValue;
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
}
