package com.team_08.hi_run.hi_run.Fragments;

/**
 * Created by Sohail on 7/16/15.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
import com.team_08.hi_run.hi_run.util.SlidingTabLayout;
import com.team_08.hi_run.hi_run.util.Utils;
import com.team_08.hi_run.hi_run.util.ViewPagerAdapter;

import java.util.ArrayList;


public class JobsTempF extends android.support.v4.app.Fragment {
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Posted","Accepted","Completed"};
    int Numboftabs =3;

    public static final String TAG = "Jobs_Fragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jobs, container, false);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getChildFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) rootView.findViewById(R.id.pager);




            // Assiging the Sliding Tab Layout View
            tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
            tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View

            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
                }
             });

        // Setting the ViewPager For the SlidingTabsLayout

            pager.setAdapter(adapter);
            tabs.setViewPager(pager);

        return rootView;


    }

}