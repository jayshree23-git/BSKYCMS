/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.SnaSysRejectedList;

/**
 * @author rajendra.sahoo
 *
 */
public interface SystemRejectedReportsService {

	List<SnaSysRejectedList> sysrejreports(String formdate, String todate, String state, String dist, String hospital,
			String userid);

}
