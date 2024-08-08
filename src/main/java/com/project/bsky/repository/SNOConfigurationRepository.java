package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.SNOConfiguration;

/**
 * @author ronauk
 *
 */
@Repository
public interface SNOConfigurationRepository extends JpaRepository<SNOConfiguration, Integer> {

	@Transactional
	@Modifying
	@Query("update SNOConfiguration set status=1, updatedBy=:updatedBy, updatedOn=sysdate where snoUserId=:snoUserId")
	void inActivate(Integer snoUserId, Integer updatedBy);

	@Query("select count(*) from SNOConfiguration where snoUserId=:snoUserId")
	Integer checkSnoNameDulicacy(Integer snoUserId);

	@Query("select status from SNOConfiguration where snoUserId=:snoUserId")
	List<Integer> getStatus(int snoUserId);

	@Query("select count(*) from SNOConfiguration where hospitalCode=:hospitalCode and snoUserId!=:snoId and status=0")
	Integer checkHospitalNameDulicacy(String hospitalCode, Integer snoId);

	@Query("select count(*) from SNOConfiguration where hospitalCode=:hospitalCode and status=0 and snoUserId!=:snoUserId")
	Integer checkHospitalNameDulicacyForSNA(String hospitalCode, Integer snoUserId);

	@Query("FROM SNOConfiguration where hospitalCode=:hospitalCode and status=0")
	SNOConfiguration getSnoConfFromHospCode(String hospitalCode);

	@Query("FROM SNOConfiguration where hospitalCode=:hospitalCode and districtCode=:districtCode and stateCode=:stateCode and status=0")
	List<SNOConfiguration> findAllById(String hospitalCode, String districtCode, String stateCode);

	@Query("Select distinct hospitalCode from SNOConfiguration where snoUserId=:userid and status=0")
	List<String> gethostipals(Integer userid);
	
	@Query("select count(*) from SNOConfiguration where hospitalCode=:hospitalCode and snoUserId=:snoId")
	Integer checkSNOConfigDulicacy(String hospitalCode, Integer snoId);
	
	@Query("select status from SNOConfiguration where hospitalCode=:hospitalCode and snoUserId=:snoId")
	Integer checkStatus(String hospitalCode, Integer snoId);
	
	@Query("select status from SNOConfiguration where hospitalCode=:hospitalCode and snoUserId=:snoId")
	List<Integer> getStats(String hospitalCode, Integer snoId);
	
	@Query("FROM SNOConfiguration where hospitalCode=:hospitalCode and snoUserId=:snoId")
	List<SNOConfiguration> getSnoConfig(String hospitalCode, Integer snoId);
	
	@Query("select count(*) from SNOConfiguration where hospitalCode=:hospitalCode and status=0")
	Integer checkSnoConfFromHospCode(String hospitalCode);
	
	@Query("select count(*) from SNOConfiguration where snoUserId=:snoUserId and status=0")
	Integer checkSnoConf(Integer snoUserId);
	
	@Query("FROM SNOConfiguration where snoUserId=:snoId and status=0")
	List<SNOConfiguration> getAllSnoConfig(Integer snoId);

	@Query("select hospitalCode from SNOConfiguration where snoUserId=:userid and status=0")
	List<String> gettaggedhospitallistfosna(Integer userid);

	@Query(value = "select h.hospital_name,\r\n"
			+ "		h.hospital_code,\r\n"
			+ "		s.statename,\r\n"
			+ "		d.districtname,\r\n"
			+ "        nvl(t.APPROVEFLAG,'N')\r\n"
			+ "        from user_Sna_mapping us \r\n"
			+ "inner join hospital_info h on h.hospital_code=us.HOSPITAL_CODE AND us.status_flag=0\r\n"
			+ "LEFT JOIN STATE S ON s.statecode=H.STATE_CODE\r\n"
			+ "LEFT JOIN district D ON  d.districtcode=H.DISTRICT_CODE AND d.statecode=s.statecode\r\n"
			+ "left join TBL_HOSPITALAPVBYSNA t on t.HOSPITALCODE=us.HOSPITAL_CODE and t.STATUSFLAG=0\r\n"
			+ "where us.SNO_USER_ID=?1\r\n"
			+ "AND h.STATE_CODE= decode(?2, NULL, h.STATE_CODE, ?2)\r\n"
			+ "AND to_number(h.DISTRICT_CODE) = decode(?3, NULL, h.DISTRICT_CODE, to_number(?3))\r\n"
			+ "AND h.hospital_code=decode(?4, NULL, h.hospital_code,?4)\r\n"
			+ "order by h.hospital_name" , nativeQuery = true)
	List<Object[]> getalltaggedhospitallist(Long userid, String state, String dist, String hospital);
	
}
