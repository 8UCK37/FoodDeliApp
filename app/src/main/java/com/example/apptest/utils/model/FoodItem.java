package com.example.apptest.utils.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItem implements Parcelable {


    private String foodName, foodBrandName,foodDesc,type;
    private int foodImage;
    private double foodPrice;

    public FoodItem(String foodName, String foodBrandName, int foodImage, double foodPrice,String foodDesc,String type) {
        this.foodName = foodName;
        this.foodBrandName = foodBrandName;
        this.foodImage = foodImage;
        this.foodPrice = foodPrice;
        this.foodDesc = foodDesc;
        this.type = type;
    }

    protected FoodItem(Parcel in) {
        foodName = in.readString();
        foodBrandName = in.readString();
        foodImage = in.readInt();
        foodPrice = in.readDouble();
        foodDesc = in.readString();
        type= in.readString();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };



    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodBrandName() {
        return foodBrandName;
    }
    public void setFoodBrandName(String foodBrandName) {
        this.foodBrandName = foodBrandName;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public int getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(int foodImage) {
        this.foodImage = foodImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getFoodPrice() {

        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(foodName);
        parcel.writeString(foodBrandName);
        parcel.writeInt(foodImage);
        parcel.writeDouble(foodPrice);
        parcel.writeString(foodDesc);
        parcel.writeString(type);
    }
}
