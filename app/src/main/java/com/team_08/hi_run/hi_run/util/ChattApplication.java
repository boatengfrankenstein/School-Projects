package com.team_08.hi_run.hi_run.util;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import com.team_08.hi_run.hi_run.model.Message;

/**
 * Created by Sohail on 4/10/15.
 */
public class ChattApplication extends Application {

    public static final String APP_KEY_ID = "1YxcWigzT5alO8FRiqoLLnL2cqnRYARqVVC2eNR9";
    public static final String APP_CLIENT_ID = "75bUEQrn3juUcBbUbTIH3jR6wKzpiJRgFgFeDBRi";

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Message.class);

        Parse.initialize(this, APP_KEY_ID, APP_CLIENT_ID);

    }
}
