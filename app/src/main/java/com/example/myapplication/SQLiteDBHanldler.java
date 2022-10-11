package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDBHanldler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION =1;
    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "ToDo_Table";
    private static final String COL1 = "ID";
    private static final String COL2 = "Name";
    private static final String COL3 = "Date";
    private static final String COL4 = "Time";

    public SQLiteDBHanldler(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "("                + COL1 + " integer primary key, "                + COL2 + " TEXT, "                + COL3 + " DATE, "                + COL4 + " TIME" + ")";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE_CMD = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(DROP_TABLE_CMD);
    }
    void addTask(DoModel doModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, doModel.getName());
        cv.put(COL3,doModel.getDate());
        cv.put(COL4,doModel.getTime());


        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    DoModel getTask(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL1,COL2,COL3,COL4}, COL1 + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();
        DoModel theTask = new DoModel(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),cursor.getString(3) );
        db.close();
        return theTask;
    }

    List<DoModel> getAllTask(){
        List<DoModel> theList = new ArrayList<DoModel>();
        String SELECT_ALL_CMD = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_CMD, null);

        if (cursor.moveToFirst()){
            do {
                DoModel theTask = new DoModel(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2),cursor.getString(3));
                theList.add(theTask);
            } while (cursor.moveToNext());
        }

        db.close();

        return theList;
    }

    public int updateTask(DoModel selectedTask){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, selectedTask.getName());
        cv.put(COL3,selectedTask.getDate());
        cv.put(COL4,selectedTask.getTime());

        return db.update(TABLE_NAME,cv,COL1 + " = ?", new String[]{String.valueOf(selectedTask.getId())});


    }

    public void deleteTask(DoModel selectedTask){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL1 + " = ?", new String[]{String.valueOf(selectedTask.getId())});
        db.close();

    }

    public void deleteAllTask(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}

