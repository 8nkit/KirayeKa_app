package com.example.kirayeka.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;import com.example.kirayeka.DemoStructure.Wishlist;
import com.example.kirayeka.DemoStructure.RentReq;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;


public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "LocalRent.db";
    private static final int DB_VER = 2;
    private int countCart;

    public Database(Context context){
        super(context, DB_NAME,null, DB_VER);
    }


    public boolean checkItemExists(String itemId,String userPhone){
        boolean flag=false;
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=null;
        String SQLQuery=String.format("SELECT * FROM RentRequest WHERE PhoneNo='%s' AND ProductId='%s'",userPhone,itemId);
        cursor=db.rawQuery(SQLQuery,null);
        if(cursor.getCount()>0)
            flag=true;
        else
            flag= false;
        cursor.close();
        return flag;
    }
    public List<RentReq> getCarts(String userPhone){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"PhoneNo","ProductName", "ProductId", "Dayzz", "Rent", "Off5","Image"};
        String sqlTable = "RentRequest";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, "PhoneNo=?", new String[]{userPhone}, null, null, null);

        final List<RentReq> result = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                result.add(new RentReq(c.getString(c.getColumnIndex("PhoneNo")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Dayzz")),
                        c.getString(c.getColumnIndex("Rent")),
                        c.getString(c.getColumnIndex("Off5")),
                        c.getString(c.getColumnIndex("Image"))
                ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(RentReq order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO RentRequest(PhoneNo, ProductId, ProductName, Dayzz, Rent, Off5,Image) VALUES('%s','%s','%s','%s','%s','%s','%s');",
                order.getPhoneNo(),
                order.getProductId(),
                order.getProductName(),
                order.getDayss(),
                order.getRent(),
                order.getOff5(),
                order.getImage()
                );

        db.execSQL(query);
    }

//    public void removeFromCart(String order){

  //      SQLiteDatabase db = getReadableDatabase();

    //    String query = String.format("DELETE FROM RentRequest WHERE ProductId='"+order+"'");
      //  db.execSQL(query);
    //}
    public void removeFromCart(String productId,String phone)
    {
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format("DELETE FROM RentRequest WHERE PhoneNo='%s' and ProductId='%s'",phone,productId);
        db.execSQL(query);

    }

    public void cleanCart(String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM RentRequest WHERE PhoneNo='%s'",userPhone);
        db.execSQL(query);
    }
    public void addToFavorites(Wishlist item){
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("INSERT INTO Wishlist(ItemId,ItemName,ItemRent,ItemMenuId,ItemImage,ItemOff5,ItemDetails,PhoneNo) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s');"
                ,item.getItemId()
                ,item.getItemName()
                ,item.getItemRent()
                ,item.getItemMenuId()
                ,item.getItemImage()
                ,item.getItemOff5()
                ,"Best product 2"
                ,item.getPhoneNo()
                );
        db.execSQL(query);
    }

    public void removeFromFavorites(String itemId,String userPhone){
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("DELETE FROM Wishlist WHERE ItemId='%s';",itemId);
        db.execSQL(query);
    }

    public boolean isFavorites(String itemId,String userPhone){
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("SELECT * FROM Wishlist WHERE ItemId='%s';",itemId);
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.getCount()<=0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getCountCart(String userPhone) {
        int count =0;
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("SELECT COUNT(*) FROM RentRequest WHERE PhoneNo='%s'",userPhone);
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do{
                count=cursor.getInt(0);
            }while(cursor.moveToNext());
        }
        return count;
    }



    public List<Wishlist> getAllFavorites(String userPhone){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"PhoneNo","ItemId", "ItemName", "ItemRent", "ItemMenuId","ItemImage","ItemOff5","ItemDetails"};
        String sqlTable = "Wishlist";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, "PhoneNo=?", new String[]{userPhone}, null, null, null);

        final List<Wishlist> result = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                result.add(new Wishlist(
                        c.getString(c.getColumnIndex("ItemId")),
                        c.getString(c.getColumnIndex("ItemName")),
                        c.getString(c.getColumnIndex("ItemRent")),
                        c.getString(c.getColumnIndex("ItemMenuId")),
                        c.getString(c.getColumnIndex("ItemImage")),
                        c.getString(c.getColumnIndex("ItemOff5")),
                        c.getString(c.getColumnIndex("ItemDetails")),
                        c.getString(c.getColumnIndex("PhoneNo"))



                ));
            }while (c.moveToNext());
        }
        return result;
    }


    public void setCountCart(int countCart) {
        this.countCart = countCart;
    }
}
