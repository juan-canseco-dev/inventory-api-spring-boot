package com.jcanseco.inventoryapi.security.resources;

import org.springframework.stereotype.Component;

@Component("Resource")
public final class ResourceType {
    public static final String Dashboard = "Dashboard";
    public static final String Users = "Users";
    public static final String Roles = "Roles";
    public static final String UnitsOfMeasurement = "UnitsOfMeasurement";
    public static final String Categories = "Categories";
    public static final String Suppliers = "Suppliers";
    public static final String Customers = "Customers";
    public static final String Products = "Products";
    public static final String Purchases = "Purchases";
    public static final String Orders = "Orders";
}
