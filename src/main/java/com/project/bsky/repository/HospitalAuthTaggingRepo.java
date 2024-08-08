package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.HospitalAuthTagging;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface HospitalAuthTaggingRepo extends JpaRepository<HospitalAuthTagging, Integer>{

	@Query("select count(*) from HospitalAuthTagging where hospitalCode=:hospitalCode and status=0")
	Integer findDuplicate(String hospitalCode);

	@Query(value = "select m.AUTHTAGGING_ID,m.tag_hospitalcode,m.user_id,m.group_id,m.status_flag,u.FULL_NAME,h.HOSPITAL_NAME,s.statename,d.districtname from M_HOSPITALAUTHTAGGING m\r\n"
			+ "         inner join user_details_profile u on m.user_id=u.USER_ID\r\n"
			+ "         inner join hospital_info h on m.tag_hospitalcode=h.HOSPITAL_CODE\r\n"
			+ "         left join District d on u.DISTRICTCODE=d.districtcode AND d.statecode = u.STATE_CODE\r\n"
			+ "         left join State s on d.statecode=s.statecode\r\n"
			+ "         where m.status_flag=0 and u.STATUS_FLAG=0 and h.STATUS_FLAG=0\r\n"
			+ "         order by m.AUTHTAGGING_ID\r\n"
			+ "", nativeQuery = true)
	List<Object> getAuthDetails();

	@Query("select count(*) from HospitalAuthTagging where userId=:authId and tagHospitalCode=:hosCode and status=0")
	Integer checkHospitalConfigDuplicate(String hosCode, Integer authId);

	@Transactional
	@Modifying
	@Query("update HospitalAuthTagging set status=1, updatedBy=:updatedBy, updatedOn=sysdate where userId=:authTaggingId")
	void inActivate(Integer authTaggingId, Integer updatedBy);

	@Query("select status from HospitalAuthTagging where tagHospitalCode=:hosCode and userId=:authId")
	Integer checkStatus(String hosCode, Integer authId);

	@Query("FROM HospitalAuthTagging where tagHospitalCode=:hosCode and userId=:authId")
	List<HospitalAuthTagging> getHosConfig(String hosCode, Integer authId);

	@Query("select count(*) from HospitalAuthTagging where userId=:authId and tagHospitalCode=:hosCode")
	Integer checkUpdateHospitalConfigDuplicate(String hosCode, Integer authId);

}
