package com.techta.databaseapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    private static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    private static final String COLUMN_CUSTOMER_PG = "CUSTOMER_PURCHASED_GOODS";
    private static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
    private static final String COLUMN_ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "customers.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_PG + " TEXT, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addItem(CustomerModel customerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
        cv.put(COLUMN_CUSTOMER_PG, customerModel.getPurchasedGoods());
        cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());

        long insert = db.insert(CUSTOMER_TABLE, null, cv);

        return insert != -1;
    }

    public boolean deleteItem(CustomerModel customerModel) {

        SQLiteDatabase database = getWritableDatabase();
        String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_ID + " = " + customerModel.getId();

        Cursor cursor = database.rawQuery(queryString, null);

        return cursor.moveToFirst();
    }

    public ArrayList<CustomerModel> getEveryone() {

        ArrayList<CustomerModel> returnList = new ArrayList<>();

        //get data from database

        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int customerID = cursor.getInt(0);
                String customerName = cursor.getString(1);
                String customerPurchasedGoods = cursor.getString(2);
                boolean customerActive = cursor.getInt(3) == 1;

                CustomerModel newCustomer = new CustomerModel(customerID, customerName, customerPurchasedGoods, customerActive);
                returnList.add(newCustomer);

            } while (cursor.moveToNext());

        } else {
            //list is empty
        }

        cursor.close();
        db.close();
        return returnList;
    }
}
