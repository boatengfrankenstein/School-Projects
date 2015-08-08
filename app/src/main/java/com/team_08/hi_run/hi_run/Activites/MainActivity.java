package com.team_08.hi_run.hi_run.Activites;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.team_08.hi_run.hi_run.Fragments.FaqFragment;
import com.team_08.hi_run.hi_run.Fragments.JobsFragment;
import com.team_08.hi_run.hi_run.Fragments.NotificationsFragment;
import com.team_08.hi_run.hi_run.Fragments.PaymentsFragment;
import com.team_08.hi_run.hi_run.Fragments.ProfileFragment;
import com.team_08.hi_run.hi_run.NavDrawer.NavigationDrawerCallbacks;
import com.team_08.hi_run.hi_run.NavDrawer.NavigationDrawerFragment;
import com.team_08.hi_run.hi_run.R;
import com.team_08.hi_run.hi_run.util.SlidingTabLayout;
import com.team_08.hi_run.hi_run.util.ViewPagerAdapter;

import org.json.JSONException;

import java.math.BigDecimal;

import serverside.serversrc.db.RunFunctions;
import serverside.serversrc.models.User;


public class MainActivity extends AppCompatActivity implements NavigationDrawerCallbacks {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private Handler mHandler;
    private final String TAG = "Main Activity";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK).clientId("Test_it");
    ViewPager pager;
    ViewPagerAdapter adapter;
    int selectedid;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Posted","Pending","Accepted","Completed"};
    int Numboftabs =4;
    User user = LoginActivity.getUser();
    String role=user.getRole().toLowerCase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        if (role.equals("client")) {

            mToolbar.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
            tabs.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
        }
        else if(role.equals("admin")){
            mToolbar.setBackgroundColor(getResources().getColor(R.color.sinch_purple));
            tabs.setBackgroundColor(getResources().getColor(R.color.sinch_purple));
        }
        else {

            mToolbar.setBackgroundColor(getResources().getColor(R.color.bg_login));
            tabs.setBackgroundColor(getResources().getColor(R.color.bg_login));
        }

        mHandler = new Handler();
        mHandler.postDelayed(m_Runnable, 5000);

        invalidateOptionsMenu();
        //getting intents from login activity
        String EMAIL = user.getEmail();
        String Fullname = user.getfName() +" "+ user.getlName();

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);


        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("Main Activity", String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer
        mNavigationDrawerFragment.setUserData(Fullname, EMAIL, BitmapFactory.decodeResource(getResources(), R.drawable.avatar));
    }


    //button actions for each of the items on drawer
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;

        switch (position) {
            case 0: //Jobs//
                if (role.equals("client")){
                    selectedid =1;
                }
                else{
                    selectedid=3;
                }
                invalidateOptionsMenu();
                fragment = getFragmentManager().findFragmentByTag(JobsFragment.TAG);

                if (fragment == null) {
                    fragment = new JobsFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.container, fragment, JobsFragment.TAG).commit();
                try {
                    if(tabs.getVisibility()==View.GONE||pager.getVisibility()==View.GONE)
                    {
                        tabs.setVisibility(View.VISIBLE);
                        pager.setVisibility(View.VISIBLE);
                    }
                }

                catch(NullPointerException e)
                {
                    Log.v("@","got null");
                }
                break;
            case 1: //Notifications//
                selectedid=2;
                invalidateOptionsMenu();
                if(tabs.getVisibility()==View.VISIBLE||pager.getVisibility()==View.VISIBLE)
                {
                    tabs.setVisibility(View.GONE);
                    pager.setVisibility(View.GONE);


                }
                fragment = getFragmentManager().findFragmentByTag(NotificationsFragment.TAG);
                if (fragment == null) {
                    fragment = new NotificationsFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.container, fragment, NotificationsFragment.TAG).commit();
                break;
            case 2: //my account //
                selectedid=2;
                invalidateOptionsMenu();
                if(tabs.getVisibility()==View.VISIBLE||pager.getVisibility()==View.VISIBLE)
                {
                    tabs.setVisibility(View.GONE);
                    pager.setVisibility(View.GONE);

                }
                mHandler.removeCallbacks(m_Runnable);
                fragment = getFragmentManager().findFragmentByTag(ProfileFragment.TAG);
                if (fragment == null) {
                    fragment = new ProfileFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.container, fragment, ProfileFragment.TAG).commit();
                break;
            case 3: //FAQS //
                selectedid=2;
                invalidateOptionsMenu();
                if(tabs.getVisibility()==View.VISIBLE||pager.getVisibility()==View.VISIBLE)
                {
                    tabs.setVisibility(View.GONE);
                    pager.setVisibility(View.GONE);

                }
                fragment = getFragmentManager().findFragmentByTag(FaqFragment.TAG);
                if (fragment == null) {
                    fragment = new FaqFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.container, fragment, FaqFragment.TAG).commit();
                break;
            case 4: //Payments//
                selectedid=2;
                invalidateOptionsMenu();
                if(tabs.getVisibility()==View.VISIBLE||pager.getVisibility()==View.VISIBLE)
                {
                    tabs.setVisibility(View.GONE);
                    pager.setVisibility(View.GONE);

                }


                fragment = getFragmentManager().findFragmentByTag(PaymentsFragment.TAG);
                if (fragment == null) {
                    fragment = new PaymentsFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.container, fragment, PaymentsFragment.TAG).commit();
                break;
        }


    }

    @Override
    public void onBackPressed() {
        if(mNavigationDrawerFragment.isDrawerOpen()) mNavigationDrawerFragment.closeDrawer();
        else {
            new AlertDialog.Builder(this);
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Exit");
            alert.setMessage("Are you sure you want to exit?");
            alert.setNegativeButton(android.R.string.no, null);
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });


            alert.show();
        }
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("200"), "USD", "sample item",
                paymentIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_addM:
                if (!user.getVerification().equals("Yes")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Email Verification");
                    builder.setMessage("Your email is not verfied" +
                            " navigate to 'my account' to input pin and update profile to get verfied. ")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            }).create().show();

                }
                else
                {
                    activity_start(new Intent(getApplicationContext(), PostActivity.class));
                }
                return true;
            case R.id.action_logout:
                activity_start(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            case R.id.action_refresh:
                //refresh runs
                new RefreshCall().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {

        switch (selectedid) {
            case 1:
                try {
                    menu.findItem(R.id.action_addM).setVisible(true);
                } catch (NullPointerException e) {
                    Log.v("search", "got null");
                }
                return true;
            case 2:

                try {
                    menu.findItem(R.id.action_addM).setVisible(false);
                } catch (NullPointerException e) {
                    Log.v("search2", "got null");
                }
                return true;
            case 3:
                try {
                    menu.findItem(R.id.action_addM).setVisible(false);
                } catch (NullPointerException e) {
                    Log.v("search2", "got null");
                }
                return true;
            case 4:
                try {
                    menu.findItem(R.id.action_logout).setVisible(false);
                } catch (NullPointerException e) {
                    Log.v("search2", "got null");
                }
                return true;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public class RefreshCall extends AsyncTask<Void, Void, Void>
    {
        private int post, pending, accepted, complete;
        @Override
        protected Void doInBackground(Void... params) {

            post = LoginActivity.getmPosted().size();
            pending = LoginActivity.getmPending().size();
            accepted = LoginActivity.getmAccepted().size();
            complete = LoginActivity.getmComplete().size();

            if(user.getRole().equalsIgnoreCase("client"))
            {
                //Log.d(TAG, "hes a client");
                try {
                    RunFunctions.ClientPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(user.getRole().equalsIgnoreCase("runner"))
            {
                //Log.d(TAG, "hes a runner");
                try {
                    RunFunctions.RunnerPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(user.getRole().equalsIgnoreCase("admin"))
            {
                //Log.d(TAG, "hes a admin");
                try {
                    RunFunctions.AdminPOV(user.getUserID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            LoginActivity.updatelists();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(post != LoginActivity.getmPosted().size() || pending != LoginActivity.getmPending().size() || accepted != LoginActivity.getmAccepted().size() || complete != LoginActivity.getmComplete().size())
            {
                int position;
                position = pager.getCurrentItem();
                adapter.notifyDataSetChanged();
                pager.setAdapter(adapter);
                pager.setCurrentItem(position);
            }
            //Toast.makeText(getApplicationContext(), "updated thing" + position, Toast.LENGTH_SHORT).show();

            //mHandler.postDelayed(m_Runnable,5000);
            super.onPostExecute(aVoid);

        }
    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            //Toast.makeText(getApplicationContext(), "in runnable", Toast.LENGTH_SHORT).show();

            mHandler.postDelayed(m_Runnable, 2000);
            new RefreshCall().execute();
        }

    };//runnable

    private void activity_start(Intent intent)
    {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "stopped when getting out");
        mHandler.removeCallbacks(m_Runnable);
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "returned!!");
        mHandler.postDelayed(m_Runnable, 2000);
        super.onResume();
    }


}