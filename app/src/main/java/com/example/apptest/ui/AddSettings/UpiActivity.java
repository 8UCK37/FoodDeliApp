package com.example.apptest.ui.AddSettings;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptest.DashNavActivity;
import com.example.apptest.R;
import com.example.apptest.database.DbHelper;

public class UpiActivity extends AppCompatActivity {
    private EditText upiEdit,phnEdit;
    private TextView  add;
    private ImageView back, dltUpi,dltPhn;
    private String upi_placeholder,phn;
    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi);
        db=new DbHelper(this);

        upiEdit =findViewById(R.id.upiIdEnter);
        phnEdit=findViewById(R.id.phnEnter);
        try{
            upi_placeholder =getUPIData();
            upiEdit.setText(upi_placeholder);
        }catch(Exception e){
            upiEdit.setText(upi_placeholder);
        }
        try{
            phn =getPhnData();
            phnEdit.setText(phn);
        }catch(Exception e){
            phnEdit.setText(phn);
        }

        back=findViewById(R.id.back_tap);

        add =findViewById(R.id.toTest);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpiActivity.this, DashNavActivity.class));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String upi= upiEdit.getText().toString();
                String phn=phnEdit.getText().toString();

                if(upi.equals("") && phn.equals("")){
                    Toast.makeText(getApplicationContext(),"Please do not leave empty fields",Toast.LENGTH_SHORT).show();
                }else{
                    Boolean checkUpi=db.updateUpiData(DashNavActivity.userEmail,upi,phn);
                    if(checkUpi){
                        Toast.makeText(getApplicationContext(), "New details added", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(), "Failed to Add UPI Id", Toast.LENGTH_SHORT).show();
                    }
                }
                finish(); startActivity(getIntent());//refreshes the activity leaving nothing behind
            }
        });
        dltUpi =findViewById(R.id.UpiDeleteBtn);
        dltUpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean checkdeletedata = db.deleteUpiData(DashNavActivity.userEmail);
                if(checkdeletedata){
                    Toast.makeText(getApplicationContext(), "Current UPI Id Deleted", Toast.LENGTH_SHORT).show();
                    finish(); startActivity(getIntent());//refreshes the activity leaving nothing behind
                }
                else{
                    Toast.makeText(getApplicationContext(), "Probably your didn't save your UPI Id yet ", Toast.LENGTH_SHORT).show();}
                finish(); startActivity(getIntent());//refreshes the activity leaving nothing behind
            }
        });
        dltPhn=findViewById(R.id.PhnDeleteBtn);
        dltPhn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean checkdeletedata = db.deletePhnData(DashNavActivity.userEmail);
                if(checkdeletedata){
                    Toast.makeText(getApplicationContext(), "Current Phone no Deleted", Toast.LENGTH_SHORT).show();
                    finish(); startActivity(getIntent());//refreshes the activity leaving nothing behind
                }
                else{
                    Toast.makeText(getApplicationContext(), "Probably your didn't save your Phone no yet ", Toast.LENGTH_SHORT).show();}
                finish(); startActivity(getIntent());//refreshes the activity leaving nothing behind
            }
        });

    }
    private String getUPIData() {
        Cursor cursor = db.getAddTableData(DashNavActivity.userEmail);
        String UPIid = null;
        while (cursor.moveToNext()) {
            UPIid = cursor.getString(2);
        }
        return UPIid;
    }
    private String getPhnData() {
        Cursor cursor = db.getAddTableData(DashNavActivity.userEmail);
        String UPIid = null;
        while (cursor.moveToNext()) {
            UPIid = cursor.getString(3);
        }
        return UPIid;
    }
}