package com.example.des808.my_tcp_ip_client;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBChatManager {
    private static DBChatManager instance;
    public     String  SnameIndex, SipadrIndex, SportIndex;
    public      int     SidIndex;

    public static  DBChatManager getInstance(Context con){
        if(instance == null){
            instance = new DBChatManager(con);}
        return  instance;
    }

    private SQLiteDatabase dbchat;
    private    Context context;
    private DBChat dbChat;
    //проекция-массив с именами полей таблицы ипадреса
    private String[] columns = new String[]{
            DBChat.MESSAGE_IN,
            DBChat.DATA_IN_MESSAGE,
            DBChat.MESSAGE_OUT,
            DBChat.DATA_OUT_MESSAGE };

    public DBChatManager(Context c) {
        context = c;
        dbChat = new DBChat( context );
    }
    //открытие БД
    public void openBd(){dbchat = dbChat.getWritableDatabase();}

    //закрытие БД
    public void closeBd(){dbchat.close();}

    //получение полного списка контактов
   /* public ArrayList<adapter_listview> getAllContacts(){
        openBd();
        Cursor cursor = dbchat.query( DBChat.TABLE_NAME,null,null,null,null,null,null );
        ArrayList<adapter_listview> list = new ArrayList<adapter_listview>(  );
        if(cursor != null&& cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                adapter_listview item = new adapter_listview();
                item.setID( cursor.getInt( 0 ) );
                item.setTextname( cursor.getString( 1 ) );
                item.setTextipadr( cursor.getString( 2 ) );
                item.setTextport( cursor.getString( 3 ) );
                list.add( item );
            }while (cursor.moveToNext());}
        closeBd();
        return list;
    }*/

 /*   //получение одной строки контакта
    public HashMap<String, String> readContact(int position){
        openBd();
        Cursor cursor = dbchat.query( DBChat.TABLE_NAME,null,null,null,null,null,null );
        if (cursor.moveToPosition( position )){
            //int idIndex =    cursor.getColumnIndex( DBHelper._ID );
            int nameIndex =  cursor.getColumnIndex( DBChat.NAME );
            int ipadrIndex = cursor.getColumnIndex( DBChat.IPADR );
            int portIndex =  cursor.getColumnIndex( DBChat.PORT );
            int portIndex =  cursor.getColumnIndex( DBChat.PORT );
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
    }*/

    //добавление нового контакта в список
    public  int addChatString(adapter_listview entity){
        openBd();
        ContentValues values = new ContentValues( 3 );
       /* values.put( DBChat.NAME, entity.getTextname() );
        values.put( DBChat.IPADR, entity.getTextipadr() );
        values.put( DBChat.PORT, entity.getTextport() );*/

        int res = (int)dbchat.insertOrThrow( DBChat.TABLE_NAME, null, values );
        closeBd();
        return res;
    }

 /*   //обновление существующего контакта
    public  int updateChat(adapter_listview entity){
        openBd();
        ContentValues values = new ContentValues( 3 );
        *//*values.put( DBChat.NAME, entity.getTextname() );
        values.put( DBChat.IPADR, entity.getTextipadr() );
        values.put( DBChat.PORT, entity.getTextport() );*//*

        String where = String.format( "%s=%d",DBChat._ID,entity.getID() );
        int res = dbchat.update( DBChat.TABLE_NAME,values,where,null );
        closeBd();
        return res;
    }*/


    //удаление контакта
    public int deleteChat(int contactID){
        openBd();
        String where = String.format( "%s=%d",DBChat._ID,contactID );
        int res = dbchat.delete( DBChat.TABLE_NAME,where,null );
        closeBd();
        return res;
    }
}
