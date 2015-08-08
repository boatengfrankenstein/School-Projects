package com.team_08.hi_run.hi_run.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team_08.hi_run.hi_run.Activites.BidActivity;
import com.team_08.hi_run.hi_run.Activites.LoginActivity;
import com.team_08.hi_run.hi_run.Activites.MainActivity;
import com.team_08.hi_run.hi_run.BuildConfig;
import com.team_08.hi_run.hi_run.R;
import com.team_08.hi_run.hi_run.mail.Mail;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import serverside.serversrc.db.RatingFunctions;
import serverside.serversrc.db.RunFunctions;
import serverside.serversrc.db.UserFunctions;
import serverside.serversrc.models.Rating;
import serverside.serversrc.models.Run;
import serverside.serversrc.models.User;

/**
 * Created by hector on 7/23/15.
 */
public class DetailJobFragment extends Fragment {

    private InputStream input;
    int selectedid1;

    public static final String Status = "Status";
    public static final String JobID = "JobID";

    private SendEmailAsyncTask mTask;

    private ArrayList<Run> mJobs;

    public static String EmailU;
    public static String urprice;
    public static String urating;
    public static String ureview;
    private String stat;
    private int ID;
    private Run mRun;

    private LinearLayout bottombar;
    String NewEmail;
    private HashMap<String, String> map = new HashMap<String, String>();

    //Dialog
    private ProgressDialog pDialog;


    User user = LoginActivity.getUser();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ArrayList<Run> mJobsA = LoginActivity.getmAccepted();
        ID = getArguments().getInt(JobID, 0);
        stat = getArguments().getString(Status, "queued");


        //for Java mail
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //for Java mail
        StrictMode.setThreadPolicy(policy);

        if(stat.equalsIgnoreCase("queued")) mJobs = LoginActivity.getmPosted();
        else if(stat.equalsIgnoreCase("accepted")) {
            mJobs = LoginActivity.getmAccepted();

        }
        else if(stat.equalsIgnoreCase("pending")) {
            mJobs = LoginActivity.getmPending();

        }
        else mJobs = LoginActivity.getmComplete();

        for(int i = 0; i < mJobs.size(); i++)
        {

            if(mJobs.get(i).getRunId() == ID)
            {
                mRun = mJobs.get(i);
                break;
            }
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Reload current fragment

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        View v = inflater.inflate(R.layout.activity_detail_view, container, false);
        Button Bid = (Button)v.findViewById(R.id.bid);
        Button location = (Button) v.findViewById(R.id.location);
        Button Delete = (Button)v.findViewById(R.id.DeleteButtonDV);
        Button showDialog = (Button) v.findViewById(R.id.showrating);
        Button Refuse = (Button)v.findViewById(R.id.RefuseButtonDV);
        Button Flag = (Button)v.findViewById(R.id.FlagButtonDV);
        Button Acpt = (Button)v.findViewById(R.id.AcceptButtonDV);
        Button UserDetails = (Button) v.findViewById(R.id.ClientDV);
        Button AcptC = (Button)v.findViewById(R.id.AcceptButtonDVC);
        Button Cmp = (Button)v.findViewById(R.id.CompletedButtonDV);
        TextView title = (TextView)v.findViewById(R.id.JobNameDV);
        TextView description = (TextView)v.findViewById(R.id.JobDescriptionDV);

        TextView offer = (TextView)v.findViewById(R.id.JobPayDV);
        TextView date = (TextView)v.findViewById(R.id.JobDateDV);
        LinearLayout bottombar = (LinearLayout)v.findViewById(R.id.bottomBar);

        //Date
        Date posted = mRun.getTimePosted();


        SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy hh:mm aa");

        Log.d("DetailFragemnt", String.valueOf(mRun.getAmount()));
        title.setText(mRun.getTitle());
        description.setText("    "+mRun.getDescription());
        offer.setText("Offered: " +"$"+ mRun.getAmount()+"0");
        date.setText("Posted on: "+sf.format(posted));



        String role = user.getRole().toLowerCase();

        if(mRun.getTitle().length() == 0){
            title.setText("");
        }
        else {
            title.setText(CapsFirst(mRun.getTitle()));
        }

        if (role.equals("client")) {
            Bid.setText("Bids");
            bottombar.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
            location.setBackgroundColor(getResources().getColor(R.color.bg_login));
            Bid.setBackgroundColor(getResources().getColor(R.color.bg_login));
        }
        else if(role.equals("admin")){
            Bid.setText("Bids");
            bottombar.setBackgroundColor(getResources().getColor(R.color.sinch_purple));
            location.setBackgroundColor(getResources().getColor(R.color.sinch_purple));
            Bid.setBackgroundColor(getResources().getColor(R.color.sinch_purple));
        }
        else {
            bottombar.setBackgroundColor(getResources().getColor(R.color.bg_login));
            location.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
            Bid.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
        }

        if(mRun.getRunType().equals("Bid")) {
            Bid.setVisibility(View.VISIBLE);

        }
        if(mRun.getRunType().equals("Offer")) {
            Bid.setVisibility(View.GONE);
        }

        if(role.equals("admin")){
            Flag.setVisibility(View.GONE);
            Cmp.setVisibility(View.GONE);
            Acpt.setVisibility(View.GONE);
            Refuse.setVisibility(View.GONE);
            Delete.setVisibility(View.VISIBLE);
            AcptC.setVisibility(View.GONE);
            showDialog.setVisibility(View.GONE);
            UserDetails.setVisibility(View.GONE);
        }
        if(role.equals("client")&& mRun.getStatus().equals("Accepted")){
            Flag.setVisibility(View.GONE);
            Cmp.setVisibility(View.GONE);
            Acpt.setVisibility(View.GONE);
            Refuse.setVisibility(View.GONE);
            Delete.setVisibility(View.GONE);
            AcptC.setVisibility(View.GONE);
            showDialog.setVisibility(View.GONE);
            Bid.setText("Chat");
        }
        else if(role.equals("client")&& mRun.getStatus().equals("Queued")){
            Flag.setVisibility(View.GONE);
            Cmp.setVisibility(View.GONE);
            Refuse.setVisibility(View.GONE);
            Acpt.setVisibility(View.GONE);
            AcptC.setVisibility(View.GONE);
            Delete.setVisibility(View.VISIBLE);
            showDialog.setVisibility(View.GONE);
            UserDetails.setVisibility(View.GONE);


        }
        else if(role.equals("client")&& mRun.getStatus().equals("Pending")){
            Flag.setVisibility(View.GONE);
            Cmp.setVisibility(View.GONE);
            AcptC.setVisibility(View.VISIBLE);
            Refuse.setVisibility(View.VISIBLE);
            Delete.setVisibility(View.GONE);
            Acpt.setVisibility(View.GONE);
            showDialog.setVisibility(View.GONE);
            Bid.setVisibility(View.GONE);

        }
        else if(role.equals("runner")&& mRun.getStatus().equals("Accepted")){
            Flag.setVisibility(View.GONE);
            Cmp.setVisibility(View.VISIBLE);
            Acpt.setVisibility(View.GONE);
            AcptC.setVisibility(View.GONE);
            Refuse.setVisibility(View.GONE);
            Delete.setVisibility(View.GONE);
            showDialog.setVisibility(View.GONE);
            Bid.setText("Chat");
        }
        else if(role.equals("runner")&& mRun.getStatus().equals("Queued")){
            Flag.setVisibility(View.VISIBLE);
            Cmp.setVisibility(View.GONE);
            AcptC.setVisibility(View.GONE);
            Acpt.setVisibility(View.VISIBLE);
            Delete.setVisibility(View.GONE);
            Refuse.setVisibility(View.GONE);
            showDialog.setVisibility(View.GONE);

        }

        else if(role.equals("runner")&& mRun.getStatus().equals("Pending")){
            Flag.setVisibility(View.GONE);
            Cmp.setVisibility(View.GONE);
            AcptC.setVisibility(View.GONE);
            Refuse.setVisibility(View.GONE);
            Delete.setVisibility(View.GONE);
            Acpt.setVisibility(View.GONE);
            showDialog.setVisibility(View.GONE);
            Bid.setVisibility(View.GONE);
        }
        else if(role.equals("client")||role.equals("runner")&& mRun.getStatus().equals("Completed")){
            showDialog.setVisibility(View.VISIBLE);
            if(role.equals("client")) {
                if (mRun.getCReviewed() == 1) {
                    showDialog.setVisibility(View.GONE);
                }
            }
            else if(role.equals("client")){
                if (mRun.getRReviewed() == 1){showDialog.setVisibility(View.GONE);}
            }
            Bid.setVisibility(View.GONE);
            Flag.setVisibility(View.GONE);
            Cmp.setVisibility(View.GONE);
            AcptC.setVisibility(View.GONE);
            Acpt.setVisibility(View.GONE);
            Refuse.setVisibility(View.GONE);
            Delete.setVisibility(View.GONE);

        }
        Bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity(), BidActivity.class);
                startActivity(intent);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);


                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create(); //Read Update
                alertDialog.setTitle("Location");
                alertDialog.setMessage(mRun.getcAddress());

                alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();  //<-- See This!
            }

        });
        showDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = (LayoutInflater.from(getActivity())).inflate(R.layout.user_input, null);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setView(view);
                final TextView userrating = (TextView) view.findViewById(R.id.userrating);
                final TextView userreview = (TextView) view.findViewById(R.id.userreview);

                alertBuilder.setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                urating = userrating.getText().toString();
                                ureview = userreview.getText().toString();
                                if(user.getRole().equalsIgnoreCase("client")) {
                                    if (mRun.getCReviewed() == 1) {
                                        Toast.makeText(getActivity(), "Run already reviewed", Toast.LENGTH_LONG)
                                                .show();
                                    } else {
                                        selectedid1 = 10;
                                        try {
                                            input = getActivity().getAssets().open("fulgentcorp.crt");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        new RunUpdate().execute();
                                    }
                                }
                                if(user.getRole().equalsIgnoreCase("runner")) {
                                    if (mRun.getRReviewed() == 1) {
                                        Toast.makeText(getActivity(), "Run already reviewed", Toast.LENGTH_LONG)
                                                .show();
                                    } else {
                                        selectedid1 = 9;
                                        try {
                                            input = getActivity().getAssets().open("fulgentcorp.crt");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        new RunUpdate().execute();
                                    }
                                }

//                                    Log.d("testing","Run has already been reviewed");

//                                Log.d("dialog", ratings.getText().toString()+review.getText().toString());
                            }
                        });
                Dialog dialog = alertBuilder.create();
                dialog.show();
            }
        });

        UserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getRole().equalsIgnoreCase("client")) {
                    selectedid1 = 8;
                    try {
                        input = getActivity().getAssets().open("fulgentcorp.crt");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(getActivity(), "Runner details shown!", Toast.LENGTH_LONG).show();

                    new UserDetail().execute();
                } else if (user.getRole().equalsIgnoreCase("runner")) {
                    selectedid1 = 7;
                    try {
                        input = getActivity().getAssets().open("fulgentcorp.crt");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(getActivity(), "Client details shown!", Toast.LENGTH_LONG).show();

                    new UserDetail().execute();
                } else {
                    Toast.makeText(getActivity(), "Nothing to show!...yet", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        //Refuse button displays alert dialog
        Refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize alert dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Deny");
                alert.setMessage("Are you sure you want to refuse runner ?");
                alert.setNegativeButton("No", null);
                //on yes click the DBA clears the job from the jobs table
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        selectedid1 =5;

                        try {
                            input = getActivity().getAssets().open("fulgentcorp.crt");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        new RunUpdate().execute();

                        //toast to display job completed
                        Toast.makeText(getActivity(), "Runner Refused!", Toast.LENGTH_LONG)
                                .show();
                    }

                });
                alert.show();
            }
        });

        //completed button displays alert dialog
        Cmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize alert dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Completed");
                alert.setMessage("Did you really complete this job ?");
                alert.setNegativeButton("No", null);
                //on yes click the DBA clears the job from the jobs table
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        selectedid1 =4;

                        try {
                            input = getActivity().getAssets().open("fulgentcorp.crt");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        new RunUpdate().execute();
                        //toast to display job completed
                        Toast.makeText(getActivity(), "Job marked as completed!", Toast.LENGTH_LONG)
                                .show();
                    }
                });
                alert.show();
            }
        });

        //Accept button displays alert dialog
        Acpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user.getVerification().equals("Yes")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Email Verification");
                    builder.setMessage("Your email is not verfied" +
                            " navigate to 'my account' to input pin and update profile to get verfied. ")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }

                    else if (mRun.getRunType().equals("Bid"))
                    {
                        View view = (LayoutInflater.from(getActivity())).inflate(R.layout.user_input2, null);

                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                        alertBuilder.setView(view);
                        final TextView userp = (TextView) view.findViewById(R.id.userp);

                        alertBuilder.setCancelable(true)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        urprice = userp.getText().toString();

                                        selectedid1 = 11;
                                        try {
                                            input = getActivity().getAssets().open("fulgentcorp.crt");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        new RunUpdate().execute();
                                    }
                                });
                        Dialog dialog = alertBuilder.create();
                        dialog.show();

                    }                    //initialize alert dialog
                else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Accept");
                    alert.setMessage("Are you sure you want to accept this Job?");
                    alert.setNegativeButton("No", null);
                    //on yes click the DBA clears the job from the jobs table
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            selectedid1 = 3;
                            try {
                                input = getActivity().getAssets().open("fulgentcorp.crt");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            new RunUpdate().execute();
                        }
                    });
                    alert.show();


                }
            }
        });

        //Accept button displays alert dialog
        AcptC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize alert dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Accept");
                alert.setMessage("Are you sure you want to accept this Runner?");
                alert.setNegativeButton("No", null);
                //on yes click the DBA clears the job from the jobs table
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedid1 = 6;
                        try {
                            input = getActivity().getAssets().open("fulgentcorp.crt");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        new RunUpdate().execute();

                        //toast to display job delelted
                        Toast.makeText(getActivity(), "Runner Accepted!", Toast.LENGTH_LONG)
                                .show();
                    }

                });
                alert.show();
            }
        });

        //delete button displays alert dialog
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize alert dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete this Job?");
                alert.setNegativeButton("No", null);
                //on yes click the DBA clears the job from the jobs table
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        selectedid1 =1;

                        try {
                            input = getActivity().getAssets().open("fulgentcorp.crt");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        new RunUpdate().execute();

                        Toast.makeText(getActivity(), "Job Deleted!", Toast.LENGTH_LONG)
                                .show();
                    }
                });
                alert.show();
            }
        });

        //flag button displays alert dialog
        Flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize alert dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Flag");
                alert.setMessage("Are you sure you want to flag this Job?");
                alert.setNegativeButton("No", null);
                //on yes click the DBA clears the job from the jobs table
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedid1 =2;

                        try {
                            input = getActivity().getAssets().open("fulgentcorp.crt");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        new RunUpdate().execute();

                        //toast to display job delelted
                        Toast.makeText(getActivity(), "Job Flagged!", Toast.LENGTH_LONG)
                                .show();
                    }
                });
                alert.show();
            }
        });

        Log.d("flag", String.valueOf(mRun.getTimesFlagged()));
        return v;
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
    //Sending Welcome Email
    public void Email() {
        //Calling async task to send Json
        new SendEmailAsyncTask().execute();
    }

    public class UserDetail extends AsyncTask<Void, Void, Boolean>
    {
        ArrayList<Rating> mrating =null;
        Rating mrated;
        String firstname, lastname, CEmail, Ratings, CCR, RatingAll;

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Name: " + firstname + " " + lastname + "\n" + "Email: " + CEmail +
                    "\n" + "Average Ratings: " + Ratings +
                    "\n" + "Completed runs as client: " + CCR + "\n" + "\n====All Ratings====\n" + "\n" + RatingAll)
                    .setTitle("Client Details")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if(selectedid1 == 7) {

                try {
                    User client = UserFunctions.getClient(mRun.getClntId());
                     lastname = client.getlName();
                     firstname = client.getfName();
                     CEmail = client.getEmail();
                     Ratings = Double.toString(client.getClientRating());
                     CCR = Double.toString(client.getComRunsClient());
                    mrating = client.getRatings();
                    Log.d("&&1", mrating.toString());

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mrating.size(); i++) {

                        mrated = mrating.get(i);

                        sb.append("\n Reviewer: " + mrated.getPostedName() + "\n\b Rating: " + mrated.getRating() +
                                "\n\b Review: " + mrated.getReview() + "\n");

                    }
                    RatingAll = sb.toString();

                    /*getActivity().runOnUiThread(new Thread() {
                        public void run() {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Name: " + firstname + " " + lastname + "\n" + "Email: " + CEmail +
                                    "\n" + "Average Ratings: " + Ratings +
                                    "\n" + "Completed runs as client: " + CCR + "\n" + "\n====All Ratings====\n" + "\n" + RatingAll)
                                    .setTitle("Client Details")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).create().show();

                        }

                    });*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //get runner details
            else {

                try {
                    User runner = UserFunctions.getRunner(mRun.getRnerId());
                     lastname = runner.getlName();
                     firstname = runner.getfName();
                     CEmail = runner.getEmail();
                     Ratings = Double.toString(runner.getClientRating());
                     CCR = Double.toString(runner.getComRunsRunner());
                    mrating = runner.getRatings();
                    Log.d("&&1", mrating.toString());

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mrating.size(); i++) {
                        mrated = mrating.get(i);


                        sb.append("\n Reviewer: " + mrated.getPostedName() + "\n\b Rating: " + mrated.getRating() +
                                "\n\b Review: " + mrated.getReview() + "\n");


                    }
                    final String RatingAll = sb.toString();
                    /*getActivity().runOnUiThread(new Thread() {
                        public void run() {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Name: " + firstname + " " + lastname + "\n" + "Email: " + REmail +
                                    "\n" + "Average Ratings: " + Ratings +
                                    "\n" + "Completed runs as client: " + CCR + "\n" + "\n====All Ratings====\n" + "\n" + RatingAll)
                                    .setTitle("Runner Details")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).create().show();

                        }

                    });*/

                    //RunFunctions.AppUpdateRun(mRun);  //update app
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
    public class RunUpdate extends AsyncTask<Void, Void, Boolean>
    {
        final int selectedid = selectedid1;
        final ArrayList<Run> mJobsA = LoginActivity.getmAccepted();
        final ArrayList<Run> mJobsQ = LoginActivity.getmPosted();
        final ArrayList<Run> mJobsP = LoginActivity.getmPending();
        final ArrayList<Run> mJobsC = LoginActivity.getmComplete();
        ArrayList<Rating> mrating =null;
        Rating mrated;
        User client= null;
        User runner=null;
        Rating rating = null;

        public void refreshactivity()
        {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            getActivity().finish();
            startActivity(intent);
        }
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
//            pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();

        }
        @Override
        protected void onPostExecute(Boolean aVoid) {

//            if (pDialog.isShowing())
//                pDialog.dismiss();
            switch (selectedid1) {
                case 1:
                    Email();
                    Log.d("&&7","email sent");
                    refreshactivity();
                    break;
                case 2:
                    refreshactivity();
                    break;
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            switch (selectedid) {
                //delete
                case 1:
                    try {

                        User client= UserFunctions.getClient(mRun.getClntId());
                        EmailU = client.getEmail();
                        Log.d("12", EmailU.toString());
                        mRun.voided(mJobs);
                        RunFunctions.AppUpdateRun(mRun);  //update app
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    selectedid1 =1;
                    break;
                //flag
                case 2:

                    try {
                        int currentFlags= mRun.getTimesFlagged();
                        if(currentFlags ==1){
                            mRun.voided(mJobs);
                            User client= UserFunctions.getClient(mRun.getClntId());
                            EmailU = client.getEmail();
                            selectedid1 =1;
                        }
                        else{
                            mRun.setTimesFlagged(currentFlags+1);}
                        RunFunctions.AppUpdateRun(mRun);  //update app
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                //add to Pending jobs
                case 3:

                    try {
                        String ver = RunFunctions.getVersion(mRun);
                        if (!ver.equals(mRun.getVersion())) {
                            System.out.println("Run is out of date, please refresh");
                            getActivity().runOnUiThread(new Thread() {
                                public void run() {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Poor mans notification");
                                    builder.setMessage("Run is out of date please refresh.")
                                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //add refresh
                                                    new LoginActivity.RefreshCall().execute();
                                                }
                                            }).create().show();
                                    selectedid1 = 2;
                                }

                            });

                        } else {
                            mRun.pending(user.getUserID(), mJobsQ, mJobsP);
                            System.out.println("Run moved to Pending!");
                            User client= UserFunctions.getClient(mRun.getClntId());
                            RunFunctions.AppUpdateRun(mRun);  //update app
                            EmailU = client.getEmail();
                            Log.d("12", EmailU.toString());
                            getActivity().runOnUiThread(new Thread() {
                                public void run() {
                                    //Toast.makeText(getActivity(), "Job Accepted!", Toast.LENGTH_LONG).show();
                                }

                            });
                            selectedid1 =1;

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                //runner add to completed jobs
                case 4:

                    try {
                        mRun.completed( mJobsA,mJobsC );
                        User client= UserFunctions.getClient(mRun.getClntId());
                        EmailU = client.getEmail();
                        Log.d("12", EmailU.toString());
                        selectedid1 =1;
                        RunFunctions.AppUpdateRun(mRun);  //update app
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                //Client refused runner
                case 5:

                    try {
                        User runner= UserFunctions.getRunner(mRun.getRnerId());
                        EmailU = runner.getEmail();
                        Log.d("12", EmailU.toString());
                        selectedid1 =1;
                        mRun.refused(mJobsP, mJobsQ);
                        RunFunctions.AppUpdateRun(mRun);  //update app

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                //Client accepted runner
                case 6:

                    try {
                        User runner= UserFunctions.getRunner(mRun.getRnerId());
                        EmailU = runner.getEmail();
                        Log.d("12", EmailU.toString());
                        selectedid1 =1;
                        mRun.accepted(mJobsP, mJobsA);
                        RunFunctions.AppUpdateRun(mRun);  //update app
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                //get client details
                /*case 7:

                    try {
                        User client= UserFunctions.getClient(mRun.getClntId());
                        final String lastname = client.getlName();
                        final String firstname = client.getfName();
                        final String CEmail = client.getEmail();
                        final String Ratings = Double.toString(client.getClientRating());
                        final String CCR = Double.toString(client.getComRunsClient());
                        mrating= client.getRatings();
                        Log.d("&&1", mrating.toString());

                        StringBuilder sb = new StringBuilder();
                        for(int i = 0; i < mrating.size(); i++)
                        {

                            mrated = mrating.get(i);

                            sb.append("\n Reviewer: " + mrated.getPostedName() + "\n\b Rating: " + mrated.getRating() +
                                    "\n\b Review: " + mrated.getReview()+"\n");

                        }
                        final String RatingAll= sb.toString();

                        getActivity().runOnUiThread(new Thread() {
                            public void run() {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Name: "+firstname+" "+lastname+"\n"+"Email: "+ CEmail+
                                        "\n"+"Average Ratings: "+Ratings+
                                        "\n"+"Completed runs as client: "+CCR+"\n"+"\n====All Ratings====\n"+"\n"+RatingAll)
                                        .setTitle("Client Details")
                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }).create().show();

                            }

                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                //get runner details
                case 8:

                    try {
                        User runner= UserFunctions.getRunner(mRun.getRnerId());
                        final String lastname = runner.getlName();
                        final String firstname = runner.getfName();
                        final String REmail = runner.getEmail();
                        final String Ratings = Double.toString(runner.getClientRating());
                        final String CCR = Double.toString(runner.getComRunsRunner());
                        mrating = runner.getRatings();
                        Log.d("&&1", mrating.toString());

                        StringBuilder sb = new StringBuilder();
                        for(int i = 0; i < mrating.size(); i++)
                        {
                            mrated = mrating.get(i);


                            sb.append("\n Reviewer: " + mrated.getPostedName() + "\n\b Rating: " + mrated.getRating() +
                                    "\n\b Review: " + mrated.getReview()+"\n");


                        }
                        final String RatingAll= sb.toString();
                        getActivity().runOnUiThread(new Thread() {
                            public void run() {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Name: "+firstname+" "+lastname+"\n"+"Email: "+ REmail+
                                        "\n"+"Average Ratings: "+Ratings+
                                        "\n"+"Completed runs as client: "+CCR+"\n"+"\n====All Ratings====\n"+"\n"+RatingAll)
                                        .setTitle("Runner Details")
                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }).create().show();

                            }

                        });

                        //RunFunctions.AppUpdateRun(mRun);  //update app
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                //rate Client*/
                case 9:

                    do{

                        rating = new Rating(mRun.getRunId(),mRun.getClntId(), user.getUserName(),Double.parseDouble(urating), ureview);
                        //map = rating.getErrors();

                        if(map.size()>0){ //if there are errors in Run model

                            System.out.println("\nInvalid Rating:");

                            if(map.get("rating")!=null){
                                System.out.println(map.get("rating")); 	//print out error
                            }

                            if(map.get("review")!=null){
                                System.out.println(map.get("review")); 	//print out error
                            }
                        }else{ //valid Run object created
                            System.out.println("\nRating Creation Successful!");
                            System.out.println("\nAdding to database...");
                            try {
                                User client= UserFunctions.getClient(mRun.getClntId());
                                EmailU = client.getEmail();
                                Log.d("12", EmailU.toString());
                                selectedid1 =1;
                                RatingFunctions.AppAddClientRating(rating);   //attempt to add to database
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(rating.getErrors().get("database")==null){		  //if adding was successful
                                System.out.println("Adding Successful!");
                                System.out.println(rating);
                                mRun.setRReviewed(1);
                            }else{								//if unsuccessful
                                System.out.println("Adding Unsuccessful!");
                                System.out.println(rating.getErrors().get("database"));
                                System.out.println();
                            }
                        }

                    }while(map.size()>0);
                    break;
                //rate runner
                case 10:

                    do{

                        rating = new Rating(mRun.getRunId(),mRun.getRnerId(), user.getUserName(),Double.parseDouble(urating), ureview);
                        //map = rating.getErrors();

                        if(map.size()>0){ //if there are errors in Run model

                            System.out.println("\nInvalid Rating:");

                            if(map.get("rating")!=null){
                                System.out.println(map.get("rating")); 	//print out error
                            }

                            if(map.get("review")!=null){
                                System.out.println(map.get("review")); 	//print out error
                            }
                        }else{ //valid Run object created
                            System.out.println("\nRating Creation Successful!");
                            System.out.println("\nAdding to database...");
                            try {
                                User runner= UserFunctions.getRunner(mRun.getRnerId());
                                EmailU = runner.getEmail();
                                Log.d("12", EmailU.toString());
                                selectedid1 =1;
                                RatingFunctions.AppAddRunnerRating(rating);   //attempt to add to database
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(rating.getErrors().get("database")==null){		  //if adding was successful
                                System.out.println("Adding Successful!");
                                System.out.println(rating);
                                mRun.setCReviewed(1);
                            }else{								//if unsuccessful
                                System.out.println("Adding Unsuccessful!");
                                System.out.println(rating.getErrors().get("database"));
                                System.out.println();
                            }
                        }

                    }while(map.size()>0);
                    break;
                case 11:
                    mRun.setAmount(Double.parseDouble(urprice));
                    mRun.pending(user.getUserID(), mJobsQ, mJobsP);
                    //System.out.println("Run moved to Pending!");
                    User client= null;
                    try {
                        client = UserFunctions.getClient(mRun.getClntId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        RunFunctions.AppUpdateRun(mRun);  //update app
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    EmailU = client.getEmail();
                    Log.d("12", EmailU.toString());
                    getActivity().runOnUiThread(new Thread() {
                        public void run() {
                            Toast.makeText(getActivity(), "Job Accepted!", Toast.LENGTH_LONG)
                                    .show();
                        }

                    });
                    selectedid1 =1;
                    break;
            }
            return null;
        }
    }
    //ASYNC TASK to send email using the JAVA Mail API
    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        final String Email = EmailU;

        Mail m = new Mail("hirundevolopers@gmail.com", "a3wv2j09jskjnv390kca4okpn");

        public SendEmailAsyncTask() {
            if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
            Toast.makeText(getActivity(), Email, Toast.LENGTH_LONG)
                    .show();
            String[] toArr = {Email};
            m.setTo(toArr);
            m.setFrom("Hi-Run!");
            m.setSubject("The run: "+mRun.getTitle()+" was Updated");
            m.setBody("Run changes have occured.");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
            try {
                m.send();
                return true;
            } catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
                return false;
            } catch (MessagingException e) {
                Log.e(SendEmailAsyncTask.class.getName(), m.getBody() + "failed");
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

    }


    public static DetailJobFragment newInstance(String status, int jobID)
    {
        Bundle args = new Bundle();
        args.putString(Status, status);
        args.putInt(JobID, jobID);
        DetailJobFragment jobs = new DetailJobFragment();
        jobs.setArguments(args);
        return jobs;
    }
}