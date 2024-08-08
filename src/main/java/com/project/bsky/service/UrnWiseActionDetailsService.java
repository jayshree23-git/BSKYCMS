/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.UrnWiseDataBean;

/**
 * @author priyanka.singh
 *
 */
public interface UrnWiseActionDetailsService {

	UrnWiseDataBean getUrnWiseDetails(String urnNo, Long transId);

	List<Object> getDishouner(String urnNo, Long clmId);

	List<Object> geturnWiseWardData(String urnNo, Long transId);

	List<Object> getHighEndDrugData(String urnNo, Long transId);

	List<Object> getImplantDetails(String urnNo, Long transId);


}
