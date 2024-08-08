package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.Implant;

@Repository
public interface ImplantRepository  extends JpaRepository<Implant, Long> {
	@Query("SELECT g.implantId FROM Implant g WHERE g.implantName=:implantName")
	Long getImplantIdByImplantName(String implantName);
	@Query("SELECT g.implantId FROM Implant g WHERE g.implantCode=:implantCode")
	
	
	Long getImplantIdByImplantCode(String implantCode);
	@Query("SELECT count(*) FROM Implant g WHERE g.implantCode=:implantCode")
	Integer countRowForCheckDuplicateImplantCode(String implantCode);
	Implant findByImplantCode(String implantCode);

}
