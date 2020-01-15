package com.cyfoes.aditya.slate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDatabase extends SQLiteOpenHelper {

    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "CHAT";
    public static final String COL_4 = "TIME";
    public static final String COL_5 = "PARICIPANTS";
    public static final String DataBase_Name = "slate_chat2.db";
    public static final int Database_Version = 1;
    public static final String Table_Name = "chat_table2";

    public ChatDatabase(Context context) {
        super(context, DataBase_Name, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE chat_table2 (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, CHAT TEXT, TIME TEXT, PARICIPANTS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chat_table2");
    }

    public void insertdata(String name, String chat, String time, String participants){
        SQLiteDatabase writabledatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, chat);
        contentValues.put(COL_4, time);
        contentValues.put(COL_5, participants);
        writabledatabase.insert(Table_Name, null , contentValues);
    }

    public Cursor getalldata(){
        return getWritableDatabase().rawQuery("SELECT * FROM chat_table2", null);
    }

    public int deleteData(String str) {
        return getWritableDatabase().delete(Table_Name, "NAME=? ", new String[]{str});
    }
}
