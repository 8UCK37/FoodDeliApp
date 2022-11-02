package com.example.apptest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.apptest.database.DbHelper;
import com.example.apptest.utils.model.FoodCart;
import com.example.apptest.utils.model.FoodItem;
import com.example.apptest.viewmodel.CartViewModel;
import com.example.apptest.views.CartActivity;

import java.util.ArrayList;
import java.util.List;


public class FoodDetailActivity extends AppCompatActivity {

    TextView food_name, food_price,food_descTv,resName;
    ImageView food_image,favtap;
    private FoodItem food;
    TextView addToCart,goToCart;
    private List<FoodCart> foodCartList= new ArrayList<>();
    private CartViewModel viewModel;
    DbHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        Intent intent = getIntent();
        db=new DbHelper(this);
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        viewModel.getAllCartItems().observe(this, new Observer<List<FoodCart>>() {
            @Override
            public void onChanged(List<FoodCart> foodCarts) {

                foodCartList.addAll(foodCarts);
            }
        });
        food_name = findViewById(R.id.detailActivityFoodNameTv);
        food_price = findViewById(R.id.detailActivityFoodPriceTv);
        food_image = findViewById(R.id.detailActivityFoodTV);
        food_descTv = findViewById(R.id.description_details);
        resName=findViewById(R.id.detailActivityFoodBrandNameTv);

        String name=intent.getStringExtra("food_name");
        double price=Double.valueOf(intent.getStringExtra("food_price"));
        String desc=intent.getStringExtra("food_desc");
        String org=intent.getStringExtra("origin");
        int img=intent.getIntExtra("food_image", R.drawable.biriyani);

        food = new FoodItem(name,org,img,price,desc,"type");
        if (food != null) {
            food.setFoodImage(img);
            food.setFoodName(name);
            food.setFoodBrandName(org);
            food.setFoodPrice(price);
            food.setFoodDesc(desc);
        }


        food_name.setText(name);
        food_price.setText(String.valueOf(price));
        food_descTv.setText(desc);
        resName.setText(org);
        food_image.setImageResource(img);

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
                startActivity(new Intent(FoodDetailActivity.this, CartActivity.class));
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
}