/**
 * 
 */
package com.project.bsky.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.DynamicReportConfiguration;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface DynamicReportConfigRepository extends JpaRepository<DynamicReportConfiguration, Long> {

	@Query("from DynamicReportConfiguration order by slno desc")
	List<DynamicReportConfiguration> getalldata();

	@Query(value = "select rd.TRANSACTIONDETAILSID,rd.CLAIMID from TBL_DYNAMIC_REPORT_DETAILS1 where rd.URN=?1 AND rd.SLNO=?2 order by rd.URN desc", nativeQuery = true)
	List<Object[]> getdynamicreportdetails(String urn,Integer flag);

	@Query(value = "SELECT ME.REMARKS,T.URN,C.CLAIM_NO,C.CASENO,T.PATIENTNAME,T.PATIENTPHONENO,T.HOSPITALNAME,\r\n"
			+ "        T.HOSPITALCODE,P.PROCEDURECODE,P.PACKAGESUBCATEGORYNAME,\r\n"
			+ "        TO_DATE(T.ACTUALDATEOFADMISSION,'DD-MM-RR')actualdateofadmission,\r\n"
			+ "        TO_DATE(T.ACTUALDATEOFDISCHARGE,'DD-MM-RR'),TO_NUMBER(T.TOTALAMOUNTCLAIMED),\r\n"
			+ "        c.claimid,t.TRANSACTIONDETAILSID,t.TXNPACKAGEDETAILID,TO_CHAR(ME.ACTIONON,'DD-MON-YYYY HH:MM:SS'),\r\n"
			+ "        CLAIMSUBMITTEDORNOT \r\n" + "    FROM TBL_ME_ACTION ME\r\n"
			+ "    LEFT JOIN TXNTRANSACTIONDETAILS T ON ME.TRANSACTIONDETAILSID=T.TRANSACTIONDETAILSID AND T.STATUSFLAG=0\r\n"
			+ "    LEFT JOIN TXNPACKAGEDETAILS_TMS P ON T.TXNPACKAGEDETAILID=P.TXNPACKAGEDETAILID AND P.DELETEDFLAG=0\r\n"
			+ "    LEFT JOIN TXNCLAIM_APPLICATION C ON T.TRANSACTIONDETAILSID=C.TRANSACTIONDETAILSID AND C.STATUSFLAG=0\r\n"
			+ "        where ME.STATUSFLAG=0\r\n"
			+ "        AND TO_DATE(T.actualdateofdischarge,'DD-MM-RR') BETWEEN ?1 AND ?2\r\n"
			+ "        ORDER BY ME_REMARKID DESC", nativeQuery = true)
	List<Object[]> getmeactiontakendetails(Date fromdate, Date todate);

	@Query("from DynamicReportConfiguration where status = 0 order by reportname asc")
	List<DynamicReportConfiguration> findAllActiveTrigger();

	@Query(value = "select rd.TRANSACTIONDETAILSID,rd.CLAIMID \r\n"
			+ "from TBL_DYNAMIC_REPORT_DETAILS1 rd\r\n"
			+ "where rd.SLNO=?2\r\n"
			+ "and rd.MEMBER_ID=(select rd1.MEMBER_ID from TBL_DYNAMIC_REPORT_DETAILS1 rd1 \r\n"
			+ "where rd1.TRANSACTIONDETAILSID=?1 and rd1.SLNO =?2)\r\n"
			+ "order by rd.URN desc", nativeQuery = true)
	List<Object[]> getdynamicreportthroughmemberid(Long txnId, Integer flag);

	@Query(value = "select rd.TRANSACTIONDETAILSID,rd.CLAIMID \r\n"
			+ "from TBL_DYNAMIC_REPORT_DETAILS1 rd\r\n"
			+ "where rd.SLNO=?2\r\n"
			+ "and rd.PHONENO=(select rd1.PHONENO from TBL_DYNAMIC_REPORT_DETAILS1 rd1 \r\n"
			+ "where rd1.TRANSACTIONDETAILSID=?1 and rd1.SLNO =?2)\r\n"
			+ "order by rd.URN desc", nativeQuery = true)
	List<Object[]> getdynamicreportthroughmobileno(Long txnId, Integer flag);

	@Query(value = "select rd.TRANSACTIONDETAILSID,rd.CLAIMID \r\n"
			+ "from TBL_DYNAMIC_REPORT_DETAILS1 rd\r\n"
			+ "where rd.SLNO=?2\r\n"
			+ "and rd.URN=?1\r\n"
			+ "order by rd.URN desc", nativeQuery = true)
	List<Object[]> getdynamicreportthroughurn(String urn, Integer flag);

}
