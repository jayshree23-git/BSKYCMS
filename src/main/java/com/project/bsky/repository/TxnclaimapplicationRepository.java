package com.project.bsky.repository;

import com.project.bsky.model.Txnclaimapplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TxnclaimapplicationRepository extends JpaRepository<Txnclaimapplication, Integer> {
    @Query(value = "SELECT\n" +
            "    TXNAPP.CLAIMID,\n" +
            "    TXNAPP.TRANSACTIONDETAILSID,\n" +
            "    TXNAPP.HOSPITALCODE,\n" +
            "    TXNAPP.URN,\n" +
            "    TXNDETAILS.DATEOFADMISSION,\n" +
            "    TXNAPP.DISCHARGESLIP,\n" +
            "    TXNAPP.ADDTIONAL_DOC,\n" +
            "    TXNAPP.ADDITIONAL_DOC1,\n" +
            "    TXNAPP.ADDITIONAL_DOC2,\n" +
            "    TXNAPP.INVESTIGATIONDOC,\n" +
            "    TXNAPP.INVESTIGATIONDOC2,\n" +
            "    TXNAPP.PRESURGERYPHOTO,\n" +
            "    TXNAPP.POSTSURGERYPHOTO,\n" +
            "    TXNAPP.INTRA_SURGERY_PHOTO,\n" +
            "    TXNAPP.SPECIMEN_REMOVAL_PHOTO,\n" +
            "    TXNAPP.PATIENT_PHOTO\n" +
            "FROM TXNCLAIM_APPLICATION TXNAPP\n" +
            "    LEFT JOIN TXNTRANSACTIONDETAILS TXNDETAILS\n" +
            "    ON\n" +
            "        TXNAPP.CLAIMID = TXNDETAILS.CLAIM_ID\n" +
            "ORDER BY TXNAPP.CLAIMID ASC", nativeQuery = true)
    List<Object[]> getCustomTXNClaimApplication();

    @Query(value = "SELECT SNA_MORTALITY FROM TXNCLAIM_APPLICATION WHERE CLAIMID=?1", nativeQuery = true )
	String getsnamortalitystatus(Long claimid);
    
    @Query(value = "SELECT MORTALITY,CPD_MORTALITY,SNA_MORTALITY FROM TXNCLAIM_APPLICATION WHERE CLAIMID=?1", nativeQuery = true )
    List<Object[]> getmortalitystatus(Long claimid);
}