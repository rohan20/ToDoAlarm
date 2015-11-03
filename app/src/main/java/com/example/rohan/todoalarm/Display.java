package com.example.rohan.todoalarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohan.todoSQLMaterialDesign.R;

public class Display extends AppCompatActivity {

    TextView titleOfToDo;
    TextView descriptionOfToDo;
    TextView alarmTimeOfToDo;

    String title;
    String description;
    String alarm;

    int position;
    Bundle b;

    AllToDosHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleOfToDo = (TextView)findViewById(R.id.titleTextView);
        descriptionOfToDo = (TextView)findViewById(R.id.descriptionTextView);
        alarmTimeOfToDo = (TextView)findViewById(R.id.alarmTextView);

        Intent intent = getIntent();
        b = intent.getExtras();

        position = b.getInt("positionInList");
        title = b.getString("title");
        description = b.getString("description");
        alarm = b.getString("alarm");

        setTitle(title);

        titleOfToDo.setText(title);
        descriptionOfToDo.setText(description);
        alarmTimeOfToDo.setText(alarm);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
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
        if(id == R.id.action_cancel)
        {
            Intent cancel = new Intent(Display.this, AllToDos.class);
            setResult(1, cancel);
            finish();
        }

        if (id == R.id.action_delete)
        {

            AlertDialog.Builder b = new AlertDialog.Builder(this);
//            b.setTitle("Are you sure ??");

//            LayoutInflater inflater = getLayoutInflater();
//            View v = inflater.inflate(R.layout.delete, null);

            b.setTitle("Delete!");
            b.setMessage("Are you sure you want to delete this ToDo?");

//            TextView tv = (TextView)v.findViewById(R.id.textView);
//            tv.setText("Delete " + title + " ?");
//            b.setView(v);


            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent delete = new Intent(Display.this, AllToDos.class);
                    delete.putExtra("title", title);
                    delete.putExtra("positionInList", position);
                    setResult(2, delete);
                    finish();
                }
            });

            b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });

            b.create().show();

        }

//        if (id == R.id.action_edit)
//        {
//            Intent i = new Intent(Display.this, Edit.class);
//            i.putExtra("title", b.getString("title"));
//            i.putExtra("description", b.getString("description"));
//            startActivityForResult(i, 0);
//            finish();
//
//        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        //edit
        if(resultCode == 4)
        {
            if (data == null)
                return;

            b = data.getExtras();

            String newTitleAfterEdit = b.getString("newTitle");
            String newDescriptionAfterEdit = b.getString("newDescription");

            titleOfToDo.setText(newTitleAfterEdit);
            descriptionOfToDo.setText(newDescriptionAfterEdit);

            Toast.makeText(this, b.getString("newTitle") + " has been edited", Toast.LENGTH_SHORT).show();

        }
        if(resultCode == 5)
        {

        }

    }
}
