package com.team_08.hi_run.hi_run.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.team_08.hi_run.hi_run.Fragments.DetailJobFragment;
import com.team_08.hi_run.hi_run.R;
import com.team_08.hi_run.hi_run.model.User;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import serverside.serversrc.db.RunFunctions;
import serverside.serversrc.db.UserFunctions;
import serverside.serversrc.models.Run;

/**
 * Created by hector on 7/23/15.
 */
public class JobPagerActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ArrayList<Run> mJobs;
    private String Status;
    private int jobID;

    private Toolbar mToolbar;
    private static final String TAG = "JobPagerActivity";

    //Dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Status = (String)getIntent().getStringExtra(DetailJobFragment.Status);
        jobID = (int)getIntent().getIntExtra(DetailJobFragment.JobID, 0);
        Log.d("JobPagerActivity", "Status: " + Status + " jobID: " + jobID);

        if(Status.equalsIgnoreCase("queued"))
        {
            mJobs = LoginActivity.getmPosted();
        }
        else if(Status.equalsIgnoreCase("pending"))
        {
            mJobs = LoginActivity.getmPending();
        }
        else if(Status.equalsIgnoreCase("accepted"))
        {
            mJobs = LoginActivity.getmAccepted();
        }
        else mJobs = LoginActivity.getmComplete();

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);


        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                jobID = mJobs.get(position).getRunId();
                return DetailJobFragment.newInstance(Status, jobID);
            }

            @Override
            public int getCount() {
                return mJobs.size();
            }
        });

        for(int i = 0; i < mJobs.size(); i++)
        {
            if(mJobs.get(i).getRunId() == jobID)
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}