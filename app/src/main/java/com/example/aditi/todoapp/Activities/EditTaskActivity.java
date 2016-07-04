package com.example.aditi.todoapp.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aditi.todoapp.Database.Task;
import com.example.aditi.todoapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by aditi on 7/1/2016.
 */
public class EditTaskActivity extends AppCompatActivity {
    EditText etName;
    EditText etDate;
    String tvName;
    String tvDate;
    String tvStatus;
    Button btnSave;
    Spinner spinStatus;

    private static final int ADD_ITEM=1;
    private static final int EDIT_ITEM=2;

    private static int action =ADD_ITEM;
    private String prioritySelected;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePicker;
    int list_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_item);
        setInitialPageLoadView();
        btnSave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onSaveItem(v);
            }
        });

        etDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                datePicker.show();
            }
        });

        spinStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prioritySelected = (String)parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setInitialPageLoadView(){
        btnSave = (Button) findViewById(R.id.buttonSave);
        etName = (EditText) findViewById(R.id.editTextName);
        etDate = (EditText)findViewById(R.id.editTextDate);
        etDate.setInputType(InputType.TYPE_NULL);
        spinStatus = (Spinner) findViewById(R.id.spinnerID);

        initDatePicker();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle !=null){
            action = EDIT_ITEM;
            populateItemInfo(bundle);
        }else{
            action = ADD_ITEM;
        }
    }

    private void setSpinValue(){
        String status[] = getResources().getStringArray(R.array.status);
        int index = 0;
        for (int i=0;i<spinStatus.getCount();i++){
            if (spinStatus.getItemAtPosition(i).toString().equalsIgnoreCase(tvStatus)){
                index = i;
                break;
            }
        }
        spinStatus.setSelection(index);
    }

    private void populateItemInfo(Bundle bundle){
        tvName = (String)bundle.get("name");
        tvDate = (String)bundle.get("date");
        tvStatus = (String)bundle.get("status");
        list_position = bundle.getInt("listPosition");


        etName.setText(tvName);
        etDate.setText(tvDate);
        setSpinValue();
    }

    private void initDatePicker(){
        Calendar calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calDueDate = Calendar.getInstance();
                calDueDate.set(year, monthOfYear, dayOfMonth);
                String strDate = simpleDateFormat.format(calDueDate.getTime());
                etDate.setText(strDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }


    public void onSaveItem(View v){
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra("name", etName.getText().toString());
        data.putExtra("date", etDate.getText().toString());
        data.putExtra("status", prioritySelected);
        if (action == EDIT_ITEM) {
            data.putExtra("listPosition", list_position);
        }

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}
