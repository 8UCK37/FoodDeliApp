package com.example.apptest.ui.Favourites;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptest.DashNavActivity;
import com.example.apptest.R;
import com.example.apptest.database.DbHelper;
import com.example.apptest.utils.adapter.FoodItemAdapter;
import com.example.apptest.utils.model.FoodCart;
import com.example.apptest.utils.model.FoodItem;
import com.example.apptest.viewmodel.CartViewModel;
import com.example.apptest.views.CartActivity;
import com.example.apptest.views.DetailedActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity implements FoodItemAdapter.FoodClickedListeners{
    private RecyclerView recyclerView;
    private ImageView backImg,cartImageView;
    protected List<FoodItem> foodItemList;
    private FoodItemAdapter adapter;
    private CartViewModel viewModel;
    private List<FoodCart> foodCartList;
    private CoordinatorLayout coordinatorLayout;
    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        db=new DbHelper(this);
        initializeVariables();
        setUpList();

        adapter.setFoodItemList(foodItemList);
        recyclerView.setAdapter(adapter);

        backImg=findViewById(R.id.backtap);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FavouriteActivity.this, CartActivity.class));
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getAllCartItems().observe(this, new Observer<List<FoodCart>>() {
            @Override
            public void onChanged(List<FoodCart> foodCarts) {
                foodCartList.addAll(foodCarts);
            }
        });
    }

    private void setUpList() {
        Cursor cursor=db.getFavdata(DashNavActivity.userEmail);
        if(cursor.getCount()==0){
            Toast.makeText(getApplicationContext(),"No favourites yet!!",Toast.LENGTH_SHORT).show();
            return;
        }else{
            while(cursor.moveToNext()){
                String foodName= cursor.getString(1);
                String resName=cursor.getString(2);
                int image=Integer.valueOf(cursor.getString(3));
                double price=Double.valueOf(cursor.getString(4));
                String description=cursor.getString(5);
                foodItemList.add(new FoodItem(foodName, resName, image, price,description));

            }
        }
    }

    @Override
    public void onCardClicked(FoodItem foodItem) {
        Intent intent = new Intent(FavouriteActivity.this, DetailedActivity.class);
        intent.putExtra("foodItem", foodItem);
        startActivity(intent);
    }

    @Override
    public void onAddToCartBtnClicked(FoodItem foodItem) {
        FoodCart foodCart = new FoodCart();
        foodCart.setFoodName(foodItem.getFoodName());
        foodCart.setFoodBrandName(foodItem.getFoodBrandName());
        foodCart.setFoodPrice(foodItem.getFoodPrice());
        foodCart.setFoodImage(foodItem.getFoodImage());

        final int[] quantity = {1};
        final int[] id = new int[1];

        if (!foodCartList.isEmpty()) {
            for (int i = 0; i < foodCartList.size(); i++) {
                if (foodCart.getFoodName().equals(foodCartList.get(i).getFoodName())) {
                    quantity[0] = foodCartList.get(i).getQuantity();
                    quantity[0]++;
                    id[0] = foodCartList.get(i).getId();
                }
            }
        }

        Log.d("TAG", "onAddToCartBtnClicked: " + quantity[0]);

        if (quantity[0] == 1) {
            foodCart.setQuantity(quantity[0]);
            foodCart.setTotalItemPrice(quantity[0] * foodCart.getFoodPrice());
            viewModel.insertCartItem(foodCart);
        } else {
            viewModel.updateQuantity(id[0], quantity[0]);
            viewModel.updatePrice(id[0], quantity[0] * foodCart.getFoodPrice());
        }

        makeSnackBar("Item Added To Cart");
    }

    @Override
    public void onFavBtnClicked(FoodItem foodItem) {

        String foodNameTXT = foodItem.getFoodName();
        Boolean checkFavDeleteData = db.deleteFavdata(foodNameTXT);
        if(checkFavDeleteData) {
            Toast.makeText(getApplicationContext(), "Removed from Favourites", Toast.LENGTH_SHORT).show();
            finish(); startActivity(getIntent());//refreshes the activity leaving nothing behind
        }else {
            Toast.makeText(getApplicationContext(), "Some error occurred when removing", Toast.LENGTH_SHORT).show();
        }

    }

    private void initializeVariables() {

        cartImageView = findViewById(R.id.cartIv);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        foodCartList = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        foodItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new FoodItemAdapter(this);

    }
    private void makeSnackBar(String msg) {
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT)
                .setAction("Go to Cart", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(FavouriteActivity.this, CartActivity.class));
                    }
                }).show();
    }
}