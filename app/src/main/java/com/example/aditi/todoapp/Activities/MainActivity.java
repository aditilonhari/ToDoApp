package com.example.aditi.todoapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.aditi.todoapp.Database.Task;
import com.example.aditi.todoapp.Model.CustomTaskAdapter;
import com.example.aditi.todoapp.R;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    List<Task> toDoItemsTop;
    List<Task> toDoItemsBottom;
    CustomTaskAdapter customTaskAdapterTop;
    CustomTaskAdapter customTaskAdapterBottom;
    ListView lvItemsTop;
    ListView lvItemsBottom;
    Button btnAdd;
    String editName;
    String editDate;

    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE_listClick = 20;
    private final int REQUEST_CODE_btnClick = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        int titleId = getResources().getIdentifier("action_bar_title", "id","android");
        populateArrayItems();

        lvItemsTop = (ListView) findViewById(R.id.lvItemsTop);
        lvItemsTop.setAdapter(customTaskAdapterTop);

        lvItemsBottom = (ListView) findViewById(R.id.lvItemsBottom);
        lvItemsBottom.setAdapter(customTaskAdapterBottom);

        btnAdd = (Button)findViewById(R.id.btnAddItem);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,EditTaskActivity.class), REQUEST_CODE_btnClick);
            }
        });

        lvItemsTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this, EditTaskActivity.class);
                Task editTask = toDoItemsTop.get(position);
                editName = editTask.getName();
                editDate = editTask.getDate();

                i.putExtra("name", editName);
                i.putExtra("date", editDate);
                i.putExtra("status", editTask.getStatus());
                i.putExtra("listPosition", position);
                startActivityForResult(i, REQUEST_CODE_listClick);
            }
        });

        lvItemsTop.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task removeTask = toDoItemsTop.get(position);
                String removeName = removeTask.getName();
                String removeDate = removeTask.getDate();
                String removeStatus = removeTask.getStatus();
                removeTask  = queryTask(removeName, removeDate);
                final Task remove = removeTask;
                final int index = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm delete")
                        .setMessage("Oops! Delete a task in-progress???")
                        .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(MainActivity.this, "Pheww!!! Disaster averted!", Toast.LENGTH_LONG).show();
                            }})
                        .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton) {
                                remove.delete();
                                toDoItemsTop.remove(index);
                                Toast.makeText(MainActivity.this, "Delete successful", Toast.LENGTH_SHORT).show();
                                setDynamicHeight(lvItemsTop);
                                setDynamicHeight(lvItemsBottom);
                                customTaskAdapterTop.notifyDataSetChanged();
                                customTaskAdapterBottom.notifyDataSetChanged();
                            }}).show();
                return true;
            }
        });

        lvItemsBottom.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task removeTask = toDoItemsBottom.get(position);
                String removeName = removeTask.getName();
                String removeDate = removeTask.getDate();
                String removeStatus = removeTask.getStatus();
                removeTask  = queryTask(removeName, removeDate);
                final Task remove = removeTask;
                final int index = position;

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm delete")
                        .setMessage("Delete the task?")
                        .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(MainActivity.this, "Item back in the completed list", Toast.LENGTH_LONG).show();
                            }})
                        .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton) {
                                remove.delete();
                                toDoItemsBottom.remove(index);
                                Toast.makeText(MainActivity.this, "Delete successful", Toast.LENGTH_LONG).show();
                                setDynamicHeight(lvItemsTop);
                                setDynamicHeight(lvItemsBottom);
                                customTaskAdapterTop.notifyDataSetChanged();
                                customTaskAdapterBottom.notifyDataSetChanged();
                            }}).show();
                return true;
            }
        });

        setDynamicHeight(lvItemsTop);
        setDynamicHeight(lvItemsBottom);
        customTaskAdapterTop.notifyDataSetChanged();
        customTaskAdapterBottom.notifyDataSetChanged();
    }

    public void populateArrayItems(){
        List<Task> toDoItems = Task.listAll(Task.class);
        toDoItemsTop =  Task.listAll(Task.class);
        toDoItemsTop.clear();
        toDoItemsBottom=  Task.listAll(Task.class);
        toDoItemsBottom.clear();
        for(Task t : toDoItems){
            if(t.getStatus().equals("In progress")){
                toDoItemsTop.add(t);
            }
            else if(t.getStatus().equals("Done")){
                toDoItemsBottom.add(t);
            }
        }
        customTaskAdapterTop = new CustomTaskAdapter(this, R.layout.each_list_item, toDoItemsTop);
        customTaskAdapterBottom = new CustomTaskAdapter(this, R.layout.each_list_item, toDoItemsBottom);
    }

    private boolean setDynamicHeight(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems;itemPos++){
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public Task queryTask(String name, String date){
        Select specificQuery = Select.from(Task.class).where(Condition.prop("name").eq(name),Condition.prop("date").eq(date));
        List<Task> t = specificQuery.list();
        return t.get(0);
    }

    //time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Extract name value from result extras
        String newName = data.getExtras().getString("name");
        String newDate = data.getExtras().getString("date");
        String newStatus = data.getExtras().getString("status");
        Task newTask = new Task(newName, newDate, newStatus);

        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_listClick) {

            int position = data.getExtras().getInt("listPosition");

            toDoItemsTop.remove(position);
            if(newStatus.equals("In progress")){
                toDoItemsTop.add(position, newTask);
            }
            else {
                toDoItemsBottom.add(newTask);
            }
            Task editTask = queryTask(editName, editDate);

            editTask.setName(newName);
            editTask.setDate(newDate);
            editTask.setStatus(newStatus);
            editTask.save();
        }
        else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_btnClick) {
            if(newStatus.equals("In progress")){
                toDoItemsTop.add(newTask);
            }
            else{
                toDoItemsBottom.add(newTask);
            }
            newTask.save();
        }
        //Toast.makeText(MainActivity.this, "Task Updated",Toast.LENGTH_LONG).show();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_layout,
                (ViewGroup) findViewById(R.id.relativeLayout1));

        Toast toast = new Toast(getBaseContext());
        toast.setView(view);
        toast.getView().setBackgroundColor(Color.argb(150, 0, 0, 0));
        toast.show();

        setDynamicHeight(lvItemsTop);
        setDynamicHeight(lvItemsBottom);
        customTaskAdapterTop.notifyDataSetChanged();
        customTaskAdapterBottom.notifyDataSetChanged();
    }
}
