package com.example.rohan.todoalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ToDoCompletedHelper extends SQLiteOpenHelper
{

    public static final String TODOS_COMPLETED_TABLE = "completed";
    public static final String TODOS_COMPLETED_TITLE = "title";
    public static final String TODOS_COMPLETED_DESCRIPTION = "description";
    public static final String TODOS_COMPLETED_TIME_CREATED = "timeCreated";
    public static final String TODOS_COMPLETED_ALARM_TIME = "alarmTime";

    public ToDoCompletedHelper(Context context, SQLiteDatabase.CursorFactory factory, int version)
    {
            super(context, "Completed Todos", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TODOS_COMPLETED_TABLE + " ( " + TODOS_COMPLETED_TITLE +
                " varchar(255)," + TODOS_COMPLETED_DESCRIPTION + " varchar(255), " +
                TODOS_COMPLETED_TIME_CREATED + " integer, " + TODOS_COMPLETED_ALARM_TIME + " integer )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
