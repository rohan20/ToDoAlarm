package com.example.rohan.todoalarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.rohan.todoSQLMaterialDesign.R;

import java.util.ArrayList;

public class AllToDos extends AppCompatActivity implements AdapterView.OnItemClickListener
{

    ToDoArrayAdapter adapter;
    ArrayList<ToDo> todos;
    ListView lv;
    CheckBox cb;
    AllToDosHelper helper;
    SQLiteDatabase db;
    int backButtonCount = 0;

    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backButtonCount = 0;
            startActivity(intent);
        }
        else
        {
            Snackbar.make(this.findViewById(android.R.id.content), "Press the back button once again to\nclose the application.", Snackbar.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    public String[] getAllTodos()
    {
        helper = new AllToDosHelper(this, null, 1);
        db = helper.getReadableDatabase();

        String columns[] = {AllToDosHelper.ALL_TODOS_TITLE, AllToDosHelper.ALL_TODOS_DESCRIPTION, AllToDosHelper.ALL_TODOS_TIME_CREATED, AllToDosHelper.ALL_TODOS_ALARM_TIME};
        Cursor c = db.query(AllToDosHelper.ALL_TODOS_TABLE, columns, null, null, null, null, AllToDosHelper.ALL_TODOS_TIME_CREATED + " DESC");


        int i = 0;
        String output[] = new String[4 * c.getCount()];

        while(c.moveToNext())
        {
            output[i++] = c.getString(c.getColumnIndex(AllToDosHelper.ALL_TODOS_TITLE));
            output[i++] = c.getString(c.getColumnIndex(AllToDosHelper.ALL_TODOS_DESCRIPTION));
            output[i++] = c.getString(c.getColumnIndex(AllToDosHelper.ALL_TODOS_TIME_CREATED));
            output[i++] = c.getString(c.getColumnIndex(AllToDosHelper.ALL_TODOS_ALARM_TIME));
        }

        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_todos);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Alarms Notifs");

        cb = (CheckBox)findViewById(R.id.checkBox);

        String allTodos[] = getAllTodos();
        todos = new ArrayList<>();

        for(int i=0; i < allTodos.length; i+=4)
        {
            todos.add(new ToDo(allTodos[i], allTodos[i+1], allTodos[i+2], allTodos[i+3]));
        }

        adapter = new ToDoArrayAdapter(this, 0, todos);
        lv = (ListView)findViewById(R.id.toDoListView);
        lv.setAdapter(adapter);
//        lv.setNestedScrollingEnabled(true);
        lv.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_todos, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        else if(id == R.id.action_add)
        {
            Intent i = new Intent(AllToDos.this, Add.class);
            startActivity(i);
        }
        else if(id == R.id.action_email)
        {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:rohan.taneja@outlook.com"));
            i.putExtra(Intent.EXTRA_SUBJECT, "Hey there! I'm using Android Studio.");
            startActivity(i);
        }
        else if(id == R.id.action_fav)
        {
            Intent i = new Intent(AllToDos.this, ToDoCompleted.class);
            startActivity(i);
        }
        else if(id == R.id.action_about)
        {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://www.google.com"));
            startActivity(i);
        }
        else if(id == R.id.action_contact)
        {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel: +919891417273"));
            startActivity(i);
        }
        else if(id == R.id.action_delete)
        {
            final AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("THIS WILL DELETE ALL YOUR TODOS!");
            b.setMessage("Are you sure you want to delete ALL todos?");
//            LayoutInflater inflater = getLayoutInflater();
//            View v = inflater.inflate(R.layout.delete, null);

//            TextView tv = (TextView)v.findViewById(R.id.textView);
//            tv.setText("Delete " + title + " ?");
//            b.setView(v);


            b.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    todos.clear();
                    lv.setAdapter(adapter);
                    db = helper.getWritableDatabase();
                    db.execSQL("DELETE from " + AllToDosHelper.ALL_TODOS_TABLE);

                    Snackbar.make(AllToDos.this.findViewById(android.R.id.content), "All todos deleted", Snackbar.LENGTH_LONG).show();
                }
            });

            b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });

            b.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        ToDo t = adapter.getItem(position);

        Intent i = new Intent();
        i.setClass(this, Display.class);
        i.putExtra("positionInList", position);
        i.putExtra("title", t.title);
        i.putExtra("description", t.description);
        i.putExtra("alarm", t.alarm);
        startActivityForResult(i, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //add
        if(resultCode == 0)
        {
            if (data == null)
                return;

//            Bundle b = data.getExtras();
//            String titleOfToDo = b.getString("title");
//            String descriptionOfToDo = b.getString("description");
//
//            todos.add(new ToDo(titleOfToDo, descriptionOfToDo));
//            Toast.makeText(this, "New To Do Added", Toast.LENGTH_SHORT).show();

            setContentView(R.layout.activity_all_todos);

            String allTodos[] = getAllTodos();
            todos = new ArrayList<>();

            for(int i=0; i < allTodos.length; i+=3)
            {
                todos.add(new ToDo(allTodos[i], allTodos[i+1], allTodos[i+2], allTodos[i+3]));
            }

            adapter = new ToDoArrayAdapter(this, 0, todos);
            lv = (ListView)findViewById(R.id.toDoListView);
            Snackbar.make(this.findViewById(android.R.id.content), "New To Do added", Snackbar.LENGTH_LONG).show();
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);

        }

        //cancel
        else if(resultCode == 1)
        {
            adapter = new ToDoArrayAdapter(this, 0, todos);
            lv = (ListView)findViewById(R.id.toDoListView);
            lv.setAdapter(adapter);
//        lv.setNestedScrollingEnabled(true);
            lv.setOnItemClickListener(this);
        }

        //delete
        else if(resultCode == 2)
        {
            Bundle b = data.getExtras();
            int position = b.getInt("positionInList");
            String title = b.getString("title");
            helper = new AllToDosHelper(this, null, 1);
            db = helper.getWritableDatabase();

            ToDo todo = adapter.getItem(position);
            String epochTime = todo.time;


            Snackbar.make(this.findViewById(android.R.id.content), "Note deleted.", Snackbar.LENGTH_LONG).show();
            db.delete(AllToDosHelper.ALL_TODOS_TABLE, AllToDosHelper.ALL_TODOS_TIME_CREATED + " = " + epochTime, null);
            todos.remove(position);
            adapter.notifyDataSetChanged();
            lv.setAdapter(adapter);

            Snackbar.make(this.findViewById(android.R.id.content),  "'" + title + "' deleted", Snackbar.LENGTH_LONG).show();
        }

    }

}
