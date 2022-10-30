package com.example.apptest;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptest.database.DbHelper;
import com.example.apptest.ui.AddSettings.AddingNewAddActivity;
import com.example.apptest.ui.AddSettings.UpiActivity;


public class AccountDetailsActivity extends AppCompatActivity {
    private TextView name,email,address,phone,upi;
    private ImageView backtap;

    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        db= new DbHelper(this);
        name=findViewById(R.id.UserName);
        email=findViewById(R.id.UserEmail);
        address=findViewById(R.id.UserAddress);
        phone=findViewById(R.id.UserPhone);
        upi=findViewById(R.id.UserUpi);
        backtap=findViewById(R.id.backtap);
        backtap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountDetailsActivity.this, DashNavActivity.class));
            }
        });
        name.setText(DashNavActivity.userName);
        email.setText(DashNavActivity.userEmail);
        if(getUPIData()==""){
            upi.setText("To set up your UPI ID click here");
            upi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(new Intent(AccountDetailsActivity.this, UpiActivity.class));
                }
            });
        }else{

            upi.setText(getUPIData());
        }


        if(getAddData()==""){

            address.setText("To set up your preferred delivery address click here");
            address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(new Intent(AccountDetailsActivity.this, AddingNewAddActivity.class));
                }
            });
        }else{

            address.setText(getAddData());
        }
        if(getPhnData()==""){

            phone.setText("To set up your phone no click here");
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(new Intent(AccountDetailsActivity.this, UpiActivity.class));
                }
            });
        }else{

            phone.setText(getPhnData());
        }


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
    private String getPhnData() {
        Cursor cursor = db.getAddTableData(DashNavActivity.userEmail);
        String phn = "";
        while (cursor.moveToNext()) {
            phn = cursor.getString(3);
        }
        return phn;
    }
}