package com.team_08.hi_run.hi_run.Fragments;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.team_08.hi_run.hi_run.Activites.LoginActivity;
import com.team_08.hi_run.hi_run.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import serverside.serversrc.db.RunFunctions;
import serverside.serversrc.db.UserFunctions;
import serverside.serversrc.models.User;


public class ProfileFragment extends Fragment {
    public static final String TAG = "Profile_Fragment";
    User user = LoginActivity.user;


    //Components
    private EditText mEditTextZip,mEditTextPhone,mEditTextEmail,mEditTextAddress,emailv;
    private TextView mName,mDOB;
    private Spinner mSpinnerRole;
    private EditText mTextViewState,mTextViewCity;
    private Button mupdate;

    //Dialog
    private ProgressDialog pDialog;

    private HashMap<String, String> map = new HashMap<String, String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container,
                false);

        //Components
        mName = (TextView) rootView.findViewById(R.id.nameu);
        mDOB = (TextView)  rootView.findViewById(R.id.Birthdayu);
        mupdate = (Button) rootView.findViewById(R.id.btnUpdate);
        mEditTextZip = (EditText) rootView.findViewById(R.id.Zipu);
        mEditTextAddress = (EditText) rootView.findViewById(R.id.Addressu);
        mEditTextPhone = (EditText) rootView.findViewById(R.id.Phoneu);
        mEditTextEmail = (EditText) rootView.findViewById(R.id.emailu);
        mTextViewCity = (EditText) rootView.findViewById(R.id.Cityu);
        mTextViewState = (EditText) rootView.findViewById(R.id.Stateu);
        mSpinnerRole = (Spinner) rootView.findViewById(R.id.spinner2u);
        emailv = (EditText) rootView.findViewById(R.id.emailV);
        //for mRole
        mSpinnerRole.setGravity(20);

        if (user.getVerification().equals("Yes")) {
            emailv.setVisibility(View.GONE);
        }
        //setting current user details
        mDOB.setText(user.getBirthday());
        mName.setText(user.getfName() +" "+ user.getlName());
        mEditTextZip.setText(user.getZipCode());
        mEditTextEmail.setText(user.getEmail());
        mTextViewState.setText(user.getState());
        mEditTextPhone.setText(user.getPhone());
        mTextViewCity.setText(user.getCity());
        mEditTextAddress.setText(user.getAddress());

        //For Spinner role
        List listC = new ArrayList();
        listC.add("Client");
        listC.add("Runner");
        listC.add("Both");
        ArrayAdapter dataAdapterC = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, listC);
        dataAdapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Spinner set adapter
        mSpinnerRole.setAdapter(dataAdapterC);

        if(user.getRole().equalsIgnoreCase("client"))
        {
            mSpinnerRole.setSelection(0);
        } else if(user.getRole().equalsIgnoreCase("runner"))
        {
            mSpinnerRole.setSelection(1);
        }
        else
        {
            mSpinnerRole.setSelection(2);
        }

        //
        if (user.getRole().equalsIgnoreCase("admin")){
            mupdate.setVisibility(View.GONE);
        }

        mupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateProfile().execute();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public class UpdateProfile extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Updating Profile...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        //


        @Override
        protected Void doInBackground(Void... params) {
            map = new HashMap<String, String>();//HashMap for errors


            do{

                String phone = mEditTextPhone.getText().toString();	     //user edits phone number
                String email = mEditTextEmail.getText().toString(); 	 //user edits email
                String city = mTextViewCity.getText().toString();	     //user edits city
                String address = mEditTextAddress.getText().toString();    //user edits address
                String state = mTextViewState.getText().toString();	     //user edits state
                String zipcode = mEditTextZip.getText().toString();	     //user edits zipcode
                String role = mSpinnerRole.getSelectedItem().toString(); //user edits role
                String pin = emailv.getText().toString();



                //create User from input
                user.editUser(role, phone, email, address,city, state, zipcode);
                // map = user.getErrors();	//put any errors into map

                if(map.size()>0){ //if there are errors in User model
                    System.out.println("\nInvalid Values:");

                    if(map.get("address")!=null){						//if address error occurred
                        Log.d(TAG, map.get("address"));    //print out error
                    }

                    if(map.get("phone")!=null){						//if phone error occurred
                        Log.d(TAG, map.get("phone"));    //print out error
                    }

                    if(map.get("email")!=null){						//if email error occurred
                        Log.d(TAG, map.get("email"));    //print out error
                    }

                    if(map.get("city")!=null){						//if city error occurred
                        Log.d(TAG, map.get("city"));    //print out error
                    }

                    if(map.get("state")!=null){						//if state error occurred
                        Log.d(TAG, map.get("state"));    //print out error
                    }

                    if(map.get("zipcode")!=null){						//if city error occurred
                        Log.d(TAG, map.get("zipcode"));    //print out error
                    }

                    if(map.get("role")!=null){						//if role error occurred
                        Log.d(TAG, map.get("role"));    //print out error
                    }

                }else{ //valid User object edited
                    System.out.println("\nUser Edit Successful!");
                    System.out.println(user.toString());
                    System.out.println("\nUpdating values in database...");
                    try {

                        if(!user.getVerification().equals("Yes")&& pin.equalsIgnoreCase(user.getVerification())){
                            UserFunctions.AppVerifyUser(user);
                        }
                        UserFunctions.AppUpdateUser(user);   //attempt to edit with database
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    if(user.getErrors().size()==0){		  //if registration was successful
//                        System.out.println("Update Successful!");
//                    }else{								//if registration was unsuccessful
//                        System.out.println("Update Unsuccessful!");
//                        System.out.println(user.getErrors().get("database"));
//                        Log.d(TAG,user.getErrors().get("email"));
//                        System.out.println();
//                    }
                }
            }while(map.size()>0);

            if(user.getRole().equalsIgnoreCase("client")) {
                try {
                    RunFunctions.ClientPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LoginActivity.updatelists();
            }
            else if(user.getRole().equalsIgnoreCase("runner"))
            {
                try {
                    RunFunctions.RunnerPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LoginActivity.updatelists();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            Intent intent = getActivity().getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().finish();
            startActivity(intent);
            super.onPostExecute(aVoid);
        }
    }


}

