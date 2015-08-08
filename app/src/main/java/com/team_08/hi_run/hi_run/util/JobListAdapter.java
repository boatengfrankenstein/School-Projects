package com.team_08.hi_run.hi_run.util;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team_08.hi_run.hi_run.R;

import java.util.ArrayList;

import serverside.serversrc.models.Run;

/**
 * Created by hector on 7/23/15.
 */
public class JobListAdapter extends ArrayAdapter<Run> {
    public JobListAdapter(Context context, int resource, ArrayList<Run> objects) {
        super(context, resource, objects);
    }

    String CapsFirst(String str) {
        String[] words = str.split(" ");
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < words.length; i++) {
            ret.append(Character.toUpperCase(words[i].charAt(0)));
            ret.append(words[i].substring(1));
            if(i < words.length - 1) {
                ret.append(' ');
            }
        }
        return ret.toString();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = View.inflate(getContext(),R.layout.list_jobs,null);
        }

        TextView Title = (TextView)convertView.findViewById(R.id.job_title);
        Run mruns = getItem(position);

        if(mruns.getTitle().length() == 0){
         Title.setText("");
        }
        else {
            Title.setText(CapsFirst(mruns.getTitle()));
        }
        Title.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        TextView Description = (TextView)convertView.findViewById(R.id.job_description);

        //Description set up
        int length = mruns.getDescription().length();
        if(length <= 30) {
            String RunD = mruns.getDescription().substring(0, length);
            Description.setText(RunD+"...");
        }
        else {
            String RunD1 = mruns.getDescription().substring(0, 30);
            Description.setText(RunD1 + "...");
        }
        return convertView;
    }

}
