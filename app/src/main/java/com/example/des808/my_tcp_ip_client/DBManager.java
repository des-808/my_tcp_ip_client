package com.example.des808.my_tcp_ip_client;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DBManager {
    private static DBManager instance;
    public     String  SnameIndex, SipadrIndex, SportIndex;
    public      int     SidIndex;
    Cursor cursor;

    public static  DBManager getInstance(Context context){
        if(instance == null){
            instance = new DBManager(context);}
        return  instance;
    }

    private SQLiteDatabase db;
    private    Context context;
    private DBHelper dbHelper;
    //проекция-массив с именами полей таблицы ипадреса
    private String[] columns = new String[]{
            DBHelper.NAME,
            DBHelper.IPADR,
            DBHelper.PORT };

    public DBManager(Context c) {
        context = c;
        dbHelper = new DBHelper( context );
    }
    //открытие БД
    public void openBd(){db = dbHelper.getWritableDatabase();}

    //закрытие БД
    public void closeBd(){db.close();
        cursor.close();}

    //получение полного списка контактов
    public ArrayList<TitleChatsItems>getAllContacts(){
        openBd();
        cursor = db.query( DBHelper.TABLE_NAME,null,null,null,null,null,null );
        ArrayList<TitleChatsItems> list = new ArrayList<TitleChatsItems>(  );
        if(cursor != null&& cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                TitleChatsItems item = new TitleChatsItems();
                item.setID( cursor.getInt( 0 ) );
                item.setName( cursor.getString( 1 ) );
                item.setIp_adr( cursor.getString( 2 ) );
                item.setPort( cursor.getString( 3 ) );
                list.add( item );
            }while (cursor.moveToNext());}
        closeBd();
        return list;
    }

    //получение одной строки контакта
    public HashMap<String, String> readContact(int position){
        openBd();
        cursor = db.query( DBHelper.TABLE_NAME,null,null,null,null,null,null );
        if (cursor.moveToPosition( position )){
            //int idIndex =    cursor.getColumnIndex( DBHelper._ID );
            int nameIndex =  cursor.getColumnIndex( DBHelper.NAME );
            int ipadrIndex = cursor.getColumnIndex( DBHelper.IPADR );
            int portIndex =  cursor.getColumnIndex( DBHelper.PORT );
               SnameIndex = cursor.getString( nameIndex );
               SipadrIndex = cursor.getString( ipadrIndex );
               SportIndex = cursor.getString( portIndex );
            cursor.close();
        }
        closeBd();
        HashMap<String, String> tmpHashMap = new HashMap<String, String>();
        tmpHashMap.put("param1", SnameIndex);
        tmpHashMap.put("param2", SipadrIndex);
        tmpHashMap.put("param3", SportIndex);
        return tmpHashMap;
    }

    //добавление нового контакта в список
    public  int addContact(TitleChatsItems entity){
        openBd();
        ContentValues values = new ContentValues( 3 );
        values.put( DBHelper.NAME, entity.getName() );
        values.put( DBHelper.IPADR, entity.getIp_adr() );
        values.put( DBHelper.PORT, entity.getPort() );

        int res = (int)db.insertOrThrow( DBHelper.TABLE_NAME, null, values );
        closeBd();
        return res;
    }

    //обновление существующего контакта
    public  int updateContact(TitleChatsItems entity){
        openBd();
        ContentValues values = new ContentValues( 3 );
        values.put( DBHelper.NAME, entity.getName() );
        values.put( DBHelper.IPADR, entity.getIp_adr() );
        values.put( DBHelper.PORT, entity.getPort() );

        String where = String.format( "%s=%d",DBHelper._ID,entity.getID() );
        int res = db.update( DBHelper.TABLE_NAME,values,where,null );
        closeBd();
        return res;
        }


    //удаление контакта
    public int deleteContact(int contactID){
        openBd();
        String where = String.format( "%s=%d",DBHelper._ID,contactID );
        int res = db.delete( DBHelper.TABLE_NAME,where,null );
        closeBd();
        return res;
    }
}

