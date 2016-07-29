package com.example.aditi.todoapp.Database;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

/**
 * Created by aditi on 6/21/2016.
 */
public class Task extends SugarRecord<Task> {
    private Long id;

    private String name;
    private String date;
    private String status;

    public Task(){
        this.name = "";
        this.date = "";
        this.status = "";
    }

    public Task(String name, String date, String status) {

        this.name = name;
        this.date = date;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

