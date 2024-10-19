package com.example.des808.my_tcp_ip_client;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBChatAdapter {

    private DBChatHelper dbChatHelper;
    private SQLiteDatabase db;


    public DBChatAdapter(Context con){
        dbChatHelper = new DBChatHelper(con.getApplicationContext());

    }

    public DBChatAdapter open(){
        db = dbChatHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbChatHelper.close();
    }


    /*//открытие БД
    public void openBd(){db = dbChatHelper.getWritableDatabase();}

    //закрытие БД
    public void closeBd(){db.close();}*/


    private Cursor getAllEntries(String TableName){
        String[] columns = new String[] {DBChatHelper.MESSAGE_ID, DBChatHelper.MESSAGE_IN, DBChatHelper.DATA_IN_MESSAGE, DBChatHelper.MESSAGE_OUT, DBChatHelper.DATA_OUT_MESSAGE};
        return  db.query(TableName, columns, null, null, null, null, null);
    }

    public List<Chat> getMessages(String TableName) {
        ArrayList<Chat> messages = new ArrayList<>();
        Cursor cursor = getAllEntries(TableName);
        while (cursor.moveToNext()){
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DBChatHelper.MESSAGE_ID));
            @SuppressLint("Range") String msg_in = cursor.getString(cursor.getColumnIndex(DBChatHelper.MESSAGE_IN));
            @SuppressLint("Range") String data_msg_in = cursor.getString(cursor.getColumnIndex(DBChatHelper.DATA_IN_MESSAGE));
            @SuppressLint("Range") String msg_out = cursor.getString(cursor.getColumnIndex(DBChatHelper.MESSAGE_OUT));
            @SuppressLint("Range") String data_msg_out = cursor.getString(cursor.getColumnIndex(DBChatHelper.DATA_OUT_MESSAGE));
            messages.add(new Chat(id, msg_in, data_msg_in, msg_out, data_msg_out));
        }
        cursor.close();
        return messages;
    }

    public long getCount(String TableName){
        return DatabaseUtils.queryNumEntries(db, TableName);
    }

    public Chat getMessage(String TableName,long id){
        Chat chat = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",TableName, DBChatHelper._ID);
        Cursor cursor = db.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            @SuppressLint("Range") String msg_in = cursor.getString(cursor.getColumnIndex(DBChatHelper.MESSAGE_IN));
            @SuppressLint("Range") String data_msg_in = cursor.getString(cursor.getColumnIndex(DBChatHelper.DATA_IN_MESSAGE));
            @SuppressLint("Range") String msg_out = cursor.getString(cursor.getColumnIndex(DBChatHelper.MESSAGE_OUT));
            @SuppressLint("Range") String data_msg_out = cursor.getString(cursor.getColumnIndex(DBChatHelper.DATA_OUT_MESSAGE));
            chat = new Chat( msg_in, data_msg_in, msg_out, data_msg_out);
        }
        cursor.close();
        return  chat;
    }

    // Добавление нового сообщения
    public long addMessage(String TableName,Chat  msg_in) {
        ContentValues values = new ContentValues();
        values.put(DBChatHelper.MESSAGE_IN, msg_in.getMessage_in());
        values.put(DBChatHelper.DATA_IN_MESSAGE, msg_in.getData_in_message());
        values.put(DBChatHelper.MESSAGE_OUT, msg_in.getMessage_out());
        values.put(DBChatHelper.DATA_OUT_MESSAGE, msg_in.getData_out_message());

        long res = db.insert(TableName, null, values);
        return res;
    }

    // Удаление сообщения
    public int deleteMessage(String TableName,int id) {
        String where = String.format("%s=%d", DBChatHelper._ID, id);
        int res = db.delete(TableName, where, null);
        return res;
    }

    // Редактирование сообщения
    public int updateMessage(String TableName,int id, Chat msg_in) {;
        ContentValues values = new ContentValues();
        values.put(DBChatHelper.MESSAGE_IN, msg_in.getMessage_in());
        values.put(DBChatHelper.DATA_IN_MESSAGE, msg_in.getData_in_message());
        values.put(DBChatHelper.MESSAGE_OUT, msg_in.getMessage_out());
        values.put(DBChatHelper.DATA_OUT_MESSAGE, msg_in.getData_out_message());

        String where = String.format("%s=%d", DBChatHelper._ID, id);
        int res = db.update(TableName, values, where, null);
        return res;
    }
}
