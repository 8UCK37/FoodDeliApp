package com.example.apptest.ui.AddSettings;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apptest.DashNavActivity;
import com.example.apptest.R;
import com.example.apptest.database.DbHelper;
import com.example.apptest.views.CartActivity;


public class AddSettingsActivity extends AppCompatActivity {
    private ImageView back,cart,plus,dlt;
    private  TextView add,add_add;
    private String addstring="We don't have your address yet!!";
    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new DbHelper(this);
        setContentView(R.layout.activity_add_settings);
        add=findViewById(R.id.Address);
        try{
            addstring=getAddData();
            add.setText(addstring);
        }catch(Exception e){
            add.setText(addstring);
        }
        add=findViewById(R.id.Address);
        back=findViewById(R.id.backtap);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddSettingsActivity.this, DashNavActivity.class));
            }
        });
        cart=findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddSettingsActivity.this, CartActivity.class));
            }
        });
        plus=findViewById(R.id.addnew);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddSettingsActivity.this, AddingNewAddActivity.class));
            }
        });
        add_add=findViewById(R.id.title1);
        add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddSettingsActivity.this, AddingNewAddActivity.class));
            }
        });
        dlt=findViewById(R.id.UpiDeleteBtn);
        dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean checkdeletedata = db.deleteAddData(DashNavActivity.userEmail);
                if(checkdeletedata){
                    Toast.makeText(getApplicationContext(), "Current Address Deleted", Toast.LENGTH_SHORT).show();
                    finish(); startActivity(getIntent());//refreshes the activity leaving nothing behind
                }
                else
                    Toast.makeText(getApplicationContext(), "Probably your didn't save your address yet ", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private String getAddData() {
        Cursor cursor = db.getAddTableData(DashNavActivity.userEmail);
        String address = null;
        while (cursor.moveToNext()) {
            address = cursor.getString(1);
        }
        return address;
    }
}