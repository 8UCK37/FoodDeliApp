package com.example.apptest.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.apptest.dao.CartDAO;
import com.example.apptest.database.CartDatabase;
import com.example.apptest.utils.model.FoodCart;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CartRepo {

    private CartDAO cartDAO;
    private LiveData<List<FoodCart>> allCartItemsLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();

    public LiveData<List<FoodCart>> getAllCartItemsLiveData() {
        return allCartItemsLiveData;
    }

    public CartRepo(Application application){
        cartDAO = CartDatabase.getInstance(application).cartDAO();
        allCartItemsLiveData = cartDAO.getAllCartItems();
    }

    public void insertCartItem(FoodCart foodCart){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.insertCartItem(foodCart);
            }
        });
    }

    public void deleteCartItem(FoodCart foodCart){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.deleteCartItem(foodCart);
            }
        });
    }

    public void updateQuantity(int id , int quantity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.updateQuantity(id, quantity);
            }
        });
    }

    public void updatePrice(int id , double price){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.updatePrice(id , price);
            }
        });
    }

    public void deleteAllCartItems(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.deleteAllItems();
            }
        });
    }
}
