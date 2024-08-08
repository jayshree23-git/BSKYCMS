/**
 * 
 */
package com.project.bsky.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.MeTriggerDetails;

/**
 * @author santanu.barad
 *
 */
@Repository
public interface MeTriggerDetailsRepository extends JpaRepository<MeTriggerDetails, Long> {

	MeTriggerDetails findByClaimIdAndSlNo(Long claimId, Long slNo);

	List<MeTriggerDetails> findByPhoneNoAndSlNo(String phoneNo, Long slNo);

	List<MeTriggerDetails> findByUrnAndSlNo(String urn, Long slNo);

	@Query("from MeTriggerDetails where  doctorRegNo=:doctorRegNo and  trunc(surgeryDate)=trunc(:surgeryDate)")
	List<MeTriggerDetails> findByDoctorRegNoAndSurgeryDate(String doctorRegNo, Date surgeryDate);

	List<MeTriggerDetails> findByUrnAndCaseNo(String urn, String caseNo);
}
