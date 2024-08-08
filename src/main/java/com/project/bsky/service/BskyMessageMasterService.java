/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.BskyMessageMaster;

/**
 * @author rajendra.sahoo
 *
 */
public interface BskyMessageMasterService {

	Response savemessage(BskyMessageMaster messagemaster);

	List<BskyMessageMaster> getalldata();

	Response updatemessage(BskyMessageMaster messagemaster);

}
