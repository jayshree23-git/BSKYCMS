package com.project.bsky.serviceImpl;

import com.project.bsky.repository.ActionRemarkRepository;
import com.project.bsky.service.OldBlockedClaimMonitoringService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Project : BSKY Backend
 * @Author : Sambit Kumar Pradhan
 * @Created On : 11/07/2023 - 4:11 PM
 */
@Service
public class OldBlockedClaimMonitoringServiceImpl implements OldBlockedClaimMonitoringService {

    private static final ResourceBundle bskyFileResourceBundle = ResourceBundle.getBundle("fileConfiguration");

    @Autowired
    private Logger logger;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ActionRemarkRepository actionRemarkRepository;

    @Override
    public Object getOldBlockedClaimList(Map<String, Object> request) throws Exception {
        //logger.info("Inside OldBlockedClaimMonitoringServiceImpl getOldBlockedClaimList method");
        List<Map<String, Object>> responseMapList = new ArrayList<>();
        final String NA = "NA";
        try {
            StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("USP_OLDBLOCKEDDATAVIEWBYSNA")
                    .registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_USERID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_TXNPACKAGEDETAILID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_PENDINGAT", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_CLAIMSTATUS", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
                    .registerStoredProcedureParameter("CUR_INFO", void.class, ParameterMode.REF_CURSOR)
                    .registerStoredProcedureParameter("CUR_IMP", void.class, ParameterMode.REF_CURSOR)
                    .registerStoredProcedureParameter("CUR_HED", void.class, ParameterMode.REF_CURSOR)
                    .registerStoredProcedureParameter("CUR_APV", void.class, ParameterMode.REF_CURSOR)
                    .registerStoredProcedureParameter("CUR_URNINFO", void.class, ParameterMode.REF_CURSOR)
                    .registerStoredProcedureParameter("CUR_BALANCE", void.class, ParameterMode.REF_CURSOR);

            if (request.get("action").equals("A")) {
                storedProcedureQuery.setParameter("P_ACTION", request.get("action"));
                storedProcedureQuery.setParameter("P_USERID", request.get("userId"));
                storedProcedureQuery.setParameter("P_FROMDATE", new SimpleDateFormat("dd-MMM-yyyy").parse(request.get("fromDate").toString()));
                storedProcedureQuery.setParameter("P_TODATE", new SimpleDateFormat("dd-MMM-yyyy").parse(request.get("toDate").toString()));
                storedProcedureQuery.setParameter("P_STATECODE", request.get("stateCode"));
                storedProcedureQuery.setParameter("P_DISTCODE", request.get("districtCode"));
                storedProcedureQuery.setParameter("P_HOSPITALCODE", request.get("hospitalCode"));
                storedProcedureQuery.setParameter("P_FLAG", request.get("flag"));

                storedProcedureQuery.execute();

                List<?> resultList = storedProcedureQuery.getResultList();

                return resultList.stream()
                        .map(result -> {
                            Object[] resultArray = (Object[]) result;
                            Map<String, Object> resultMap = new LinkedHashMap<>();

                            resultMap.put("transactionId", resultArray[0] != null ? resultArray[0] : NA);
                            resultMap.put("txnPackageDetailId", resultArray[1] != null ? resultArray[1] : NA);
                            resultMap.put("id", resultArray[2] != null ? resultArray[2] : NA);
                            resultMap.put("queryCount", resultArray[3] != null ? resultArray[3] : NA);
                            resultMap.put("urn", resultArray[4] != null ? resultArray[4] : NA);
                            resultMap.put("memberId", resultArray[5] != null ? resultArray[5] : NA);
                            resultMap.put("hospitalCode", resultArray[6] != null ? resultArray[6] : NA);
                            resultMap.put("hospitalName", resultArray[7] != null ? resultArray[7] : NA);
                            resultMap.put("patientName", resultArray[8] != null ? resultArray[8] : NA);
                            resultMap.put("admissionDate", resultArray[9] != null ? resultArray[9] : NA);
                            resultMap.put("requestDate", resultArray[10] != null ? resultArray[10] : NA);
                            resultMap.put("invoiceNo", resultArray[11] != null ? resultArray[11] : NA);
                            resultMap.put("procedureName", resultArray[12] != null ? resultArray[12] : NA);
                            resultMap.put("procedureCode", resultArray[13] != null ? resultArray[13] : NA);
                            resultMap.put("status", resultArray[14] != null ? resultArray[14] : NA);
                            resultMap.put("hospitalRemark", resultArray[15] != null ? resultArray[15] : NA);

                            return resultMap;
                        })
                        .collect(Collectors.toList());
            } else if (request.get("action").equals("B")) {
            	Integer clms=Integer.parseInt((String) request.get("clmstatus"));
            	
            	Integer pendingat=0;
            	Integer claimstatus=0;
            			switch (clms) {
						case 0:pendingat=null;claimstatus=null;							
							break;
						case 1:pendingat=1;claimstatus=0;							
							break;
						case 2:pendingat=1;claimstatus=3;							
							break;
						case 3:pendingat=2;claimstatus=1;							
							break;
						case 4:pendingat=2;claimstatus=2;							
							break;
						case 5:pendingat=0;claimstatus=3;							
							break;
						case 6:pendingat=2;claimstatus=3;							
							break;
						case 7:pendingat=3;claimstatus=1;							
							break;
						case 8:pendingat=3;claimstatus=2;							
							break;
						case 9:pendingat=0;claimstatus=4;							
							break;
						case 10:pendingat=2;claimstatus=4;							
							break;
						case 11:pendingat=3;claimstatus=3;							
							break;
						case 12:pendingat=2;claimstatus=7;							
							break;
						default:pendingat=null;claimstatus=null;
							break;
						}
            	
                storedProcedureQuery.setParameter("P_ACTION", request.get("action"));
                storedProcedureQuery.setParameter("P_USERID", request.get("userId"));
                storedProcedureQuery.setParameter("P_FROMDATE", new SimpleDateFormat("dd-MMM-yyyy").parse(request.get("fromDate").toString()));
                storedProcedureQuery.setParameter("P_TODATE", new SimpleDateFormat("dd-MMM-yyyy").parse(request.get("toDate").toString()));
                storedProcedureQuery.setParameter("P_STATECODE", request.get("stateCode").toString());
                storedProcedureQuery.setParameter("P_DISTCODE", request.get("districtCode").toString());
                storedProcedureQuery.setParameter("P_HOSPITALCODE", request.get("hospitalCode"));
                storedProcedureQuery.setParameter("P_FLAG", request.get("flag"));
                storedProcedureQuery.setParameter("P_PENDINGAT", pendingat);
                storedProcedureQuery.setParameter("P_CLAIMSTATUS", claimstatus);

                storedProcedureQuery.execute();

                List<?> resultList = storedProcedureQuery.getResultList();

                return resultList.stream()
                        .map(result -> {
                            Object[] resultArray = (Object[]) result;
                            Map<String, Object> resultMap = new LinkedHashMap<>();

                            resultMap.put("transactionId", resultArray[0] != null ? resultArray[0] : NA);
                            resultMap.put("txnPackageDetailId", resultArray[1] != null ? resultArray[1] : NA);
                            resultMap.put("id", resultArray[2] != null ? resultArray[2] : NA);
                            resultMap.put("urn", resultArray[3] != null ? resultArray[3] : NA);
                            resultMap.put("memberId", resultArray[4] != null ? resultArray[4] : NA);
                            resultMap.put("hospitalCode", resultArray[5] != null ? resultArray[5] : NA);
                            resultMap.put("hospitalName", resultArray[6] != null ? resultArray[6] : NA);
                            resultMap.put("patientName", resultArray[7] != null ? resultArray[7] : NA);
                            resultMap.put("admissionDate", resultArray[8] != null ? DateFormat.formatStringToDate((String) resultArray[8]) : NA);
                            resultMap.put("requestDate", resultArray[9] != null ? DateFormat.formatStringToDate((String) resultArray[9]) : NA);
                            resultMap.put("invoiceNo", resultArray[10] != null ? resultArray[10] : NA);
                            resultMap.put("procedureName", resultArray[11] != null ? resultArray[11] : NA);
                            resultMap.put("procedureCode", resultArray[12] != null ? resultArray[12] : NA);
                            resultMap.put("status", resultArray[13] != null ? resultArray[13] : NA);
                            resultMap.put("dischargeStatus", resultArray[14] != null ? resultArray[14] : NA);
                            resultMap.put("dateOfDischarge", resultArray[15] != null ? resultArray[15] : NA);
                            resultMap.put("claimStatus", resultArray[16] != null ? resultArray[16] : NA);
                            resultMap.put("claimSubmitDate", resultArray[17] != null ? resultArray[17] : NA);
                            resultMap.put("currentCPDName", resultArray[18] != null ? resultArray[18] : NA);
                            resultMap.put("currentClaimStatus", resultArray[19] != null ? resultArray[19] : NA);
                            
                            return resultMap;
                        })
                        .collect(Collectors.toList());
            } else if (request.get("action").equals("C")) {
                Map<String, Object> responseMap = new LinkedHashMap<>();
                storedProcedureQuery.setParameter("P_ACTION", request.get("action"));
                storedProcedureQuery.setParameter("P_TXNPACKAGEDETAILID", request.get("txnPackageDetailId"));
                storedProcedureQuery.setParameter("P_ID", request.get("id"));

                storedProcedureQuery.execute();

                ResultSet curInfo = (ResultSet) storedProcedureQuery.getOutputParameterValue("CUR_INFO");
                ResultSet curImp = (ResultSet) storedProcedureQuery.getOutputParameterValue("CUR_IMP");
                ResultSet curHed = (ResultSet) storedProcedureQuery.getOutputParameterValue("CUR_HED");
                ResultSet  curApv= (ResultSet) storedProcedureQuery.getOutputParameterValue("CUR_APV");
                ResultSet curUrnInfo = (ResultSet) storedProcedureQuery.getOutputParameterValue("CUR_URNINFO");
                ResultSet curBalance = (ResultSet) storedProcedureQuery.getOutputParameterValue("CUR_BALANCE");

                if (curInfo != null && curInfo.next()) {
                    Map<String, Object> resultMap = new LinkedHashMap<>();
                    resultMap.put("transactionId", curInfo.getString(1) != null ? curInfo.getString(1) : "NA");
                    resultMap.put("txnPackageDetailId", curInfo.getString(2) != null ? curInfo.getString(2) : "NA");
                    resultMap.put("id", curInfo.getString(3) != null ? curInfo.getString(3) : "NA");
                    resultMap.put("urn", curInfo.getString(4) != null ? curInfo.getString(4) : "NA");
                    resultMap.put("memberId", curInfo.getString(5) != null ? curInfo.getString(5) : "NA");
                    resultMap.put("patientName", curInfo.getString(6) != null ? curInfo.getString(6) : "NA");
                    resultMap.put("hospitalCode", curInfo.getString(7) != null ? curInfo.getString(7) : "NA");
                    resultMap.put("hospitalName", curInfo.getString(8) != null ? curInfo.getString(8) : "NA");
                    resultMap.put("admissionDate",  curInfo.getString(9) != null ? DateFormat.formatStringToDate(curInfo.getString(9)) : "NA");
                    resultMap.put("blockingDate", curInfo.getString(10) != null ? DateFormat.formatStringToDate(curInfo.getString(10)) : "NA");
                    resultMap.put("phoneNo", curInfo.getString(11) != null ? curInfo.getString(11) : "NA");
                    resultMap.put("invoiceNo", curInfo.getString(12) != null ? curInfo.getString(12) : "NA");
                    resultMap.put("caseNo", curInfo.getString(13) != null ? curInfo.getString(13) : "NA");
                    resultMap.put("gender", curInfo.getString(14) != null ? curInfo.getString(14) : "NA");
                    resultMap.put("policyStartDate", curInfo.getString(15) != null ? DateFormat.formatStringToDate(curInfo.getString(15)) : "NA");
                    resultMap.put("policyEndDate", curInfo.getString(16) != null ? DateFormat.formatStringToDate(curInfo.getString(16)) : "NA");
                    resultMap.put("latestStatus", curInfo.getString(17) != null ? curInfo.getString(17) : "NA");
                    resultMap.put("packageCode", curInfo.getString(18) != null ? curInfo.getString(18) : "NA");
                    resultMap.put("packageName", curInfo.getString(19) != null ? curInfo.getString(19) : "NA");
                    resultMap.put("procedureName", curInfo.getString(20) != null ? curInfo.getString(20) : "NA");
                    resultMap.put("procedureCode", curInfo.getString(21) != null ? curInfo.getString(21) : "NA");
                    resultMap.put("wardName", curInfo.getString(22) != null ? curInfo.getString(22) : "NA");
                    resultMap.put("preAuthStatus", curInfo.getString(23) != null ? curInfo.getString(23) : "NA");
                    resultMap.put("blockedAmount", curInfo.getString(24) != null ? curInfo.getString(24) : "NA");
                    resultMap.put("verifiedMemberName", curInfo.getString(25) != null ? curInfo.getString(25) : "NA");
                    resultMap.put("hospitalState", curInfo.getString(26) != null ? curInfo.getString(26) : "NA");
                    resultMap.put("hospitalDistrict", curInfo.getString(27) != null ? curInfo.getString(27) : "NA");
                    resultMap.put("noOfDays", curInfo.getString(28) != null ? curInfo.getString(28) : "NA");
                    resultMap.put("approvalStatus", curInfo.getString(29) != null ? curInfo.getString(29) : "NA");

                    responseMap.put("patientInfo", resultMap);
                }

                if (curImp != null) {
                    Map<String, Object> implantMap = new LinkedHashMap<>();
                    List<Map<String, Object>> resultMapList = new ArrayList<>();
                    while (curImp.next()) {
                        Map<String, Object> resultMap = new LinkedHashMap<>();
                        resultMap.put("implantCode", curImp.getString(1) != null ? curImp.getString(1) : "NA");
                        resultMap.put("implantName", curImp.getString(2) != null ? curImp.getString(2) : "NA");
                        resultMap.put("unit", curImp.getString(3) != null ? curImp.getString(3) : "NA");
                        resultMap.put("amount", curImp.getString(4) != null ? curImp.getString(4) : "NA");

                        resultMapList.add(resultMap);
                    }
                    implantMap.put("implantInfoList", resultMapList);

                    responseMap.put("implantInfo", implantMap);
                }

                if (curHed != null) {
                    Map<String, Object> hedMap = new LinkedHashMap<>();
                    List<Map<String, Object>> resultMapList = new ArrayList<>();
                    while (curHed.next()) {
                        Map<String, Object> resultMap = new LinkedHashMap<>();
                        resultMap.put("hedCode", curHed.getString(1) != null ? curHed.getString(1) : "NA");
                        resultMap.put("hedName", curHed.getString(2) != null ? curHed.getString(2) : "NA");
                        resultMap.put("unit", curHed.getString(3) != null ? curHed.getString(3) : "NA");
                        resultMap.put("amount", curHed.getString(4) != null ? curHed.getString(4) : "NA");

                        resultMapList.add(resultMap);
                    }
                    hedMap.put("hedInfoList", resultMapList);

                    responseMap.put("hedInfo", hedMap);
                }

                if (curApv != null && curApv.next()) {
                    Map<String, Object> resultMap = new LinkedHashMap<>();
                    resultMap.put("queryCount", curApv.getString(1) != null ? curApv.getString(1) : "NA");
                    resultMap.put("requestDate", curApv.getString(2) != null ? DateFormat.formatStringToDate(curApv.getString(2)) : "NA");
                    resultMap.put("remark", curApv.getString(3) != null ? curApv.getString(3) : "NA");
                    resultMap.put("approveDate", curApv.getString(4) != null ? curApv.getString(4) : "NA");
                    resultMap.put("approveBy", curApv.getString(5) != null ? curApv.getString(5) : "NA");
                    resultMap.put("snaRemark", curApv.getString(6) != null ? curApv.getString(6) : "NA");
                    resultMap.put("snaDescription", curApv.getString(7) != null ? curApv.getString(7) : "NA");
                    resultMap.put("queryRemark1", curApv.getString(8) != null ? curApv.getString(8) : "NA");
                    resultMap.put("queryDescription1", curApv.getString(9) != null ? curApv.getString(9) : "NA");
                    resultMap.put("queryDate1", curApv.getString(10) != null ? DateFormat.formatStringToDate(curApv.getString(10)) : "NA");
                    resultMap.put("hospitalReplayRemark1", curApv.getString(11) != null ? curApv.getString(11) : "NA");
                    resultMap.put("hospitalReplayDate1", curApv.getString(12) != null ? DateFormat.formatStringToDate(curApv.getString(12)) : "NA");
                    resultMap.put("queryRemark2", curApv.getString(13) != null ? curApv.getString(13) : "NA");
                    resultMap.put("queryDescription2", curApv.getString(14) != null ? curApv.getString(14) : "NA");
                    resultMap.put("queryDate2", curApv.getString(15) != null ? DateFormat.formatStringToDate(curApv.getString(15)) : "NA");
                    resultMap.put("hospitalReplayRemark2", curApv.getString(16) != null ? curApv.getString(16) : "NA");
                    resultMap.put("hospitalReplayDate2", curApv.getString(17) != null ? DateFormat.formatStringToDate(curApv.getString(17)) : "NA");
                    resultMap.put("doc1", curApv.getString(18) != null ? curApv.getString(18) : "NA");
                    resultMap.put("doc2", curApv.getString(19) != null ? curApv.getString(19) : "NA");
                    resultMap.put("doc3", curApv.getString(20) != null ? curApv.getString(20) : "NA");
                    resultMap.put("uploadYear", curApv.getString(21) != null ? curApv.getString(21) : "NA");

                    responseMap.put("approvalInfo", resultMap);
                }

                if (curUrnInfo != null) {
                    List<Map<String, Object>> resultMapList = new ArrayList<>();
                    while (curUrnInfo.next()) {
                        Map<String, Object> resultMap = new LinkedHashMap<>();
                        resultMap.put("hospitalName", curUrnInfo.getString(1) != null ? curUrnInfo.getString(1) : "NA");
                        resultMap.put("hospitalCode", curUrnInfo.getString(2) != null ? curUrnInfo.getString(2) : "NA");
                        resultMap.put("memberId", curUrnInfo.getString(3) != null ? curUrnInfo.getString(3) : "NA");
                        resultMap.put("patientName", curUrnInfo.getString(4) != null ? curUrnInfo.getString(4) : "NA");
                        resultMap.put("caseNo", curUrnInfo.getString(5) != null ? curUrnInfo.getString(5) : "NA");
                        resultMap.put("procedureName", curUrnInfo.getString(6) != null ? curUrnInfo.getString(6) : "NA");
                        resultMap.put("procedureCode", curUrnInfo.getString(7) != null ? curUrnInfo.getString(7) : "NA");
                        resultMap.put("amountBlocked", curUrnInfo.getString(8) != null ? curUrnInfo.getString(8) : "NA");
                        resultMap.put("actualDateOfAdmission", curUrnInfo.getString(9) != null ? DateFormat.formatStringToDate(curUrnInfo.getString(9)) : "NA");
                        resultMap.put("blockedDate", curUrnInfo.getString(10) != null ? DateFormat.formatStringToDate(curUrnInfo.getString(10)) : "NA");
                        resultMap.put("status", curUrnInfo.getString(11) != null ? curUrnInfo.getString(11) : "NA");
                        resultMap.put("dischargeDate", curUrnInfo.getString(12) != null ? DateFormat.formatStringToDate(curUrnInfo.getString(12)) : "NA");
                        resultMap.put("actualDateOfDischarge", curUrnInfo.getString(13) != null ? DateFormat.formatStringToDate(curUrnInfo.getString(13)) : "NA");
                        resultMap.put("claimedAmount", curUrnInfo.getString(14) != null ? curUrnInfo.getString(14) : "NA");

                        resultMapList.add(resultMap);
                    }
                    responseMap.put("urnInfoList", resultMapList);
                }

                if(curBalance != null && curBalance.next()) {
                    Map<String, Object> resultMap = new LinkedHashMap<>();
                    resultMap.put("availableBalance", curBalance.getString(1) != null ? curBalance.getString(1) : "NA");
                    resultMap.put("amountBlocked", curBalance.getString(2) != null ? curBalance.getString(2) : "NA");
                    resultMap.put("claimedAmount", curBalance.getString(3) != null ? curBalance.getString(3) : "NA");
                    resultMap.put("femaleFund", curBalance.getString(4) != null ? curBalance.getString(4) : "NA");
                    resultMap.put("policyStartDate", curBalance.getString(5) != null ? DateFormat.formatStringToDate(curBalance.getString(5)) : "NA");
                    resultMap.put("policyEndDate", curBalance.getString(6) != null ? DateFormat.formatStringToDate(curBalance.getString(6)) : "NA");

                    responseMap.put("balanceInfo", resultMap);
                }else
                    responseMap.put("balanceInfo",  new HashMap<String, Object>() {{
                        put("availableBalance", "NA");
                        put("amountBlocked", "NA");
                        put("claimedAmount", "NA");
                        put("femaleFund", "NA");
                        put("policyStartDate", "NA");
                        put("policyEndDate", "NA");
                    }});

                responseMap.put("actionRemarkList", actionRemarkRepository.findAll());
                return responseMap;
            }else
                throw new Exception("Invalid Action Code");
        } catch (Exception e) {
            logger.error("Exception occurred in OldBlockedClaimMonitoringServiceImpl getOldBlockedClaimList method : ", e);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> submitOldBlockedActionDetails(Map<String, Object> request) {
        //logger.info("Inside OldBlockedClaimMonitoringServiceImpl submitOldBlockedActionDetails method");
        try {
            StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("USP_T_SNAAPPROVEOLDBLOCKEDDATA")
                    .registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_USERID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_TXNPACKAGEDETAILID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_ID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_SNAREMARKID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_SNADESCRIPTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);

            storedProcedureQuery.setParameter("P_ACTION", request.get("action"));
            storedProcedureQuery.setParameter("P_USERID", request.get("userId"));
            storedProcedureQuery.setParameter("P_TXNPACKAGEDETAILID", request.get("txnPackageDetailId"));
            storedProcedureQuery.setParameter("P_ID", request.get("id"));
            storedProcedureQuery.setParameter("P_SNAREMARKID", request.get("actionRemarkId"));
            storedProcedureQuery.setParameter("P_SNADESCRIPTION", request.get("description"));

            storedProcedureQuery.execute();
            Integer out = (Integer) storedProcedureQuery.getOutputParameterValue("P_OUT");

            String action = Optional.ofNullable(request.get("action"))
                    .map(String::valueOf)
                    .orElse("");

            if (out != null && out == 1) {
                String message = "Blocked Data " + (action.equalsIgnoreCase("Y") ? "Approved" : (action.equals("Q") ? "Queried" : "Rejected")) + " Successfully";
                return new LinkedHashMap<>(Collections.singletonMap("message", message));
            } else {
                throw new Exception("Failed To Take Action On Blocked Data");
            }

        } catch (Exception e) {
            logger.error("Exception occurred in OldBlockedClaimMonitoringServiceImpl submitOldBlockedActionDetails method : ", e);
        }
        return null;
    }

    @Override
    public void downloadOldDataDoc(String fileName, String year, String hospitalCode, HttpServletResponse response) {
        //logger.info("Inside OldBlockedClaimMonitoringServiceImpl downloadOldDataDoc method");
        try {
            String folderName = bskyFileResourceBundle.getString("folder.OldBlockDocument");
            CommonFileUpload.downloadOldDataDocument(fileName, response, folderName, year, hospitalCode);
        } catch (Exception e) {
            logger.error("Exception occurred in OldBlockedClaimMonitoringServiceImpl downloadOldDataDoc method : ", e);
        }
    }

	@Override
	public Map<String,Object> viewblockeddataactioncount(Long userId, String stateCode, String districtCode, Date fromDate,
			Date toDate, String hospitalCode, String flag, Integer clmstatus) throws Exception {
		Map<String,Object> map=new HashMap<>();
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("USP_OLDBLOCKEDDATA_STATUSCOUNT")
                    .registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

            storedProcedureQuery.setParameter("P_ACTION", "A");
            storedProcedureQuery.setParameter("P_USERID", userId);
            storedProcedureQuery.setParameter("P_FROMDATE", fromDate);
            storedProcedureQuery.setParameter("P_TODATE", toDate);
            storedProcedureQuery.setParameter("P_STATECODE",stateCode);
            storedProcedureQuery.setParameter("P_DISTCODE", districtCode);
            storedProcedureQuery.setParameter("P_HOSPITALCODE",hospitalCode);

            storedProcedureQuery.execute();
            rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
            if(rs.next()) {
            	map.put("total", rs.getString(1));
            	map.put("reected", rs.getString(2));
            	map.put("query", rs.getString(3));
            	map.put("approved", rs.getString(4));
            	map.put("unblocked", rs.getString(5));
            	map.put("percentage", rs.getString(6));
            }
		}catch (Exception e) {
			throw new Exception(e);
		}
		return map;
	}
}
