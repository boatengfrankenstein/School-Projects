package com.team_08.hi_run.hi_run.Fragments;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.team_08.hi_run.hi_run.Activites.PostActivity;
import com.team_08.hi_run.hi_run.R;
import com.team_08.hi_run.hi_run.data.CustomListviewAdapter;
import com.team_08.hi_run.hi_run.data.DatabaseHandler;
import com.team_08.hi_run.hi_run.model.*;
import com.team_08.hi_run.hi_run.util.Utils;

import java.util.ArrayList;


public class JobsFragment extends Fragment {

    public static final String TAG = "Jobs_Fragment";
    private DatabaseHandler dba;
    private ArrayList<Job> dbFoods = new ArrayList<>();
    private CustomListviewAdapter JobAdapter;
    private ListView listView;



    private Job myJob;
    private TextView totalMoney, totalJobs;
    Context context; //Declare the variable context

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jobs, container, false);

        context = rootView.getContext(); // Assign your rootView to context

        listView = (ListView) rootView.findViewById(R.id.list);//list

        totalMoney = (TextView) rootView.findViewById(R.id.totalAmountTextView);
        totalJobs = (TextView) rootView.findViewById(R.id.totalItemsTextView);

        ImageView Add = (ImageView) rootView.findViewById(R.id.imageViewAdd); //ADD button
//        refreshData();

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(context, PostActivity.class));
            }
        });
        return rootView;
    }
    private void refreshData() {
        dbFoods.clear();

        dba = new DatabaseHandler(getActivity().getApplicationContext());

        ArrayList<Job> jobsFromDB = dba.getJobs();

        int TtlValue = dba.totalMoney();
        int totalItems = dba.getTotalItems();

        String formattedValue = Utils.formatNumber(TtlValue);
        String formattedItems = Utils.formatNumber(totalItems);

        totalMoney.setText("Total Cost:$ " + formattedValue);
        totalJobs.setText("Total Jobs: " + formattedItems);

        for (int i = 0; i < jobsFromDB.size(); i++){

            String name = jobsFromDB.get(i).getJobName();
            String dateText = jobsFromDB.get(i).getRecordDate();
            String address = jobsFromDB.get(i).getJobAddress();
            String Description = jobsFromDB.get(i).getJobDescription();
            int money = jobsFromDB.get(i).getJobPay();
            int jobId = jobsFromDB.get(i).getJobId();

            Log.v("FOOD IDS: ", String.valueOf(jobId));


            myJob = new Job();
            myJob.setJobName(name);
            myJob.setRecordDate(dateText);
            myJob.setJobPay(money);
            myJob.setJobAddress(address);
            myJob.setJobDescription(Description);
            myJob.setJobId(jobId);

            dbFoods.add(myJob);



        }
        dba.close();

        //setup adapter
       // JobAdapter = new CustomListviewAdapter(getActivity(),context, R.layout.list_row, dbFoods);
       // listView.setAdapter(JobAdapter);
//        JobAdapter.notifyDataSetChanged();


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}

