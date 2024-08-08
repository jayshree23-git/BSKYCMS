package com.project.bsky.bean;

import lombok.Data;

@Data
public class specialitybean {
    public String packageheadercode;
    public String packageheader;
    
    
    //packagesubcategorycode
    public String packagesubcode;
    public String packagesubheader;
    
    
    //packagesubcategorycode
    public String procedurecode;
    public String subpackagename;
    
    
    //viewlist
    public String mandatorypreauth;
    public String maximumdays;
    public String packageexceptionflag;
    public String packageextention;
    public String priceeditable;
    public String preauthdocs;
    public String claimprocesseddocs;
    public String id;
    public String surgicaltype;
    
    //details
    private String proceduredescrption;
    private String packagecategorytype;
    private String staytype;
    private String daycare;
    private String multyprocedure;
    private String packageunderexception;
}
