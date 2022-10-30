package com.example.apptest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "UserAccDetails";




    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table Userfavs(email TXT ,foodname TXT primary key,resName TXT, img int, price TXT,description TXT )");
        db.execSQL("create Table UserDetails(email TXT primary key ,address TXT,upiId TXT,phn TXT )");
        db.execSQL("create Table FoodItems(id TXT primary key,foodname TXT ,resName TXT, img int, price TXT,description TXT )");
        db.execSQL("create Table OrderHis(email TXT ,orderid TXT ,foodName TXT,resName TXT, img int, price TXT,quantity TXT,time TXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists Userfavs");
        sqLiteDatabase.execSQL("drop Table if exists UserDetails");
        sqLiteDatabase.execSQL("drop Table if exists FoodItems");
        sqLiteDatabase.execSQL("drop Table if exists OrderHis");
        onCreate(sqLiteDatabase);
    }

    //insert primary data of user
    public Boolean insertFavData(String email,String foodname,String resName,int img,String price,String description)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
            contentValues.put("email", email);
            contentValues.put("foodname",foodname );
            contentValues.put("resname",resName );
            contentValues.put("img",img );
            contentValues.put("price",price );
            contentValues.put("description",description );

        long result=DB.insert("Userfavs", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean deleteFavdata(String fname)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userfavs where foodname = ?", new String[]{fname});
        if (cursor.getCount() > 0) {
            long result = DB.delete("Userfavs", "foodname=?", new String[]{fname});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Cursor getFavdata(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Userfavs where email=?", new String[]{id});
        return cursor;
    }

    public Boolean insertAddData(String email,String homeAdd,String upiId,String phn)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("address",homeAdd );
        contentValues.put("upiId",upiId );
        contentValues.put("phn",phn );
        long result=DB.insert("UserDetails", null, contentValues);
            if (result == -1) {
                return false;
            } else {
                return true;
            }

    }

    public Boolean updateAddData(String email,String homeAdd)
    {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("Select * from UserDetails where email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("email", email);
            contentValues.put("address",homeAdd );
            long result = DB.update("UserDetails", contentValues, "email=?", new String[]{email});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else if (cursor.getCount() == 0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("email", email);
            contentValues.put("address",homeAdd);
            contentValues.put("upiId","");
            contentValues.put("phn","");
            long result = DB.insert("UserDetails",null, contentValues);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
        else {
            return false;
        }
    }
    public Boolean updateUpiData(String email,String upiId,String phn) {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("Select * from UserDetails where email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("email", email);
            contentValues.put("upiId",upiId );
            contentValues.put("phn",phn );
            long result = DB.update("UserDetails", contentValues, "email=?", new String[]{email});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else if (cursor.getCount() == 0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("email", email);
            contentValues.put("address","");
            contentValues.put("upiId",upiId );
            contentValues.put("phn","");
            long result = DB.insert("UserDetails",null, contentValues);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
        else {
            return false;
        }
    }
    public Boolean deleteAddData(String email)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from UserDetails where email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("address","");
            long result = DB.update("UserDetails",contentValues,"email=?", new String[]{email});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Boolean deleteUpiData(String email)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from UserDetails where email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("upiId","");
            long result = DB.update("UserDetails",contentValues,"email=?", new String[]{email});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Boolean deletePhnData(String email)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from UserDetails where email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("phn","");
            long result = DB.update("UserDetails",contentValues,"email=?", new String[]{email});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Cursor getAddTableData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from UserDetails where email=?", new String[]{id});
        return cursor;
    }
    public Cursor getOrderTableData(String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderHis where email=?", new String[]{email});
        return cursor;
    }
    public Boolean insertFoodData(String id,String foodname,String resName,int img,String price,String description)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("Select * from FoodItems where id = ?", new String[]{id});
        if(cursor.getCount()>0){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id );
        contentValues.put("foodname",foodname );
        contentValues.put("resname",resName );
        contentValues.put("img",img );
        contentValues.put("price",price );
        contentValues.put("description",description );

            long result = DB.update("FoodItems",contentValues,"id=?", new String[]{id});
        if(result==-1){
            return false;
        }else{
            return true;
        }
        }else if(cursor.getCount() == 0){

                ContentValues contentValues = new ContentValues();
                contentValues.put("id",id );
                contentValues.put("foodname",foodname );
                contentValues.put("resname",resName );
                contentValues.put("img",img );
                contentValues.put("price",price );
                contentValues.put("description",description );

                long result=DB.insert("FoodItems", null, contentValues);
                if(result==-1){
                    return false;
                }else{
                    return true;
                }
        }else{
            return false;
        }
    }

    public Boolean insertOrderData(String email,String oid,String foodname,String resName,int img,String price,String quantitiy,String dateTime)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("orderid", oid);
        contentValues.put("foodname",foodname );
        contentValues.put("resname",resName );
        contentValues.put("img",img );
        contentValues.put("price",price );
        contentValues.put("quantity",quantitiy );
        contentValues.put("time",dateTime );
        long result=DB.insert("OrderHis", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Cursor getFoodData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from FoodItems where id=?", new String[]{id});
        return cursor;
    }
}
