package com.gabrielgomarques.firebasetest.data.local.util;

/**
 * Created by Gabriel on 18/01/2017.
 */

public class Scripts {


    public static final String USER_TABLE_NAME = "TB_USER";

    public static final String USER_TABLE_COLUMN_USER_ID = "USER_ID";

    public static final String USER_TABLE_COLUMN_NAME = "NAME";

    public static final String USER_TABLE_COLUMN_INTRODUCTION = "INTRODUCTION";

    public static final String USER_TABLE_COLUMN_IMAGE_URL = "IMAGE_URL";

    public static final String USER_TABLE_COLUMN_EMAIL = "EMAIL";

    public static final String USER_TABLE_COLUMN_PASSWORD = "PASSWORD";

    public static final String CREATE_USER_TABLE_SCRIPT = "CREATE TABLE "+ USER_TABLE_NAME +
            " ( "+ USER_TABLE_COLUMN_USER_ID +" VARCHAR(500) ," +
            USER_TABLE_COLUMN_NAME + " VARCHAR(255) ," +
            USER_TABLE_COLUMN_INTRODUCTION + " VARCHAR(1000)," +
            USER_TABLE_COLUMN_IMAGE_URL + " VARCHAR(255) ," +
            USER_TABLE_COLUMN_EMAIL + " VARCHAR(255) ,"+
            USER_TABLE_COLUMN_PASSWORD + " VARCHAR(255) )";

}
