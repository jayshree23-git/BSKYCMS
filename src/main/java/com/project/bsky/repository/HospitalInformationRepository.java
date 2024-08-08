/**
 * 
 */
package com.project.bsky.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.HospitalInformation;

/**
 * @author ronauk
 *
 */
@Repository
public interface HospitalInformationRepository extends JpaRepository<HospitalInformation, Integer>{

@Query("FROM HospitalInformation where hospitalCode=:hospitalCode")
HospitalInformation findHospitalByCode(String hospitalCode);
@Query("FROM HospitalInformation where hospitalCode=:hospitalCode and status=0")
HospitalInformation getHospital(String hospitalCode);

@Query("select hospitalName FROM HospitalInformation where hospitalCode=:hospitalCode")
String findHospitalNameByCode(String hospitalCode);

@Query("FROM HospitalInformation where userId.userId=:userId")
HospitalInformation findByUserId(Long userId);
// @Query("select c.hospitalName,c.mobile,c.status,c.emailId,s.stateName,d.districtname,c.cpdApprovalRequired,c.hospitalId,c.hospitalCode,d.districtcode,s.stateCode FROM HospitalInformation c left join State s on c.stateCode = s.stateCode left join DistrictMaster d on c.districtcode = d.districtcode where d.statecode=s.stateCode and c.userId.userId=:userId")
// List<Object[]> findHospByUserId(Long userId);

@Query("SELECT h.hospitalId FROM HospitalInformation h WHERE h.hospitalCode=:hospitalCode")
Integer getHospitalIdByHospitalCode(String hospitalCode);

@Query("SELECT count(*) FROM HospitalInformation h WHERE upper(h.hospitalCode)=:hospitalCode")
Integer checkHospitalByHospitalCode(String hospitalCode);

@Transactional
@Modifying
@Query("update HospitalInformation set assigned_dc=null, updatedBy=:updatedBy, updatedOn=sysdate where assigned_dc=:dcUserId and status=0")
void inActivateDC(Long dcUserId, Long updatedBy);
@Transactional
@Modifying
@Query("UPDATE HospitalInformation htd SET htd.status=1 where htd.hospitalId=:hospitalId")
void deleteById(@Param(value="hospitalId")Integer hospitalId);
@Transactional
@Modifying
@Query("UPDATE HospitalInformation htd SET htd.mobile=:mobile, htd.emailId=:emailId, htd.updatedBy=:updatedBy, htd.updatedOn=sysdate where htd.hospitalCode=:hospitalCode")
void updateProfile(@Param(value="mobile")String mobile, @Param(value="emailId")String emailId, @Param(value="hospitalCode")String hospitalCode, @Param(value="updatedBy")Long updatedBy);

@Transactional
@Modifying
@Query("UPDATE HospitalInformation htd SET htd.assigned_dc=null, updatedOn=sysdate, updatedBy=:updatedBy where htd.hospitalCode=:hospitalCode and htd.assigned_dc=:dcId")
void removeFromHospital(@Param(value="dcId")Long dcId, @Param(value="hospitalCode")String hospitalCode, @Param(value="updatedBy")Long updatedBy);

@Transactional
@Query("SELECT hus.hospitalId FROM HospitalInformation hus WHERE hus.hospitalCode=:hospitalCode")
Integer getHospitalUserSaveByHospitalCode(@Param(value="hospitalCode")String hospitalCode);

@Query("FROM HospitalInformation order by hospitalId desc")
List<HospitalInformation> findAllDetails();
// @Query("select c.hospitalName,c.mobile,c.status,c.emailId,s.stateName,d.districtname,c.cpdApprovalRequired,c.hospitalId,c.hospitalCode FROM HospitalInformation c left join State s on c.stateCode = s.stateCode left join DistrictMaster d on c.districtcode = d.districtcode where d.statecode=s.stateCode order by c.hospitalId desc")
// List<Object[]> findAllHospitals();
// @Query("select c.hospitalName,c.mobile,c.status,c.emailId,s.stateName,d.districtname,c.cpdApprovalRequired,c.hospitalId,c.hospitalCode FROM HospitalInformation c "
// + "left join State s on c.stateCode = s.stateCode left join DistrictMaster d on c.districtcode = d.districtcode "
// + "where d.statecode=s.stateCode "
// + "and d.districtcode=decode(:districtId, NULL, d.districtcode, :districtId) "
// + "and s.stateCode=decode(:stateId, NULL, s.stateCode, :stateId) "
// + "and c.cpdApprovalRequired=decode(:cpdApprovalRequired, NULL, c.cpdApprovalRequired, :cpdApprovalRequired) order by c.hospitalName")
// List<Object[]> findHospitals(String stateId, String districtId, String cpdApprovalRequired);

@Query("select c.hospitalName,c.status,s.stateName,d.districtname,c.hospitalId,c.hospitalCode,c.assigned_dc,us.snoUserId,u.fullname,ua.fullname FROM HospitalInformation c left join State s on c.districtcode.statecode.stateCode = s.stateCode left join SNOConfiguration us on us.hospitalCode=c.hospitalCode left join UserDetails u on u.userId=c.assigned_dc left join UserDetails ua on ua.userId=us.snoUserId left join DistrictMaster d on c.districtcode.districtcode = d.districtcode where d.statecode=s.stateCode")
List<Object[]> findAllHospitalsreport();
@Query("select hospitalName,districtcode.statecode.stateName,districtcode.districtname,hospitalCode FROM HospitalInformation")
List<Object[]> hospital();
@Query("FROM HospitalInformation WHERE districtcode.statecode.stateCode=:stateId order by hospitalId desc")
List<HospitalInformation> findAllDetailsByState(String stateId);
@Query("FROM HospitalInformation WHERE districtcode.statecode.stateCode=:stateId and districtcode.districtcode=:districtId order by  hospitalId desc")
List<HospitalInformation> findAllDetailsByDistrict(String stateId, String districtId);
// @Query("select c.hospitalName,c.mobile,c.status,c.emailId,s.stateName,d.districtname,c.cpdApprovalRequired,c.hospitalId,c.hospitalCode FROM HospitalInformation c left join State s on c.stateCode = s.stateCode left join DistrictMaster d on c.districtcode = d.districtcode where d.statecode=s.stateCode and s.stateCode=:stateId order by c.hospitalId desc")
// List<Object[]> findAllHospitalsByState(String stateId);
// 
// @Query("select c.hospitalName,c.mobile,c.status,c.emailId,s.stateName,d.districtname,c.cpdApprovalRequired,c.hospitalId,c.hospitalCode FROM HospitalInformation c left join State s on c.stateCode = s.stateCode left join DistrictMaster d on c.districtcode = d.districtcode where d.statecode=s.stateCode and d.districtcode=:districtId and s.stateCode=:stateId order by c.hospitalId desc")
// List<Object[]> findAllHospitalsByDistrict(String stateId, String districtId);
@Query("select count(*) from HospitalInformation where hospitalCode=:hospitalCode and assigned_dc is not null and assigned_dc!=:dcId")
Integer checkDcNameDulicacy(String hospitalCode, Long dcId);

//for reset password of DC
@Query("FROM HospitalInformation where assigned_dc=:userId order by  hospitalId desc")
List<HospitalInformation> findAssignedDCData(Long userId);

// @Query(value = "SELECT HOSPITAL_CODE, HOSPITAL_NAME, STATENAME, DISTRICTNAME, STATUS, USER_ID\n" +
// "FROM HOSPITAL_INFO\n" +
// "    INNER JOIN STATE on HOSPITAL_INFO.STATE_CODE = STATE.STATECODE\n" +
// "    INNER JOIN DISTRICT on HOSPITAL_INFO.DISTRICT_CODE = DISTRICT.DISTRICTCODE\n" +
// "WHERE HOSPITAL_INFO.ASSIGNED_DC = ?1", nativeQuery = true)
// List<Object[]> getHospitalInformationByDcLoginId(Long userId);
@Query(value="SELECT h.HOSPITAL_CODE, h.HOSPITAL_NAME, s.STATENAME, d.DISTRICTNAME,h.STATUS_FLAG, h.USER_ID\r\n"
+ "FROM HOSPITAL_INFO h\r\n"
+ "INNER JOIN DISTRICT d on d.DISTRICTCODE = h.DISTRICT_CODE and d.STATECODE=h.STATE_CODE\r\n"
+ "INNER JOIN state  s on s.STATECODE=h.STATE_CODE\r\n"
+ "AND h.STATUS_FLAG=0\r\n"
+ "WHERE h.ASSIGNED_DC =?1\r\n", nativeQuery=true)
List<Object[]> getHospitalInformationByDcLoginId(Long userId);
@Query("select count(*) from HospitalInformation where hospitalCode=:hospitalCode and (assigned_dc!=:assignedDc)")
Integer checkOtherDcNameDulicacy(String hospitalCode, Long assignedDc);

@Query("FROM HospitalInformation WHERE assigned_dc=:userid ORDER BY hospitalName ASC")
List<HospitalInformation> dchospitallist(Long userid);
@Query("select count(*) from HospitalInformation where hospitalCode=:hospitalCode and assigned_dc=:dcId")
Integer checkDCConfigDulicacy(String hospitalCode, Long dcId);

@Query("select count(*) from HospitalInformation where assigned_dc=:dcId")
Integer checkDCConfig(Long dcId);

@Query("From HospitalInformation U where U.status=0")
List<HospitalInformation> findHospitalUser();
@Transactional
@Modifying
@Query("UPDATE HospitalInformation SET status=1,updatedBy=:updatedBy,updatedOn=sysdate where userId.userId=:userId")
void inactivate(Long userId, Long updatedBy);
@Transactional
@Modifying
@Query("UPDATE HospitalInformation SET status=0,updatedBy=:updatedBy,updatedOn=sysdate where userId.userId=:userId")
void activate(Long userId, Long updatedBy);

@Query("select hospitalName,hospitalCode from HospitalInformation where status=0")
List<Object[]> gethospital();

@Query(value = "SELECT STATENAME FROM HOSPITAL_INFO HI\n" +
"    INNER JOIN STATE S ON S.STATECODE = HI.STATE_CODE\n" +
"    WHERE HI.HOSPITAL_CODE = ?1" +
"    AND HI.STATUS_FLAG = 0", nativeQuery = true)
String getStateNameByHospitalCode(String hospitalCode);

@Query(value = "SELECT DISTRICTNAME FROM HOSPITAL_INFO HI\n" +
"    INNER JOIN STATE S ON S.STATECODE = HI.STATE_CODE\n" +
"    INNER JOIN DISTRICT D ON D.STATECODE = HI.STATE_CODE AND D.DISTRICTCODE = HI.DISTRICT_CODE\n" +
"    WHERE HI.HOSPITAL_CODE = ?1" +
"    AND HI.STATUS_FLAG = 0", nativeQuery = true)
String getDistrictNameByHospitalCode(String hospitalCode);

HospitalInformation findByhospitalCode(String hospitalcode);

@Query("FROM HospitalInformation where assigned_dc=:dcId and status=0")
List<HospitalInformation> getAllDcConfig(Long dcId);

@Query("FROM HospitalInformation where hospitalCode=:hospitalId")
HospitalInformation finddatabyHospitalCode(String hospitalId);

@Query("select count(*) from HospitalInformation where hospitalCode=:hospitalCode and status=0")
Integer countRowForCheckDuplicatehospCode(String hospitalCode);

@Query("select hospitalName,hospitalCode from HospitalInformation")
List<Object[]> gethospitalList();

@Query("select h.hospitalName,h.hospitalCode,h.districtcode.statecode.stateName, h.districtcode.districtname,"
+ "h.mobile,h.emailId,h.hospitalCategoryid,h.hcValidFromDate,h.hcValidToDate,h.mou,h.mouStartDate,"
+ "h.mouEndDate from HospitalInformation h")
List<Object[]> allHospList();

@Query(value = "SELECT\n" +
"    H.HOSPITAL_NAME,\n" +
"    H.HOSPITAL_CODE,\n" +
"    S.STATENAME,\n" +
"    D.DISTRICTNAME,\n" +
"    H.MOBILE,\n" +
"    H.EMAIL_ID,\n" +
"    HC.HOSPITALCATEGORYNAME,\n" +
"    H.HC_VALID_FROM_DATE,\n" +
"    H.HC_VALID_TO_DATE,\n" +
"    H.MOU,\n" +
"    H.MOU_START_DATE,\n" +
"    H.MOU_END_DATE,\n" +
"    H.EMPANELMENTSTATUS_FLAG,\n" +
"    H.MOU_STATUS,\n" +
"    H.MOU_DOC_UPLOAD,\n" +
"    H.IS_BLOCK_ACTIVE,\n" +
"    H.CPD_APPROVAL_REQUIRED,\n" +
"    H.EXCEPTIONHOSPITAL\n" +
"FROM HOSPITAL_INFO H\n" +
"LEFT JOIN STATE S ON S.STATECODE = H.STATE_CODE\n" +
"LEFT JOIN DISTRICT D ON D.DISTRICTCODE = H.DISTRICT_CODE AND D.STATECODE = S.STATECODE\n" +
"LEFT JOIN HOSPITALCATEGORYMASTER HC ON HC.HOSPITALCATEGORYID = H.HOSPITAL_CATEGORYID\n" +
"ORDER BY UPDATED_ON DESC NULLS LAST", nativeQuery = true)
List<Object[]> getAllEmpaneledHospitalList();
// List<HospitalInformation> getListOfHosp();
@Query(value = "SELECT\n" +
"    H.HOSPITAL_NAME,\n" +
"    H.HOSPITAL_CODE,\n" +
"    S.STATENAME,\n" +
"    D.DISTRICTNAME,\n" +
"    H.MOBILE,\n" +
"    H.EMAIL_ID,\n" +
"    HC.HOSPITALCATEGORYNAME,\n" +
"    H.INTPROFILEID,\n" +
"    H.INTONLINESERVICEID,H.HOSPITAL_ID\n"+
"FROM HOSPITAL_INFO H\n" +
"LEFT JOIN STATE S ON S.STATECODE = H.STATE_CODE\n" +
"LEFT JOIN DISTRICT D ON D.DISTRICTCODE = H.DISTRICT_CODE AND D.STATECODE = S.STATECODE\n" +
"LEFT JOIN HOSPITALCATEGORYMASTER HC ON HC.HOSPITALCATEGORYID = H.HOSPITAL_CATEGORYID\n" +
"where H.HOSPITAL_CODE=:hospitalCode " +
"AND H.EMPANELMENTSTATUS_FLAG=0" +
"AND H.STATUS_FLAG=0", nativeQuery = true)
List<Object[]> getListforEmpanelmentDetailUpdation(String hospitalCode); 

@Query(value = "SELECT    \r\n"
+ "    h.hospital_name,\r\n"
+ "    H.hospital_code,\r\n"
+ "    s.statename,\r\n"
+ "    D.districtname,\r\n"
+ "    m.hospitalcategoryname,\r\n"
+ "    H.hc_valid_from_date,\r\n"
+ "    H.hc_valid_to_date,\r\n"
+ "    H.mou_start_date,\r\n"
+ "    H.mou_end_date,\r\n"
+ "    H.backdate_admission_days,\r\n"
+ "    H.backdate_discharge_days\r\n"
+ "        FROM hospital_info   h\r\n"
+ "        INNER JOIN state     s ON H.state_code = s.statecode\r\n"
+ "        INNER JOIN district  d ON d.DISTRICTCODE = h.DISTRICT_CODE AND d.STATECODE=H.state_code\r\n"
+ "        INNER JOIN hospitalcategorymaster m ON h.hospital_categoryid = m.hospitalcategoryid\r\n"
+ "		   INNER JOIN USERDETAILS U ON U.USERID=H.USER_ID AND U.TMS_LOGIN_STATUS=0 AND U.STATUS_FLAG=0\r\n"
+ "        WHERE h.STATUS_FLAG=0\r\n"
+ "        order by H.backdate_discharge_days desc", nativeQuery = true) 
List<Object[]> getallhospitalbackdatelogdata();

List<HospitalInformation> findBymouEndDateBetween(Date fromdate, Date todate);

List<HospitalInformation> findByhcValidToDateBetween(Date fromdate, Date todate);
@Query(value=" SELECT distinct\r\n"
+ "                    h.hospital_id,\r\n"
+ "                    (h.hospital_name||' ('||h.hospital_code||')') as hospname,\r\n"
+ "                    h.hospital_code,\r\n"
+ "                    h.hospital_name\r\n"
+ "                FROM\r\n"
+ "                    hospital_info h\r\n"
+ "                WHERE\r\n"
+ "                    h.ASSIGNED_DC=?1\r\n"
+ "                    AND h.district_code =?3\r\n"
+ "                    AND h.state_code =?2\r\n"
+ "                    AND h.STATUS_FLAG=0\r\n"
+ "                ORDER BY\r\n"
+ "                    h.hospital_name",nativeQuery=true)
List<Object[]> getHospitalbyDistrictIddcid(Long dcid, String stateid, String distid);

@Query(value="select count(*) from hospital_info h \r\n"
+ "where h.hospital_code=?3 \r\n"
+ "and h.state_code=?1 and To_number(district_code)=?2",nativeQuery=true)
Integer checkhospitalexistornot(String hospstate, String hospdist, String hospital);

@Query(value= "Select  H.Hospital_Id Hospital_Id,\r\n"
+ "        H.Hospital_Name Hospital_Name,\r\n"
+ "        H.Hospital_Code Hospital_Code\r\n"
+ "    From Hospital_Info H\r\n"
+ "    INNER JOIN USERDETAILS U ON U.USERID=H.USER_ID AND U.STATUS_FLAG=0 AND U.TMS_LOGIN_STATUS=0\r\n"
+ "    And H.Status_Flag=0 And H.STATE_CODE=decode(?1,null,H.STATE_CODE,?1)\r\n"
+ "    And H.DISTRICT_CODE=decode(?2,null,H.DISTRICT_CODE,?2)\r\n"
+ "    ORDER BY H.Hospital_Name",nativeQuery=true)
List<Object[]> gettmasactivehospitallist(String state, String dist);
@Query(value = "SELECT HI.HOSPITAL_ID,HI.HOSPITAL_NAME ||'('|| HI.HOSPITAL_CODE||')' as HospitalName,HI.STATE_CODE,HI.DISTRICT_CODE FROM HOSPITAL_INFO HI\r\n"
+ " WHERE HI.HOSPITAL_CODE = ?1 \r\n"
+ " AND HI.STATUS_FLAG = 0", nativeQuery = true)
List<Object[]> getStateDistrictByHospitalCode(String hospitalCode);

@Query(value = "select count(1) from HOSPITAL_SCHEME_SPECIALITY_MAPPING C   \r\n"
		+ "				INNER JOIN HOSPITAL_INFO HI ON C.HOSPITALCODE=HI.HOSPITAL_CODE AND HI.STATUS_FLAG=0\r\n"
		+ "				where C.PACKAGEHEADERCODE=?1\r\n"
		+ "				AND HI.HOSPITAL_ID=?2\r\n"
		+ "				AND C.STATUSFLAG=0", nativeQuery = true)
Integer checkschemepackagetaggedornot(String packagecode,Long hospitalid);
}