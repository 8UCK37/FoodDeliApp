package com.example.apptest.views;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptest.DashNavActivity;
import com.example.apptest.LoginActivity;
import com.example.apptest.R;
import com.example.apptest.database.DbHelper;
import com.example.apptest.ui.Favourites.FavouriteActivity;
import com.example.apptest.utils.adapter.FoodItemAdapter;
import com.example.apptest.utils.model.FoodCart;
import com.example.apptest.utils.model.FoodItem;
import com.example.apptest.viewmodel.CartViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuListActivity extends AppCompatActivity implements FoodItemAdapter.FoodClickedListeners {

    private RecyclerView recyclerView;
    protected List<FoodItem> foodItemList;
    private FoodItemAdapter adapter;
    private CartViewModel viewModel;
    private List<FoodCart> foodCartList;
    private TextView itemCount;
    private CoordinatorLayout coordinatorLayout;
    private static int count=0;
    private ImageView cartImageView,favImgView,backtap;
    private SearchView searchView;
    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        db=new DbHelper(this);
        searchView = findViewById(R.id.food_search);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        count=getCountData();
        initializeVariables();
        setUpList();

        adapter.setFoodItemList(foodItemList);
        recyclerView.setAdapter(adapter);


        cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), String.valueOf(count), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MenuListActivity.this, CartActivity.class));
            }
        });
        itemCount=findViewById(R.id.cartCount);
        itemCount.setText(String.valueOf(count));
        favImgView=findViewById(R.id.favtap);
        favImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuListActivity.this, FavouriteActivity.class));
            }
        });
        backtap=findViewById(R.id.backtap);
        backtap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(MenuListActivity.this, DashNavActivity.class));
            }
        });
    }
    private void setUpList() {
        for(int i = 1; i<= DashNavActivity.foodNo; i++) {
            Cursor cursor = db.getFoodData(String.valueOf(i));
            String org = "null";
            String name = "null";
            int img = 0;
            String price = "null";
            String desc = "null";
            while (cursor.moveToNext()) {
                name = cursor.getString(1);
                org = cursor.getString(2);
                img = cursor.getInt(3);
                price = cursor.getString(4);
                desc = cursor.getString(5);
            }
            foodItemList.add(new FoodItem(name, org, img, Double.valueOf(price), desc,"type"));
        }
    }

    private void filterList(String text) {
        List<FoodItem> filteredList = new ArrayList<>();
        for(FoodItem item: foodItemList){
               if(item.getFoodName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))){
                   filteredList.add(item);
               }
        }
        if(filteredList.isEmpty()){
            //Toast.makeText(getApplicationContext(),"No Match Found",Toast.LENGTH_SHORT).show();
        }else{
                adapter.setFilteredList(filteredList);
        }
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

    @Override
    public void onCardClicked(FoodItem foodItem) {

        Intent intent = new Intent(MenuListActivity.this, DetailedActivity.class);
        intent.putExtra("foodItem", foodItem);
        startActivity(intent);

    }

    @Override
    public void onAddToCartBtnClicked(FoodItem foodItem) {
        count++;
        setCount(count);
        itemCount.setText(String.valueOf(count));
        FoodCart foodCart = new FoodCart();
        foodCart.setFoodName(foodItem.getFoodName());
        foodCart.setFoodBrandName(foodItem.getFoodBrandName());
        foodCart.setFoodPrice(foodItem.getFoodPrice());
        foodCart.setFoodImage(foodItem.getFoodImage());
        foodCart.setFoodDesc(foodItem.getFoodDesc());
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
        String userEmailTXT = DashNavActivity.userEmail;
        String foodnameTXT = foodItem.getFoodName();
        String resNameTXT = foodItem.getFoodBrandName();
        int imageInt = foodItem.getFoodImage();
        String priceTXT = String.valueOf(foodItem.getFoodPrice());
        String descTXT = foodItem.getFoodDesc();

        if(userEmailTXT.equals("")|| foodnameTXT.equals("") ) {
            Toast.makeText(getApplicationContext(), "empty insertion", Toast.LENGTH_SHORT).show();
        }else {
            Boolean checkInsertData = db.insertFavData(userEmailTXT,foodnameTXT,resNameTXT,imageInt,priceTXT,descTXT);
            if (checkInsertData == true) {
                Toast.makeText(getApplicationContext(), "Added to Favourite", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getApplicationContext(), "Already added to favourites", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void makeSnackBar(String msg) {
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT)
                .setAction("Go to Cart", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MenuListActivity.this, CartActivity.class));
                    }
                }).show();
    }
    public void setCount(int i)  {
        Boolean checkUp=db.updateCount(DashNavActivity.userEmail,i);
        if(!checkUp){
            Toast.makeText(getApplicationContext(), "Failed to Add UPI Id", Toast.LENGTH_SHORT).show();

        }

    }
    private int getCountData() {
        Cursor cursor = db.getCountdata(DashNavActivity.userEmail);
        int count = 0;
        while (cursor.moveToNext()) {
            count = cursor.getInt(4);
        }
        return count;
    }
}