/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.SurveyMaster;

/**
 * @author santanu.barad
 *
 */
@Repository
public interface SurveyMasterRepository extends JpaRepository<SurveyMaster, Long> {

	@Query("from SurveyMaster order by surveyId desc")
	List<SurveyMaster> findAlldesc();

	@Query("from SurveyMaster where statusFlag=0 order by surveyName asc")
	List<SurveyMaster> getactivesurveylist();

	@Query(value="select GT.TYPE_ID,\r\n"
			+ "          GT.GROUP_TYPE_NAME,\r\n"
			+ "          CASE WHEN nvl(TSG.SURVEY_ID, '00') = '00' THEN\r\n"
			+ "            1 ELSE 0 END AS status\r\n"
			+ "   from group_type GT\r\n"
			+ "   LEFT JOIN tbl_survey_group_mapping TSG \r\n"
			+ "          ON GT.TYPE_ID=TSG.GROUPTYPE_ID \r\n"
			+ "           AND TSG.SURVEY_ID=?1\r\n"
			+ "           AND TSG.STATUS_FLAG=0\r\n"
			+ "   WHERE GT.STATUS=0\r\n"
			+ "   ORDER BY GT.GROUP_TYPE_NAME ASC",nativeQuery = true)
	List<Object[]> getgrouplistbysurveyid(Long surveyid);

	@Query("select count(*) from SurveyMaster where surveyName=:surveyName")
	Integer checkduplicate(String surveyName);

	@Query("from SurveyMaster where surveyName=:surveyName")
	SurveyMaster findByname(String surveyName);

	@Query(value="select QM.QUESTION_ID,\r\n"
			+ "QM.QUESTION_NAME,\r\n"
			+ "CASE WHEN nvl(TSQ.SURVEY_ID, '00') = '00' THEN\r\n"
			+ "1 ELSE 0 END AS status\r\n"
			+ "from question_master QM\r\n"
			+ "LEFT JOIN tbl_survey_QUESTION_mapping TSQ\r\n"
			+ "ON QM.QUESTION_ID=TSQ.QUESTION_ID \r\n"
			+ "AND TSQ.SURVEY_ID=?1\r\n"
			+ "AND TSQ.STATUS_FLAG=0\r\n"
			+ "WHERE QM.STATUSFLAG=0\r\n"
			+ "ORDER BY QM.QUESTION_NAME ASC",nativeQuery = true)
	List<Object[]> getquestionlistbysurveyid(Long surveyid);

	@Query(value="SELECT SURVEY_ID,SURVEY_NAME FROM SURVEY_MASTER\r\n"
			+ "WHERE STATUSFLAG=0\r\n"
			+ "AND TRUNC(EFFECTIVE_FROM)>TRUNC(SYSDATE)\r\n"
			+ "ORDER BY SURVEY_NAME ASC",nativeQuery = true)
	List<Object[]> getactivevalidsurveylist();

}
