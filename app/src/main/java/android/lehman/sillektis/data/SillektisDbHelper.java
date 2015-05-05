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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.lehman.sillektis.data.SillektisContract.DebtorEntry;
import android.lehman.sillektis.data.SillektisContract.PaymentEntry;

/**
 * Manages a local database for debtor data.
 */
public class SillektisDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version manually.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "sillektis.db";

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
                PaymentEntry.COLUMN_DATE_PAYMENT + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + PaymentEntry.COLUMN_ID_DEBTOR + ") REFERENCES " +
                DebtorEntry.TABLE_NAME + " (" + DebtorEntry._ID + "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_DEBTOR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PAYMENT_TABLE);
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
}
