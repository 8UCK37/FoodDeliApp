package com.example.apptest.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.apptest.utils.model.FoodCart;

import java.util.List;

@Dao
public interface CartDAO {

    @Insert
    void insertCartItem(FoodCart foodCart);

    @Query("SELECT * FROM food_table")
    LiveData<List<FoodCart>> getAllCartItems();

    @Delete
    void deleteCartItem(FoodCart foodCart);

    @Query("UPDATE food_table SET quantity=:quantity WHERE id=:id")
    void updateQuantity(int id , int quantity);

    @Query("UPDATE food_table SET totalItemPrice=:totalItemPrice WHERE id=:id")
    void updatePrice(int id , double totalItemPrice);

    @Query("DELETE FROM food_table")
    void deleteAllItems();

}
