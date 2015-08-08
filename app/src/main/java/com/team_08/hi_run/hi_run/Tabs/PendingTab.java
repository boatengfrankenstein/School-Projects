package com.team_08.hi_run.hi_run.Tabs;

/**
 * Created by Sohail on 7/15/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.team_08.hi_run.hi_run.Activites.JobPagerActivity;
import com.team_08.hi_run.hi_run.Activites.LoginActivity;
import com.team_08.hi_run.hi_run.Fragments.DetailJobFragment;
import com.team_08.hi_run.hi_run.R;
import com.team_08.hi_run.hi_run.util.JobListAdapter;

import java.util.ArrayList;

import serverside.serversrc.models.Run;

public class PendingTab extends ListFragment {
    private ArrayList<Run> mjobs;
    private final String TAG = "PendingTab";

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mjobs = LoginActivity.getmAccepted();
        // Log.d("AcceptedTab", String.valueOf(mjobs.size()));

        //postedAdapter mposted = new postedAdapter(mjobs);
        JobListAdapter mposted = new JobListAdapter(getActivity(), 0, mjobs);
        setListAdapter(mposted);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mjobs.size() == 0) {
            Log.d(TAG, "List is empty bro");
            View v = inflater.inflate(R.layout.empty, container, false);
            return v;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    private class postedAdapter extends ArrayAdapter<Run>
    {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
            {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_jobs,null);
            }

            TextView Title = (TextView)convertView.findViewById(R.id.job_title);
            Title.setText(getItem(position).getTitle().toString());
            return convertView;
        }

        public postedAdapter(ArrayList<Run> jobs)
        {
            super(getActivity(), 0, jobs);


        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Run mrun = mjobs.get(position);

        Log.d(TAG, mrun.getStatus());

        Intent intent = new Intent(getActivity(), JobPagerActivity.class);
        intent.putExtra(DetailJobFragment.JobID, mrun.getRunId());
        intent.putExtra(DetailJobFragment.Status, mrun.getStatus());
        startActivity(intent);
    }
}