package com.example.aditi.todoapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    EditText et;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        et = (EditText) findViewById(R.id.editMultilineText);
        et.setTextColor(Color.BLUE);
        Intent intent = getIntent();
        String edit = intent.getExtras().getString("itemName");
        pos = intent.getExtras().getInt("itemPos");
        System.out.println("Name: " + edit + "  Pos:" +  pos);
        populateEditForm(edit, pos);
    }

    private void populateEditForm(String name, int position){
        et.setText(name);
        et.setSelection(et.getText().length());
    }

    public void onSaveItem(View v){
        System.out.println("Onsave clicked");
        EditText etName = (EditText) findViewById(R.id.editMultilineText);
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("name", etName.getText().toString());
        data.putExtra("pos",pos);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}

