package com.project.bsky.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import lombok.Data;


@Data
public class Result{
    public int intProcessId;
    public int intModuleId;
    public String vchProcessName;
    public String vchFormType;
    public String vchTableName;
    public String txtSchemeDescription;
    public String vchModuleName;
    public JSONArray vchSection;
  //  public List<VchSection> vchSection;
    public String vchSchemePoster;
    public String vchSchemeGuideline;
    public int intServiceMode;
    public int intBaseType;
    public JSONArray vchAPIURLDtls;
   // public List<VchAPIURLDtl> vchAPIURLDtls;
   // public List<VchAPIReferenceURLDtl> vchAPIReferenceURLDtls;
    public JSONArray vchAPIReferenceURLDtls;
  //  public List<VchAPIStatusURLDtl> vchAPIStatusURLDtls;
    public JSONArray vchAPIStatusURLDtls;
    
    public String vchRedirectURL;
    public String vchredirectWindowType;
    public int intPayment;
    public int intApproval;
    public int intDocument;
    public int viewAtCitizen;
    public int intAdminApplication;
    public int intWebsiteApplication;
    public String dtmCreatedOn;
    public int intCreatedBy;
    public int tinStatus;
    public int tinPublishStatus;
    public int bitDeletedFlag;
    public int tinGridType;
    public String vchSchemeGuidelineUrl;
    public String vchSchemePosterUrl;
}
