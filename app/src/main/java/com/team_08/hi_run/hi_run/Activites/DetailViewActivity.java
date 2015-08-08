package com.team_08.hi_run.hi_run.Activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.team_08.hi_run.hi_run.R;
import com.team_08.hi_run.hi_run.data.DatabaseHandler;
import com.team_08.hi_run.hi_run.model.Job;


public class DetailViewActivity extends AppCompatActivity {
    //UI Components initialized
    private TextView JobName, JobDescription, JobPay, JobAddress, JobDate;
    private Button Delete;
    private Button Flag;

    //get the unique ID associated with each JOB
    private int jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        //UI components connected
        JobName = (TextView) findViewById(R.id.JobNameDV);
        JobDescription = (TextView) findViewById(R.id.JobDescriptionDV);
        JobPay = (TextView) findViewById(R.id.JobPayDV);

        JobDate = (TextView) findViewById(R.id.JobDateDV);
        Delete = (Button) findViewById(R.id.DeleteButtonDV);

        //Model component get frozen state
        Job job = (Job) getIntent().getSerializableExtra("userObj");

        //fill the textfield with data
        JobName.setText(job.getJobName());
        JobDescription.setText(job.getJobDescription());
        JobPay.setText("$"+String.valueOf(job.getJobPay()));

        JobDate.setText("Posted On:"+job.getRecordDate());

        //set min height for description
        JobDescription.setMinHeight(120);
        //get unique ID of job
        jobId = job.getJobId();


       //delete button displays alert dialog
       Delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //initialize alert dialog
               AlertDialog.Builder alert = new AlertDialog.Builder(DetailViewActivity.this);
               alert.setTitle("Delete");
               alert.setMessage("Are you sure you want to delete this Job?");
               alert.setNegativeButton("No", null);
               //on yes click the DBA clears the job from the jobs table
               alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       //call to get DBA data
                       DatabaseHandler dba = new DatabaseHandler(getApplicationContext());

                       //call to delete the job using ID
                       dba.deleteJob(jobId);
                       //toast to display job delelted
                       Toast.makeText(getApplicationContext(), "Job Deleted!", Toast.LENGTH_LONG)
                               .show();
                       //go back to main activity
                       startActivity(new Intent(DetailViewActivity.this, MainActivity.class));


                       //remove this activity from activity stack
                       DetailViewActivity.this.finish();
                   }

               });
               alert.show();


           }
       });
    }
}
