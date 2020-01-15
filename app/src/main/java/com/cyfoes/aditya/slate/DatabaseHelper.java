package com.cyfoes.aditya.slate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "POINT";
    public static final String COL_4 = "RES";
    public static final String COL_5 = "TIME";
    public static final String COL_6 = "PARICIPANTS";
    public static final String DataBase_Name = "slate4.db";
    public static final int Database_Version = 1;
    public static final String Table_Name = "pointtable4";

    public DatabaseHelper(Context context) {
        super(context, DataBase_Name, null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE pointtable4 (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT ,POINT TEXT, RES TEXT, TIME TEXT, PARICIPANTS TEXT) ");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL(" DROP TABLE IF EXISTS pointtable4");
    }

    public boolean insertData(String str, String str2, String str3, String str4, String str5) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, str);
        contentValues.put(COL_3, str2);
        contentValues.put(COL_4, str3);
        contentValues.put(COL_5, str4);
        contentValues.put(COL_6, str5);
        long insert = writableDatabase.insert(Table_Name, null, contentValues);
        writableDatabase.close();
        return insert != -1;
    }

    public void update_name(String str, String str2, String str3) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, str);
        contentValues.put(COL_5, str3);
        writableDatabase.update(Table_Name, contentValues, "ID = ?", new String[]{str2});
    }

    public Cursor getAllData() {
        return getWritableDatabase().rawQuery("SELECT * from pointtable4", null);
    }

    public void deleteall() {
        getWritableDatabase().execSQL("delete from pointtable4");
    }

    public int deleteData(String str) {
        return getWritableDatabase().delete(Table_Name, "ID=? ", new String[]{str});
    }

    public Cursor getlast() {
        return getWritableDatabase().query(Table_Name, null, null, null, null, null, "ID ASC", "1");
    }
}
