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

import com.project.bsky.model.ReferralDoctorMapping;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface ReferralDoctorMappingRepositoty extends JpaRepository<ReferralDoctorMapping, Long> {

	@Query("Select Count(*) from ReferralDoctorMapping where userid=:cpdId and hospitalcode=:hospitalCode")
	Integer checkduplicate(String hospitalCode, Long cpdId);

	@Query("from ReferralDoctorMapping where userid=:cpdId and hospitalcode=:hospitalCode")
	ReferralDoctorMapping getdataofduplicate(String hospitalCode, Long cpdId);

	@Query(value="SELECT UDM.USERID,UDM.REFERAL_DOCTOR_ID,UDD.FULL_NAME,COUNT(*) \r\n"
			+ "FROM USER_REFERAL_DOCTOR_MAPPING UDM\r\n"
			+ "LEFT JOIN user_details_referal_doctor  UDD ON UDD.REFERAL_DOCTOR_ID=UDM.REFERAL_DOCTOR_ID\r\n"
			+ "WHERE UDM.STATUS_FLAG=0\r\n"
			+ "GROUP BY UDM.USERID,UDM.REFERAL_DOCTOR_ID,UDD.FULL_NAME\r\n"
			+ "order by UDD.FULL_NAME",nativeQuery=true)
	List<Object[]> getdoctortaglist();

	@Query(value="select dc.HOSPITAL_CODE,h.HOSPITALNAME,\r\n"
			+ "        'ODISHA',d.DISTRICTNAME,\r\n"
			+ "        b.BLOCKNAME,ht.HOSPITALTYPENAME\r\n"
			+ "from USER_REFERAL_DOCTOR_MAPPING dc\r\n"
			+ "left join HOSPITAL_INFO_REFERRAL h on h.HOSPITALID=dc.HOSPITAL_CODE\r\n"
			+ "left join district d on d.DISTRICTCODE=h.DISTCODE and d.STATECODE=h.STATECODE\r\n"
			+ "left join block b on b.BLOCKCODE=h.BLOCKCODE and b.DISTRICTCODE=h.DISTCODE and b.STATECODE=h.STATECODE\r\n"
			+ "left join hospital_type_master ht on ht.HOSPITALTYPEID=h.HOSPITALTYPE \r\n"
			+ "where USERID=?1 and dc.STATUS_FLAG=0\r\n"
			+ "order by h.HOSPITALNAME",nativeQuery=true)
	List<Object[]> getdoctortaglistbydoctorid(Long userid);

	@Query("from ReferralDoctorMapping where userid=:cpdId")
	List<ReferralDoctorMapping> getalltagged(Long cpdId);

	@Transactional
	@Modifying
	@Query("update ReferralDoctorMapping set status=1,updateby=:updatedBy,updateon=sysdate where userid=:cpdId")
	void inactiveall(Long cpdId, Long updatedBy);

}
