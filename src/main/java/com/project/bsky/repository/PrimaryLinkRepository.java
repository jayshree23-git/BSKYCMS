package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.PrimaryLink;

@Repository
public interface PrimaryLinkRepository extends JpaRepository<PrimaryLink, Long>{

	@Query("from PrimaryLink order by primaryLinkId desc")
	List<PrimaryLink> findAlldesc();

	@Query("select count(*) from PrimaryLink where FUNCTION_ID=:functionId")
	Integer cheakduplicate(Long functionId);

	@Query("from PrimaryLink where GLOBAL_LINK_ID=:globalLinkId and FUNCTION_ID=:functionId")
	PrimaryLink cheak(Long functionId, Long globalLinkId);

	@Query("from PrimaryLink where FUNCTION_ID=:functionId")
	PrimaryLink cheak(Long functionId);

	@Query("from PrimaryLink where FUNCTION_ID=:functionid1")
	List<PrimaryLink> findbyfunctionid(Long functionid1);

	@Query("from PrimaryLink where GLOBAL_LINK_ID=:globalid1")
	List<PrimaryLink> findbyglobalid(Long globalid1);

	@Query("from PrimaryLink where GLOBAL_LINK_ID=:globalid1 and FUNCTION_ID=:functionid1")
	List<PrimaryLink> filter(Long functionid1, Long globalid1);

	@Query("from PrimaryLink where GLOBAL_LINK_ID=:gid order by primaryLinkName asc")
	List<PrimaryLink> getrespmlist(Long gid);

	@Query("select count(*) from PrimaryLink where order=:order and GLOBAL_LINK_ID=:globalLinkId")
	Integer cheakduplicateorder(Long globalLinkId, Integer order);

	@Query("from PrimaryLink where GLOBAL_LINK_ID=:globalLinkId and order=:order")
	PrimaryLink cheakorder(Long globalLinkId, Integer order);
	
	@Query("select primaryLinkId from PrimaryLink where functionMaster.functionId=:funcId and bitStatus=0")
	Long getPrimaryLink(Long funcId);
	
	@Query("select primaryLinkId from PrimaryLink where functionMaster.functionId=:funcId and bitStatus=0")
	List<Long> getPrimaryLinkIdList(Long funcId);
	
	@Query("from PrimaryLink where functionMaster.functionId=:funcId")
	List<PrimaryLink> getPrimaryLinkList(Long funcId);
	
	@Query("select globalLink.globalLinkName from PrimaryLink where primaryLinkId=:primaryLinkId and bitStatus=0")
	String getGlobalLinkName(Long primaryLinkId);

}
