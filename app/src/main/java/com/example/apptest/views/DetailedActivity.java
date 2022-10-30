package com.example.apptest.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.apptest.DashNavActivity;
import com.example.apptest.R;
import com.example.apptest.database.DbHelper;
import com.example.apptest.utils.model.FoodCart;
import com.example.apptest.utils.model.FoodItem;
import com.example.apptest.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {

    private ImageView foodImageView,favtap;
    private TextView foodNameTV, foodBrandNameTV, foodPriceTV, foodDescTv;
    private TextView addToCart,goToCart;
    private FoodItem food;
    private CartViewModel viewModel;
    private List<FoodCart> foodCartList;
    DbHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        db=new DbHelper(this);
        food = getIntent().getParcelableExtra("foodItem");
        initializeVariables();

        viewModel.getAllCartItems().observe(this, new Observer<List<FoodCart>>() {
            @Override
            public void onChanged(List<FoodCart> foodCarts) {

                foodCartList.addAll(foodCarts);
            }
        });

        if (food != null) {
            setDataToWidgets();
        }
        addToCart=findViewById(R.id.food_detail_add_to_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertToRoom();
                Toast.makeText(getApplicationContext(),"Item added to Cart",Toast.LENGTH_SHORT).show();
            }
        });
        goToCart=findViewById(R.id.food_detail_checkout);
        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailedActivity.this, CartActivity.class));
            }
        });
        favtap=findViewById(R.id.favtap);
        favtap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFavBtnClicked(food);
            }
        });

    }
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
            ;                        if (checkInsertData == true) {
                Toast.makeText(getApplicationContext(), "Added to Favourite", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getApplicationContext(), "Already added to favourites", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void insertToRoom(){
        FoodCart foodCart = new FoodCart();
        foodCart.setFoodName(food.getFoodName());
        foodCart.setFoodBrandName(food.getFoodBrandName());
        foodCart.setFoodPrice(food.getFoodPrice());
        foodCart.setFoodImage(food.getFoodImage());
        foodCart.setFoodDesc(food.getFoodDesc());

        final int[] quantity = {1};
        final int[] id = new int[1];

        if (!foodCartList.isEmpty()){
            for(int i = 0; i< foodCartList.size(); i++){
                if (foodCart.getFoodName().equals(foodCartList.get(i).getFoodName())){
                    quantity[0] = foodCartList.get(i).getQuantity();
                    quantity[0]++;
                    id[0] = foodCartList.get(i).getId();
                }
            }
        }

        if (quantity[0]==1){
            foodCart.setQuantity(quantity[0]);
            foodCart.setTotalItemPrice(quantity[0]*foodCart.getFoodPrice());
            viewModel.insertCartItem(foodCart);
        }else{

            viewModel.updateQuantity(id[0] ,quantity[0]);
            viewModel.updatePrice(id[0] , quantity[0]*foodCart.getFoodPrice());
        }

        //startActivity(new Intent(DetailedActivity.this , CartActivity.class));
    }

    private void setDataToWidgets() {
        foodNameTV.setText(food.getFoodName());
        foodBrandNameTV.setText(food.getFoodBrandName());
        foodPriceTV.setText(String.valueOf(food.getFoodPrice()));
        foodImageView.setImageResource(food.getFoodImage());
        foodDescTv.setText(food.getFoodDesc());
    }

    private void initializeVariables() {

        foodCartList = new ArrayList<>();
        foodImageView = findViewById(R.id.detailActivityFoodIV);
        foodNameTV = findViewById(R.id.detailActivityFoodNameTv);
        foodBrandNameTV = findViewById(R.id.detailActivityFoodBrandNameTv);
        foodPriceTV = findViewById(R.id.detailActivityFoodPriceTv);
        foodDescTv=findViewById(R.id.description_details);


        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }

}