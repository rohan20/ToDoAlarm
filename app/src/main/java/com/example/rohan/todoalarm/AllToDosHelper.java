package com.example.rohan.todoalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rohan on 22-Oct-15.
 */
public class AllToDosHelper extends SQLiteOpenHelper
{
    public static final String ALL_TODOS_TABLE = "AllTodosTable";
    public static final String ALL_TODOS_TITLE = "AllTodosTitle";
    public static final String ALL_TODOS_DESCRIPTION = "AllTodosDescription";
    public static final String ALL_TODOS_TIME_CREATED = "AllTodosTimeCreated";
    public static final String ALL_TODOS_ALARM_TIME = "AllTodosAlarmTime";


    public AllToDosHelper(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "To Do Database", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + ALL_TODOS_TABLE + " ( " + ALL_TODOS_TITLE +
                " varchar(255)," + ALL_TODOS_DESCRIPTION + " varchar(255), " + ALL_TODOS_TIME_CREATED
                + " integer, " + ALL_TODOS_ALARM_TIME + " integer )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
