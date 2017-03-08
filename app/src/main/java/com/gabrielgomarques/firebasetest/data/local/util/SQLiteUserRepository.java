package com.gabrielgomarques.firebasetest.data.local.util;

import static com.gabrielgomarques.firebasetest.data.local.util.Scripts.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.gabrielgomarques.firebasetest.enitities.User;

/**
 * Created by Gabriel on 18/01/2017.
 */

public class SQLiteUserRepository extends SQLiteRepository {

    public SQLiteUserRepository(Context context) {
        super(context);
    }

    public void deleteUser() throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(USER_TABLE_NAME, null, null);
    }

    public void saveUser(User user) throws Exception {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(USER_TABLE_COLUMN_USER_ID, user.getUserId());
        values.put(USER_TABLE_COLUMN_NAME, user.getName());
        values.put(USER_TABLE_COLUMN_INTRODUCTION, user.getIntroduction());
        values.put(USER_TABLE_COLUMN_IMAGE_URL, user.getImageUrl());
        values.put(USER_TABLE_COLUMN_EMAIL, user.getEmail());
        values.put(USER_TABLE_COLUMN_PASSWORD, user.getPassword());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(USER_TABLE_NAME, null, values);
    }

    public void updateUser(User user) throws Exception {

        // Gets the data repository in write mode
        SQLiteDatabase db = getReadableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(USER_TABLE_COLUMN_USER_ID, user.getUserId());
        values.put(USER_TABLE_COLUMN_NAME, user.getName());
        values.put(USER_TABLE_COLUMN_INTRODUCTION, user.getIntroduction());
        values.put(USER_TABLE_COLUMN_IMAGE_URL, user.getImageUrl());
        values.put(USER_TABLE_COLUMN_EMAIL, user.getEmail());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.update(USER_TABLE_NAME, values, null, null);
    }

    public User getUser() throws Exception {

        SQLiteDatabase db = getReadableDatabase();
            User user = null;

            Cursor cursor = db.query(
                    Scripts.USER_TABLE_NAME,
                    new String[]{
                            USER_TABLE_COLUMN_USER_ID,
                            USER_TABLE_COLUMN_NAME,
                            USER_TABLE_COLUMN_INTRODUCTION,
                            USER_TABLE_COLUMN_IMAGE_URL,
                            USER_TABLE_COLUMN_EMAIL,
                            USER_TABLE_COLUMN_PASSWORD},
                    null, null, null, null, null);


            if (cursor.moveToFirst()) {
                user = new User();
                user.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(USER_TABLE_COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(USER_TABLE_COLUMN_NAME)));
                user.setIntroduction(cursor.getString(cursor.getColumnIndexOrThrow(USER_TABLE_COLUMN_INTRODUCTION)));
                user.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(USER_TABLE_COLUMN_IMAGE_URL)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(USER_TABLE_COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(USER_TABLE_COLUMN_PASSWORD)));
            }


        return user;
    }
}
