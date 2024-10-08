package com.example.des808.my_tcp_ip_client;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBChat  extends SQLiteOpenHelper
        implements BaseColumns {

    public static final String DB_NAME = "chat.db";
    public static final String TABLE_NAME = "mesages";
    public static final String MESSAGE_IN = "messageIn";
    public static final String MESSAGE_OUT = "messageOut";
    public static final String DATA_IN_MESSAGE = "dataInMessage";
    public static final String DATA_OUT_MESSAGE = "dataOutMessage";
    private Context context;




    public DBChat(Context context) {
        // конструктор суперкласса
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу с полями
        db.execSQL("CREATE TABLE " + TABLE_NAME
                + "(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MESSAGE_IN        + " TEXT,"
                + DATA_IN_MESSAGE   + " TEXT,"
                + MESSAGE_OUT       + " TEXT,"
                + DATA_OUT_MESSAGE  + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( " DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate( db );
    }
}
