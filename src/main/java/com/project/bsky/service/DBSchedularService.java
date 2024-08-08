/**
 * 
 */
package com.project.bsky.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Response;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.Mstschedular;
import com.project.bsky.model.Schedulartracker;

/**
 * @author rajendra.sahoo
 *
 */
public interface DBSchedularService {

	List<Mstschedular> getalldata();

	List<Schedulartracker> getdbschedularlist(String procedure, Date fromdate, Date todate);

	List<Schedulartracker> getdbschedularlist(String procedure, String month, String year);

	Map<String, Object> getschedulardetailslist(Integer procid, Date date);

	Response savescheduler(Mstschedular mstscheduler);

	AuthRequest generateotpforscheduler();

	AuthRequest validateotpforscheduler(String otp);

	List<Mstschedular> getallschedulerlist();

	Response updatescheduler(Mstschedular mstscheduler);

	List<Object> getschedulerloglist(Long scheduler) throws Exception;

	List<Object> getcpddishonorcountlist(Date formdate) throws Exception;

	Map<String, Object> deactivecpddishonour(Date formdate, String remark, String cpdid, Long createdby) throws Exception;

}
