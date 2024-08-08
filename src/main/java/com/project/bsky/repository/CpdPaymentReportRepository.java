package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.CpdPaymentReportModel;

public interface CpdPaymentReportRepository extends JpaRepository<CpdPaymentReportModel,Long> {
	@Query("FROM CpdPaymentReportModel where statusFlag=0 order by id desc ")
	CpdPaymentReportModel getNote();

}
