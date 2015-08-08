package com.team_08.hi_run.hi_run.Activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.team_08.hi_run.hi_run.R;
import com.team_08.hi_run.hi_run.data.DatabaseHandler;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import serverside.serversrc.db.RunFunctions;
import serverside.serversrc.models.Run;
import serverside.serversrc.models.User;

public class PostActivity extends AppCompatActivity {

    //UI Components initialized
    private EditText JobName, JobDescription,JobPay, JobAddress;

    //UI Buttons
    private Button SubmitButton, auto;

    private Spinner mSpinnertype;

    //Dialog
    private ProgressDialog pDialog;

    public static String bid;
    private Toolbar mToolbar;
    //TAG
    private static final String TAG = "PostActivity";

    //Get Database
    private DatabaseHandler dba;

    //google places
    AutoCompleteTextView atvPlaces;


    //Post Call
    private InputStream input;
    private HashMap<String, String> map = new HashMap<String, String>();

    User user = LoginActivity.getUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mSpinnertype = (Spinner) findViewById(R.id.spinnerP);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
        mSpinnertype.setGravity(20);

        addItemsOnSpinner();

        auto = (Button) findViewById(R.id.autofillp);


        //UI components connected
        JobAddress = (EditText) findViewById(R.id.JobAddress);
        JobName = (EditText) findViewById(R.id.JobEdittext);
        JobDescription = (EditText) findViewById(R.id.DescriptionEdittext);
        JobPay = (EditText) findViewById(R.id.MoneyEdittext);
        SubmitButton = (Button) findViewById(R.id.submit_button);


        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<String>();
                list.add("Can someone get me tacos");
                list.add("Need Some food");
                list.add("glazed donuts ");
                list.add("Starbucks");
                list.add("chipotle");
                list.add("McDonalds");
                list.add("Burger King");
                list.add("Papa Johns");
                Random rand = new Random();
                String random = list.get(rand.nextInt(list.size()));
                JobName.setText(random);
                JobDescription.setText("I just got to school but I forgot my lunch. So can someone please bring me 6 tacos.");
                JobPay.setText("500");
                JobAddress.setText("13514 Broketerry Dawn, Tx, San Antonio, 78217 ");
            }
        });

        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    input = getApplicationContext().getAssets().open("fulgentcorp.crt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new PostCall().execute();

            }
        });
    }

    // add items into role spinner
    public void addItemsOnSpinner() {

        mSpinnertype = (Spinner) findViewById(R.id.spinnerP);
        List list = new ArrayList();
        list.add("Offer");
        list.add("Bid");

        ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnertype.setAdapter(dataAdapter);

    }

    //show alert box if user presses "back" to cancel post
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(PostActivity.this);
        alert.setTitle("Cancel");
        alert.setMessage("Are you sure you want to cancel?");
        alert.setNegativeButton(android.R.string.no, null);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                Mainreturn();
            }
        });


        alert.show();
    }




    public class PostCall extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PostActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected void onPostExecute(Void aVoid) {

            if (pDialog.isShowing())
                pDialog.dismiss();
                if (map.size() > 0) { //if there are errors in Run model
                    Toast.makeText(getApplicationContext(), "Invalid Run", Toast.LENGTH_SHORT).show();

                    if (map.get("title") != null) {
                        Toast.makeText(getApplicationContext(), "Invalid title", Toast.LENGTH_SHORT).show(); //print out error
                        Log.d(TAG, map.get("title"));
                    }

                    if (map.get("description") != null) {
                        Toast.makeText(getApplicationContext(), "Invalid description", Toast.LENGTH_SHORT).show(); //print out error
                        Log.d(TAG, map.get("description"));
                    }

                    if (map.get("amount") != null) {
                        Toast.makeText(getApplicationContext(), "Invalid amount", Toast.LENGTH_SHORT).show(); //print out error
                        Log.d(TAG, map.get("amount"));
                    }

                    if (map.get("address") != null) {
                        Toast.makeText(getApplicationContext(), "Invalid address", Toast.LENGTH_SHORT).show(); //print out error
                        Log.d(TAG, map.get("address"));
                    }

                    if (map.get("city") != null) {                        //if city error occurred
                        Toast.makeText(getApplicationContext(), "Invalid city", Toast.LENGTH_SHORT).show(); //print out error
                        Log.d(TAG, map.get("city"));
                    }

                    if (map.get("state") != null) {                        //if state error occurred
                        Toast.makeText(getApplicationContext(), "Invalid state", Toast.LENGTH_SHORT).show(); //print out error
                        Log.d(TAG, map.get("state"));
                    }

                    if (map.get("zipcode") != null) {                        //if zipcode error occurred
                        Toast.makeText(getApplicationContext(), "Invalid zipcode", Toast.LENGTH_SHORT).show(); //print out error
                        Log.d(TAG, map.get("zipcode"));
                    }


                    Toast.makeText(getApplicationContext(), "Job Posting failed", Toast.LENGTH_SHORT).show();
                }
            else { //valid Run object created
                Toast.makeText(getApplicationContext(),"Run Creation Successful!", Toast.LENGTH_SHORT).show();
                    Mainreturn();
            }

            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {

            Run run = null;					//initialize User
            map = new HashMap<String, String>();//HashMap for errors

            ArrayList<Run> qRuns = RunFunctions.getqRuns(); //fill queued runs
            String title = JobName.getText().toString();
            String description = JobDescription.getText().toString();
            String amount = JobPay.getText().toString();
            String address = JobAddress.getText().toString();
            String city = JobAddress.getText().toString();
            String state = JobAddress.getText().toString();
            String zipcode = JobAddress.getText().toString();
            String Runtype = mSpinnertype.getSelectedItem().toString();

            //
            Log.d("!!2", Runtype);
            do{


                run = new Run(user.getUserID(),title, description,Runtype, amount, address, city, state, zipcode);

                //map = run.getErrors();	//put any errors into map

                if(map.size()>0){ //if there are errors in Run model
                    System.out.println("\nInvalid Run:");

                    if(map.get("title")!=null){
                        Log.d(TAG, map.get("title")); 	//print out error
                        System.out.println(map.get("title")); 	//print out error
                    }

                    if(map.get("description")!=null){
                        Log.d(TAG, map.get("description")); 	//print out error
                        System.out.println(map.get("description")); 	//print out error
                    }

                    if(map.get("amount")!=null){
                        Log.d(TAG, map.get("amount")); 	//print out error
                        System.out.println(map.get("amount")); 		//print out error
                    }

                    if(map.get("address")!=null){
                        Log.d(TAG, map.get("address")); 	//print out error
                        System.out.println(map.get("address")); 		//print out error
                    }

                    if(map.get("city")!=null){						//if city error occurred
                        Log.d(TAG, map.get("city")); 	//print out error
                        System.out.println(map.get("city")); 		//print out error
                    }

                    if(map.get("state")!=null){						//if state error occurred
                        Log.d(TAG, map.get("state")); 	//print out error
                        System.out.println(map.get("state")); 		//print out error
                    }

                    if(map.get("zipcode")!=null){						//if zipcode error occurred
                        Log.d(TAG, map.get("zipcode")); 	//print out error
                        System.out.println(map.get("zipcode")); 		//print out error
                    }

                }else{ //valid Run object created

                    try {
                        RunFunctions.AppAddRun(run);   //attempt to add to database
                        qRuns.add(0,run);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(run.getRunId()!=0){		  //if adding was successful
                        System.out.println("Adding Successful!");
                        System.out.println("Run Id from database: " + run.getRunId());
//                        qRuns.add(0, run); //add to queued runs
                    }else{								//if unsuccessful
                        System.out.println("Adding Unsuccessful!");
//                        System.out.println(user.getErrors().get("database"));
                        System.out.println();
                    }
                }
            }while(map.size()>0);
            return null;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Mainreturn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Mainreturn()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
