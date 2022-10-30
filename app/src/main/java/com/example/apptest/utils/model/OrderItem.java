package com.example.apptest.utils.model;

public class OrderItem extends FoodItem{
    private String date;
    public OrderItem(String foodName, String foodBrandName, int foodImage, double foodPrice, String foodDesc,String date) {
        super(foodName, foodBrandName, foodImage, foodPrice, foodDesc);
        this.date=date;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
