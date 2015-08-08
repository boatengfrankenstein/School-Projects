package com.team_08.hi_run.hi_run.model;

import java.io.Serializable;

/**
 * Created by Sohail on 6/28/15.
 */
public class Job {
    private static final long serialVerisionUID = 10L;
    private String jobName;
    private String jobDescription;
    private String jobAddress;
    private int jobPay;
    private int JobId;
    private String recordDate;

    public Job(String jobName, String jobDescription, String jobAddress, int jobPay, int jobId, String recordDate) {
        jobName = jobName;
        jobDescription = jobDescription;
        jobAddress = jobAddress;
        jobPay = jobPay;
        //JobId = jobId;
        recordDate = recordDate;
    }
    public Job(){

    }
    public static long getSerialVerisionUID() {
        return serialVerisionUID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public int getJobPay() {
        return jobPay;
    }

    public void setJobPay(int jobPay) {
        this.jobPay = jobPay;
    }

    public int getJobId() {
        return JobId;
    }

    public void setJobId(int jobId) {
        JobId = jobId;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

}
