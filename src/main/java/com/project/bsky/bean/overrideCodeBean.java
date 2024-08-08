package com.project.bsky.bean;

import lombok.Data;
import org.json.JSONObject;

import java.util.Date;

@Data
public class overrideCodeBean {
    private Integer id;
    private String patientName;
    private String urn;
    private String hospitalCode;
    private String description;
    private String requestedDate;
    private String fpOverrideCode;
    private String hospitalName;
    private boolean status;
    private String remarks;
    private String generatedThrough;
    private  String memberId;
    private Integer noOfDays;
    private String pdfName;
    private String FULLNAMEENGLISH;
    private String AADHARNUMBER;
    private String Statusvalue;
    private String createdOn;
    private String approveStatus;
    private String approveDate;
    
}
