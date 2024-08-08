/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.FoRemark;

/**
 * @author rajendra.sahoo
 *
 */

public interface FoRemarkservice {

	Response saveforemark(FoRemark foremark);

	List<FoRemark> getforemark();

	Response updateforemark(FoRemark foremark);

	List<FoRemark> getactiveforemark();

}
