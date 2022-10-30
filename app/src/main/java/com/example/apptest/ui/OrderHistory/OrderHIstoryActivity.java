package com.example.apptest.ui.OrderHistory;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptest.DashNavActivity;
import com.example.apptest.R;
import com.example.apptest.database.DbHelper;
import com.example.apptest.utils.adapter.CartAdapter;
import com.example.apptest.utils.adapter.OrderAdapter;
import com.example.apptest.utils.model.FoodCart;
import com.example.apptest.utils.model.FoodItem;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHIstoryActivity extends AppCompatActivity implements CartAdapter.CartClickedListeners{
    private RecyclerView recyclerView;
    OrderAdapter adapter;
    private ImageView back;

    private  ArrayList<String> name;
    private  ArrayList<String> res;
    private  ArrayList<String> img;
    private  ArrayList<String> price;
    private  ArrayList<String> quant;
    private  ArrayList<String> keyList;
    public   ArrayList<String> date;
    public   ArrayList<String> datesend;

    DbHelper db;
    public  HashMap<String, ArrayList<FoodItem>> OrderDetails =new HashMap<>();
    public  ArrayList<String> orderUid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        db=new DbHelper(this);

        orderUid = new ArrayList<>();
        name=new ArrayList<>();
        res=new ArrayList<>();
        img=new ArrayList<>();
        price=new ArrayList<>();
        quant=new ArrayList<>();
        keyList=new ArrayList<>();
        date = new ArrayList<>();
        datesend = new ArrayList<>();

        getOrderData();
        parseData();
        recyclerView=findViewById(R.id.OrderRecyclerView);
        adapter=new OrderAdapter(this,name,res,img,price,quant,keyList,datesend);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back=findViewById(R.id.backtap);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(OrderHIstoryActivity.this, DashNavActivity.class));
            }
        });

    }

    private void getOrderData() {
        Cursor cursor = db.getOrderTableData(DashNavActivity.userEmail);
        if (cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(),"Your orderHistory seems to be empty", Toast.LENGTH_SHORT).show();
        }
            else{
            while (cursor.moveToNext()) {
                String orderid = cursor.getString(1);
                String fname = cursor.getString(2);
                String fresname = cursor.getString(3);
                int fimg = Integer.valueOf(cursor.getString(4));
                double fprice = Double.valueOf(cursor.getString(5));
                String fquant = cursor.getString(6);
                String dateHolder = cursor.getString(7);
                String[] s = dateParser(dateHolder);
                String odate = "Order Date:" + s[0] + " At: " + s[2];
                FoodItem f = new FoodItem(fname, fresname, fimg, fprice, fquant);
                if (OrderDetails.containsKey(orderid)) {
                    ArrayList<FoodItem> foodItems = OrderDetails.get(cursor.getString(1));
                    foodItems.add(f);
                    OrderDetails.put(cursor.getString(1), foodItems);

                } else {
                    ArrayList<FoodItem> foodItems = new ArrayList<>();
                    foodItems.add(f);
                    OrderDetails.put(cursor.getString(1), foodItems);
                    orderUid.add(orderid);
                    date.add(odate);
                }

            }


        return;
        }
    }
    private void parseData(){
        int i=0;
        double totalPrice=0;
        for(String key:orderUid){
            keyList.add("Order id:\n"+key);
            name.add("");
            res.add("");
            img.add(String.valueOf(R.drawable.ic_baseline_assignment_returned_24));
            price.add("");
            quant.add("");
            datesend.add(date.get(i));
            i++;
            for(FoodItem f :OrderDetails.get(key)){
                keyList.add("");
                name.add(f.getFoodName());
                res.add(f.getFoodBrandName());
                img.add(String.valueOf(f.getFoodImage()));
                price.add(String.valueOf(f.getFoodPrice()));
                quant.add("Quantity: "+f.getFoodDesc());
                datesend.add("");

                }

        }

    }

    @Override
    public void onDeleteClicked(FoodCart foodCart) {

    }

    @Override
    public void onPlusClicked(FoodCart foodCart) {

    }

    @Override
    public void onMinusClicked(FoodCart foodCart) {

    }
    private String[] dateParser(String s){
        //String.valueOf(date.get(position)))[0]+" "+dateParser(String.valueOf(date.get(position)))[2]
        return  s.split(" ");
    }
}