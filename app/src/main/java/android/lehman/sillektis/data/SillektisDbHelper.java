/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.lehman.sillektis.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteQueryBuilder;
import android.lehman.sillektis.data.SillektisContract.DebtorEntry;
import android.lehman.sillektis.data.SillektisContract.PaymentEntry;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages a local database for debtor data.
 */
public class SillektisDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version manually.
    private static final int DATABASE_VERSION = 3; //sqlite3

    static final String DATABASE_NAME = "sillektis.db";

    private static final String PAYMENT = "payment";
    private static final String DEBTOR = "debtor";

    public SillektisDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //creating table Debtor
        final String SQL_CREATE_DEBTOR_TABLE = "CREATE TABLE " + DebtorEntry.TABLE_NAME + " (" +
                DebtorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                DebtorEntry.COLUMN_NAME_DEBTOR + " TEXT NOT NULL, " +
                DebtorEntry.COLUMN_EMAIL_DEBTOR + " TEXT NOT NULL, " +
                DebtorEntry.COLUMN_PHONE_DEBTOR + " TEXT NOT NULL, " +

                DebtorEntry.COLUMN_DATE_DEBTOR + " TEXT NOT NULL, " +
                DebtorEntry.COLUMN_AMOUNT_DEBTOR + " REAL NOT NULL, " +

                DebtorEntry.COLUMN_PAID_DEBTOR + " INTEGER NOT NULL);";

        //creating table Payment
        final String SQL_CREATE_PAYMENT_TABLE = "CREATE TABLE " + PaymentEntry.TABLE_NAME + " (" +
                PaymentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                PaymentEntry.COLUMN_AMOUNT_PAYMENT + " REAL NOT NULL, " +
                PaymentEntry.COLUMN_DATE_PAYMENT + " TEXT NOT NULL); ";

//                +" FOREIGN KEY (" + PaymentEntry.COLUMN_ID_DEBTOR + ") REFERENCES " +
//                DebtorEntry.TABLE_NAME + "(" + DebtorEntry._ID + "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_DEBTOR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PAYMENT_TABLE);

        /*final SQLiteQueryBuilder sPaymentByDebtorSettingQueryBuilder;
        sPaymentByDebtorSettingQueryBuilder = new SQLiteQueryBuilder();
        sPaymentByDebtorSettingQueryBuilder.setTables(
            SillektisContract.PaymentEntry.TABLE_NAME + " INNER JOIN " +
                SillektisContract.DebtorEntry.TABLE_NAME +
                " ON " + SillektisContract.PaymentEntry.TABLE_NAME +
                "." + SillektisContract.PaymentEntry.COLUMN_ID_DEBTOR +
                " = " + SillektisContract.DebtorEntry.TABLE_NAME +
                "." + SillektisContract.DebtorEntry._ID);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /*
         * Dropping values because until official version
         */
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DebtorEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PaymentEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insert(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        String table = queryValues.get("table");

        switch (table) {
            case PAYMENT:
                values.put(PaymentEntry.COLUMN_AMOUNT_PAYMENT, queryValues.get(DebtorEntry.COLUMN_NAME_DEBTOR));
                values.put(PaymentEntry.COLUMN_DATE_PAYMENT, queryValues.get(DebtorEntry.COLUMN_AMOUNT_DEBTOR));
                values.put(PaymentEntry.COLUMN_ID_DEBTOR, Double.valueOf(queryValues.get(DebtorEntry.COLUMN_EMAIL_DEBTOR)));

                break;
            case DEBTOR:
                values.put(DebtorEntry.COLUMN_NAME_DEBTOR, queryValues.get(DebtorEntry.COLUMN_NAME_DEBTOR));
                values.put(DebtorEntry.COLUMN_AMOUNT_DEBTOR, queryValues.get(DebtorEntry.COLUMN_AMOUNT_DEBTOR));
                values.put(DebtorEntry.COLUMN_EMAIL_DEBTOR, queryValues.get(DebtorEntry.COLUMN_EMAIL_DEBTOR));
                values.put(DebtorEntry.COLUMN_PHONE_DEBTOR, queryValues.get(DebtorEntry.COLUMN_PHONE_DEBTOR));
                values.put(DebtorEntry.COLUMN_DATE_DEBTOR, queryValues.get(DebtorEntry.COLUMN_DATE_DEBTOR));
                values.put(DebtorEntry.COLUMN_PAID_DEBTOR, queryValues.get(DebtorEntry.COLUMN_PAID_DEBTOR));
                break;
        }

        database.insert(table, null, values);
        database.close();
    }

    /*public void delete(HashMap<String, String> queryValues) {
        final SQLiteDatabase database = this.getWritableDatabase();

//                rows = db.delete(SillektisContract.PaymentEntry.TABLE_NAME, selection, selectionArgs);
        String deleteQuery = "DELETE FROM "+ DEBTOR +" WHERE _ID='" + queryValues.get("id") + "';"
                + "DELETE FROM " + PAYMENT + " WHERE DEBTOR_ID='"+ queryValues.get("id") + "'";

        database.execSQL(deleteQuery);
        database.close();
    }*/

    public ArrayList<HashMap<String, String>> selectAllDebtors(){
        ArrayList<HashMap<String, String>> debtorsArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM "+ DEBTOR +" WHERE "+ DebtorEntry.COLUMN_PAID_DEBTOR +"=0 ORDER BY "+ DebtorEntry._ID;

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                HashMap<String, String> contactMap = new HashMap<String, String>();

                contactMap.put(DebtorEntry._ID, cursor.getString(0));
                contactMap.put(DebtorEntry.COLUMN_NAME_DEBTOR, cursor.getString(1));
                contactMap.put(DebtorEntry.COLUMN_AMOUNT_DEBTOR, cursor.getString(2));
                contactMap.put(DebtorEntry.COLUMN_EMAIL_DEBTOR, cursor.getString(3));
                contactMap.put(DebtorEntry.COLUMN_PHONE_DEBTOR, cursor.getString(4));
                contactMap.put(DebtorEntry.COLUMN_DATE_DEBTOR, cursor.getString(5));

                debtorsArrayList.add(contactMap);

            } while(cursor.moveToNext());

        }

        return debtorsArrayList;
    }

//    public int updateContact(HashMap<String, String> queryValues){
//
//        SQLiteDatabase database = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put(DebtorEntry.COLUMN_NAME_DEBTOR, queryValues.get(DebtorEntry.COLUMN_NAME_DEBTOR));
//        values.put(DebtorEntry.COLUMN_AMOUNT_DEBTOR, queryValues.get(DebtorEntry.COLUMN_AMOUNT_DEBTOR));
////        values.put(DebtorEntry.COLUMN_EMAIL_DEBTOR, queryValues.get(DebtorEntry.COLUMN_EMAIL_DEBTOR));
////        values.put(DebtorEntry.COLUMN_PHONE_DEBTOR, queryValues.get(DebtorEntry.COLUMN_PHONE_DEBTOR));
////        values.put(DebtorEntry.COLUMN_DATE_DEBTOR, queryValues.get(DebtorEntry.COLUMN_DATE_DEBTOR));
////        values.put(DebtorEntry.COLUMN_PAID_DEBTOR, queryValues.get(DebtorEntry.COLUMN_PAID_DEBTOR));
//
//        String updateQuery = "UPDATE debtor SET "+ DebtorEntry.COLUMN_AMOUNT_DEBTOR +"= ";
//
//        database.execSQL(updateQuery);
//        database.close();
//
//    }
}
