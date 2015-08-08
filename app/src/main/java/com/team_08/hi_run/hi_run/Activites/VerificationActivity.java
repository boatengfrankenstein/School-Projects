package com.team_08.hi_run.hi_run.Activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.verification.Config;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.sinch.verification.VerificationListener;
import com.team_08.hi_run.hi_run.R;


public class VerificationActivity extends Activity {

    private static final String TAG = Verification.class.getSimpleName();
    private final String APPLICATION_KEY = "0f07adbc-8d24-43ff-89e5-b36483c95ee9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
            String phoneNumber = intent.getStringExtra(ClientRegisterActivity.INTENT_PHONENUMBER);
            TextView phoneText = (TextView) findViewById(R.id.numberText);
            phoneText.setText(phoneNumber);
            createVerification(phoneNumber);



    }

    void createVerification(String phoneNumber) {
        Config config = SinchVerification.config().applicationKey(APPLICATION_KEY).context(getApplicationContext())
                .build();
        VerificationListener listener = new MyVerificationListener();
        Verification verification;
        verification = SinchVerification.createSmsVerification(config, phoneNumber, listener);

        verification.initiate();
    }

    class MyVerificationListener implements VerificationListener {

        @Override
        public void onInitiated() {
            Log.d(TAG, "Initialized!");
        }

        @Override
        public void onInitiationFailed(Exception exception) {
            Log.e(TAG, "Verification initialization failed: " + exception.getMessage());
            hideProgress(R.string.failed, false);
        }

        @Override
        public void onVerified() {
            Log.d(TAG, "Verified!");
            hideProgress(R.string.verified, true);
            Toast.makeText(getApplicationContext(), "Account Successfully Created!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }

        @Override
        public void onVerificationFailed(Exception exception) {
            Log.e(TAG, "Verification failed: " + exception.getMessage());
            Toast.makeText(getApplicationContext(), "Verification Failed", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), ClientRegisterActivity.class);
            startActivity(i);
            finish();
            hideProgress(R.string.failed, false);
        }
    }

    void hideProgress(int message, boolean success) {
        if (success) {
            ImageView checkMark = (ImageView) findViewById(R.id.checkmarkImage);
            checkMark.setVisibility(View.VISIBLE);
        }
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        progressBar.setVisibility(View.INVISIBLE);
        TextView progressText = (TextView) findViewById(R.id.progressText);
        progressText.setVisibility(View.INVISIBLE);
        TextView messageText = (TextView) findViewById(R.id.textView);
        messageText.setText(message);
    }


}
