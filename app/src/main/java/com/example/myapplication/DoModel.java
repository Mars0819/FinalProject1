package com.example.myapplication;

public class DoModel {
    public static DoModel selectedTask;
    private int id;
    private String name;
    private String date;
    private String time;


    public DoModel(int id, String name, String date,String time) {
        this.id = id;
        this.name = name;
        this.date =date;
        this.time = time;

    }

    public int getId() {return id;}

    public String getName() {return name;}
    public String getDate() {return date;}
    public String getTime() {return time;}


}
