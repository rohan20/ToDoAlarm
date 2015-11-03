package com.example.rohan.todoalarm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.rohan.todoSQLMaterialDesign.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Add extends AppCompatActivity {

    EditText displayTitle;
    EditText displayDescription;
    Button bTimePicker;
    TextView tvTimePicker;
    Button bDatePicker;
    TextView tvDatePicker;

    final Calendar calendar = Calendar.getInstance();

//    EditText myEditText = (EditText) findViewById(R.id.editPasswd);
//
//    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
//            .showSoftInput(myEditText, InputMethodManager.SHOW_FORCED);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

    Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


        bTimePicker = (Button)findViewById(R.id.buttonTimePicker);
        tvTimePicker = (TextView)findViewById(R.id.TextViewTimePicker);

        bTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(Add.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        tvTimePicker.setText("Alarm Time: " + hourOfDay + " : " + minute + " (HH : MM)");
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        bDatePicker = (Button)findViewById(R.id.buttonDatePicker);
        tvDatePicker = (TextView)findViewById(R.id.TextViewDatePicker);

        bDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(Add.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        tvDatePicker.setText("Alarm Date: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year + " (DD/MM/YY)");
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });




//        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(displayTitle, InputMethodManager.SHOW_FORCED);
//        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(displayDescription, InputMethodManager.SHOW_FORCED);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
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

        if(id == R.id.action_saveToDo)
        {

            AllToDosHelper helper = new AllToDosHelper(this, null, 1);
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();

            displayTitle = (EditText)findViewById(R.id.title);
            displayDescription = (EditText)findViewById(R.id.description);

            SimpleDateFormat df = new SimpleDateFormat("hh:mm z '\n'EEEE, MMMM d, yyyy");
            String date = df.format(new Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

            cv.put("AllTodosTitle", displayTitle.getText().toString());
            cv.put("AllTodosDescription", displayDescription.getText().toString());
            cv.put("AllTodosTimeCreated", System.currentTimeMillis());
            cv.put("AllTodosAlarmTime", date);


            db.insert(AllToDosHelper.ALL_TODOS_TABLE, null, cv);

//            Snackbar.make(Add.this.findViewById(android.R.id.content), calendar.getTimeInMillis() + "\n", Snackbar.LENGTH_LONG).show();


            if(displayTitle.getText().toString().equals(""))
                Toast.makeText(this, "Can't leave title empty", Toast.LENGTH_SHORT).show();
            else
            {
                AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(this, AlarmReceiver.class);
                i.putExtra("AllTodosTitle", displayTitle.getText().toString());
                i.putExtra("AllTodosDescription", displayDescription.getText().toString());

                PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, i, 0);
    //            Toast.makeText(this, System.currentTimeMillis() + "", Toast.LENGTH_SHORT).show();
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pIntent);

                Intent intent = new Intent(Add.this, AllToDos.class);

                setResult(0, intent);
                startActivity(intent);
            }

        }

        if(id == R.id.action_cancel)
        {
            Intent cancel = new Intent(Add.this, AllToDos.class);
            setResult(1, cancel);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
