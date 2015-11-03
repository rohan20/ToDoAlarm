package com.example.rohan.todoalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.rohan.todoSQLMaterialDesign.R;

import java.util.List;

/**
 * Created by Rohan on 10-Sep-15.
 */

public class ToDoArrayAdapter extends ArrayAdapter<ToDo>
{

    Context context;

    public ToDoArrayAdapter(Context context, int resource, List<ToDo> objects)
    {
        super(context, resource, objects);
        this.context = context;
    }

    public static class ToDoViewHolder
    {
        TextView title;
        CheckBox checkBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        if(convertView == null)
        {
            convertView = View.inflate(context, R.layout.todo_item_layout, null);
            ToDoViewHolder vh = new ToDoViewHolder();
            vh.title = (TextView)convertView.findViewById(R.id.toDoListItem);
            vh.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);

            convertView.setTag(vh);
        }

        ToDo a = getItem(position);
        ToDoViewHolder vh = (ToDoViewHolder)convertView.getTag();
        vh.title.setText(a.title);

        vh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    ToDoCompletedHelper helper = new ToDoCompletedHelper(context, null, 1);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues cv = new ContentValues();

                    cv.put("title", getItem(position).title);
                    cv.put("description", getItem(position).description);
                    cv.put("timeCreated", getItem(position).time);
                    cv.put("alarmTime", getItem(position).alarm);

                    db.insert(ToDoCompletedHelper.TODOS_COMPLETED_TABLE, null, cv);

                    Snackbar.make(buttonView, "'" + getItem(position).title.toString() + "' added to completed todos", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        return  convertView;

    }
}
