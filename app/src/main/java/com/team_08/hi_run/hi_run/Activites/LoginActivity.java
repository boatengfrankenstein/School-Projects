package com.team_08.hi_run.hi_run.Activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.team_08.hi_run.hi_run.R;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import serverside.serversrc.db.RunFunctions;
import serverside.serversrc.db.UserFunctions;
import serverside.serversrc.models.Run;
import serverside.serversrc.models.User;


public class    LoginActivity extends Activity implements View.OnClickListener {
    //buttons for UI
    private Button btnLogin;
    private Button btnLinkToRegister;
    private LinearLayout login;
    //Jobs lists
    private static ArrayList<Run> mPosted, mComplete, mAccepted, mPending;


    //Strings to store in shared preference
    private String username,password;

    //UI checkbox to add username and password to shared preference
    private CheckBox saveLoginCheckBox;

    //declaring Shared preferences requirements
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    //To determine whther box is checked
    private Boolean saveLogin;

    //Dialog
    private ProgressDialog pDialog;

    private static final String TAG = "LoginActivity";

    //UI editext fields for password and username
    private EditText inputEmail;
    private EditText inputPassword;

    //Database asset and model
    private InputStream caInput = null;
    public static User user = null;

    //max attempts of login
    private int loginAttempts = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (LinearLayout) findViewById(R.id.LLogin);
        // Prepare the View for the animation
        Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_up);

        login.startAnimation(bottomUp);
        login.setVisibility(View.VISIBLE);

        //UI connected to interface
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnLogin.setOnClickListener(this);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);

        //Shared preference for remember me
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        //Shared preference store email
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            inputEmail.setText(loginPreferences.getString("username", ""));
            inputPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
        //Link to register
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),
                        ClientRegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
    //Login Button
    public void onClick(View view) {
        if (view == btnLogin) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputEmail.getWindowToken(), 0);

            //User and pass to string
            username = inputEmail.getText().toString();
            password = inputPassword.getText().toString();

            //if UI feilds empty put toast message
            if (inputEmail.equals("") || inputPassword.equals("")) {
                Toast.makeText(getApplicationContext(),"All feilds required", Toast.LENGTH_LONG).show();
            }
            //else login and check if check box is checked, if checked hold info, else clear data
            else {
                // Prepare the View for the animation
                Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.bottom_down);

                login.startAnimation(bottomUp);
                login.setVisibility(View.GONE);


                if (saveLoginCheckBox.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

                parseCreateOrLogin();
                Login();
            }
        }
    }

    //call to login Async task
    public void Login() {

        try {
            caInput = getApplicationContext().getAssets().open("fulgentcorp.crt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        new LoginCall().execute();

    }
    //attempt to login if successful add items to intent put extra
    public class LoginCall extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            //login is failure show toast
            if (loginAttempts == 0) {
                Toast.makeText(getApplicationContext(), "Incorrect Username/Password", Toast.LENGTH_SHORT).show();
                // Prepare the View for the animation
                Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.bottom_up);
                login.setVisibility(View.VISIBLE);
                login.startAnimation(bottomUp);

                Log.d(TAG, "Failed to login");
            }
            //if successful add user details to intent
            else {


                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }

            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {

            loginAttempts = 3;
            user = null;
            HashMap<String, String> login_map = new HashMap<String, String>();

            do {

                System.out.println("\nProcessing...");
                try {
                    user = UserFunctions.AppLogin(inputEmail.getText().toString(), inputPassword.getText().toString(), caInput); //Login attempted through database
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                login_map = user.getErrors();                            //put any errors into map
                if (login_map.get("database") != null) {                    //if database error occurred, e.g. bad username or password
                    Log.d(TAG, login_map.get("database") + "\n"); //print out error
                    loginAttempts--;
                }

            } while (login_map.size() > 0 && loginAttempts != 0);


            if (user.getRole().equalsIgnoreCase("client")) {
                Log.d(TAG, "hes a client");
                try {
                    RunFunctions.ClientPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (user.getRole().equalsIgnoreCase("runner")) {
                Log.d(TAG, "hes a runner");
                try {
                    RunFunctions.RunnerPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (user.getRole().equalsIgnoreCase("admin")) {
                Log.d(TAG, "hes a admin");
                try {
                    RunFunctions.AdminPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    RunFunctions.BothPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            updatelists();

            return null;

        }
    }
    public static class RefreshCall extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {


            if(user.getRole().equalsIgnoreCase("client"))
            {
                Log.d(TAG, "hes a client");
                try {
                    RunFunctions.ClientPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(user.getRole().equalsIgnoreCase("runner"))
            {
                Log.d(TAG, "hes a runner");
                try {
                    RunFunctions.RunnerPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(user.getRole().equalsIgnoreCase("admin"))
            {
                Log.d(TAG, "hes a admin");
                try {
                    RunFunctions.AdminPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            updatelists();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//                Toast.makeText(ge(), "Runs Updated", Toast.LENGTH_SHORT).show();

        }
    }




    public static ArrayList<Run> getmPosted()
    {
        return mPosted;
    }
    public static ArrayList<Run> getmAccepted() { return mAccepted; }
    public static ArrayList<Run> getmComplete() { return mComplete; }
    public static ArrayList<Run> getmPending() { return mPending; }

    public static User getUser(){
        return user;
    }

    //Parse create or login
    public void parseCreateOrLogin() {
        final String uName = username;
        final String pWord = password;
        ParseUser.logInInBackground(uName, pWord, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {

                    //Toast.makeText(getApplicationContext(), "Login Successfully!", Toast.LENGTH_LONG).show();

                } else {
                    //CREATE PARSE USER
                    ParseUser user = new ParseUser();
                    user.setUsername(uName);
                    user.setPassword(pWord);
                    //Add Parse USER
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //we are good!
                               //Toast.makeText(getApplicationContext(), "Parse created user successfully", Toast.LENGTH_LONG).show();
                            } else {
                               //Toast.makeText(getApplicationContext(), "failed to create in parse", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }

            }
        });

    }

    public static void updatelists()
    {
        mPosted = RunFunctions.getqRuns();
        mComplete = RunFunctions.getcRuns();
        mAccepted = RunFunctions.getaRuns();
        mPending = RunFunctions.getpRuns();
    }


}