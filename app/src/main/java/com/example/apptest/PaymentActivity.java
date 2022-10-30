package com.example.apptest;

import static com.example.apptest.views.CartActivity.cartAdapter;
import static com.example.apptest.views.CartActivity.cartTotal;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.apptest.database.DbHelper;
import com.example.apptest.ui.AddSettings.AddingNewAddActivity;
import com.example.apptest.ui.AddSettings.UpiActivity;
import com.example.apptest.utils.model.FoodCart;
import com.example.apptest.viewmodel.CartViewModel;
import com.example.apptest.views.CartActivity;
import com.example.apptest.views.FoodListActivity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class PaymentActivity extends AppCompatActivity {
    private CartViewModel cartViewModel;
    private ImageView backtap;
    private CheckBox check;
    private TextView upi_id_show,address_show,price_show,confirm,test;
    private String upi_flag="",add_flag="",order_placed_flag="";
    public static ArrayList<FoodCart> order;
    public static String OrderPackage,randTime;
    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        db= new DbHelper(this);
        order=new ArrayList<FoodCart>();
        order_placed_flag="";
        upi_id_show=findViewById(R.id.UPI_id);
        if(getUPIData()==""){
            upi_flag="notPresent";
            upi_id_show.setText("To set up your UPI ID and Phone no click here");
            upi_id_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(new Intent(PaymentActivity.this, UpiActivity.class));
                }
            });
        }else{
            upi_flag="present";
            upi_id_show.setText(getUPIData());
        }

        address_show=findViewById(R.id.Address);
        if(getAddData()==""){
            add_flag="notPresent";
            address_show.setText("To set up your preferred delivery address click here");
            address_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(new Intent(PaymentActivity.this, AddingNewAddActivity.class));
                }
            });
        }else{
            add_flag="present";
            address_show.setText(getAddData());
        }

        price_show=findViewById(R.id.Price);
        price_show.setText("Total amount To pay: "+String.valueOf(cartTotal)+" Rs");
        backtap=findViewById(R.id.backtap);
        backtap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentActivity.this, CartActivity.class));
            }
        });
        check=findViewById(R.id.CheckBox);
        confirm=findViewById(R.id.confirmBtn);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(order_placed_flag.equals("prevOrder")){
                    Toast.makeText(getApplicationContext(),"Are you sure you want to place the same order again?",Toast.LENGTH_SHORT).show();
                }else{
                if(check.isChecked() && upi_flag.equals("present") && add_flag.equals("present")){
                    CartActivity.cartViewModel.deleteAllCartItems();
                    OrderPackage = String.valueOf(order);
                    String orderid = UUID.randomUUID().toString();
                    String dateTime="";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z");
                        dateTime= ZonedDateTime.now( ZoneId.of( "Asia/Calcutta" ) ).format(formatter);
                    }
                    for(FoodCart f: order){
                        addToOrderHis(f,orderid,dateTime);
                    }
                    randTime=randomTime();
                    AlertDialog.Builder builder=new AlertDialog.Builder(PaymentActivity.this);
                    builder.setCancelable(true);
                    builder.setIcon(R.drawable.correct);
                    builder.setTitle("Order Confirmed\nArriving in "+randTime+" mins");
                    builder.setInverseBackgroundForced(true);
                    builder.setNegativeButton("Close",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                            startActivity(new Intent(PaymentActivity.this, FoodListActivity.class));
                        }
                    });
                    AlertDialog alert=builder.create();
                    alert.show();
                    order_placed_flag="prevOrder";
                }else if(upi_flag.equals("notPresent") && add_flag.equals("notPresent")){
                    Toast.makeText(getApplicationContext(),"Please Add your address and payment details before proceeding",Toast.LENGTH_SHORT).show();
                }
                else if(upi_flag.equals("notPresent")){
                    Toast.makeText(getApplicationContext(),"Please Add your UPI Id before proceeding",Toast.LENGTH_SHORT).show();
                }else if(add_flag.equals("notPresent")){
                    Toast.makeText(getApplicationContext(),"Please Add address Id before proceeding",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Confirm order details by checking the box",Toast.LENGTH_SHORT).show();

                }
             }
            }
        });
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        cartViewModel.getAllCartItems().observe(this, new Observer<List<FoodCart>>() {
            @Override
            public void onChanged(List<FoodCart> foodCarts) {
                double price = 0;
                cartAdapter.setFoodCartList(foodCarts);


                for (FoodCart i:foodCarts){
                    order.add(i);
                }

                cartTotal=price;

            }
        });



    }
    private String getUPIData() {
        Cursor cursor = db.getAddTableData(DashNavActivity.userEmail);
        String UPIid = "";
        while (cursor.moveToNext()) {
            UPIid=cursor.getString(2);

        }
        return UPIid+"";

    }
    private String getAddData() {
        Cursor cursor = db.getAddTableData(DashNavActivity.userEmail);
        String address = "";
        while (cursor.moveToNext()) {
                address =cursor.getString(1) ;
        }
        return address+"";
    }
    public void addToOrderHis(FoodCart f, String orderid,String dateTime) {
        String userEmailTXT = DashNavActivity.userEmail;
        String foodnameTXT = f.getFoodName();
        String resNameTXT = f.getFoodBrandName();
        int imageInt = f.getFoodImage();
        String priceTXT = String.valueOf(f.getFoodPrice());
        String quantity =String.valueOf(f.getQuantity());
        if(userEmailTXT.equals("")|| foodnameTXT.equals("") ) {
            Toast.makeText(getApplicationContext(), "empty insertion", Toast.LENGTH_SHORT).show();
        }else {
            Boolean checkInsertData = db.insertOrderData(userEmailTXT,orderid,foodnameTXT,resNameTXT,imageInt,priceTXT,quantity,dateTime);
            if (checkInsertData == false) {
                Toast.makeText(getApplicationContext(), "Already added to favourites", Toast.LENGTH_SHORT).show();
            }

        }
    }
    @NonNull
    private static String randomTime(){
        ArrayList<Integer> timeList=new ArrayList<>(List.of(15,25,27,35,40,45));
        Random random= new Random();
        int time=timeList.get(random.nextInt(5));
        return String.valueOf(time);
    }

}