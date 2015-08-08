package com.team_08.hi_run.hi_run.Fragments;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team_08.hi_run.hi_run.Activites.LoginActivity;
import com.team_08.hi_run.hi_run.R;

import serverside.serversrc.models.User;


public class FaqFragment extends Fragment {
    public static final String TAG = "Faq_Fragment";

    User user = LoginActivity.getUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_faq, null);
        ExpandableListView elv = (ExpandableListView) v.findViewById(R.id.Exlist);
        elv.setAdapter(new SavedTabsListAdapter());
        Display newDisplay = getActivity().getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            elv.setIndicatorBounds(width-120, width);
        } else {
            elv.setIndicatorBoundsRelative(width-120, width);
        }
        elv.setChildDivider(getResources().getDrawable(R.color.selected_gray));
        elv.setDivider(getResources().getDrawable(R.color.white));
        elv.setDividerHeight(2);


        return v;
    }

    public class SavedTabsListAdapter extends BaseExpandableListAdapter {

        private String[] groups = { "What is Hi-Run?", "How do I post a Run?", "When do I pay for my job? ", "Why Should I use Hi-Run instead of the leading ?" };

        private String[][] children = {
                { "Hi-Run is an app where you can post any task your too lazy too do, and than choose a person to do the task for you.  " },
                { "To post open Nav-drawer and select jobs which should change your views, now just press the '+' button under the 'Post' tab and follow the onscreen instructions to post a run."},
                { "You will be prompted to pay for the job once you have choosen your Runner." },
                { "Hi-Run distinguishes itself from the leading competition by allowing the runners to work when they want and how they want." }
        };


        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return children[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return groups[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return children[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(FaqFragment.this.getActivity());
            textView.setText(getGroup(i).toString());
            textView.setTextSize(25);
            textView.setPadding(10,0,0,0);
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(FaqFragment.this.getActivity());
            textView.setText(getChild(i, i1).toString());
            textView.setTextSize(15);
            textView.setPadding(20,0,0,0);
            ImageView divider = new ImageView(FaqFragment.this.getActivity());
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 2, 0, 2);
            divider.setLayoutParams(lp);
            divider.setBackgroundColor(Color.BLACK);
            return textView;

        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }

}



