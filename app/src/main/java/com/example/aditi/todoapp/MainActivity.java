package com.example.aditi.todoapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> toDoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etEditText;
    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        populateArrayItems();
        lvItems= (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText)findViewById(R.id.etEditText);
        etEditText.setTextColor(Color.BLUE);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("itemPos", position); // pass arbitrary data to launched activity
                String edit = ((TextView)view).getText().toString();
                i.putExtra("itemName",edit);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                toDoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    public void populateArrayItems(){
        readItems();
        aToDoAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toDoItems);
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try{
            toDoItems = new ArrayList<String>(FileUtils.readLines(file));
        }catch (IOException e){
            System.out.println("readFile exception");
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(file, toDoItems);
        }catch (IOException e){
            System.out.println("writeFile exception");
        }
    }


    public void onAddItem(View view) {
        aToDoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
        InputMethodManager inputManager =
                (InputMethodManager) this.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("inside activity result");
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String editedName = data.getExtras().getString("name");
            int pos = data.getExtras().getInt("pos");
            System.out.println("Name: " + editedName + "  pos: " + pos);
            toDoItems.remove(pos);
            toDoItems.add(pos,editedName);
            writeItems();
            aToDoAdapter.notifyDataSetChanged();
        }
    }
}
