package com.example.rohan.todoalarm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rohan.todoSQLMaterialDesign.R;

import java.util.List;

public class ToDoArrayAdapterCompleted extends ArrayAdapter<ToDo>
{

    Context context;

    public ToDoArrayAdapterCompleted(Context context, int resource, List<ToDo> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public static class CompletedToDoViewHolder
    {
        TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if(convertView == null)
        {
            convertView = View.inflate(context, R.layout.todo_completed_item_layout, null);
            CompletedToDoViewHolder vh = new CompletedToDoViewHolder();

            vh.title = (TextView)convertView.findViewById(R.id.completedToDoListItem);

            convertView.setTag(vh);
        }

        CompletedToDoViewHolder vh = (CompletedToDoViewHolder)convertView.getTag();
        ToDo a = getItem(position);
        vh.title.setText(a.title);

        return convertView;
    }
}
