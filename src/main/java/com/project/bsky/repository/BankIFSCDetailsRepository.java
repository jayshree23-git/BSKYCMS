package com.project.bsky.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.BankIFSCDetails;

@Repository
public interface BankIFSCDetailsRepository extends JpaRepository<BankIFSCDetails, Integer> {

	 @Query(value = "SELECT INT_IFSC_ID, VCH_IFSC_CODE, VCH_BRANCH_NAME, INT_MIN_ACCOUNT_NO, INT_MAX_ACCOUNT_NO " +
	            "FROM M_IFSC_DETAILS " +
	            "WHERE VCH_BANK_NAME = ?1 " +
	            "AND VCH_DISTRICT = ?2",
	            nativeQuery = true)
	List<Map<String, Object>> getBankIFSCDetails(String bankName, String districtName);

}
