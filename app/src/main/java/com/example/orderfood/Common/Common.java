package com.example.orderfood.Common;

import com.example.orderfood.Model.User;

public class Common {

    public static User currentUser;

    public static String convertCodeToStatus(String status) {

        if (status.equals("0"))
            return "Placed";

        else if (status.equals("1"))
            return "On the Way";

        else
            return "Shipped";
    }

    public static final String DELETE="Delete";








}
