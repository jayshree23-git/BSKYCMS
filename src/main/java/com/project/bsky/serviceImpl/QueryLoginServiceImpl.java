/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.QueryCheck;
import com.project.bsky.model.QueryLoginMater;
import com.project.bsky.model.QueryLoginMaterLog;
import com.project.bsky.repository.QueryCheckRepository;
import com.project.bsky.repository.QueryLoginLogRepository;
import com.project.bsky.repository.QueryLoginRepository;
import com.project.bsky.service.QueryLoginService;

/**
 * @author santanu.barad
 *
 */
@Service
public class QueryLoginServiceImpl implements QueryLoginService {
	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String userName;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.driver-class-name}")
	private String driverClass;

	@Autowired
	private QueryLoginRepository repository;

	@Autowired
	private QueryLoginLogRepository logRepository;

	@Autowired
	private QueryCheckRepository checkRepository;

	@Override
	public String authenticate(AuthRequest authRequest) throws Exception {
		QueryLoginMater queryLogin = null;
		String responce = null;
		Calendar calendar = Calendar.getInstance();
		QueryLoginMaterLog loginLog = new QueryLoginMaterLog();
		loginLog.setUserName(authRequest.getUserName());
		loginLog.setUserId(authRequest.getUserId());
		loginLog.setCreatedOn(calendar.getTime());
		try {
			queryLogin = repository.findByUserName(authRequest.getUserName());
			if (queryLogin != null) {
				byte[] decodedBytes = Base64.getUrlDecoder().decode(queryLogin.getPassWord());
				String decodedPassword = new String(decodedBytes);
				if (authRequest.getPassWord().equalsIgnoreCase(decodedPassword)) {
					loginLog.setStatus(0);
					responce = "Login successfully completed";
				} else {
					loginLog.setStatus(1);
					throw new Exception("Invalid credential");
				}
			} else {
				throw new RuntimeException("User not available");
			}
		} catch (Exception exception) {
			throw exception;
		} finally {
			logRepository.save(loginLog);
		}
		return responce;
	}

	@Override
	public Map<String, Object> queryRequest(AuthRequest authRequest) throws Exception {
		Map<String, Object> object = new HashMap<String, Object>();
		List<String> columnNames = new ArrayList<>();
		List<Object[]> columnData = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		QueryCheck checkQuery = new QueryCheck();
		checkQuery.setQueryBy(authRequest.getUserId());
		checkQuery.setCreatedOn(calendar.getTime());
		String query = null;
		try {
			// step1 load the driver class
			Class.forName(driverClass);

			// step2 create the connection object
			Connection con = DriverManager.getConnection(url, userName, password);

			// step3 create the statement object
			Statement stmt = con.createStatement();

			// step4 execute query
			query = authRequest.getMessage();
			if (query != null) {
				query.trim();
			}
			checkQuery.setQueryContent(query);
			ResultSet rs = stmt.executeQuery(query);
			if (rs != null) {
				ResultSetMetaData columns = rs.getMetaData();
				int i = 0;
				while (i < columns.getColumnCount()) {
					i++;
					columnNames.add(columns.getColumnName(i));
				}

				Object[] objects = null;
				while (rs.next()) {
					objects = new Object[columnNames.size()];
					for (i = 0; i < columnNames.size(); i++) {
						objects[i] = rs.getString(columnNames.get(i));
					}
					columnData.add(objects);
				}

			}
			con.close();
			object.put("columnNames", columnNames);
			object.put("columnData", columnData);
			checkRepository.save(checkQuery);
		} catch (Exception e) {
			////System.out.println(e);
			throw e;
		}
		return object;
	}

}
