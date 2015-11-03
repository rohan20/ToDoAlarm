package com.example.rohan.todoalarm;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rohan.todoSQLMaterialDesign.R;

import java.util.ArrayList;

public class ToDoCompleted extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    ToDoArrayAdapterCompleted adapter;
    ArrayList<ToDo> todos;
    ListView lv;

    ToDoCompletedHelper helper;
    SQLiteDatabase db;

    public String[] getCompletedTodos()
    {
        helper = new ToDoCompletedHelper(this, null, 1);
        db = helper.getReadableDatabase();

        String columns[] = {ToDoCompletedHelper.TODOS_COMPLETED_TITLE, ToDoCompletedHelper.TODOS_COMPLETED_DESCRIPTION, ToDoCompletedHelper.TODOS_COMPLETED_TIME_CREATED, ToDoCompletedHelper.TODOS_COMPLETED_ALARM_TIME};
        Cursor c = db.query(ToDoCompletedHelper.TODOS_COMPLETED_TABLE, columns, null, null, null, null, ToDoCompletedHelper.TODOS_COMPLETED_TIME_CREATED);


        int i = 0;
        String output[] = new String[4 * c.getCount()];

        while(c.moveToNext())
        {
            output[i++] = c.getString(c.getColumnIndex(ToDoCompletedHelper.TODOS_COMPLETED_TITLE));
            output[i++] = c.getString(c.getColumnIndex(ToDoCompletedHelper.TODOS_COMPLETED_DESCRIPTION));
            output[i++] = c.getString(c.getColumnIndex(ToDoCompletedHelper.TODOS_COMPLETED_TIME_CREATED));
            output[i++] = c.getString(c.getColumnIndex(ToDoCompletedHelper.TODOS_COMPLETED_ALARM_TIME));
        }

        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_to_do);

        Snackbar.make(this.findViewById(android.R.id.content), "Long Press ToDo to delete.", Snackbar.LENGTH_LONG).show();

        String completedTodos[] = getCompletedTodos();
        todos = new ArrayList<>();

        for(int i=0; i < completedTodos.length; i+=4)
        {
            todos.add(new ToDo(completedTodos[i], completedTodos[i+1], completedTodos[i+2], completedTodos[i+3]));
        }

        adapter = new ToDoArrayAdapterCompleted(this, 0, todos);
        lv = (ListView)findViewById(R.id.toDoListView2);
        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Completed Todos");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_completed_todos, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_clear)
        {
            db.execSQL("DELETE from " + ToDoCompletedHelper.TODOS_COMPLETED_TABLE);
            todos.clear();
            lv.setAdapter(adapter);
            Snackbar.make(this.findViewById(android.R.id.content), "Completed todos cleared.", Snackbar.LENGTH_LONG).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ToDo t = adapter.getItem(position);

        Intent i = new Intent(ToDoCompleted.this, Display.class);
        i.putExtra("positionInList", position);
        i.putExtra("title", t.title);
        i.putExtra("description", t.description);
        i.putExtra("alarm", t.alarm);
        startActivity(i);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        ToDo toDoPosition = adapter.getItem(position);
        String epochTime = toDoPosition.time;
        helper = new ToDoCompletedHelper(this, null, 1);
        db = helper.getWritableDatabase();

        db.delete(ToDoCompletedHelper.TODOS_COMPLETED_TABLE, ToDoCompletedHelper.TODOS_COMPLETED_TIME_CREATED + " = " + epochTime, null);
        todos.remove(toDoPosition);
        Snackbar.make(view, toDoPosition.title + " removed.", Snackbar.LENGTH_LONG).show();
        adapter.notifyDataSetChanged();
        lv.setAdapter(adapter);


        return false;
    }
}
