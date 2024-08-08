/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.FoBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.FinancialOfficerService;



/**
 * @author hrusikesh.mohanty
 *
 */

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class FinancialOfficer {

	@Autowired
	private FinancialOfficerService financilaservice;
	
	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getfinanciladetails")
	public List<Object> getfodetails(@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "finacialno", required = false) String finacialno) {
		List<Object> fiancialList = null;
		try {
			if (Objects.equals(fromDate, "") && Objects.equals(toDate, "")) {
				fiancialList = financilaservice.getfinaciladetails(null, null, finacialno);
			} else {
				fiancialList = financilaservice.getfinaciladetails(fromDate, toDate, finacialno);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return fiancialList;

	}

	@ResponseBody
	@GetMapping(value = "/getfinanciladetailsid")
	public List<Object> getfinacilareportthroughid(@RequestParam(value = "id", required = false) String id) {
		List<Object> listInteger = null;
		try {
			listInteger = financilaservice.getUSerDetailsDAta(id);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listInteger;
	}

	@ResponseBody()
	@GetMapping(value = "/inseertdatainfinacialdetails")
	public ResponseEntity<Response> inseertdatainfinacialdetails(
			@RequestParam(value = "remarks", required = false) String remarks,
			@RequestParam(value = "value", required = false) long value,
			@RequestParam(value = "userid", required = false) long userid,
			@RequestParam(value = "amount", required = false) long amount,
			@RequestParam(value = "floatid", required = false) long floatid,
			@RequestParam(value = "floatno", required = false) String floatno,
			@RequestParam(value = "flag", required = false) String flag) {
		Response response = null;
		try {
			response = financilaservice.insertdata(remarks, value, userid,amount,floatid,floatno,flag);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);

	}

	@ResponseBody()
	@GetMapping(value = "/updatedatainfinacialdetails")
	public ResponseEntity<?> updatedatainfinacialdetails(
		   @RequestParam(value = "dataArray", required = false)String dataArray,
		   @RequestParam(value = "ApprovedAmount", required = false)String ApprovedAmount,
		   @RequestParam(value = "userid", required = false)String userid,
		   @RequestParam(value = "remarks", required = false)String remarks
			) {
		Response response = null;
		List<FoBean> foBeanList = new ArrayList<FoBean>();
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(dataArray);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				FoBean foBean = new FoBean();
				foBean.setId(String.valueOf(jsonObject.get("id")));
				foBean.setAmount(String.valueOf(jsonObject.get("amount")));
				foBeanList.add(foBean);
//				financilaservice.updateData(jsonObject.get("id"), jsonObject.get("amount"));
//				////System.out.println(jsonObject.get("id"));
//				////System.out.println(jsonObject.get("amount"));
			}
			response=financilaservice.updatedvalue(foBeanList,ApprovedAmount,userid,remarks);
//			////System.out.println(Arrays.toString(foBeanList.toArray()));
//			for (FoBean num : foBeanList) { 		      
//		           ////System.out.println(num);
//		      }
		} catch (JSONException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
}
