package com.example.apptest.ui.AddSettings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptest.DashNavActivity;
import com.example.apptest.R;
import com.example.apptest.database.DbHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddingNewAddActivity extends AppCompatActivity {
    private EditText houseno,locality,area,city,pincode,state;
    private TextView updateAdd;
    private ImageView back;
    private String shouseno,slocality,sarea,scity,spincode,sstate;
    DbHelper db;

    String[] items =  {"Material","Design","Components","Android","5.0 Lollipop"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    Pattern regex1 = Pattern.compile("[^\\w\\/,. ]");
    Pattern regex2 = Pattern.compile("[^A-Za-z ]");
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

        /*autoCompleteTxt = findViewById(R.id.auto_complete_txt);

        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });*/

        updateAdd=findViewById(R.id.mod_address);
        updateAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shouseno=houseno.getText().toString();
                Matcher housenomtchr = regex1.matcher(shouseno);
                slocality=locality.getText().toString();
                Matcher localitymtchr = regex1.matcher(slocality);
                sarea=area.getText().toString();
                Matcher areamtchr = regex2.matcher(sarea);
                scity=city.getText().toString();
                Matcher citymtchr = regex2.matcher(scity);
                spincode=pincode.getText().toString();
                sstate=state.getText().toString();
                Matcher statemtchr = regex2.matcher(sstate);
                if(shouseno.equals("")|| slocality.equals("") || sarea.equals("") || scity.equals("")||spincode.equals("") || sstate.equals("")){
                    Toast.makeText(getApplicationContext(),"Please do not leave an empty field",Toast.LENGTH_SHORT).show();
                }
                else if (shouseno.length()<1){
                    houseno.setError("House number can't be empty");
                }
                else if(housenomtchr.find()) {
                    houseno.setError("House number can't contain any special characters other than /");
                }
                else if (slocality.length()<2){
                    locality.setError("Locality can't be 1 letter");
                }
                else if(localitymtchr.find()) {
                    locality.setError("Locality can't contain any special characters other than / , .");
                }
                else if (sarea.length()<2){
                    area.setError("Area name can't be 1 letter");
                }
                else if(areamtchr.find()) {
                    area.setError("Area can't contain any special characters or numbers");
                }
                else if (scity.length()<2){
                    city.setError("City name can't be 1 letter");
                }
                else if(citymtchr.find()) {
                    city.setError("Name of the city can't contain any special characters or numbers");
                }
                else if (state.length()<2){
                    state.setError("State name can't be 1 letter");
                }
                else if(statemtchr.find()) {
                    city.setError("Name of the state can't contain any special characters or numbers");
                }
                else if(spincode.length()<6){
                    pincode.setError("Pincode must be 6 digits");
                }
                else{
                    String compiledAddress = shouseno+" "+slocality+" "+sarea+" "+scity+" "+spincode+" "+sstate;
                    Boolean checkAddUpdate = db.updateAddData(DashNavActivity.userEmail,compiledAddress);
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