package com.example.apptest.ui.AddSettings;

import android.content.Intent;
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

public class AddingNewAddActivity extends AppCompatActivity {
    private EditText houseno,locality,area,city,pincode,state;
    private TextView updateAdd;
    private ImageView back;
    private String shouseno,slocality,sarea,scity,spincode,sstate;
    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_new_add);
        db=new DbHelper(this);
        houseno=findViewById(R.id.upiIdEnter);
        locality=findViewById(R.id.locality);
        area=findViewById(R.id.area);
        city=findViewById(R.id.city);
        pincode=findViewById(R.id.pincode);
        state=findViewById(R.id.state);

        back=findViewById(R.id.back_tap);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateAdd=findViewById(R.id.mod_address);
        updateAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shouseno=houseno.getText().toString();
                slocality=locality.getText().toString();
                sarea=area.getText().toString();
                scity=city.getText().toString();
                spincode=pincode.getText().toString();
                sstate=state.getText().toString();
                if(shouseno.equals("")|| slocality.equals("") || sarea.equals("") || scity.equals("")||spincode.equals("") || sstate.equals("")){
                    Toast.makeText(getApplicationContext(),"Please do not leave an empty field",Toast.LENGTH_SHORT).show();
                }
                else if(spincode.length()<6){
                    pincode.setError("Pincode must be 6 digits");
                }
                else if (shouseno.length()<1){
                    houseno.setError("House number can't be empty");
                }
                else if (slocality.length()<2){
                    locality.setError("Locality can't be 1 letter");
                }
                else if (sarea.length()<2){
                    area.setError("Area name can't be 1 letter");
                }
                else if (scity.length()<2){
                    city.setError("City name can't be 1 letter");
                }
                else if (sstate.length()<2){
                    state.setError("State name can't be 1 letter");
                }
                else{
                    String compiledAddress=shouseno+" "+slocality+" "+sarea+" "+scity+" "+spincode+" "+sstate;
                    Boolean checkAddUpdate= db.updateAddData(DashNavActivity.userEmail,compiledAddress);
                    if(checkAddUpdate){
                        Toast.makeText(getApplicationContext(), "Address Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddingNewAddActivity.this,AddSettingsActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(), "Please use the update Button Instead", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}