package com.project.bsky.bean;

import lombok.Data;

@Data
public class Bulkrevertbean {
  public String bulk_aprv_status;
  public String urn;
  public String patientname;
  public String packagename;
  public String procedurename;
  public String actualdateofdischarge;
  public String actualdateofadmission;
  //for submit
  private String formdate;
  private String todate;
  private Long userid;
}
