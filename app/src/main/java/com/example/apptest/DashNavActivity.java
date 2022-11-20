package com.example.apptest;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.apptest.database.DbHelper;
import com.example.apptest.databinding.ActivityDashNavBinding;
import com.example.apptest.ui.AddSettings.AddSettingsActivity;
import com.example.apptest.ui.AddSettings.UpiActivity;
import com.example.apptest.ui.Favourites.FavouriteActivity;
import com.example.apptest.ui.OrderHistory.OrderHIstoryActivity;
import com.example.apptest.utils.model.FoodItem;
import com.example.apptest.views.CartActivity;
import com.example.apptest.views.MenuListActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class DashNavActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView cart, menuImg,userAcc;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView emailshow,nameshow,accDet;
    public static String userName, userEmail;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDashNavBinding binding;
    protected static List<FoodItem> foodItemList;
    protected static List<FoodItem> indian;
    protected static List<FoodItem> chinese;
    protected static List<FoodItem> snacks;
    public static int foodNo;
    DbHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db=new DbHelper(this);
        foodItemList = new ArrayList<>();
        indian = new ArrayList<>();
        chinese = new ArrayList<>();
        snacks = new ArrayList<>();
        setUpList();
        try {
            setUpFoodDb();
        } catch (Exception e) {
            e.printStackTrace();
        }


        setSupportActionBar(binding.appBarDashNav.toolbar);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            userName = acct.getDisplayName();
            userEmail = acct.getEmail();
        }

        cart=findViewById(R.id.food_cart);
        menuImg =findViewById(R.id.menu);

        tabLayout = findViewById(R.id.food_tab);
        viewPager = findViewById(R.id.food_viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("Indian"));      //0
        tabLayout.addTab(tabLayout.newTab().setText("Chinese"));    //1
        tabLayout.addTab(tabLayout.newTab().setText("Snacks"));    //2
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final FoodTabAdapter adapter = new FoodTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.Address_Settings, R.id.OrderHis, R.id.logoutBtn)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dash_nav);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashNavActivity.this, CartActivity.class));
            }
        });
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashNavActivity.this, MenuListActivity.class));
            }
        });
        //setupListByType();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_nav, menu);
        nameshow=findViewById(R.id.name);
        nameshow.setText(userName);
        emailshow=findViewById(R.id.email);
        emailshow.setText(userEmail);
        userAcc=findViewById(R.id.user_imageView);
        userAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashNavActivity.this, AccountDetailsActivity.class));
            }
        });
        accDet=findViewById(R.id.Acc_details);
        accDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashNavActivity.this, AccountDetailsActivity.class));
            }
        });
        findViewById(R.id.Address_Settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashNavActivity.this, AddSettingsActivity.class));
            }
        });
        findViewById(R.id.OrderHis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashNavActivity.this, OrderHIstoryActivity.class));
            }
        });
        findViewById(R.id.favourites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashNavActivity.this, FavouriteActivity.class));
            }
        });
        findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {//bottom button
            @Override
            public void onClick(View view) {
                signout();
            }
        });
        findViewById(R.id.upiBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashNavActivity.this, UpiActivity.class));
            }
        });
        return true;
    }
    private void setUpList() {
        foodItemList.add(new FoodItem("Chicken Dum Biriyani ", "Restaurant5", R.drawable.biriyani, 150,"Authentic kolkata style Dum-Biriyani","i"));
        foodItemList.add(new FoodItem("Momo", "Restaurant3", R.drawable.momo, 100,"Steamed Momo","c"));
        foodItemList.add(new FoodItem("Pizza", "Restaurant1", R.drawable.pizza, 350,"Pizza with Mozzarella cheese and salami toppings","s"));
        foodItemList.add(new FoodItem("Chicken Kathi Roll", "Restaurant4", R.drawable.chicken_kathi_roll, 85,"Roll with Check Kathi Kebab as Fillings","s"));
        foodItemList.add(new FoodItem("Fried Rice", "Restaurant4", R.drawable.friedrice, 120,"Indian style Fried Rice/Pulao","c"));
        foodItemList.add(new FoodItem("Chicken Kebab", "Restaurant2", R.drawable.chicken_kebab, 220,"Grilled Chicken Kebab","i"));
        foodItemList.add(new FoodItem("Chilli Chicken", "Restaurant3", R.drawable.chilli_chicken, 120,"Semi gravy Chilli Chicken","c"));
        foodItemList.add(new FoodItem("Hakka Noodles", "Restaurant5", R.drawable.hakka_noodles, 150,"Veg/mixed hakka noodles","c"));
        foodItemList.add(new FoodItem("Burger", "Restaurant3", R.drawable.burger, 90,"Chicken Burger","s"));
        foodItemList.add(new FoodItem("Mutton Chaap", "Restaurant5", R.drawable.mutton_chaap, 220,"Mutton Chaap","i"));
        foodItemList.add(new FoodItem("Tandoori Chicken", "Restaurant4", R.drawable.tandoori_chicken, 250,"Chicken Tandoori","i"));
        foodItemList.add(new FoodItem("Fish Cutlet", "Restaurant3", R.drawable.fish_cutlet, 120,"Fish Cutlet","s"));
        foodItemList.add(new FoodItem("Masala Dosa", "Restaurant1", R.drawable.masala_dosa, 95,"Masala Dosa","i"));
        foodItemList.add(new FoodItem("Chicken Spring Roll", "Restaurant2", R.drawable.spring_roll, 65,"Chicken Spring Roll","s"));
        foodItemList.add(new FoodItem("Chicken Shawarma", "Restaurant1", R.drawable.chicken_shawarma, 95,"Chicken Shawarma","s"));
        foodItemList.add(new FoodItem("Chicken Sandwich", "Restaurant2", R.drawable.chicken_sandwich, 55,"Chicken Sandwich","s"));
        foodItemList.add(new FoodItem("Italian Pasta", "Restaurant6", R.drawable.pasta, 120,"Authentic Italian Style Pasta with mariana pasta sauce and Parmesan cheese","s"));
        foodItemList.add(new FoodItem("White sauce Pasta", "Restaurant6", R.drawable.white_sauce_pasta, 120,"Authentic Italian Style Pasta with Bechamel sauce and Parmesan cheese","s"));
    }
    private void setUpFoodDb() throws Exception {
        int id=0;
        for( FoodItem i : foodItemList){
            id++;
            if(i.getType().equals("i")){
                indian.add(new FoodItem(i.getFoodName(),i.getFoodBrandName(),i.getFoodImage(),i.getFoodPrice(),i.getFoodDesc(),i.getType()));
            }
            else if(i.getType().equals("c")){
                chinese.add(new FoodItem(i.getFoodName(),i.getFoodBrandName(),i.getFoodImage(),i.getFoodPrice(),i.getFoodDesc(),i.getType()));
            }else if(i.getType().equals("s")){
                snacks.add(new FoodItem(i.getFoodName(),i.getFoodBrandName(),i.getFoodImage(),i.getFoodPrice(),i.getFoodDesc(),i.getType()));
            }
            Boolean checkFoodInsertData=db.insertFoodData(String.valueOf(id),i.getFoodName(),i.getFoodBrandName(),i.getFoodImage(),String.valueOf(i.getFoodPrice()),i.getFoodDesc(),i.getType());
            if(checkFoodInsertData){
                continue;
            }else{
                throw new Exception("FoodItemTable Insert Error for i= "+id);
            }

        }
        foodNo = id;
    }
    private void setupListByType(){
        Cursor Indcursor = db.getFoodDataByType("i");
        while (Indcursor.moveToNext()) {
             indian.add(new FoodItem(Indcursor.getString(1),Indcursor.getString(2),Indcursor.getInt(3),Indcursor.getDouble(4),Indcursor.getString(5),Indcursor.getString(6)));
        }
        Cursor chnCursor = db.getFoodDataByType("c");
        while (chnCursor.moveToNext()) {
            chinese.add(new FoodItem(chnCursor.getString(1),chnCursor.getString(2),chnCursor.getInt(3),chnCursor.getDouble(4),chnCursor.getString(5),chnCursor.getString(6)));
        }
        Cursor snacksCursor = db.getFoodDataByType("s");
        while (snacksCursor.moveToNext()) {
            snacks.add(new FoodItem(snacksCursor.getString(1),snacksCursor.getString(2),snacksCursor.getInt(3),snacksCursor.getDouble(4),snacksCursor.getString(5),snacksCursor.getString(6)));
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dash_nav);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    void signout(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(DashNavActivity.this,LoginActivity.class));
            }
        });
    }

}