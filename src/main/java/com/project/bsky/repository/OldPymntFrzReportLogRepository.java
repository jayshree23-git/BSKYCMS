package com.project.bsky.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.OldPaymentFreezeReportLog;

/**
 * @author ronauk
 *
 */
@Repository
public interface OldPymntFrzReportLogRepository extends JpaRepository<OldPaymentFreezeReportLog, Integer> {

	@Query("FROM OldPaymentFreezeReportLog l where l.userId=:userId AND trunc(l.createdOn) BETWEEN trunc(:fromdate) AND trunc(:todate)"
			+ " AND l.hospitalCode=decode(:hospitalCode,NULL,l.hospitalCode,:hospitalCode)"
			+ " AND l.districtCode=decode(:districtCode,NULL,l.districtCode,:districtCode)"
			+ " AND l.stateCode=decode(:stateCode,NULL,l.stateCode,:stateCode) AND l.statusflag=0"
			+ " ORDER BY l.createdOn DESC")
	List<OldPaymentFreezeReportLog> getGeneratedReports(Long userId, Date fromdate, Date todate, String hospitalCode,
			String districtCode, String stateCode);

}
