/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.BskyDocumentmaster;

/**
 * 
 */
@Repository
public interface BskyDocumentmasterRepository extends JpaRepository<BskyDocumentmaster, Long> {

	@Query(value = "select d.DOCUMENT_ID,\r\n"
			+ "d.DOCUMENT_NAME,\r\n"
			+ "nvl(u.full_name,'N/A'),\r\n"
			+ "To_char(d.CREATEDON ,'DD-MON-YYYY HH:MI:SS AM'),\r\n"
			+ "d.STATUSFLAG from TBL_MDR_DOCUMENT_MASTER d\r\n"
			+ "left join userdetails u on u.userid=d.CREATEDBY\r\n"
			+ "order by d.DOCUMENT_ID desc",nativeQuery = true)
	List<Object[]> getdocumentmst();

	@Query("select count(1) from BskyDocumentmaster where documentname=:documentname")
	Integer checkduplicate(String documentname);

	@Query("from BskyDocumentmaster where documentname=:documentname")
	BskyDocumentmaster getduplicate(String documentname);

	@Query(value = "SELECT A.DOCUMENT_ID,B.DOCUMENT_NAME\r\n"
			+ "FROM TBL_MDR_DOCUMENT_MAPPING A\r\n"
			+ "LEFT JOIN TBL_MDR_DOCUMENT_MASTER B ON A.DOCUMENT_ID=B.DOCUMENT_ID\r\n"
			+ "WHERE A.PROCEDURECODE=?1\r\n"
			+ "AND A.STATUSFLAG=0 AND A.DOCUMENT_TYPE=?2" ,nativeQuery = true)
	List<Object[]> gettaggeddocumentbyprocedure(String procedureCode, Integer doctype);

}
