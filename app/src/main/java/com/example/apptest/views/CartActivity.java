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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptest.DashNavActivity;
import com.example.apptest.PaymentActivity;
import com.example.apptest.R;
import com.example.apptest.database.DbHelper;
import com.example.apptest.utils.adapter.CartAdapter;
import com.example.apptest.utils.model.FoodCart;
import com.example.apptest.viewmodel.CartViewModel;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartClickedListeners {

    private RecyclerView recyclerView;
    public static CartViewModel cartViewModel;
    private TextView totalCartPriceTv, textView, toPaymentBtn;
    public static CartAdapter cartAdapter;
    public static Double cartTotal;
    private ImageView back;
    private static int count=0;
    static DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeVariables();
        db=new DbHelper(this);

        back= findViewById(R.id.backtap);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, MenuListActivity.class));
            }
        });
        cartViewModel.getAllCartItems().observe(this, new Observer<List<FoodCart>>() {

            @Override
            public void onChanged(List<FoodCart> foodCarts) {
                double price = 0;
                count=foodCarts.size();
                setCount(count);
                cartAdapter.setFoodCartList(foodCarts);
                for (int i=0;i<foodCarts.size();i++){
                    price = price + foodCarts.get(i).getTotalItemPrice();
                }
                cartTotal=price;
                totalCartPriceTv.setText(String.valueOf(price));
            }
        });

        toPaymentBtn =findViewById(R.id.toTest);
        toPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartTotal!=0){
                    finish();
                    startActivity(new Intent(CartActivity.this, PaymentActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"Your cart seems to be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void initializeVariables() {

        cartAdapter = new CartAdapter(this);
        textView = findViewById(R.id.textView2);
        totalCartPriceTv = findViewById(R.id.cartActivityTotalPriceTv);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

    }

    @Override
    public void onDeleteClicked(FoodCart foodCart) {
        cartViewModel.deleteCartItem(foodCart);
    }

    @Override
    public void onPlusClicked(FoodCart foodCart) {
        int quantity = foodCart.getQuantity() + 1;
        cartViewModel.updateQuantity(foodCart.getId() , quantity);
        cartViewModel.updatePrice(foodCart.getId() , quantity*foodCart.getFoodPrice());
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMinusClicked(FoodCart foodCart) {
        int quantity = foodCart.getQuantity() - 1;
        if (quantity != 0){
            cartViewModel.updateQuantity(foodCart.getId() , quantity);
            cartViewModel.updatePrice(foodCart.getId() , quantity*foodCart.getFoodPrice());
            cartAdapter.notifyDataSetChanged();
        }else{
            cartViewModel.deleteCartItem(foodCart);
        }

    }
    public static void setCount(int i)  {
        Boolean checkUp=db.updateCount(DashNavActivity.userEmail,i);
        if(!checkUp){
        }

    }
}