package com.project.bsky.util;

import com.project.bsky.model.HealthDepartmentBasicDetailsAadharaauthLog;
import com.project.bsky.model.HealthDepartmentMemberDetailsAadharaauthLog;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Project : BSKY Backend
 * @Auther : Sambit Kumar Pradhan
 * @Created On : 23/06/2023 - 11:51 AM
 */
public class CommonClassHelper {

    public static List<Map<String, Object>> convertBasicDetailsAadharaauthLogModelListToMapList(List<HealthDepartmentBasicDetailsAadharaauthLog> modelList) {
        return modelList.stream()
                .map(model -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                            map.put("healthSlNo", model.getHealthSlNo() != null ? model.getHealthSlNo() : "NA");
                            map.put("rationCardNumber", model.getRationCardNumber() != null ? model.getRationCardNumber() : "NA");
                            map.put("rationCardType", model.getRationCardType() != null ? model.getRationCardType() : "NA");
                            map.put("fullNameEnglish", model.getFullNameEnglish() != null ? model.getFullNameEnglish() : "NA");
                            map.put("fullNameOdiya", model.getFullNameOdiya() != null ? model.getFullNameOdiya() : "NA");
                            map.put("aadhaarNumber", model.getAadhaarNumber() != null ? model.getAadhaarNumber() : "NA");
                            map.put("gender", model.getGender() != null ? model.getGender() : "NA");
                            map.put("spouseFullName", model.getSpouseFullName() != null ? model.getSpouseFullName() : "NA");
                            map.put("fatherFullName", model.getFatherFullName() != null ? model.getFatherFullName() : "NA");
                            map.put("mobileNumber", model.getMobileNumber() != null ? model.getMobileNumber() : "NA");
                            map.put("district", model.getDistrict() != null ? model.getDistrict() : "NA");
                            map.put("districtId", model.getDistrictId() != null ? model.getDistrictId() : "NA");
                            map.put("blockUlb", model.getBlockUlb() != null ? model.getBlockUlb() : "NA");
                            map.put("blockIdUlbId", model.getBlockIdUlbId() != null ? model.getBlockIdUlbId() : "NA");
                            map.put("gpWard", model.getGpWard() != null ? model.getGpWard() : "NA");
                            map.put("gpIdWardId", model.getGpIdWardId() != null ? model.getGpIdWardId() : "NA");
                            map.put("localityVillage", model.getLocalityVillage() != null ? model.getLocalityVillage() : "NA");
                            map.put("localityidVillageId", model.getLocalityidVillageId() != null ? model.getLocalityidVillageId() : "NA");
                            map.put("fpsName", model.getFpsName() != null ? model.getFpsName() : "NA");
                            map.put("schemeType", model.getSchemeType() != null ? model.getSchemeType() : "NA");
                            map.put("status", model.getStatus() != null ? model.getStatus() : "NA");
                            map.put("additionDeletionStatus", model.getAdditionDeletionStatus() != null ? model.getAdditionDeletionStatus() : "NA");
                            map.put("exportDate", model.getExportDate() != null ? DateFormat.formatDateWithTime(model.getExportDate()) : "NA");
                            map.put("updateDate", model.getUpdateDate() != null ? DateFormat.formatDateWithTime(model.getUpdateDate()) : "NA");
                            map.put("createdOn", model.getCreatedOn() != null ? DateFormat.formatDate(model.getCreatedOn()) : "NA");
                            map.put("createdBy", model.getCreatedBy() != null ? model.getCreatedBy() : "NA");
                            map.put("dataStatus", model.getDataStatus() == 0 ? "Inserted" : "Updated");
//                            map.put("updatedOn", model.getUpdatedOn() != null ? model.getUpdatedOn() : "NA");
//                            map.put("updatedBy", model.getUpdatedBy() != null ? model.getUpdatedBy() : "NA");
//                            map.put("statusFlag", model.getStatusFlag() != null ? model.getStatusFlag() : "NA");
                            map.put("oldData", model.getOldDataId());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public static List<Map<String, Object>> convertMemberDetailsAadhaarAuthLogModelListToMapList(List<HealthDepartmentMemberDetailsAadharaauthLog> modelList) {
            return modelList.stream()
                    .map(model -> {
                            Map<String, Object> map = new LinkedHashMap<>();
                            map.put("healthMemeberSlNo", model.getHealthMemeberSlNo() != null ? model.getHealthMemeberSlNo() : "NA");
                            map.put("rationcardNumber", model.getRationcardNumber() != null ? model.getRationcardNumber() : "NA");
                            map.put("memberId", model.getMemberId() != null ? model.getMemberId() : "NA");
                            map.put("fullNameEnglish", model.getFullNameEnglish() != null ? model.getFullNameEnglish() : "NA");
                            map.put("fullNameOdiya", model.getFullNameOdiya() != null ? model.getFullNameOdiya() : "NA");
                            map.put("aadharNumber", model.getAadharNumber() != null ? model.getAadharNumber() : "NA");
                            map.put("gender", model.getGender() != null ? model.getGender() : "NA");
                            map.put("dateOfBirth", model.getDateOfBirth() != null ? DateFormat.formatDate(model.getDateOfBirth()) : "NA");
                            map.put("age", model.getAge() != null ? model.getAge() : "NA");
                            map.put("relationWithFamilyHead", model.getRelationWithFamilyHead());
                            map.put("schemeType", model.getSchemeType() != null ? model.getSchemeType() : "NA");
                            map.put("mobileNumber", model.getMobileNumber() != null ? model.getMobileNumber() : "NA");
                            map.put("status", model.getStatus() != null ? model.getStatus() : "NA");
                            map.put("additionDeletionStatus", model.getAdditionDeletionStatus() != null ? model.getAdditionDeletionStatus() : "NA");
                            map.put("exportDate", model.getExportDate() != null ? DateFormat.formatDateWithTime(model.getExportDate()) : "NA");
                            map.put("updateDate", model.getUpdateDate() != null ? DateFormat.formatDateWithTime(model.getUpdateDate()) : "NA");
                            map.put("createdOn", model.getCreatedOn() != null ? DateFormat.formatDate(model.getCreatedOn()) : "NA");
                            map.put("createdBy", model.getCreatedBy() != null ? model.getCreatedBy() : "NA");
                            map.put("dataStatus", model.getDataStatus() == 0 ? "Inserted" : "Updated");
//                            map.put("updatedOn", model.getUpdatedOn());
//                            map.put("updatedBy", model.getUpdatedBy());
//                            map.put("statusFlag", model.getStatusFlag());
                            map.put("oldData", model.getOldDataId());
                            return map;
                    })
                    .collect(Collectors.toList());
    }

    public static List<Map<String, Object>> convertMoSarkarDataToListMap(JSONObject moSarkarList) {
        List<Map<String, Object>> moSarkarMapList = new ArrayList<>();
        JSONArray moSarkarArray;
        try {
            moSarkarArray = moSarkarList.getJSONArray("data");
            for (int i = 0; i < moSarkarArray.length(); i++) {
                Map<String, Object> moSarkarMap = new LinkedHashMap<>();
                moSarkarMap.put("registrationNumber", moSarkarArray.getJSONObject(i).getString("registration_no") != null ? moSarkarArray.getJSONObject(i).getString("registration_no") : "NA");
                moSarkarMap.put("registrationDate", moSarkarArray.getJSONObject(i).getString("registration_date") != null ? moSarkarArray.getJSONObject(i).getString("registration_date") : "NA");
                moSarkarMap.put("name", moSarkarArray.getJSONObject(i).getString("name") != null ? moSarkarArray.getJSONObject(i).getString("name") : "NA");
                moSarkarMap.put("mobile", moSarkarArray.getJSONObject(i).getString("mobile") != null ? moSarkarArray.getJSONObject(i).getString("mobile") : "NA");
                moSarkarMap.put("gender", moSarkarArray.getJSONObject(i).getInt("gender") == 1 ? "Male" : "Female");
                moSarkarMap.put("age", moSarkarArray.getJSONObject(i).getString("age") != null ? moSarkarArray.getJSONObject(i).getString("age") : "NA");
                moSarkarMap.put("claimAmount", moSarkarArray.getJSONObject(i).getString("claim_amount") != null ? moSarkarArray.getJSONObject(i).getString("claim_amount") : "NA");
                moSarkarMap.put("DistrictId", moSarkarArray.getJSONObject(i).getString("district_id") != null ? moSarkarArray.getJSONObject(i).getString("district_id") : "NA");
                moSarkarMap.put("departmentInstitutionId", moSarkarArray.getJSONObject(i).getString("department_institution_id") != null ? moSarkarArray.getJSONObject(i).getString("department_institution_id") : "NA");

                JSONObject otherInfo = moSarkarArray.getJSONObject(i).getJSONObject("other_info");
                moSarkarMap.put("purpose", otherInfo.getString("purpose") != null ? otherInfo.getString("purpose") : "NA");
                moSarkarMap.put("claimId", otherInfo.getString("claim_id") != null ? otherInfo.getString("claim_id") : "NA");
                moSarkarMap.put("hospitalInfo", otherInfo.getString("hospital_info") != null ? otherInfo.getString("hospital_info") : "NA");

                moSarkarMapList.add(moSarkarMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moSarkarMapList;
    }

    public static List<Map<String, Object>> convertJSONArrayToMoSarkarMapList(JSONArray moSarkarArray) {
        List<Map<String, Object>> moSarkarMapList = new ArrayList<>();
        try {
            for (int i = 0; i < moSarkarArray.length(); i++) {
                Map<String, Object> moSarkarMap = new LinkedHashMap<>();
                moSarkarMap.put("registrationNumber", moSarkarArray.getJSONObject(i).getString("registration_no") != null ? moSarkarArray.getJSONObject(i).getString("registration_no") : "NA");
                moSarkarMap.put("registrationDate", moSarkarArray.getJSONObject(i).getString("registration_date") != null ? moSarkarArray.getJSONObject(i).getString("registration_date") : "NA");
                moSarkarMap.put("name", moSarkarArray.getJSONObject(i).getString("name") != null ? moSarkarArray.getJSONObject(i).getString("name") : "NA");
                moSarkarMap.put("mobile", moSarkarArray.getJSONObject(i).getString("mobile") != null ? moSarkarArray.getJSONObject(i).getString("mobile") : "NA");
                moSarkarMap.put("gender", moSarkarArray.getJSONObject(i).getInt("gender") == 1 ? "Male" : "Female");
                moSarkarMap.put("age", moSarkarArray.getJSONObject(i).getString("age") != null ? moSarkarArray.getJSONObject(i).getString("age") : "NA");
                moSarkarMap.put("claimAmount", moSarkarArray.getJSONObject(i).getString("claim_amount") != null ? moSarkarArray.getJSONObject(i).getString("claim_amount") : "NA");
                moSarkarMap.put("districtId", moSarkarArray.getJSONObject(i).getString("district_id") != null ? moSarkarArray.getJSONObject(i).getString("district_id") : "NA");
                moSarkarMap.put("departmentInstitutionId", moSarkarArray.getJSONObject(i).getString("department_institution_id") != null ? moSarkarArray.getJSONObject(i).getString("department_institution_id") : "NA");
                moSarkarMap.put("purpose", moSarkarArray.getJSONObject(i).getString("purpose") != null ? moSarkarArray.getJSONObject(i).getString("purpose") : "NA");
                moSarkarMap.put("claimId", moSarkarArray.getJSONObject(i).getString("claimNo") != null ? moSarkarArray.getJSONObject(i).getString("claimNo") : "NA");
                moSarkarMap.put("hospitalInfo", moSarkarArray.getJSONObject(i).getString("hospitalInfo") != null ? moSarkarArray.getJSONObject(i).getString("hospitalInfo") : "NA");

                moSarkarMapList.add(moSarkarMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moSarkarMapList;
    }

    public static List<String> getModelKeyNames(Class<?> modelClass) {
        List<String> keyNames = new ArrayList<>();

        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            keyNames.add(field.getName());
        }
        return keyNames;
    }

    public static Map<String, Object> createSuccessResponse(Object data, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("statusCode", HttpStatus.OK.value());
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    public static Map<String, Object> createNoContentResponse(String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "failure");
        response.put("statusCode", HttpStatus.NO_CONTENT.value());
        response.put("message", message);
        return response;
    }

    public static Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "failure");
        response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", message);
        return response;
    }
}
