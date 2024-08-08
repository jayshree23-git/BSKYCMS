package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.AuthorityEntity;
import com.project.bsky.entity.TSetAuthority;
@Repository
public interface TSetAuthorityRepository extends JpaRepository<TSetAuthority, Integer> {

//	@Query("From TSetAuthority where bitDeletedFlag=0 and tinStageNo=1 and intProcessId=:intProcessId")
//	List<TSetAuthority> getDataByIntOnlineServiceIdAndIntProcessId(Integer intProcessId);
	
	
	@Query(value="select NVL( LISTAGG( intPrimaryAuth, ', ') WITHIN GROUP (ORDER BY intPrimaryAuth)  ,0) AS intPrimaryAuth,NVL( LISTAGG( intATAProcessId, ', ') WITHIN GROUP (ORDER BY intATAProcessId)  ,0) AS intATAProcessId from t_set_authority where tinStageNo=:stageNo and intProcessId=:processId and bitDeletedFlag = 0 ",nativeQuery = true)
	List<Object[]> getAuthorityDetailsByProcessIdAndStageNo(Integer processId, Integer stageNo);
	
	@Query(value="select FC.formDetails, pn.vchTableName from m_dyn_form_configuration as FC join m_process_name as pn on FC.itemId = pn.intProcessId where FC.deletedFlag =0 and pn.bitDeletedFlag = 0 and FC.itemId =:processId ; ",nativeQuery = true)
	List<Object[]> getApplicationDetail(Integer processId);

	AuthorityEntity findByIntProcessIdAndTinStageNoAndIntProjectId(Integer processId, Integer stageNo, Integer projectId);

	@Query(value="SELECT NVL( LISTAGG( intPrimaryAuth, ', ') WITHIN GROUP (ORDER BY intPrimaryAuth)  ,0) as FORWARD_TO_AUTH FROM t_set_authority where intProcessId =:processId and intLabelId =:projectId and tinStageNo =:stageNo and bitDeletedFlag = 0 ",nativeQuery = true)
	String getIntProcessIdAndTinStageNoAndIntProjectId(Integer processId, Integer stageNo, Integer projectId);
	
	@Query(value=" SELECT intATAProcessId FROM t_set_authority where intProcessId =:processId and intLabelId =:projectId and tinStageNo =:stageNo and bitDeletedFlag = 0  ",nativeQuery = true)
	String getintATAProcessId(Integer processId, Integer stageNo, Integer projectId);

	@Query(value="select VCH_STATE FROM DYN_BSKY_HOS_B_INFO where INTONLINESERVICEID=:parseInt",nativeQuery = true)
	String getStateCode(int parseInt);

	@Query(value="select NVL( LISTAGG( intPrimaryAuth, ', ') WITHIN GROUP (ORDER BY intPrimaryAuth)  ,0) AS intPrimaryAuth,NVL( LISTAGG( intATAProcessId, ', ') WITHIN GROUP (ORDER BY intATAProcessId)  ,0) AS intATAProcessId from t_set_authority where tinStageNo=:stageNo and intProcessId=:processId and (:labelId=0 or INTLABELID=:labelId) and bitDeletedFlag = 0 ",nativeQuery = true)
	List<Object[]> getAuthorityDetailsByProcessIdAndStageNo(Integer processId, Integer stageNo,Integer labelId);
}
