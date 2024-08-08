package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.GrievanceBy;
import com.project.bsky.model.Grivancetype;

@Repository
public interface GrivancetypeRepository extends JpaRepository<Grivancetype, Long> {
	
	Grivancetype findBygrievancetypename(String grievancetypename);

	@Query("Select Count(*) from Grivancetype where grievancetypename=:grievancetypename")
	Integer findBytypename(String grievancetypename);
	@Query("FROM Grivancetype order by id desc")

	List<Grivancetype> getDetails();

	@Query("from Grivancetype where statusflag=0 order by grievancetypename")
	List<Grivancetype> getactivegrivancetype();

	@Query(value = "select count(*) from T_ONLINE_SERVICE_APPLICATION where intprocessid=583 and VCHAPPLICATIONNO=?1",nativeQuery=true)
	Integer checkduplicategrivid(String val);


}
