package com.team_08.hi_run.hi_run.data;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team_08.hi_run.hi_run.Activites.DetailViewActivity;
import com.team_08.hi_run.hi_run.R;
import com.team_08.hi_run.hi_run.model.*;

import java.util.ArrayList;

/**
 * Created by Sohail on 6/28/15.
 */
public class CustomListviewAdapter extends ArrayAdapter<Job> {
    private int layoutResource;
    private Activity activity;
    private ArrayList<Job> JobList = new ArrayList<>();

    public CustomListviewAdapter(Activity act, Context context, int resource, ArrayList<Job> data) {
        super(context, resource, data);
        layoutResource = resource;
        activity = act;
        JobList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return JobList.size();
    }

    @Override
    public Job getItem(int position) {
        return JobList.get(position);
    }

    @Override
    public int getPosition(Job item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;
        ViewHolder holder = null;

        if (row == null || (row.getTag() == null)){
            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResource, null);

            holder = new ViewHolder();

            holder.JobName = (TextView) row.findViewById(R.id.JobName);
            holder.JobAddress = (TextView) row.findViewById(R.id.JobAddress);
            holder.JobDescription = (TextView) row.findViewById(R.id.JobDescription);
            holder.JobPay = (TextView) row.findViewById(R.id.JobPay);
            holder.JobDate = (TextView) row.findViewById(R.id.JobDate);

            row.setTag(holder);
        }else {
            holder = (ViewHolder) row.getTag();
        }

        holder.job = getItem(position);

        holder.JobName.setText(holder.job.getJobName());
        holder.JobAddress.setText(holder.job.getJobAddress());
        holder.JobDescription.setText(holder.job.getJobDescription());
        holder.JobPay.setText(String.valueOf(holder.job.getJobPay()));
        holder.JobDate.setText(String.valueOf(holder.job.getRecordDate()));

        final ViewHolder finalHolder = holder;

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, DetailViewActivity.class);

                Bundle mBundle = new Bundle();
//                mBundle.putSerializable("userObj", finalHolder.job);
                i.putExtras(mBundle);


                activity.startActivity(i);

            }
        });

        return row;
    }


    public class ViewHolder {
        Job job;
        TextView JobName;
        TextView JobDescription;
        TextView JobAddress;
        TextView JobDate;
        TextView JobPay;


    }
}
