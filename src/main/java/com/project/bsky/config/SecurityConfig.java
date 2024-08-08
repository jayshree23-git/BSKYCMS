package com.project.bsky.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.project.bsky.filter.JwtFilter;
import com.project.bsky.serviceImpl.CustomUserDetailsService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtFilter jwtFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().antMatchers("/api/login/authenticate", "/api/login/changePassword",
				"/api/downLoadAction", "/api/downloadFile", "/api/downloadPfzFile", "/api/downLoadActionforcpd",
				"/api/doc", "/api/olddoc", "/api/saveCpdReapprovalClaimRequest", "/api/login/getUserMappingList",
				"/api/login/forgotPassword", "/api/login/otpValidate", "/api/downloadOldFile",
				"/api/downloadOldPfzFile", "/api/login/internalLogin", "/api/multipledoc",
				"/api/downLoadnotificatonDoc", "/api/downLoadintcommnDoc", "/api/downloadAllDocuments",
				"/api/UnsavedClaimFiles", "/getmodules", "/getallOfficersApi", "/getallApprovalAction",
				"/api/downLoadinternalgrivanceDoc", "/api/downloadDischargeRpts", "/getTinStatusAndNotingIdByServiceId",
				"/insertAppUpdateInHistoryTbl", "/getApplication", "/getGrievanceReport",
				"/api/downloadOldBlockedDataFile", "/viewManageFrom", "/getAuthAction", "/takeAction", "/getStateId",
				"/getSchemeApplyDetails", "/getFormName", "/tableColumnFetch", "/schemeApply", "/getnoting",
				"/getQueryDetails", "/queryReplyInsert", "/saveFileToTemp", "/downloadForm/{fileName}",
				"/downloadApprovalForm/{fileName}", "/getDistByUserName", "/api/getDistrictList", "/getUserDetails",
				"/api/downloadFileForDc", "/api/downloadFileForReferral", "/master/getDistrictDetailsByStateId",
				"/api/downloadFileForSNA", "/mobileApi/requestMobileUserLogin", "/api/login/otpDuringInternalLogin",
				"/api/login/GetSSOLoginInformation", "/api/login/mobileApiloginforweb", "/api/floatdocdownload",
				"/api/docenrollment", "/api/usermanuladoc", "/api/downlordmosarkargrivancedoc",
				"/api/downloadcpdspecdoc", "/api/downloadsmreviewdoc", "/api/downloadfileCpdRegistration",
				"/api/downloadfileCpdRegistrationPreview","/api/docFloat","/api/downLoadonlinepostDoc","/api/downLoaddeempanelDoc")

				.permitAll().anyRequest().authenticated().and().exceptionHandling().and().sessionManagement()
//				.maximumSessions(1).maxSessionsPreventsLogin(true);
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		http.cors();
	}

}
