package com.team_08.hi_run.hi_run.Fragments;

/**
 * Created by Sohail on 7/24/15.
 */

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import com.team_08.hi_run.hi_run.Activites.LoginActivity;
import com.team_08.hi_run.hi_run.data.ChatAdapter;
import com.team_08.hi_run.hi_run.model.Message;
import com.team_08.hi_run.hi_run.R;

import serverside.serversrc.models.User;

public class ChatFragment extends Fragment {

    public static final String TAG = "Chat_Fragment";

    //user
    User user = LoginActivity.user;
    //parse Login
    private String userName;
    private String password;

    private EditText message;

    private Button sendMessageButton;

    public static final String USER_ID_KEY = "userId";
    private String currentUserId;
    private ListView listView;
    private ArrayList<Message> mMessages;

    private ChatAdapter mAdapter;
    private Handler handler = new Handler();

    private  static final int MAX_CHAT_MSG_TO_SHOW = 70;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_chat,container,false);
        sendMessageButton = (Button) v.findViewById(R.id.buttonSend);
        message = (EditText) v.findViewById(R.id.etMessage);
        sendMessageButton = (Button) v.findViewById(R.id.buttonSend);
        listView = (ListView) v.findViewById(R.id.listview_chat);
        mMessages = new ArrayList<Message>();
        mAdapter = new ChatAdapter(getActivity(), currentUserId, mMessages);
        listView.setAdapter(mAdapter);
        super.onCreate(savedInstanceState);

        getCurrentUser();

        handler.postDelayed(runnable, 100);

        return v;
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 100);
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
                    msg.setBody( user.getUserName() + ": "+ message.getText().toString());
                    msg.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            receiveMessage();
                        }
                    });
                    message.setText("");
                } else {
                    Toast.makeText(getActivity(), "Empty message!",
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


}
