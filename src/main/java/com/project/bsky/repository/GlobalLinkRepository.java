package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.GlobalLink;

@Repository
public interface GlobalLinkRepository extends JpaRepository<GlobalLink, Long>{

	@Query("from GlobalLink  where bitStatus=0 order by globalLinkName asc")
	List<GlobalLink> allactiveglobaldata();
	
	@Query("from GlobalLink order by globalLinkId desc")
	List<GlobalLink> getalldata();

	@Query("select count(*) from GlobalLink where globalLinkName=:globalLinkName")
	Integer cheakduplicate(String globalLinkName);

	GlobalLink findByglobalLinkName(String globalLinkName);

	@Query("select count(*) from GlobalLink where order=:order")
	Integer cheakduplicateorder(Integer order);

	GlobalLink findByorder(Integer order);
	
	GlobalLink findByglobalLinkNameAndBitStatus(String globalLinkName,Integer status);

}
