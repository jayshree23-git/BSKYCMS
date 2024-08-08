package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.VitalStatistics;


@Repository

public interface VitalStatisticsRepository extends JpaRepository<VitalStatistics, Long> {
	@Query("SELECT g.vitalStatisticsId FROM VitalStatistics g WHERE g.vitalstatisticsname=:vitalstatisticsname")
	Long getVitalStatisticsIdByVitalstatisticsName(String vitalstatisticsname);
	@Query("SELECT g.vitalStatisticsId FROM VitalStatistics g WHERE g.vitalstatisticscode=:vitalstatisticscode")
	Long getVitalStatisticsIdByVitalstatisticsCode(String vitalstatisticscode);

	//VitalStatistics findByvitalStatisticsIdId(Long vitalStatisticsId);
	//PackageHeader findByHeaderId(Long headerId);

	//VitalStatistics findByvitalStatisticsId(Long vitalStatisticsId);

}
