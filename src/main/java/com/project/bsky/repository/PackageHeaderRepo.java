package com.project.bsky.repository;


import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.PackageHeader;

@Repository

public interface PackageHeaderRepo extends JpaRepository<PackageHeader, Long>{

	PackageHeader findByHeaderId(Long headerId);

	//Integer cheakduplicate(Long headerId);
	@Query("SELECT g.headerId FROM PackageHeader g WHERE g.packageheadername=:packageheadername")
	Long getHeaderIdByHeaderName(@Param("packageheadername") String packageheadername);
	@Query("SELECT g.headerId FROM PackageHeader g WHERE g.packageheadercode=:packageheadercode")
	Long getHeaderIdByHeaderCode(String packageheadercode);

	//List<PackageHeader> findByPackageheadercode(String packageheadercode);
	
//	@Query("select count(*) from PackageHeader where headerId=:headerId")
//	Integer cheakduplicate(Long headerId);

	@Query("select g.headerId as hedaredId, g.packageheadercode as headerCode, g.packageheadername as headerName FROM PackageHeader g order by g.packageheadercode")
	List<Map<String,Object>> getPackageHeaderCode();
}
