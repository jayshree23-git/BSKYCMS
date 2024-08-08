package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.BskyDocumentmaster;
import com.project.bsky.model.OutofpacketexpenditureMaster;


@Repository
public interface OutofpacketexpenditureRepository extends JpaRepository<OutofpacketexpenditureMaster, Long>{
	
	@Query(value = "select d.EXPENDITURE_ID,\r\n"
			+ "d.EXPENDITURE_NAME,\r\n"
			+ "nvl(u.full_name,'N/A'),\r\n"
			+ "To_char(d.CREATEDON ,'DD-MON-YYYY HH:MI:SS'),\r\n"
			+ "d.STATUSFLAG from MST_OUTOF_POCKET_EXPENDITURE d\r\n"
			+ "left join userdetails u on u.userid=d.CREATEDBY\r\n"
			+ "order by d.EXPENDITURE_ID desc",nativeQuery = true)
	List<Object[]> getexpendituremst();

	@Query("select count(1) from OutofpacketexpenditureMaster where expenditurename=:expenditurename")
	Integer checkduplicate(String expenditurename);
	@Query("from OutofpacketexpenditureMaster where expenditurename=:expenditurename")
	OutofpacketexpenditureMaster getduplicate(String expenditurename);
}
