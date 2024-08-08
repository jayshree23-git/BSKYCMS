package com.project.bsky.serviceImpl;

import com.project.bsky.model.*;
import com.project.bsky.repository.*;
import com.project.bsky.service.APIService;
import com.project.bsky.util.CommonClassHelper;
import com.project.bsky.util.CommonUtils;
import com.project.bsky.util.DateFormat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Project : BSKY Backend
 * @Auther : Sambit Kumar Pradhan
 * @Created On : 13/06/2023 - 1:07 PM
 */
@Service
public class APIServiceImpl  implements APIService {

    @Autowired
    private MstApiRepository mstApiRepository;

    @Autowired
    private Logger logger;

    @Autowired
    private APIServiceLogRepository apiServiceLogRepository;

    @Autowired
    private MoSarkarLogRepository moSarkarLogRepository;

    @Autowired
    private EdsDataLogRepository edsDataLogRepository;

    @Autowired
    private HealthDepartmentServiceReportRepository healthDepartmentServiceReportRepository;

    @Autowired
    private HealthDepartmentBasicDetailsAadharaauthLogRepository healthDepartmentBasicDetailsAadharaauthLogRepository;

    @Autowired
    private HealthDepartmentMemberDetailsAadharaauthLogRepository healthDepartmentMemberDetailsAadharaauthLogRepository;

    @Autowired
    private HealthDepartmentBasicDetailsAadharaAuthOldLogRepository healthDepartmentBasicDetailsAadharaAuthOldLogRepository;

    @Autowired
    private HealthDepartmentMemberDetailsAadharaAuthOldLogRepository healthDepartmentMemberDetailsAadharaAuthOldLogRepository;

    @Override
    public List<MstApi> getMasterAPIServices() {
        try {
            return mstApiRepository.findAllByOrder();
        } catch (Exception e) {
            logger.error("Exception Occurred in getMasterAPI of APIServiceImpl" + e.getMessage());
            throw e;
        }
    }

    @Override
	public List<Map<String, Object>> getReportDataList(String request) throws Exception {
        List<Map<String, Object>> reportMapList;
        try {
            JSONObject requestObject = new JSONObject(request);

            if (!requestObject.has("apiId") && !requestObject.has("year") && !requestObject.has("month")) {
                throw new Exception("Invalid Request/Parameter");
            }

            int apiId = requestObject.getInt("apiId");
            int year = Integer.parseInt(requestObject.getString("year"));
            int month = Integer.parseInt(requestObject.getString("month"));

            switch (apiId) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 9:
                    reportMapList = apiServiceLogRepository.getReportList((long) apiId, year, month)
                            .stream()
                            .map(report -> {
                                Map<String, Object> reportMap = new LinkedHashMap<>();

                                reportMap.put("apiId", ((Object[]) report)[0] != null ? ((Object[]) report)[0] : "NA");
                                reportMap.put("apiName", ((Object[]) report)[1] != null ? ((Object[]) report)[1] : "NA");
                                reportMap.put("apiStartTime", ((Object[]) report)[2] != null ? DateFormat.formatDateToTime((Date) ((Object[]) report)[2]) : "NA");
                                reportMap.put("apiEndTime", ((Object[]) report)[3] != null ? DateFormat.formatDateToTime((Date) ((Object[]) report)[3]) : "NA");
                                reportMap.put("dataDelivered", ((Object[]) report)[4] != null ? ((Object[]) report)[4] : "NA");
                                reportMap.put("createdOn", ((Object[]) report)[5] != null ? DateFormat.formatDate((Date) ((Object[]) report)[5]) : "NA");
                                reportMap.put("createdBy", ((Object[]) report)[6] != null ? ((Object[]) report)[6] : "NA");
                                reportMap.put("id", ((Object[]) report)[7]);

                                return reportMap;
                            })
                            .collect(Collectors.toList());
                    break;
                case 6:
                    reportMapList = healthDepartmentServiceReportRepository.getReportList((long) apiId, year, month)
                            .stream()
                            .map(report -> {
                                Map<String, Object> reportMap = new LinkedHashMap<>();

                                reportMap.put("id", ((Object[]) report)[0] != null ? ((Object[]) report)[0] : "NA");
                                reportMap.put("apiId", ((Object[]) report)[1] != null ? ((Object[]) report)[1] : "NA");
                                reportMap.put("serviceName", ((Object[]) report)[2] != null ? ((Object[]) report)[2] : "NA");
                                reportMap.put("apiStartTime", ((Object[]) report)[3] != null ? DateFormat.formatDateToTime((Date) ((Object[]) report)[3]) : "NA");
                                reportMap.put("apiEndTime", ((Object[]) report)[4] != null ? DateFormat.formatDateToTime((Date) ((Object[]) report)[4]) : "NA");
                                reportMap.put("recordsFetched", ((Object[]) report)[5] != null ? ((Object[]) report)[5] : "NA");
                                reportMap.put("recordsInserted", ((Object[]) report)[6] != null ? ((Object[]) report)[6] : "NA");
                                reportMap.put("recordsUpdated", ((Object[]) report)[7] != null ? ((Object[]) report)[7] : "NA");
                                reportMap.put("recordsFailed", ((Object[]) report)[8] != null ? ((Object[]) report)[8] : "NA");
                                reportMap.put("createdOn", ((Object[]) report)[9] != null ? DateFormat.formatDate((Date) ((Object[]) report)[9]) : "NA");
                                reportMap.put("createdBy", ((Object[]) report)[10] != null ? ((Object[]) report)[10] : "NA");
                                reportMap.put("serviceId", ((Object[]) report)[2].toString().toLowerCase().replace(" ", "").contains("basicdetails")
                                        ? 1
                                        : ((Object[]) report)[2].toString().toLowerCase().replace(" ", "").contains("memberdetails")
                                        ? 2
                                        : 0
                                );
                                return reportMap;
                            })
                            .collect(Collectors.toList());
                    break;
                case 7:
                    reportMapList = moSarkarLogRepository.getReportList((long) apiId, year, month)
                            .stream()
                            .map(report -> {
                                Map<String, Object> reportMap = new LinkedHashMap<>();

                                reportMap.put("apiId", ((Object[]) report)[0] != null ? ((Object[]) report)[0] : "NA");
                                reportMap.put("apiName", ((Object[]) report)[1] != null ? ((Object[]) report)[1] : "NA");
                                reportMap.put("apiStartTime", ((Object[]) report)[2] != null ? DateFormat.formatDateToTime((Date) ((Object[]) report)[2]) : "NA");
                                reportMap.put("apiEndTime", ((Object[]) report)[3] != null ? DateFormat.formatDateToTime((Date) ((Object[]) report)[3]) : "NA");
                                reportMap.put("dataDelivered", ((Object[]) report)[4] != null ? ((Object[]) report)[4] : "NA");
                                reportMap.put("successDataSize", ((Object[]) report)[5] != null ? ((Object[]) report)[5] : "NA");
                                reportMap.put("failedDataSize", ((Object[]) report)[6] != null ? ((Object[]) report)[6] : "NA");
                                reportMap.put("createdOn", ((Object[]) report)[7] != null ? DateFormat.formatDate((Date) ((Object[]) report)[7]) : "NA");
                                reportMap.put("createdBy", ((Object[]) report)[8] != null ? ((Object[]) report)[8] : "NA");
                                reportMap.put("id", ((Object[]) report)[9]);

                                return reportMap;
                            })
                            .collect(Collectors.toList());
                    break;
                case 8:
                    reportMapList = edsDataLogRepository.getReportList((long) apiId, year, month)
                            .stream()
                            .map(report -> {
                                Map<String, Object> reportMap = new LinkedHashMap<>();

                                reportMap.put("id", ((Object[]) report)[0] != null ? ((Object[]) report)[0] : "NA");
                                reportMap.put("apiId", ((Object[]) report)[1] != null ? ((Object[]) report)[1] : "NA");
                                reportMap.put("apiStartTime", ((Object[]) report)[2] != null ? DateFormat.formatDateToTime((Date) ((Object[]) report)[2]) : "NA");
                                reportMap.put("apiEndTime", ((Object[]) report)[3] != null ? DateFormat.formatDateToTime((Date) ((Object[]) report)[3]) : "NA");
                                reportMap.put("recordFetched", ((Object[]) report)[4] != null ? ((Object[]) report)[4] : "NA");
                                reportMap.put("recordInserted", ((Object[]) report)[5] != null ? ((Object[]) report)[5] : "NA");
                                reportMap.put("recordUpdated", ((Object[]) report)[6] != null ? ((Object[]) report)[6] : "NA");
                                reportMap.put("recordFailed", ((Object[]) report)[7] != null ? ((Object[]) report)[7] : "NA");
                                reportMap.put("createdOn", ((Object[]) report)[8] != null ? DateFormat.formatDate((Date) ((Object[]) report)[8]) : "NA");
                                reportMap.put("createdBy", ((Object[]) report)[9] != null ? ((Object[]) report)[9] : "NA");

                                return reportMap;
                            })
                            .collect(Collectors.toList());
                    break;
                default:
                    throw new Exception("Invalid API ID!");
            }
        } catch (Exception e) {
            logger.error("Exception Occurred in getReportDataList of APIServiceImpl" + e.getMessage());
            throw new Exception(e.getMessage());
        }
        return reportMapList;
    }

    @Override
    public Map<String, Object> getReportDetails(String request) throws Exception {
        Map<String, Object> reportMap = new LinkedHashMap<>();
        try {
            JSONObject requestObject = new JSONObject(request);
            if (requestObject.has("reportId") && requestObject.has("apiId")) {
                switch ((int) requestObject.getLong("apiId")) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 9:
                        APIServiceLog apiServiceLog = apiServiceLogRepository.getReportDetails(
                                requestObject.getInt("apiId"),
                                requestObject.getInt("reportId")
                        );

                        reportMap.put("apiName", apiServiceLog.getApiName() != null ? apiServiceLog.getApiName() : "NA");
                        reportMap.put("createdOn", apiServiceLog.getCreatedOn() != null ? DateFormat.formatDate(apiServiceLog.getCreatedOn()) : "NA");
                        reportMap.put("apiStartTime", apiServiceLog.getApiHitTime() != null ? DateFormat.formatDateToTime(apiServiceLog.getApiHitTime()) : "NA");
                        reportMap.put("apiEndTime", apiServiceLog.getApiEndTime() != null ? DateFormat.formatDateToTime(apiServiceLog.getApiEndTime()) : "NA");
                        reportMap.put("inputData", apiServiceLog.getInputData() != null ? CommonUtils.convertStringToMap(apiServiceLog.getInputData()) : "NA");
                        reportMap.put("outputData", apiServiceLog.getOutputData() != null ? CommonUtils.convertStringToListMap(apiServiceLog.getOutputData()) : "NA");
                        reportMap.put("headerData", reportMap.get("outputData") != null ? ((List<Map<String, Object>>) reportMap.get("outputData"))
                                .stream()
                                .findFirst()
                                .map(Map::keySet)
                                .map(ArrayList::new)
                                .orElse(new ArrayList<>()) : "NA");
                        break;
                    case 6:
                        HealthDepartmentServiceReport healthDepartmentServiceReport = healthDepartmentServiceReportRepository.getReportDetails(
                                requestObject.getLong("apiId"),
                                requestObject.getLong("reportId")
                        );

                        if (healthDepartmentServiceReport.getServiceStatus().toUpperCase().contains("BASIC") && (requestObject.getString("dataStatus").equalsIgnoreCase("") || requestObject.getString("dataStatus").equalsIgnoreCase("null"))) {
                            List<HealthDepartmentBasicDetailsAadharaauthLog> basicDetailsAadharaauthLogList = healthDepartmentBasicDetailsAadharaauthLogRepository.getReportDetails(
                                    healthDepartmentServiceReport.getStartDate(),
                                    healthDepartmentServiceReport.getEndDate()
                            );
                            reportMap.put("outputData", CommonClassHelper.convertBasicDetailsAadharaauthLogModelListToMapList(basicDetailsAadharaauthLogList));
                        } else if (healthDepartmentServiceReport.getServiceStatus().toUpperCase().contains("BASIC") && (requestObject.getInt("dataStatus") == 0 || requestObject.getInt("dataStatus") == 1)) {
                            List<HealthDepartmentBasicDetailsAadharaauthLog> basicDetailsAadharaauthLogList = healthDepartmentBasicDetailsAadharaauthLogRepository.getReportDetails(
                                    healthDepartmentServiceReport.getStartDate(),
                                    healthDepartmentServiceReport.getEndDate(),
                                    requestObject.getInt("dataStatus")
                            );
                            reportMap.put("outputData", CommonClassHelper.convertBasicDetailsAadharaauthLogModelListToMapList(basicDetailsAadharaauthLogList));
                        } else if (healthDepartmentServiceReport.getServiceStatus().toUpperCase().contains("MEMBER") && (requestObject.getString("dataStatus").equalsIgnoreCase("") || requestObject.getString("dataStatus").equalsIgnoreCase("null"))) {
                            List<HealthDepartmentMemberDetailsAadharaauthLog> memberDetailsAadharaauthLogList = healthDepartmentMemberDetailsAadharaauthLogRepository.getReportDetails(
                                    healthDepartmentServiceReport.getStartDate(),
                                    healthDepartmentServiceReport.getEndDate()
                            );
                            reportMap.put("outputData", CommonClassHelper.convertMemberDetailsAadhaarAuthLogModelListToMapList(memberDetailsAadharaauthLogList));
                        } else if (healthDepartmentServiceReport.getServiceStatus().toUpperCase().contains("MEMBER") && (requestObject.getInt("dataStatus") == 0 || requestObject.getInt("dataStatus") == 1)) {
                            List<HealthDepartmentMemberDetailsAadharaauthLog> memberDetailsAadharaauthLogList = healthDepartmentMemberDetailsAadharaauthLogRepository.getReportDetails(
                                    healthDepartmentServiceReport.getStartDate(),
                                    healthDepartmentServiceReport.getEndDate(),
                                    requestObject.getInt("dataStatus")
                            );
                            reportMap.put("outputData", CommonClassHelper.convertMemberDetailsAadhaarAuthLogModelListToMapList(memberDetailsAadharaauthLogList));
                        }
                        reportMap.put("apiName", healthDepartmentServiceReport.getServiceStatus() != null ? "NFSA " + healthDepartmentServiceReport.getServiceStatus() : "NA");
                        reportMap.put("createdOn", healthDepartmentServiceReport.getCreatedOn() != null ? DateFormat.formatDate(healthDepartmentServiceReport.getCreatedOn()) : "NA");
                        reportMap.put("apiStartTime", healthDepartmentServiceReport.getStartDate() != null ? DateFormat.formatDateToTime(healthDepartmentServiceReport.getStartDate()) : "NA");
                        reportMap.put("apiEndTime", healthDepartmentServiceReport.getEndDate() != null ? DateFormat.formatDateToTime(healthDepartmentServiceReport.getEndDate()) : "NA");
                        reportMap.put("inputData", healthDepartmentServiceReport.getCreatedOn() != null ? DateFormat.formatDate(healthDepartmentServiceReport.getCreatedOn()) : "NA");
                        reportMap.put("headerData", reportMap.get("outputData") != null ? ((List<Map<String, Object>>) reportMap.get("outputData"))
                                .stream()
                                .findFirst()
                                .map(Map::keySet)
                                .map(ArrayList::new)
                                .orElse(new ArrayList<>()) : "NA");
                        break;
                    case 7:
                        MoSarkarLog moSarkarLog = moSarkarLogRepository.getReportDetails(
                                requestObject.getInt("apiId"),
                                requestObject.getInt("reportId")
                        );
                        if (requestObject.getInt("dataStatus") == 0) {
                            reportMap.put("outputData", CommonClassHelper.convertMoSarkarDataToListMap(new JSONObject(moSarkarLog.getInputData())));
                        } else if (requestObject.getInt("dataStatus") == 1) {
                            reportMap.put("outputData", CommonClassHelper.convertJSONArrayToMoSarkarMapList(new JSONArray(moSarkarLog.getSuccessData())));
                        } else if (requestObject.getInt("dataStatus") == 2) {
                            reportMap.put("outputData", CommonClassHelper.convertJSONArrayToMoSarkarMapList(new JSONArray(moSarkarLog.getFailedData())));
                        } else
                            throw new Exception("Invalid Data Status!");
                        reportMap.put("apiName", moSarkarLog.getApiName() != null ? moSarkarLog.getApiName() : "NA");
                        reportMap.put("createdOn", moSarkarLog.getCreatedOn() != null ? DateFormat.formatDate(moSarkarLog.getCreatedOn()) : "NA");
                        reportMap.put("apiStartTime", moSarkarLog.getStartTime() != null ? DateFormat.formatDateToTime(moSarkarLog.getStartTime()) : "NA");
                        reportMap.put("apiEndTime", moSarkarLog.getEndTime() != null ? DateFormat.formatDateToTime(moSarkarLog.getEndTime()) : "NA");
                        reportMap.put("inputData", moSarkarLog.getInputData() != null ? moSarkarLog.getInputData() : "NA");
                        reportMap.put("headerData", reportMap.get("outputData") != null ? ((List<Map<String, Object>>) reportMap.get("outputData"))
                                .stream()
                                .findFirst()
                                .map(Map::keySet)
                                .map(ArrayList::new)
                                .orElse(new ArrayList<>()) : "NA");
                        //System.out.println("Report Map: " + reportMap);
                        break;
                    case 8:
                        EdsDataLog edsDataLog = edsDataLogRepository.getReportDetails(
                                requestObject.getInt("apiId"),
                                requestObject.getInt("reportId")
                        );

                        if (requestObject.getInt("dataStatus") == 0) {
                            reportMap.put("outputData", CommonUtils.convertStringToListMap(edsDataLog.getBskyData()));
                        } else if (requestObject.getInt("dataStatus") == 1) {
                            reportMap.put("outputData", CommonUtils.convertStringToListMap(edsDataLog.getBskyInsertedData()));
                        } else if (requestObject.getInt("dataStatus") == 2) {
                            reportMap.put("outputData", CommonUtils.convertStringToListMap(edsDataLog.getBskyUpdatedData()));
                        } else if (requestObject.getInt("dataStatus") == 3) {
                            reportMap.put("outputData", CommonUtils.convertStringToListMap(edsDataLog.getBskyFailedData()));
                        } else
                            throw new Exception("Invalid Data Status!");
                        reportMap.put("apiName", edsDataLog.getApiName() != null ? edsDataLog.getApiName() : "NA");
                        reportMap.put("createdOn", edsDataLog.getCreatedDate() != null ? DateFormat.formatDate(edsDataLog.getCreatedDate()) : "NA");
                        reportMap.put("apiStartTime", edsDataLog.getCreatedDate() != null ? DateFormat.formatDateToTime(edsDataLog.getCreatedDate()) : "NA");
                        reportMap.put("apiEndTime", edsDataLog.getCreatedDate() != null ? DateFormat.formatDateToTime(edsDataLog.getCreatedDate()) : "NA");
                        reportMap.put("inputData", edsDataLog.getBskyData() != null ? CommonUtils.convertStringToMap(edsDataLog.getInputData()) : "NA");
                        reportMap.put("headerData", ((List<Map<String, Object>>) reportMap.get("outputData"))
                                .stream()
                                .findFirst()
                                .map(Map::keySet)
                                .map(ArrayList::new)
                                .map(list -> {
                                    list.removeIf(Arrays.asList("statusFlag", "createdBy", "createdOn", "updatedBy", "updatedOn", "edsStatus")::contains);
                                    return list;
                                })
                                .orElse(new ArrayList<>())
                        );
                        break;
                    default:
                        throw new Exception("Invalid API ID!");
                }
            } else {
                throw new Exception("API ID and Report ID are mandatory!");
            }
        } catch (Exception e) {
            logger.error("Exception Occurred in getReportDetails of APIServiceImpl " + e.getMessage());
            throw new Exception(e.getMessage());
        }
        return reportMap;
    }

    @Override
    public Map<String, Object> getOldDataDetails(String request) throws Exception {
        Map<String, Object> oldDataMap = new LinkedHashMap<>();
        try {
            JSONObject requestObject = new JSONObject(request);
            if(requestObject.get("serviceName").toString().toUpperCase().contains("BASIC")) {
                HealthDepartmentBasicDetailsAadharaAuthOldLog healthDepartmentBasicDetailsAadharaAuthOldLog = healthDepartmentBasicDetailsAadharaAuthOldLogRepository
                        .findById(Long.parseLong(requestObject.get("oldDataId").toString()))
                        .orElse(null);

                if (healthDepartmentBasicDetailsAadharaAuthOldLog != null) {
                    oldDataMap.put("data", healthDepartmentBasicDetailsAadharaAuthOldLog);
                    oldDataMap.put("headerData", CommonClassHelper.getModelKeyNames(healthDepartmentBasicDetailsAadharaAuthOldLog.getClass()));
                }else
                    throw new Exception("No Data Found!");
            } else if (requestObject.get("serviceName").toString().toUpperCase().contains("MEMBER")) {
                HealthDepartmentMemberDetailsAadharaAuthOldLog healthDepartmentMemberDetailsAadharaAuthOldLog = healthDepartmentMemberDetailsAadharaAuthOldLogRepository
                        .findById(Long.parseLong(requestObject.get("oldDataId").toString()))
                        .orElse(null);

                if (healthDepartmentMemberDetailsAadharaAuthOldLog != null) {
                    oldDataMap.put("data", healthDepartmentMemberDetailsAadharaAuthOldLog);
                    oldDataMap.put("headerData", CommonClassHelper.getModelKeyNames(healthDepartmentMemberDetailsAadharaAuthOldLog.getClass()));
                }else
                    throw new Exception("No Data Found!");
            } else {
                throw new Exception("Invalid Service Name!");
            }
        }catch (Exception e){
            logger.error("Exception Occurred in getOldDataDetails of APIServiceImpl " + e.getMessage());
            throw new Exception(e.getMessage());
        }
        return oldDataMap;
    }
}
