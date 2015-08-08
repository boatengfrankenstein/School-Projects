package com.team_08.hi_run.hi_run.Activites;

/**
 * Created by Sohail on 7/24/15.
 */

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.team_08.hi_run.hi_run.R;
import com.team_08.hi_run.hi_run.data.ChatAdapter;
import com.team_08.hi_run.hi_run.model.Message;

import java.util.ArrayList;
import java.util.List;

import serverside.serversrc.models.User;

public class BidActivity extends AppCompatActivity {

    //user
    User user = LoginActivity.user;

    private EditText message;

    private Button sendMessageButton;

    private Toolbar mToolbar;

    public static final String USER_ID_KEY = "userId";
    private String currentUserId;
    private ListView listView;
    private ArrayList<Message> mMessages;

    private ChatAdapter mAdapter;
    private Handler handler = new Handler();

    private  static final int MAX_CHAT_MSG_TO_SHOW = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        sendMessageButton = (Button) findViewById(R.id.buttonSend);
        message = (EditText)  findViewById(R.id.etMessage);
        sendMessageButton = (Button) findViewById(R.id.buttonSend);
        listView = (ListView) findViewById(R.id.listview_chat);
        mMessages = new ArrayList<Message>();
        mAdapter = new ChatAdapter(getApplicationContext(), currentUserId, mMessages);
        listView.setAdapter(mAdapter);

        if (user.getRole().equalsIgnoreCase("client")) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
            message.setInputType(InputType.TYPE_CLASS_TEXT);

        }
        else if(user.getRole().equalsIgnoreCase("runner")){
            mToolbar.setBackgroundColor(getResources().getColor(R.color.bg_login));
            message.setInputType(InputType.TYPE_CLASS_TEXT);
        }


        getCurrentUser();

        handler.postDelayed(runnable, 2000);

    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 2000);
        }
    };

    private void getCurrentUser() {
        currentUserId = ParseUser.getCurrentUser().getObjectId();
        messagePosting();
    }

    private void messagePosting() {



        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!message.getText().toString().equals("")) {

                    Message msg = new Message();
                    msg.setUserId(currentUserId);
                    if (user.getRole().equalsIgnoreCase("runner")){
                        msg.setBody( user.getUserName() + ": "+"$" +message.getText().toString());
                    }
                    else{
                    msg.setBody( user.getUserName() + ": "+ message.getText().toString());
                    }
                    msg.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            receiveMessage();
                        }
                    });
                    message.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Empty message!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void receiveMessage() {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.setLimit(MAX_CHAT_MSG_TO_SHOW);
        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged();
                    listView.invalidate();//allows for the listview to be redrawn
                } else {
                    Log.v("Error:", "Error:" + e.getMessage());
                }
            }
        });

    }

    private void refreshMessages() {
        receiveMessage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
             finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
