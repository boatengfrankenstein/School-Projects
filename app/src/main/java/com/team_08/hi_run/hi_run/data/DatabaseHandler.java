package com.team_08.hi_run.hi_run.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.team_08.hi_run.hi_run.model.Job;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sohail on 6/28/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList <Job> JobList = new ArrayList<>();

    public DatabaseHandler(Context context) {
        super(context , Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        String CREATE_TABLE = "CREATE TABLE " +
                Constants.TABLE_NAME + "(" +
                Constants.KEY_ID + " INTEGER PRIMARY KEY, " +
                Constants.JOB_NAME + " TEXT, " +
                Constants.JOB_DESCRIPTION_NAME + " TEXT, "+
                Constants.JOB_ADDRESS + " TEXT, "+
                Constants.JOB_PAY+ " INT, "+
                Constants.DATE_NAME + " LONG);";
        db.execSQL(CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        //Create new one
        onCreate(db);
    }
    //Get total items saved

    public int getTotalItems(){
        int TotalItems = 0;

        String query = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, null);

        TotalItems = cursor.getCount();

        cursor.close();

        return TotalItems;
    }


    //get total Money posted
    public int totalMoney(){
        int Money = 0;

        SQLiteDatabase dba = this.getReadableDatabase();
        String query = "SELECT SUM( " + Constants.JOB_PAY + " ) "+
                "FROM "+ Constants.TABLE_NAME;

        Cursor cursor = dba.rawQuery(query, null);
        if(cursor.moveToFirst()){
            Money = cursor.getInt(0);
        }
        cursor.close();
        dba.close();

        return Money;
    }

    //delete Jobs Items
    public void deleteJob (int id) {
        SQLiteDatabase dba = this.getWritableDatabase();
        dba.delete(Constants.TABLE_NAME, Constants.KEY_ID + "= ?",
                new String[]{String.valueOf(id)});
        dba.close();

    }

    //Add Job Items
    public void addJob (Job job) {
        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.JOB_NAME, job.getJobName());
        values.put(Constants.JOB_DESCRIPTION_NAME, job.getJobDescription());
        values.put(Constants.JOB_ADDRESS, job.getJobAddress());
        values.put(Constants.JOB_PAY, job.getJobPay());
        values.put(Constants.DATE_NAME, System.currentTimeMillis());

        dba.insert(Constants.TABLE_NAME, null, values);

        //Log.v("added Job item", "yes!");

        dba.close();

    }


    //Get All jobs
    public ArrayList<Job> getJobs(){

        JobList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID, Constants.JOB_NAME, Constants.JOB_DESCRIPTION_NAME,
                        Constants.JOB_ADDRESS, Constants.JOB_PAY, Constants.DATE_NAME},
                        null, null, null, null, Constants.DATE_NAME + " DESC " );

            //Loop through
            if (cursor.moveToFirst()){
                do {
                   Job job = new Job();
                   job.setJobName(cursor.getString(cursor.getColumnIndex(Constants.JOB_NAME)));
                   job.setJobDescription(cursor.getString(cursor.getColumnIndex(Constants.JOB_DESCRIPTION_NAME)));
                   job.setJobAddress(cursor.getString(cursor.getColumnIndex(Constants.JOB_ADDRESS)));
                   job.setJobPay(cursor.getInt(cursor.getColumnIndex(Constants.JOB_PAY)));
                   job.setJobId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                    DateFormat dateFormat = DateFormat.getDateInstance();
                    String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))).getTime());


                    job.setRecordDate(date);

                    JobList.add(job);
                }while (cursor.moveToNext());
            }

        cursor.close();
        dba.close();


        return JobList;
    }




}


