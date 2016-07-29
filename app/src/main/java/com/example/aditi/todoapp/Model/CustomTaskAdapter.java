package com.example.aditi.todoapp.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aditi.todoapp.Database.Task;
import com.example.aditi.todoapp.R;

import java.util.List;

public class CustomTaskAdapter extends ArrayAdapter<Task> {

    int layoutResourceId_;

    static class ViewHolder
    {
        TextView name;
        TextView date;
        TextView status;
    }

    public CustomTaskAdapter(Context context, int layoutResourceId, List<Task> data) {
        super(context, layoutResourceId, data);
        layoutResourceId_ = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        ViewHolder holder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId_, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tvEachName);
            holder.date = (TextView)convertView.findViewById(R.id.tvDueDate);
            holder.status = (TextView) convertView.findViewById(R.id.tvEachStatus);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.name.setText(task.getName());
        holder.date.setText(task.getDate());
        holder.status.setText(task.getStatus());
        if(holder.status.getText().equals("In progress")){
            holder.status.setTextColor(Color.parseColor("#FFA500"));
            holder.name.setPaintFlags(0);
        }

        if(holder.status.getText().equals("Done")){
            holder.status.setTextColor(Color.parseColor("#32CD32"));
            holder.date.setText("Task completed");
            holder.name.setPaintFlags(holder.status.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return convertView;
    }


}