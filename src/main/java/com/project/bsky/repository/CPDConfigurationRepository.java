/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.CPDConfiguration;

/**
 * @author ronauk
 *
 */
@Repository
public interface CPDConfigurationRepository extends JpaRepository<CPDConfiguration, Integer> {

	@Transactional
	@Modifying
	@Query("update CPDConfiguration set status=1, updatedBy=:updatedBy, updatedOn=sysdate where cpdUserId=:cpdUserId")
	void inActivate(Integer cpdUserId, Integer updatedBy);

	@Query("select count(*) from CPDConfiguration where cpdUserId=:cpdUserId and status=0")
	Integer checkCpdNameDulicacy(Integer cpdUserId);

	@Query("select status from CPDConfiguration where cpdUserId=:cpdUserId")
	List<Integer> getStatus(int cpdUserId);

	@Query("select count(*) from CPDConfiguration where hospitalCode=:hospitalCode")
	Integer checkHospitalNameDulicacy(String hospitalCode);

	@Query("from CPDConfiguration where cpdUserId=:bskyUserId")
	List<CPDConfiguration> findBycpduserId(Integer bskyUserId);

	@Query("select count(*) from CPDConfiguration where hospitalCode=:hospitalCode and cpdUserId=:cpdId and status=0")
	Integer checkCPDHospitalNameDulicacy(String hospitalCode, Integer cpdId);
	
	@Query("from CPDConfiguration where cpdUserId=:bskyUserId")
	List<CPDConfiguration> findAllById(Integer bskyUserId);
	
	@Query("select count(*) from CPDConfiguration where hospitalCode=:hospitalCode and cpdUserId=:cpdId")
	Integer checkCPDConfigDulicacy(String hospitalCode, Integer cpdId);
	
	@Query("select status from CPDConfiguration where hospitalCode=:hospitalCode and cpdUserId=:cpdId")
	Integer checkStatus(String hospitalCode, Integer cpdId);
	
	@Query("select status from CPDConfiguration where hospitalCode=:hospitalCode and cpdUserId=:cpdId")
	List<Integer> getStats(String hospitalCode, Integer cpdId);
	
	@Query("FROM CPDConfiguration where hospitalCode=:hospitalCode and cpdUserId=:cpdId")
	List<CPDConfiguration> getCpdConfig(String hospitalCode, Integer cpdId);

	@Query("from CPDConfiguration U where U.status=0")
	List<CPDConfiguration> findCpdName();
	
	@Query("select distinct hospitalCode from CPDConfiguration where status=0 and cpdUserId=nvl(:cpdId,cpdUserId) order by hospitalCode")
	List<String> getRestrictedHospitals(Integer cpdId);
	
	@Query("FROM CPDConfiguration where cpdUserId=:cpdId and status=0")
	List<CPDConfiguration> getAllCpdConfig(Integer cpdId);

	@Query("FROM CPDConfiguration where cpdUserId=:bskyuserid and hospitalCode=:hospitalcode")
	CPDConfiguration applyforexclusion(String hospitalcode, Integer bskyuserid);
	
	@Query("FROM CPDConfiguration where cpdUserId=:bskyuserid and hospitalCode=:hospitalcode")
	CPDConfiguration applyforinclusion(String hospitalcode, Integer bskyuserid);

	@Query("FROM CPDConfiguration where status=3")
	List<CPDConfiguration> appliedexclusionlistadmin();

	@Query(value = "select UC.HOSPITAL_CODE,\r\n"
			+ "    UC.APPROVAL_STATUS,\r\n"
			+ "    UC.CPD_USER_ID,\r\n"
			+ "    US.SNO_USER_ID,\r\n"
			+ "    TO_CHAR(CREATED_ON,'DD-MON-RR'),\r\n"
			+ "    TO_CHAR(UPDATED_ON,'DD-MON-RR') \r\n"
			+ "    from user_cpd_mapping uc \r\n"
			+ "    inner join user_sna_mapping us on uc.HOSPITAL_CODE=us.HOSPITAL_CODE\r\n"
			+ "      where us.STATUS_FLAG=0\r\n"
			+ "      and uc.STATUS_FLAG=3\r\n"
			+ "      and US.SNO_USER_ID= ?1", nativeQuery = true)
	List<Object[]> appliedexclusionlistsna(Long snoid);
	
	@Query("FROM CPDConfiguration where status=2 ")
	List<CPDConfiguration> appliedinclusionlistadmin();

	@Query(value = "select UC.HOSPITAL_CODE,\r\n"
			+ "    UC.APPROVAL_STATUS,\r\n"
			+ "    UC.CPD_USER_ID,\r\n"
			+ "    US.SNO_USER_ID \r\n"
			+ "    from user_cpd_mapping uc \r\n"
			+ "    inner join user_sna_mapping us on uc.HOSPITAL_CODE=us.HOSPITAL_CODE\r\n"
			+ "      where us.STATUS_FLAG=0\r\n"
			+ "      and uc.STATUS_FLAG=2\r\n"
			+ "      and US.SNO_USER_ID= ?1", nativeQuery = true)
	List<Object[]> appliedinclusionlistsna(Long snoid);

	@Query(value = "SELECT \r\n"
			+ "DISTINCT h.HOSPITAL_CODE,h.HOSPITAL_NAME ,S.STATENAME ,D.DISTRICTNAME  \r\n"
			+ "        from hospital_info H\r\n"
			+ "        LEFT JOIN  STATE S ON S.STATECODE = H.STATE_CODE\r\n"
			+ "        LEFT JOIN DISTRICT D ON D.DISTRICTCODE = H.DISTRICT_CODE AND H.STATE_CODE=D.STATECODE \r\n"
			+ "        WHERE h.STATUS_FLAG=0\r\n"
			+ "        AND HOSPITAL_CODE not in(select HOSPITAL_CODE from user_cpd_mapping ucm\r\n"
			+ "              inner join user_details_cpd udc on udc.BSKYUSERID=ucm.CPD_USER_ID\r\n"
			+ "              where udc.USER_ID=?1\r\n"
			+ "              and ucm.STATUS_FLAG in (0,2,3))\r\n"
			+ "        AND h.STATE_CODE= decode(?2, NULL, h.STATE_CODE, ?2)\r\n"
			+ "        AND to_number(h.DISTRICT_CODE) = decode(?3, NULL, h.DISTRICT_CODE, to_number(?3))",nativeQuery = true)
	List<Object[]> getuntaghospitalofcpd(Long userid, String state, String dist);

	@Query("FROM CPDConfiguration where hospitalCode=:hospitalcode and cpdUserId=:userid")
	CPDConfiguration getthatrrcord(String hospitalcode, Integer userid);

	@Query(value = "SELECT * FROM(\r\n"
			+ "SELECT * FROM (\r\n"
			+ "SELECT\r\n"
			+ "    a.hospital_code,\r\n"
			+ "    h.hospital_name,\r\n"
			+ "    s.statename,\r\n"
			+ "    d.districtname,\r\n"
			+ "    TO_CHAR(a.created_on,'DD-MON-RR'),\r\n"
			+ "    nvl(TO_CHAR(b.created_on,'DD-MON-RR'),'N/A') created_on1,\r\n"
			+ "    a.status_flag,\r\n"
			+ "    ROW_NUMBER()OVER(PARTITION BY a.hospital_code\r\n"
			+ "                        ORDER BY b.created_on DESC\r\n"
			+ "                    ) row_num\r\n"
			+ "FROM\r\n"
			+ "    user_cpd_mapping a\r\n"
			+ "LEFT JOIN user_cpd_mapping_log B ON B.CPD_MAPPING_ID=A.CPD_MAPPING_ID\r\n"
			+ "LEFT JOIN hospital_info H ON h.hospital_code=a.hospital_code\r\n"
			+ "LEFT JOIN STATE S ON s.statecode=H.STATE_CODE\r\n"
			+ "LEFT JOIN district D ON  d.districtcode=H.DISTRICT_CODE AND d.statecode=s.statecode\r\n"
			+ "WHERE  A.CPD_USER_ID=?1\r\n"
			+ "AND    A.STATUS_FLAG=1)\r\n"
			+ "WHERE  row_num=1\r\n"
			+ "UNION\r\n"
			+ "SELECT\r\n"
			+ "    A.hospital_code,\r\n"
			+ "    h.hospital_name,\r\n"
			+ "    s.statename,\r\n"
			+ "    d.districtname,\r\n"
			+ "    TO_CHAR(A.created_on,'DD-MON-RR'),\r\n"
			+ "    'N/A' created_on1,\r\n"
			+ "    A.status_flag,\r\n"
			+ "    NULL row_num   \r\n"
			+ "FROM\r\n"
			+ "    user_cpd_mapping A\r\n"
			+ "    LEFT JOIN hospital_info H ON h.hospital_code=a.hospital_code AND h.status_flag=0\r\n"
			+ "    LEFT JOIN STATE S ON s.statecode=H.STATE_CODE \r\n"
			+ "    LEFT JOIN district D ON  d.districtcode=H.DISTRICT_CODE AND d.statecode=s.statecode\r\n"
			+ "WHERE\r\n"
			+ "        A.cpd_user_id = ?1\r\n"
			+ "    AND A.status_flag = 0)\r\n"
			+ "ORDER BY status_flag,TRIM(hospital_name)",nativeQuery = true)
	List<Object[]> getcpdtagginglog(Long userid);
	
	@Query(value = "SELECT\r\n"
			+ "    A.hospital_code,\r\n"
			+ "    h.hospital_name,\r\n"
			+ "    s.statename,\r\n"
			+ "    d.districtname,\r\n"
			+ "    TO_CHAR(A.created_on,'DD-MON-RR'),\r\n"
			+ "    NULL created_on1,\r\n"
			+ "    A.status_flag,\r\n"
			+ "    NULL row_num   \r\n"
			+ "FROM\r\n"
			+ "    user_cpd_mapping A\r\n"
			+ "    LEFT JOIN hospital_info H ON h.hospital_code=a.hospital_code AND h.status_flag=0\r\n"
			+ "    LEFT JOIN STATE S ON s.statecode=H.STATE_CODE \r\n"
			+ "    LEFT JOIN district D ON  d.districtcode=H.DISTRICT_CODE AND d.statecode=s.statecode\r\n"
			+ "WHERE\r\n"
			+ "        A.cpd_user_id = ?1\r\n"
			+ "    AND A.status_flag = 0 order by h.hospital_name",nativeQuery = true)
	List<Object[]> getcpdtagginglist(Integer userid);
	
	@Query(value = "SELECT * FROM (\r\n"
			+ "SELECT\r\n"
			+ "    a.hospital_code,\r\n"
			+ "    h.hospital_name,\r\n"
			+ "    s.statename,\r\n"
			+ "    d.districtname,\r\n"
			+ "    TO_CHAR(a.created_on,'DD-MON-RR'),\r\n"
			+ "	   nvl(TO_CHAR(b.created_on,'DD-MON-RR'),'N/A') created_on1,\r\n"
			+ "    a.status_flag,\r\n"
			+ "    ROW_NUMBER()OVER(PARTITION BY a.hospital_code\r\n"
			+ "                        ORDER BY b.created_on DESC\r\n"
			+ "                    ) row_num\r\n"
			+ "FROM\r\n"
			+ "    user_cpd_mapping a\r\n"
			+ "LEFT JOIN user_cpd_mapping_log B ON B.CPD_MAPPING_ID=A.CPD_MAPPING_ID\r\n"
			+ "LEFT JOIN hospital_info H ON h.hospital_code=a.hospital_code\r\n"
			+ "LEFT JOIN STATE S ON s.statecode=H.STATE_CODE\r\n"
			+ "LEFT JOIN district D ON  d.districtcode=H.DISTRICT_CODE AND d.statecode=s.statecode\r\n"
			+ "WHERE  A.CPD_USER_ID=?1\r\n"
			+ "AND    A.STATUS_FLAG=1 order by h.hospital_name)WHERE  row_num=1",nativeQuery = true )
	List<Object[]> getcpduntagginglist(Integer userid);

	@Query(value = "select A.hospital_code,\r\n"
			+ "			h.hospital_name,\r\n"
			+ "			s.statename,\r\n"
			+ "			d.districtname,\r\n"
			+ "			nvl(A.UPDATED_ON,A.CREATED_ON) applieddate,			\r\n"
			+ "			(case when A.status_flag=3 then 'Removal of Restriction' else 'Apply for Restriction' end) as status,\r\n"
			+ "            NULL created_on1,\r\n"
			+ "			NULL row_num  \r\n"
			+ "from user_cpd_mapping A\r\n"
			+ "LEFT JOIN hospital_info H ON h.hospital_code=a.hospital_code AND h.status_flag=0\r\n"
			+ "LEFT JOIN STATE S ON s.statecode=H.STATE_CODE\r\n"
			+ "LEFT JOIN district D ON  d.districtcode=H.DISTRICT_CODE AND d.statecode=s.statecode\r\n"
			+ "where A.CPD_USER_ID=?1\r\n"
			+ "and A.STATUS_FLAG in (2,3)\r\n"
			+ "order by applieddate desc",nativeQuery = true )
	List<Object[]> getappliedlistbycpd(Integer bskyUserId);
	
	
	
}
