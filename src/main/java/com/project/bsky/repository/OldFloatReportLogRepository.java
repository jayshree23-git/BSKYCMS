package com.project.bsky.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.OldFloatReportLog;

/**
 * @author ronauk
 *
 */
@Repository
public interface OldFloatReportLogRepository extends JpaRepository<OldFloatReportLog, Integer> {

	@Query("FROM OldFloatReportLog l where l.createdBy=:createdBy AND trunc(l.createdOn) BETWEEN trunc(:fromdate) AND trunc(:todate)"
			+ " AND l.userId=decode(:userId,NULL,l.userId,:userId)"
			+ " AND l.hospitalCode=decode(:hospitalCode,NULL,l.hospitalCode,:hospitalCode)"
			+ " AND l.districtCode=decode(:districtCode,NULL,l.districtCode,:districtCode)"
			+ " AND l.stateCode=decode(:stateCode,NULL,l.stateCode,:stateCode) AND l.statusflag=0"
			+ " ORDER BY l.createdOn DESC")
	List<OldFloatReportLog> getGeneratedReports(Long userId, Date fromdate, Date todate, String hospitalCode,
			String districtCode, String stateCode, Long createdBy);

}
