package com.example.roomdatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Expense.class}, exportSchema = false, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {
    private static final String DB_NAME = "expensedb";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDB(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, DatabaseHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration() // Recreate the database if migrations fail
                    .allowMainThreadQueries() // Allow database queries on the main thread (not recommended for production)
                    .build();
        }
        return instance;
    }

    public abstract ExpenseDao expenseDao();
}