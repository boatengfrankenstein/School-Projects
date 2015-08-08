package com.team_08.hi_run.hi_run.Activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.TelephonyManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.team_08.hi_run.hi_run.BuildConfig;
import com.team_08.hi_run.hi_run.Fragments.DatePickerFragment;
import com.team_08.hi_run.hi_run.mail.Mail;
import com.team_08.hi_run.hi_run.R;
import com.team_08.hi_run.hi_run.util.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import serverside.serversrc.db.UserFunctions;
import serverside.serversrc.models.User;

public class ClientRegisterActivity extends Activity  {

    //Phone Verification
    public static final String INTENT_PHONENUMBER = "phonenumber";

    //Dialog
    private ProgressDialog pDialog;

    private static final String TAG = "ClientRegisterActivity";

    //DatePicker
    private DatePickerFragment birthday_pick;

    // JSON Node names
    private static final String TAG_COUNTRY = "country";
    private static final String TAG_STATE = "state";
    private static final String TAG_CITY = "city";

    //button for activities
    private Button btnLinkToLoginScreen;
    private Button btnLinkToRegister;
    private Button auto;

    // components
    private EditText mEditTextuserName,mEditTextZip,mEditTextFname,mEditTextLname,birthday,mEditTextPhone,mEditTextEmail,mEditTextPassword,mEditTextValidatePassword;
    private Spinner  mSpinnerRole;
    private TextView mTextViewState,mTextViewCity;

    //Randomkey
    public static String Random;
    // Network_call
    private int registerAttempts = 5;
    private InputStream input;
    private HashMap<String, String> map = new HashMap<String, String>();

    public static User user = null;




    // URL to get Address JSON
    private static String url= "http://ziptasticapi.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        birthday_pick = new DatePickerFragment();

        //for Java mail
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //for Java mail
        StrictMode.setThreadPolicy(policy);

        //all components connected to UI
        auto = (Button) findViewById(R.id.autofill);
        birthday = (EditText)findViewById(R.id.register_birthday);
        mTextViewState = (TextView) findViewById(R.id.State);
        mTextViewCity = (TextView) findViewById(R.id.City);
        mEditTextuserName = (EditText) findViewById(R.id.userName);
        mEditTextFname = (EditText) findViewById(R.id.Fname);
        mEditTextZip = (EditText) findViewById(R.id.Zip);
        mEditTextLname = (EditText) findViewById(R.id.Lname);
        mEditTextPhone = (EditText) findViewById(R.id.Phone);
        mEditTextEmail = (EditText) findViewById(R.id.email);
        mEditTextPassword = (EditText) findViewById(R.id.password);
        mEditTextValidatePassword = (EditText) findViewById(R.id.password_chk);
        mSpinnerRole = (Spinner) findViewById(R.id.spinner2);

        mSpinnerRole.setGravity(20);
        //for mRole
        addItemsOnSpinner();


        //buttons connected to UI
        btnLinkToRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLoginScreen = (Button) findViewById(R.id.btnLinkToLoginScreen);

        //Birthday Fragment
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthday_pick.registerForContextMenu(birthday);
                birthday_pick.show(getFragmentManager(), "datepicker");
            }
        });
        //Phone Verification
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mEditTextPhone.setText(manager.getLine1Number());

        //Zip text change listener
        mEditTextZip.addTextChangedListener(ZipWatcher);

        auto.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
              mEditTextEmail.setText("bsohail99@yahoo.com");
              mEditTextuserName.setText("newuser1");
              mEditTextFname.setText("Timmy");
              mEditTextLname.setText("Huan");
              mEditTextZip.setText("78247");
              birthday.setText("3-04-1990");
              mEditTextPassword.setText("abc123");
              mEditTextValidatePassword.setText("abc123");
            }
        });
        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                try {
                    input = getApplicationContext().getAssets().open("fulgentcorp.crt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new RegisterCall().execute();

            }
        });

        // Link to Register Screen
        btnLinkToLoginScreen.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
    public static String random() {
        char[] chars1 = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        Random random1 = new Random();
        for (int i = 0; i < 5; i++)
        {
            char c1 = chars1[random1.nextInt(chars1.length)];
            sb1.append(c1);
        }
       return Random = sb1.toString();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(ClientRegisterActivity.this);
        alert.setTitle("Cancel");
        alert.setMessage("Are you sure you want to cancel?");
        alert.setNegativeButton(android.R.string.no, null);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        alert.show();
    }
    //Zip TextWatcher
    private final TextWatcher ZipWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() == 3 || s.length() >3){
                ZipToAddress();
            }
        }

        public void afterTextChanged(Editable s) {


        }
    };

    // add items into role spinner
    public void addItemsOnSpinner() {

        mSpinnerRole = (Spinner) findViewById(R.id.spinner2);
        List list = new ArrayList();
        list.add("Client");
        list.add("Runner");
        list.add("Both");

        ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerRole.setAdapter(dataAdapter);

    }

    //Phone verification intent
    private void openActivity(String Phone) {
        Intent verification = new Intent(this, VerificationActivity.class);
        verification.putExtra(INTENT_PHONENUMBER, Phone);

        startActivity(verification);
    }

    /**
     * Async task class to get json address by making HTTP call
     * */
    private class GetAddress extends AsyncTask<Void, Void, Void> {
        final String Zip = mEditTextZip.getText().toString();
        int ZipConverted=Integer.parseInt(Zip);
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ClientRegisterActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url+ZipConverted, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //final String CountryJ = jsonObj.getString(TAG_COUNTRY);
                    final String StateJ = jsonObj.getString(TAG_STATE);
                    final String CityJ = jsonObj.getString(TAG_CITY);
                       //Log.v(CountryJ,"All good" );

                    runOnUiThread(new Thread() {
                        public void run() {
                            TextView State = (TextView) findViewById(R.id.State);
                            TextView City = (TextView) findViewById(R.id.City);
                          //TextView Country = (TextView) findViewById(R.id.Country);
                            State.setText(StateJ);
                            City.setText(CityJ);
                           //Country.setText(CountryJ);
                        }

                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Thread() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Invalid zip: reenter zip", Toast.LENGTH_SHORT).show();
                            TextView State = (TextView) findViewById(R.id.State);
                            TextView City = (TextView) findViewById(R.id.City);
                            State.setText("");
                            City.setText("");
                        }

                    });
                }

            } else {


            }
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }
    // call to get zip to State and city
    public void ZipToAddress() {
        // Calling async task to get json
        new GetAddress().execute();
    }
    //Sending Welcome Email
    public void Email() {
        //Calling async task to send Json
        new SendEmailAsyncTask().execute();
    }

    //Error Dialog
    public void errorDialog(String message) {
        showDialog("Error", message);
    }

    //Show Error Dialog
    public void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Okay", null)
                .show();
    }

//ASYNC TASK to send email using the JAVA Mail API
class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
    final String Email = mEditTextEmail.getText().toString();

    Mail m = new Mail("hirundevolopers@gmail.com", "a3wv2j09jskjnv390kca4okpn");

    public SendEmailAsyncTask() {
        if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        String[] toArr = {Email};
        m.setTo(toArr);
        m.setFrom("Hi-Run!");
        m.setSubject("Welcome To Hi-Run");
        m.setBody("----Welcome to Hi-Run----"+ "\n"+"\nHere's your verification code: "+ user.getVerification() + "\n**Please note that email verfication is required to post jobs.**");
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

//register call to add user to database if no errors
public class RegisterCall extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ClientRegisterActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected void onPostExecute(Void aVoid) {

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(registerAttempts == 0)
            {
                if(map.size()>0) { //if there are errors in User model
                    Log.d(TAG, "Invalid User:");
                    if (map.get("username") != null) {                    //if user name error occurred
                        Toast.makeText(getApplicationContext(), "Invalid Username", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, map.get("username"));    //print out error
                    }

                    if (map.get("password") != null) {                    //if password error occurred
                        Toast.makeText(getApplicationContext(), "Password must have a character and numbers", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, map.get("password"));    //print out error
                    }

                    if (map.get("fName") != null) {                        //if first name error occurred
                        Toast.makeText(getApplicationContext(), "first name must contain at least 2 characters", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, map.get("fName"));        //print out error
                    }

                    if (map.get("lName") != null) {                        //if last name error occurred
                        Toast.makeText(getApplicationContext(), "Last name must contain at least 2 characters", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, map.get("lName"));        //print out error
                    }

                    if (map.get("email") != null) {                        //if email error occurred
                        Toast.makeText(getApplicationContext(), "Invalid email: check if email is valid", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, map.get("email"));        //print out error
                    }

                    if (map.get("city") != null) {                        //if city error occurred
                        Toast.makeText(getApplicationContext(), "City is empty: check if you pressed (enter) key on zipcode ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, map.get("city"));        //print out error
                    }

                    if (map.get("state") != null) {                        //if state error occurred
                        Toast.makeText(getApplicationContext(), "State is empty: check if you pressed (enter) on zipcode", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, map.get("state"));        //print out error
                    }

                    if (map.get("birthday") != null) {                    //if birthday error occurred
                        Toast.makeText(getApplicationContext(), "must enter birthday", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, map.get("birthday"));    //print out error
                    }

                    if (map.get("role") != null) {  //if role error occurred
                        Toast.makeText(getApplicationContext(), "Error on role", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, map.get("role"));        //print out error
                    } }
            Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Registration Successful!", Toast.LENGTH_SHORT).show();
            random();
            Email();
           //openActivity(mEditTextPhone.getText().toString());
            Intent i = new Intent(getApplicationContext(),
                    LoginActivity.class);
            startActivity(i);
            finish();

        }
        super.onPostExecute(aVoid);
    }

        @Override
        protected Void doInBackground(Void... params) {

            user = null;

            map = new HashMap<String, String>();//HashMap for errors

            registerAttempts = 3; //number of register attempts allowed

            String rusername = mEditTextuserName.getText().toString();
            String rpassword = mEditTextPassword.getText().toString();
            String retyped = mEditTextValidatePassword.getText().toString();
            String firstname = mEditTextFname.getText().toString();
            String lastname = mEditTextLname.getText().toString();
            String remail = mEditTextEmail.getText().toString();
            String rcity = mTextViewCity.getText().toString();
            String rstate = mTextViewState.getText().toString();
            String rbirthday = birthday.getText().toString();
            String rrole = mSpinnerRole.getSelectedItem().toString();
            String rzip = mEditTextZip.getText().toString();
            String rphone = mEditTextPhone.getText().toString();

            do{



                //create User from input
                user = new User(rusername, rpassword, retyped, firstname, lastname, rrole, rphone, remail, rcity, rstate,rzip,rbirthday);

                map = user.getErrors();	//put any errors into map

                if(map.size()>0){ //if there are errors in User model
                    Log.d(TAG, "Invalid User:");
                    if(map.get("username")!=null){					//if user name error occurred
                        Log.d(TAG, map.get("username")); 	//print out error
                    }

                    if(map.get("password")!=null){					//if password error occurred
                        Log.d(TAG, map.get("password")); 	//print out error
                    }

                    if(map.get("fName")!=null){						//if first name error occurred
                        Log.d(TAG, map.get("fName")); 		//print out error
                    }

                    if(map.get("lName")!=null){						//if last name error occurred
                        Log.d(TAG, map.get("lName")); 		//print out error
                    }

                    if(map.get("email")!=null){						//if email error occurred
                        Log.d(TAG, map.get("email")); 		//print out error
                    }

                    if(map.get("city")!=null){						//if city error occurred
                        Log.d(TAG, map.get("city")); 		//print out error
                    }

                    if(map.get("state")!=null){						//if state error occurred
                        Log.d(TAG, map.get("state")); 		//print out error
                    }

                    if(map.get("birthday")!=null){					//if birthday error occurred
                        Log.d(TAG, map.get("birthday")); 	//print out error
                    }

                    if(map.get("role")!=null){						//if role error occurred
                        Log.d(TAG, map.get("role")); 		//print out error
                    }

                    registerAttempts--;
                }else{ //valid User object created
//                    Log.d(TAG, "User Creation Successful!");
//                    Log.d(TAG, "Registering with database...");
                    try {
                        UserFunctions.AppAddUser(user, input);   //attempt to register with database
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(user.getUserID()!=0){		  //if registration was successful
                        Log.d(TAG, "Register Successful!");
                        Log.d(TAG, "User Id from database: " + user.getUserID());
                    }else{								//if registration was unsuccessful
                        Log.d(TAG, "Register Unsuccessful!");
                        Log.d(TAG, user.getErrors().get("database"));
                        //Toast.makeText(getApplicationContext(), user.getErrors().get("database"), Toast.LENGTH_LONG).show();
                    }
                }

            }while(map.size()>0 && registerAttempts!=0);
            return null;
        }
    }
    public static User getUser(){
        return user;
    }

}
