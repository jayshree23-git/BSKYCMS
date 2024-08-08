package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.UserDCFaceDetails;


/**
 * Rajendra
 */

@Repository
public interface UserDCFaceDetailsRepository extends JpaRepository<UserDCFaceDetails, Long>{

	@Query(value ="SELECT UD.FACE_DETAILS_ID,\r\n"
				+ "        UD.USER_ID,\r\n"
				+ "        U.USERNAME,\r\n"
				+ "        U.FULL_NAME,\r\n"
				+ "        U.PHONE,\r\n"
				+ "        TO_CHAR(UD.CREATEDON,'DD-MON-YYYY HH:MI:SS AM')\r\n"
				+ "FROM USER_DC_FACE_DETAILS UD\r\n"
				+ "LEFT JOIN USERDETAILS U ON U.USERID=UD.USER_ID\r\n"
				+ "WHERE UD.STATUSFLAG=0\r\n"
				+ "AND UD.USER_ID=DECODE(?1,NULL,UD.USER_ID,?1)\r\n"
				+ "AND U.GROUPID=?2\r\n"
				+ "ORDER BY U.FULL_NAME",nativeQuery = true)
	List<Object[]> getdcfacelist(Long dcId,Integer group);
	
	@Query(value ="SELECT  UD.FACE_DETAILS_ID,\r\n"
				+ "        UD.USER_ID,\r\n"
				+ "        U.USERNAME,\r\n"
				+ "        U.FULL_NAME,\r\n"
				+ "        U.PHONE,\r\n"
				+ "        TO_CHAR(UD.CREATEDON,'DD-MON-YYYY HH:MI:SS AM') CREATEON,\r\n"
				+ "        TO_CHAR(UD.UPDATEDON,'DD-MON-YYYY HH:MI:SS AM') UPDATEON,\r\n"
				+ "        U1.FULL_NAME UPDATEDBY\r\n"
				+ "FROM USER_DC_FACE_DETAILS UD\r\n"
				+ "LEFT JOIN USERDETAILS U ON U.USERID=UD.USER_ID\r\n"
				+ "LEFT JOIN USERDETAILS U1 ON U1.USERID=UD.UPDATEDBY\r\n"
				+ "WHERE UD.STATUSFLAG=1\r\n"
				+ "AND UD.USER_ID=DECODE(?1,NULL,UD.USER_ID,?1)\r\n"
				+ "AND U.GROUPID=?2\r\n"
				+ "ORDER BY UD.UPDATEDON DESC",nativeQuery = true)
	List<Object[]> getlogdtails(Long dcId,Integer group);


}
