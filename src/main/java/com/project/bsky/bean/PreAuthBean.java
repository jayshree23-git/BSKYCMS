package com.project.bsky.bean;

import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;

@Data
public class PreAuthBean {
    private String LASTACTIONDATE;
    private Date APPROVEDDATE;
    private String SNADESCRIPTION;
    private String PROCEDURENAME;
    private Long CANCELLED;
    private Long PACKAGECOST;
    private Long AUTOREJECT;
    private Long EXPIRED;
    private Long QUERYCOMPLIED;
    private Long AUTOAPPROVE;
    private String HEDCODE;
    private Long IMPLANTCODE;
    private Long UNITPERPRICE;
    private Long TOTALPACKAGECOST;
    private Long TOTALWARDCOST;
    private Long TOTALIMPLANTCOST;
    private Long TOTALHEDCOST;
    private Long FAMILYEFUND;
    private Long FEMALEFUND;
    private String NEEDMOREDOCS1DATE;

    private String NEEDMOREDOCS2DATE;

    private String PROCEDURECODE;
    private Long AMOUNTBLOCKED;
    private Long INSUFFICIENTAMOUNT;
    private Long APPROVE;
    private Long REJECT;
    private Long FRESH;
    private Long QUERY;
    private String PACKAGEHEADERCODE;
    private String PACKAGEHEADERNAME;
    private String WARDNAME;
    private String WARDAMOUNT;
    private String IMPLANTNAME;
    private String UNIT;
    private String UNITCYCLEPRICE;
    private String IMPLANTAMOUNT;
    private String HEDNAME;
    private Long HEDUNIT;
    private Long HEDPRICEPERUNIT;
    private Long HEDPRICE;
    private String ADDTIONAL_DOC1;
    private String ADDTIONAL_DOC2;
    private String ADDTIONAL_DOC3;
    private String DOCUMENTSECOND;
    private String DOCUMENTTHIRD;
    private String REPLYSECOND;
    private String REPLYTHIRD;
    private String MOREDOCSDESCRIPTION;
    private String MOREDOCSDESCRIPTION2;
    private String NOOFDAYS;
    private Long APPROVEDAMOUNT;
    private Long AMOUNT;
    private Integer ID;
    private String URNNO;
    private String AUTHORITYCODE;
    private String HOSPITALCODE;
    private String HOSPITALAUTHCODE;
    private String SDATE;
    private String MEMBERNAME;
    private String DESCRIPTION;
    private String STATECODE;
    private String STATUS;
    private boolean statusFlag;
    private String DISTRICTCODE;
    private Date POLICYSTARTDATE;
    private Date POLICYENDDATE;
    private String PDFPATH;
    private String HOSPITALUPLOADDATE;
    private String NEEDMOREDOCS;
    private String IMPLANTDATA;
    private String REFERRALCODE;
    private String HospitalName;
    private Integer QUERYCOUNT;
    private String SNAREMARKS;
    private Long TXNPACKAGEDETAILID;
    private String gender;
    private String snouserid;
}
