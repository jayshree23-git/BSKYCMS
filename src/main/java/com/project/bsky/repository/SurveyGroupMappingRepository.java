/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.SurveyGroupMapping;

/**
 * Rajendra Prasad Sahoo
 */
@Repository
public interface SurveyGroupMappingRepository extends JpaRepository<SurveyGroupMapping, Long>{

	@Query("from SurveyGroupMapping where surveyId=:surveyId and groupId=:groupId")
	SurveyGroupMapping getdatabysurveyidandgroupid(Long surveyId, Long groupId);

	
	@Query(value="SELECT S.SURVEY_NAME,G.GROUP_TYPE_NAME,U.FULL_NAME,\r\n"
			+ "        TO_CHAR(TS.CREATED_ON,'DD-MON-YYYY HH:MI:SS AM') CREATED_ON\r\n"
			+ "FROM TBL_SURVEY_GROUP_MAPPING TS\r\n"
			+ "LEFT JOIN SURVEY_MASTER S ON TS.SURVEY_ID=S.SURVEY_ID \r\n"
			+ "LEFT JOIN GROUP_TYPE G ON TS.GROUPTYPE_ID=G.TYPE_ID\r\n"
			+ "LEFT JOIN USERDETAILS U ON TS.CREATED_BY=U.USERID\r\n"
			+ "WHERE S.STATUSFLAG=0 AND TS.STATUS_FLAG=0\r\n"
			+ "AND TS.SURVEY_ID=DECODE(?1,NULL,TS.SURVEY_ID,?1)\r\n"
			+ "order by S.SURVEY_NAME,G.GROUP_TYPE_NAME",nativeQuery = true)
	List<Object[]> getallsurveygroupmappinglist(Long surveyid);

}
