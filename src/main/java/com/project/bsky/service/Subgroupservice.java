/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.Group;
import com.project.bsky.model.Subgroup;


/**
 * @author rajendra.sahoo
 *
 */
public interface Subgroupservice {

	

	List<Group> getgroupname();

	List<Subgroup> getalldata();

	Integer savesubgroup(long groupid, String subgroupname, String createdby);

	Integer delete(long subgroupid);

	Subgroup getbyid(long subgroupid);

	

	Integer update(long groupid, String subgroup, String updateby, long subgroupid, Integer status);


}
