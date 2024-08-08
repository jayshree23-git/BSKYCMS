package com.project.bsky.bean;

import lombok.Data;

@Data
public class UrnwiseutilizeReportbean {
	
	private String nameofdistrict;
	private String distrctcode;
	private String districtid;
	private String urn;
	private String totalamount;
	private String nameofhof;
	private String numberofmember;
    private String packagename;
    private String mobileno;
    
    //for blockwise Data
    private String blockname;
    private String blockid;
    
    //for gpwise data
    private String gpname;
    private String gpid;
    
    //for Villagewise data
      private String villagename;
      private String villageid;
}
