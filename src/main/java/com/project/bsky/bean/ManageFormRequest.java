package com.project.bsky.bean;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageFormRequest {
    public String moduleId;
    public String moduleName;
    public String iteamId;
    public String itemId;
    public String itemStatus;
    public String formName;
    public String formType;
    public String tableName;
    public ArrayList<Addsection> addsection;
  //  public String addsection;
    public String formicon;
    public String guideline;
    public int servicemode;
    public int basedType;
    public String redirectURL;
    public String redirectWindowType;
    public String formdescription;
    public int configurationForpayment;
    public int configurationForapproval;
    public int configurationFordocument;
    public String configurationForviewcitizen;
    public int enableToAdminapplication;
    public int enableToWebsiteapplication;
    public int intCreatedBy;
    public int intUpdatedBy;
    public ArrayList<ApiUrldetail> apiUrldetails;
    public ArrayList<Referencedetail> referencedetails;
    public ArrayList<Statusdetail> statusdetails;
    public String vchProcessName;
    
    
}
