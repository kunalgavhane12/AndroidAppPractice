package com.example.databaseapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContactsDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE_NO = "phone_no";


    public MyDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREATE TABLE contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone_no TEXT);
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "("+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_PHONE_NO + " TEXT" +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Insert Data
    public void addContact(String name, String phone_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_PHONE_NO, phone_no);

        db.insert(TABLE_NAME, null, values);
    }

    //Search Data
    public ArrayList<ContactModel> fetchContact() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<ContactModel> arrContacts = new ArrayList<>();
        while (cursor.moveToNext()) {

           ContactModel models = new ContactModel();
           models.id = cursor.getInt(0);
           models.name = cursor.getString(1);
           models.phone_no = cursor.getString(2);

           arrContacts.add(models);
        }
        return arrContacts;
    }

    //Update Data
    public  void updateContact(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PHONE_NO, contactModel.phone_no);
        contentValues.put(KEY_NAME, contactModel.name);
        db.update(TABLE_NAME, contentValues, KEY_ID + " = " + contactModel.id, null);
    }

    //Delete Data
    public void deleteContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,KEY_ID + " = ? ", new String[]{String.valueOf(id)});
    }
}
