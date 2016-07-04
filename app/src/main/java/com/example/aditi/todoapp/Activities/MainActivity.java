package com.example.aditi.todoapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.aditi.todoapp.Database.Task;
import com.example.aditi.todoapp.Model.CustomTaskAdapter;
import com.example.aditi.todoapp.R;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Task> toDoItems;
    //ArrayAdapter<String> aToDoAdapter;
    CustomTaskAdapter customTaskAdapter;
    ListView lvItems;
    Button btnAdd;
    String editName;
    String editDate;

    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE_listClick = 20;
    private final int REQUEST_CODE_btnClick = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int titleId = getResources().getIdentifier("action_bar_title", "id","android");
        populateArrayItems();
        lvItems= (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(customTaskAdapter);

        btnAdd = (Button)findViewById(R.id.btnAddItem);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,EditTaskActivity.class), REQUEST_CODE_btnClick);
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this, EditTaskActivity.class);
                Task editTask = toDoItems.get(position);
                editName = editTask.getName();
                editDate = editTask.getDate();

                i.putExtra("name", editName);
                i.putExtra("date", editDate);
                i.putExtra("status", editTask.getStatus());
                i.putExtra("listPosition", position);
                startActivityForResult(i, REQUEST_CODE_listClick);
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task removeTask = toDoItems.get(position);
                String removeName = removeTask.getName();
                String removeDate = removeTask.getDate();
                String removeStatus = removeTask.getStatus();
                removeTask  = queryTask(removeName, removeDate);
                final Task remove = removeTask;
                final int index = position;
                if(removeStatus.equals("In progress")){
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
                                    toDoItems.remove(index);
                                    Toast.makeText(MainActivity.this, "Delete successful", Toast.LENGTH_SHORT).show();
                                    customTaskAdapter.notifyDataSetChanged();
                                }}).show();
                }
                else{
                    removeTask.delete();
                    toDoItems.remove(position);
                    customTaskAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    public void populateArrayItems(){
        Task t = new Task();
        toDoItems = Task.listAll(Task.class);
        customTaskAdapter = new CustomTaskAdapter(this, R.layout.each_list_item, toDoItems);
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

            toDoItems.remove(position);
            toDoItems.add(position, newTask);
            Task editTask = queryTask(editName, editDate);

            editTask.setName(newName);
            editTask.setDate(newDate);
            editTask.setStatus(newStatus);
            editTask.save();
        }
        else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_btnClick) {
            toDoItems.add(newTask);
            newTask.save();
        }
        Toast.makeText(MainActivity.this, "Task Updated",Toast.LENGTH_LONG).show();
        customTaskAdapter.notifyDataSetChanged();
    }
}
