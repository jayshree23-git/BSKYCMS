/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.HospitalCivilInfrastructure;
import com.project.bsky.bean.HospitalSpecialistBean;
import com.project.bsky.bean.HospitalSpecialistListBean;
import com.project.bsky.bean.HospitalSpecialityapproval;
import com.project.bsky.bean.QcApprovalHospitalSpecialitybean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.Specilistbean;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.service.HospitalSpecialityUpdationService;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class HospitalSpecialityUpdationServiceImpl implements HospitalSpecialityUpdationService {

	@Autowired
	private HospitalInformationRepository hospitalinforepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;
	
	@Autowired
	private Environment env;
	
	private Connection connection = null;
	
	private CallableStatement statement = null;

	@Override
	public List<HospitalBean> gethospitalinfo(String state, String dist) {
		List<HospitalBean> list = new ArrayList<HospitalBean>();
		try {
			List<Object[]> objlist = hospitalinforepo.gettmasactivehospitallist(state, dist);
			for (Object[] obj : objlist) {
				HospitalBean bean = new HospitalBean();
				BigDecimal b = (BigDecimal) obj[0];
				bean.setHospitalId(b.intValue());
				bean.setHospitalName((String) obj[1] + " (" + (String) obj[2] + ")");
				bean.setHospitalCode((String) obj[2]);
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<HospitalSpecialistListBean> getpackagelistbyhospitalid(Long hospitalId, Long userid) {
		List<HospitalSpecialistListBean> list = new ArrayList<HospitalSpecialistListBean>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_SPECIALIST_UPDATION")
					.registerStoredProcedureParameter("p_actioncode", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospital_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagemappings", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_status", Long.class, ParameterMode.OUT);

			storedProcedure.setParameter("p_actioncode", 1l);
			storedProcedure.setParameter("p_hospital_id", hospitalId);
			storedProcedure.setParameter("p_userid", userid);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				HospitalSpecialistListBean bean = new HospitalSpecialistListBean();
				bean.setPackageid(rs.getInt(1));
				bean.setPackagecode(rs.getString(2));
				bean.setPackagename(rs.getString(3));
				bean.setAddmissionprvyear(rs.getInt(4));
				bean.setAdmissionlastyear(rs.getInt(5));
				bean.setStatus(rs.getInt(6));
				bean.setShowstatus(rs.getInt(6) == 0 ? 1 : 0);
				bean.setHospitalTypeId(rs.getLong(7));
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return list;
	}

	@Override
	public Response savepecialistconfig(HospitalSpecialistBean hospitalSpecialistBean) {
		Response response = new Response();
		ResultSet rs = null;
		try {
				String spe="";
				int count=0;				
				for (Specilistbean spc : hospitalSpecialistBean.getSpecialist()) {
					count=hospitalinforepo.checkschemepackagetaggedornot(
							spc.getPackagecode(),Long.parseLong(hospitalSpecialistBean.getHospitalcode()));
					if(count>0) {
						spe=spc.getPackagecode();
						break;
						}
				}
				if(count==0){
					String str = "";
					List<Specilistbean> specialist = hospitalSpecialistBean.getSpecialist();
					for (Specilistbean spc : specialist) {
						str += spc.getPackageid() + "#" + spc.getPrivyear() + "#" + spc.getBfrlastyear() + "#" + spc.getStatus()
								+ "#" + spc.getHospitalTypeId() + ",";
					}
					StoredProcedureQuery storedProcedure = this.entityManager
							.createStoredProcedureQuery("USP_HOSPITAL_SPECIALIST_UPDATION")
							.registerStoredProcedureParameter("p_actioncode", Long.class, ParameterMode.IN)
							.registerStoredProcedureParameter("p_hospital_id", Long.class, ParameterMode.IN)
							.registerStoredProcedureParameter("p_packagemappings", String.class, ParameterMode.IN)
							.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
							.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
							.registerStoredProcedureParameter("p_status", Integer.class, ParameterMode.OUT);
		
					storedProcedure.setParameter("p_actioncode", 2l);
					storedProcedure.setParameter("p_hospital_id", Long.parseLong(hospitalSpecialistBean.getHospitalcode()));
					storedProcedure.setParameter("p_packagemappings", str);
					storedProcedure.setParameter("p_userid", hospitalSpecialistBean.getCreatedby());
					storedProcedure.execute();
					int output = (int) storedProcedure.getOutputParameterValue("p_status");
					if (output == 1) {
						response.setStatus("200");
						response.setMessage("Success");
					} else {
						response.setStatus("400");
						response.setMessage("failed");
					}
			}else {
				response.setStatus("401");
				response.setMessage("Deactivation Process For ' "+spe+" ' can't be Process."+" Pleaase check Scheme wise Hospital Speciality Mapping .");
			} 
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Error -: " + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return response;
	}

	@Override
	public Response savecivilinfraconfig(HospitalCivilInfrastructure civilInfrastructure) {
		Response response = new Response();
		ResultSet rs = null;
		Integer count = changenumber(civilInfrastructure.getTotalNoOfBedsFullyEquipedOperationTheatre())
				+ changenumber(civilInfrastructure.getTotallNoOfBedHDU())
				+ changenumber(civilInfrastructure.getTotallNoOfBedgeneralWard())
				+ changenumber(civilInfrastructure.getTotalNoOfBedICUwv())
				+ changenumber(civilInfrastructure.getTotalNoOfBedICUwov())
				+ changenumber(civilInfrastructure.getTotalNoOfBedCasualty())
				+ changenumber(civilInfrastructure.getTotalNoOfRoomLabourRoom());
		if (changenumber(civilInfrastructure.getTotalBedStrength()) < count) {
			response.setStatus("400");
			response.setMessage("Total Bed Strength was Less then Total Count Bed Strength");
			return response;
		}
		try {
			StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("USP_CIVIL_INFRA_SUBMIT_EMP")
					.registerStoredProcedureParameter("P_HOSPITAL_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STANDARDISED_ARCH_DESIGN", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FIRE_FIGHTING_SYS", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BIO_MEDI_WASTE_MGMT", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DUTY_STAFF_RM", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CATTLE_ENTR_AND_EXIT", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AREA", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BACK_UP_ELEC_SUPPLY", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_NO_OF_FLRS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_LIFT_PROV", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RAMP_PROV", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL_BED_STRENGTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_NO_OF_INPATIENT_BEDS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FULLY_EQU_OPRN_THTR", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL_NO_BEDS_FULLY_EQU_OPRN_THTR", Integer.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_OPD", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HDU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL_BED_HDU", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GENERAL_WARD", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ICU_Without_ventilator", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_total_bed_in_ICU_Without_ventilator", Integer.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_ICU_with_ventilator", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_total_bed_in_ICU_with_ventilator", Integer.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_total_bed_in_general_ward", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CASUALTY", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL_NO_OF_BED_CASUALTY", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_LABOUR_ROOM", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL_NO_OF_BED_IN_LBR_RM", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOOD_BANK", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CSSD", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIET_AND_KITCHEN_FACILITY", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_LIEN_AND_LAUNDARY", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STORES", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MEDICAL_RECORDS_DEPT", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AMBULANCE_SERV", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PATIENT_ATTENDANT_FACILITY", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIAG_CENT_RADIOLOGY_BASIC", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIAG_CENT_RADIOLOGY_ADV", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIAG_CENT_CLINICAL_LAB_AND_DIAG", Character.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_WALLS_SHLD_BE_COVR_WT_TILES_ANTI_BA", Integer.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_AVAILABILITY_OF_SPRT_OTS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AVAILABILITY_OF_PRE_OPERATIVE_WAITINGRM", Character.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_AVAILABILITY_OF_EQU_POST_OPERATIVE_WARD", Character.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_SEPRT_CHANGING_ROOMS", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DEDICATED_SCRUB_AREA", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_WTING_RM_FR_PATIENTS", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REGISTRATION_COUNTER", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCTOR_CONSULTANT_ROOMS", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DRESSING_ROOM", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INJECTION_ROOM", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PHARMACY_WINDOW", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PLASTER_ROOM", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPRT_STAND_FOR_STAFF_PUB_VEHICLES", Character.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_SANITARY_FITMENTS", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MULTI_SIGN_MONT_SYS_HDU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OXYGEN_SPLY_HDU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PIPED_SUCTION_MEDICAL_GASES_HDU", Character.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_INF_OF_IONOTROPIC_SPRT_HDU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_EQU_FR_MTNC_OF_BODY_TEMP_HDU", Character.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_WEIGHING_SCALE_HDU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MANPWR_FR_MONTR_HDU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_EMERG_CASH_CART_HDU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UNINTERRUPTED_ELEC_SPLY_HDU", Character.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_HEATING_HDU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AIR_COND_HDU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_NO_OF_BED_HDU", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MULTI_SIGN_MONT_SYS_ICU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OXYGEN_SPLY_ICU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PIPED_SUCT_MEDI_GASES_ICU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INFUSION_OF_IONOTROPIC_SUP_ICU", Character.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_EQU_FR_MTNC_OF_BODY_TEMP_ICU", Character.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_WEIGHING_SCALE_ICU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MANPWR_FR_MONT_ICU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_EMERG_CASH_CART_ICU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UNINTERRUPTED_ELEC_SPLY_ICU", Character.class,
							ParameterMode.IN)
					.registerStoredProcedureParameter("P_HEATING_ICU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AIR_COND_ICU", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_NUMBER_OF_BED_ICU", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CREATEDBY", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDBY", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.OUT);

			query.setParameter("P_HOSPITAL_ID", civilInfrastructure.getHospitalId());
			query.setParameter("P_STANDARDISED_ARCH_DESIGN", civilInfrastructure.getStandardisedArchDesign());
			query.setParameter("P_FIRE_FIGHTING_SYS", civilInfrastructure.getFireFightingSystem());
			query.setParameter("P_BIO_MEDI_WASTE_MGMT", civilInfrastructure.getBioMedicalWasteManagement());
			query.setParameter("P_DUTY_STAFF_RM", civilInfrastructure.getDutyStaffRoom());
			query.setParameter("P_CATTLE_ENTR_AND_EXIT", civilInfrastructure.getCattleTrapAtEntranceAndExit());
			query.setParameter("P_AREA", civilInfrastructure.getAreaBed());
			query.setParameter("P_BACK_UP_ELEC_SUPPLY", civilInfrastructure.getBackUpElectricitySupply());
			query.setParameter("P_NO_OF_FLRS", civilInfrastructure.getNoOfFloors());
			query.setParameter("P_LIFT_PROV", civilInfrastructure.getLiftProvision());
			query.setParameter("P_RAMP_PROV", civilInfrastructure.getRampProvision());
			query.setParameter("P_TOTAL_BED_STRENGTH", civilInfrastructure.getTotalBedStrength());
			query.setParameter("P_NO_OF_INPATIENT_BEDS", civilInfrastructure.getNoOfInPatientBeds());
			query.setParameter("P_FULLY_EQU_OPRN_THTR", civilInfrastructure.getFullyEquipiedOperationTheatre());
			query.setParameter("P_TOTAL_NO_BEDS_FULLY_EQU_OPRN_THTR",
					civilInfrastructure.getTotalNoOfBedsFullyEquipedOperationTheatre());
			query.setParameter("P_OPD", civilInfrastructure.getOpd());
			query.setParameter("P_HDU", civilInfrastructure.getHdu());
			query.setParameter("P_TOTAL_BED_HDU", civilInfrastructure.getTotallNoOfBedHDU());
			query.setParameter("P_GENERAL_WARD", civilInfrastructure.getGeneralWard());
			query.setParameter("P_ICU_Without_ventilator", civilInfrastructure.getIcuwov());
			query.setParameter("P_total_bed_in_ICU_Without_ventilator", civilInfrastructure.getTotalNoOfBedICUwov());
			query.setParameter("P_ICU_with_ventilator", civilInfrastructure.getIcuwv());
			query.setParameter("P_total_bed_in_ICU_with_ventilator", civilInfrastructure.getTotalNoOfBedICUwv());
			query.setParameter("P_total_bed_in_general_ward", civilInfrastructure.getTotallNoOfBedgeneralWard());
			query.setParameter("P_CASUALTY", civilInfrastructure.getCasualty());
			query.setParameter("P_TOTAL_NO_OF_BED_CASUALTY", civilInfrastructure.getTotalNoOfBedCasualty());
			query.setParameter("P_LABOUR_ROOM", civilInfrastructure.getLabourRoom());
			query.setParameter("P_TOTAL_NO_OF_BED_IN_LBR_RM", civilInfrastructure.getTotalNoOfRoomLabourRoom());
			query.setParameter("P_BLOOD_BANK", civilInfrastructure.getBloodBank());
			query.setParameter("P_CSSD", civilInfrastructure.getCssd());
			query.setParameter("P_DIET_AND_KITCHEN_FACILITY", civilInfrastructure.getDietAndKitchenFacility());
			query.setParameter("P_LIEN_AND_LAUNDARY", civilInfrastructure.getLinenAndLaundry());
			query.setParameter("P_STORES", civilInfrastructure.getStores());
			query.setParameter("P_MEDICAL_RECORDS_DEPT", civilInfrastructure.getMedicalRecordsDepartment());
			query.setParameter("P_AMBULANCE_SERV", civilInfrastructure.getAmbulanceService());
			query.setParameter("P_PATIENT_ATTENDANT_FACILITY", civilInfrastructure.getPatientAttendantFacility());
			query.setParameter("P_DIAG_CENT_RADIOLOGY_BASIC", civilInfrastructure.getDiagnosticCentreRadiologyBasic());
			query.setParameter("P_DIAG_CENT_RADIOLOGY_ADV", civilInfrastructure.getDiagnosticCentreRadiologyAdvanced());
			query.setParameter("P_DIAG_CENT_CLINICAL_LAB_AND_DIAG",
					civilInfrastructure.getDiagnosticCentreClinicalLabAndDiagnostics());
			query.setParameter("P_WALLS_SHLD_BE_COVR_WT_TILES_ANTI_BA", civilInfrastructure.getWallsShouldBeCovered());
			query.setParameter("P_AVAILABILITY_OF_SPRT_OTS", civilInfrastructure.getAvailabilityOfSeparateOTs());
			query.setParameter("P_AVAILABILITY_OF_PRE_OPERATIVE_WAITINGRM",
					civilInfrastructure.getAvailabilityOfPreOperative());
			query.setParameter("P_AVAILABILITY_OF_EQU_POST_OPERATIVE_WARD",
					civilInfrastructure.getAvailabilityOfPostOperative());
			query.setParameter("P_SEPRT_CHANGING_ROOMS", civilInfrastructure.getSeparateChangingRooms());
			query.setParameter("P_DEDICATED_SCRUB_AREA", civilInfrastructure.getDedicatedScrubArea());
			query.setParameter("P_WTING_RM_FR_PATIENTS", civilInfrastructure.getWaitingRoomForPatientsAndRelatives());
			query.setParameter("P_REGISTRATION_COUNTER", civilInfrastructure.getRegistrationCounter());
			query.setParameter("P_DOCTOR_CONSULTANT_ROOMS", civilInfrastructure.getDoctorConsultantRooms());
			query.setParameter("P_DRESSING_ROOM", civilInfrastructure.getDressingRoom());
			query.setParameter("P_INJECTION_ROOM", civilInfrastructure.getInjectionRoom());
			query.setParameter("P_PHARMACY_WINDOW", civilInfrastructure.getPharmacyWindow());
			query.setParameter("P_PLASTER_ROOM", civilInfrastructure.getPlasterRoom());
			query.setParameter("P_SPRT_STAND_FOR_STAFF_PUB_VEHICLES",
					civilInfrastructure.getSeparateStandForStaffPublicVehicles());
			query.setParameter("P_SANITARY_FITMENTS", civilInfrastructure.getSanitaryFitments());
			query.setParameter("P_MULTI_SIGN_MONT_SYS_HDU", civilInfrastructure.getHduMultiSignMonitoringSystem());
			query.setParameter("P_OXYGEN_SPLY_HDU", civilInfrastructure.getHduOxygenSupply());
			query.setParameter("P_PIPED_SUCTION_MEDICAL_GASES_HDU",
					civilInfrastructure.getHduPipedSuctionAndMedicalGases());
			query.setParameter("P_INF_OF_IONOTROPIC_SPRT_HDU", civilInfrastructure.getHduInfusionOfIonotropicSupport());
			query.setParameter("P_EQU_FR_MTNC_OF_BODY_TEMP_HDU",
					civilInfrastructure.getHduEquipmentForMaintenanceOfBodyTemperature());
			query.setParameter("P_WEIGHING_SCALE_HDU", civilInfrastructure.getHduWeighingScale());
			query.setParameter("P_MANPWR_FR_MONTR_HDU", civilInfrastructure.getHduManpowerForMonitoring());
			query.setParameter("P_EMERG_CASH_CART_HDU", civilInfrastructure.getHduEmergencyCashCart());
			query.setParameter("P_UNINTERRUPTED_ELEC_SPLY_HDU",
					civilInfrastructure.getHduUninterruptedElectricSupply());
			query.setParameter("P_HEATING_HDU", civilInfrastructure.getHduHeating());
			query.setParameter("P_AIR_COND_HDU", civilInfrastructure.getHduAirConditioning());
			query.setParameter("P_NO_OF_BED_HDU", civilInfrastructure.getHduTotalNoOfBed());
			query.setParameter("P_MULTI_SIGN_MONT_SYS_ICU", civilInfrastructure.getIcuMultiSignMonitoringSystem());
			query.setParameter("P_OXYGEN_SPLY_ICU", civilInfrastructure.getIcuOxygenSupply());
			query.setParameter("P_PIPED_SUCT_MEDI_GASES_ICU", civilInfrastructure.getIcuPipedSuctionAndMedicalGases());
			query.setParameter("P_INFUSION_OF_IONOTROPIC_SUP_ICU",
					civilInfrastructure.getIcuInfusionOfIonotropicSupport());
			query.setParameter("P_EQU_FR_MTNC_OF_BODY_TEMP_ICU",
					civilInfrastructure.getIcuEquipmentForMaintenanceOfBodyTemperature());
			query.setParameter("P_WEIGHING_SCALE_ICU", civilInfrastructure.getIcuWeighingScale());
			query.setParameter("P_MANPWR_FR_MONT_ICU", civilInfrastructure.getIcuManpowerForMonitoring());
			query.setParameter("P_EMERG_CASH_CART_ICU", civilInfrastructure.getIcuEmergencyCashCart());
			query.setParameter("P_UNINTERRUPTED_ELEC_SPLY_ICU",
					civilInfrastructure.getIcuUninterruptedElectricSupply());
			query.setParameter("P_HEATING_ICU", civilInfrastructure.getIcuHeating());
			query.setParameter("P_AIR_COND_ICU", civilInfrastructure.getIcuAirConditioning());
			query.setParameter("P_NUMBER_OF_BED_ICU", civilInfrastructure.getIcuTotalNoOfBed());
			query.setParameter("P_CREATEDBY", civilInfrastructure.getCreatedByOrUpdatedBy());
			query.setParameter("P_UPDATEDBY", civilInfrastructure.getCreatedByOrUpdatedBy());
			query.setParameter("P_ACTION_CODE", (civilInfrastructure.getSaveOrUpdate().equals("SAVE") ? 1 : 2));
			query.execute();
			int output = (int) query.getOutputParameterValue("P_MSG_OUT");
			if (output == 1) {
				response.setStatus("200");
				response.setMessage("Success");
			} else {
				response.setStatus("400");
				response.setMessage("failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Some Thing Went Wrong");
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return response;
	}

	@Override
	public Map<String, Object> getEmpanelmentDetails(Integer hospitalId) {
		ResultSet rs = null;
		Map<String, Object> map = new HashMap<>();
		try {
			StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("USP_GET_EMPANELMENT_DETAILS")
					.registerStoredProcedureParameter("p_hospital_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_basic_info", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_finance_dtls", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_civil_infra", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_medical_infra", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_manpower", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_attachment", void.class, ParameterMode.REF_CURSOR);

			query.setParameter("p_hospital_id", hospitalId);
			query.execute();
			rs = (ResultSet) query.getOutputParameterValue("p_civil_infra");
			List<Map<String, Object>> rows = new ArrayList<>();
			if (rs != null) {
				try {
					ResultSetMetaData md = rs.getMetaData();
					int noOfColumns = md.getColumnCount();
					while (rs.next()) {
						Map<String, Object> row = new HashMap<>();
						for (int i = 1; i <= noOfColumns; i++) {
							row.put(md.getColumnName(i), rs.getObject(i));
						}
						rows.add(row);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			map.put("civilInfra", rows);
			map.put("length", rows.size());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return map;
	}

	public Integer changenumber(Integer val) {
		Integer var = 0;
		if (val != null) {
			var = val;
		}
		return var;
	}

	@Override
	public List<HospitalBean> gethospitalDetails(String hospitalCode) {
		List<HospitalBean> list = new ArrayList<HospitalBean>();
		try {
			List<Object[]> objlist = hospitalinforepo.getStateDistrictByHospitalCode(hospitalCode);
			for (Object[] obj : objlist) {
				HospitalBean bean = new HospitalBean();
				BigDecimal b = (BigDecimal) obj[0];
				bean.setHospitalId(b.intValue());
				bean.setHospitalName((String) obj[1]);
				bean.setStateCode((String) obj[2]);
				bean.setDistrictCode((String) obj[3]);
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public String getHospitalEmpList(String state, String dist, String hospitalId, Long userid) {
		JSONArray jsonArray = new JSONArray();
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_EMP_HOSPITAL_EMPANELLED_LIST")
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_STATE_CODE", state);
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", dist);
			storedProcedureQuery.setParameter("P_USERID", userid);
			storedProcedureQuery.setParameter("P_HOSPITAL_ID", hospitalId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (snoDetailsObj.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("hospitalName", snoDetailsObj.getString(1));
				jsonObject.put("stateName", snoDetailsObj.getString(2));
				jsonObject.put("districtName", snoDetailsObj.getString(3));
				jsonObject.put("totalBedStrength", snoDetailsObj.getLong(4));
				jsonObject.put("noOfInpatientBed", snoDetailsObj.getLong(5));
				jsonObject.put("fully_equ_oprn_thtr", snoDetailsObj.getString(6));
				jsonObject.put("total_no_beds_fully_equ_oprn_thtr", snoDetailsObj.getLong(7));
				jsonObject.put("opd", snoDetailsObj.getString(8));
				jsonObject.put("hdu", snoDetailsObj.getString(9));
				jsonObject.put("total_bed_hdu", snoDetailsObj.getLong(10));
				jsonObject.put("general_ward", snoDetailsObj.getString(11));
				jsonObject.put("casualty", snoDetailsObj.getString(12));
				jsonObject.put("total_no_of_bed_casualty", snoDetailsObj.getLong(13));
				jsonObject.put("labour_room", snoDetailsObj.getString(14));
				jsonObject.put("total_no_of_bed_in_lbr_rm", snoDetailsObj.getLong(15));
				jsonObject.put("blood_bank", snoDetailsObj.getString(16));
				jsonObject.put("ambulance_serv", snoDetailsObj.getString(17));
				jsonObject.put("patient_attendant_facility", snoDetailsObj.getString(18));
				jsonObject.put("diag_cent_radiology_basic", snoDetailsObj.getString(19));
				jsonObject.put("doctor_consultant_rooms", snoDetailsObj.getString(20));
				jsonObject.put("icu_without_ventilator", snoDetailsObj.getString(21));
				jsonObject.put("total_bed_in_icu_without_ventilator", snoDetailsObj.getLong(22));
				jsonObject.put("icu_with_ventilator", snoDetailsObj.getString(23));
				jsonObject.put("total_bed_in_icu_with_ventilator", snoDetailsObj.getLong(24));
				jsonObject.put("total_bed_in_general_ward", snoDetailsObj.getLong(25));
				jsonObject.put("hospitalId", snoDetailsObj.getLong(26));
				jsonObject.put("civilInfraId", snoDetailsObj.getLong(27));
				jsonArray.put(jsonObject);
			}
			details.put("infraList", jsonArray);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getCivilInfraDetailsById(Integer civilInfraId) throws Exception {
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_EMP_HOSPITAL_EMPANELLED_DETAILS")
					.registerStoredProcedureParameter("P_CIVILINFRA_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_CIVILINFRA_ID", civilInfraId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			if (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("civilinfra_id", snoDetailsObj.getLong(1));
				jsonObject.put("hospital_id", snoDetailsObj.getLong(2));
				jsonObject.put("standardised_arch_design", snoDetailsObj.getString(3));
				jsonObject.put("fire_fighting_sys", snoDetailsObj.getString(4));
				jsonObject.put("bio_medi_waste_mgmt", snoDetailsObj.getString(5));
				jsonObject.put("duty_staff_rm", snoDetailsObj.getString(6));
				jsonObject.put("cattle_entr_and_exit", snoDetailsObj.getString(7));
				jsonObject.put("area", snoDetailsObj.getString(8));
				jsonObject.put("back_up_elec_supply", snoDetailsObj.getString(9));
				jsonObject.put("no_of_flrs", snoDetailsObj.getLong(10));
				jsonObject.put("lift_prov", snoDetailsObj.getString(11));
				jsonObject.put("ramp_prov", snoDetailsObj.getString(12));
				jsonObject.put("total_bed_strength", snoDetailsObj.getLong(13));
				jsonObject.put("no_of_inpatient_beds", snoDetailsObj.getLong(14));
				jsonObject.put("fully_equ_oprn_thtr", snoDetailsObj.getString(15));
				jsonObject.put("total_no_beds_fully_equ_oprn_thtr", snoDetailsObj.getLong(16));
				jsonObject.put("opd", snoDetailsObj.getString(17));
				jsonObject.put("hdu", snoDetailsObj.getString(18));
				jsonObject.put("total_bed_hdu", snoDetailsObj.getLong(19));
				jsonObject.put("general_ward", snoDetailsObj.getString(20));
				jsonObject.put("icu", snoDetailsObj.getString(21));
				jsonObject.put("total_bed_in_icu", snoDetailsObj.getLong(22));
				jsonObject.put("casualty", snoDetailsObj.getString(23));
				jsonObject.put("total_no_of_bed_casualty", snoDetailsObj.getLong(24));
				jsonObject.put("labour_room", snoDetailsObj.getString(25));
				jsonObject.put("total_no_of_bed_in_lbr_rm", snoDetailsObj.getLong(26));
				jsonObject.put("blood_bank", snoDetailsObj.getString(27));
				jsonObject.put("cssd", snoDetailsObj.getString(28));
				jsonObject.put("diet_and_kitchen_facility", snoDetailsObj.getString(29));
				jsonObject.put("lien_and_laundary", snoDetailsObj.getString(30));
				jsonObject.put("stores", snoDetailsObj.getString(31));
				jsonObject.put("medical_records_dept", snoDetailsObj.getString(32));
				jsonObject.put("ambulance_serv", snoDetailsObj.getString(33));
				jsonObject.put("patient_attendant_facility", snoDetailsObj.getString(34));
				jsonObject.put("diag_cent_radiology_basic", snoDetailsObj.getString(35));
				jsonObject.put("diag_cent_radiology_adv", snoDetailsObj.getString(36));
				jsonObject.put("diag_cent_clinical_lab_and_diag", snoDetailsObj.getString(37));
				jsonObject.put("walls_shld_be_covr_wt_tiles_anti_ba", snoDetailsObj.getString(38));
				jsonObject.put("availability_of_sprt_ots", snoDetailsObj.getString(39));
				jsonObject.put("availability_of_pre_operative_waitingrm", snoDetailsObj.getString(40));
				jsonObject.put("availability_of_equ_post_operative_ward", snoDetailsObj.getString(41));
				jsonObject.put("seprt_changing_rooms", snoDetailsObj.getString(42));
				jsonObject.put("dedicated_scrub_area", snoDetailsObj.getString(43));
				jsonObject.put("wting_rm_fr_patients", snoDetailsObj.getString(44));
				jsonObject.put("registration_counter", snoDetailsObj.getString(45));
				jsonObject.put("doctor_consultant_rooms", snoDetailsObj.getString(46));
				jsonObject.put("dressing_room", snoDetailsObj.getString(47));
				jsonObject.put("injection_room", snoDetailsObj.getString(48));
				jsonObject.put("pharmacy_window", snoDetailsObj.getString(49));
				jsonObject.put("plaster_room", snoDetailsObj.getString(50));
				jsonObject.put("sprt_stand_for_staff_pub_vehicles", snoDetailsObj.getString(51));
				jsonObject.put("sanitary_fitments", snoDetailsObj.getString(52));
				jsonObject.put("multi_sign_mont_sys_hdu", snoDetailsObj.getString(53));
				jsonObject.put("oxygen_sply_hdu", snoDetailsObj.getString(54));
				jsonObject.put("piped_suction_medical_gases_hdu", snoDetailsObj.getString(55));
				jsonObject.put("inf_of_ionotropic_sprt_hdu", snoDetailsObj.getString(56));
				jsonObject.put("equ_fr_mtnc_of_body_temp_hdu", snoDetailsObj.getString(57));
				jsonObject.put("weighing_scale_hdu", snoDetailsObj.getString(58));
				jsonObject.put("manpwr_fr_montr_hdu", snoDetailsObj.getString(59));
				jsonObject.put("emerg_cash_cart_hdu", snoDetailsObj.getString(60));
				jsonObject.put("uninterrupted_elec_sply_hdu", snoDetailsObj.getString(61));
				jsonObject.put("heating_hdu", snoDetailsObj.getString(62));
				jsonObject.put("air_cond_hdu", snoDetailsObj.getString(63));
				jsonObject.put("no_of_bed_hdu", snoDetailsObj.getString(64));
				jsonObject.put("multi_sign_mont_sys_icu", snoDetailsObj.getString(65));
				jsonObject.put("oxygen_sply_icu", snoDetailsObj.getString(66));
				jsonObject.put("piped_suct_medi_gases_icu", snoDetailsObj.getString(67));
				jsonObject.put("infusion_of_ionotropic_sup_icu", snoDetailsObj.getString(68));
				jsonObject.put("equ_fr_mtnc_of_body_temp_icu", snoDetailsObj.getString(69));
				jsonObject.put("weighing_scale_icu", snoDetailsObj.getString(70));
				jsonObject.put("manpwr_fr_mont_icu", snoDetailsObj.getString(71));
				jsonObject.put("emerg_cash_cart_icu", snoDetailsObj.getString(72));
				jsonObject.put("uninterrupted_elec_sply_icu", snoDetailsObj.getString(73));
				jsonObject.put("heating_icu", snoDetailsObj.getString(74));
				jsonObject.put("air_cond_icu", snoDetailsObj.getString(75));
				jsonObject.put("number_of_bed_icu", snoDetailsObj.getLong(76));
				jsonObject.put("icu_without_ventilator", snoDetailsObj.getString(77));
				jsonObject.put("total_bed_in_icu_without_ventilator", snoDetailsObj.getLong(78));
				jsonObject.put("icu_with_ventilator", snoDetailsObj.getString(79));
				jsonObject.put("total_bed_in_icu_with_ventilator", snoDetailsObj.getLong(80));
				jsonObject.put("total_bed_in_general_ward", snoDetailsObj.getLong(81));
				details.put("actionData", jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				throw e2;
			}
		}
		return details.toString();
	}

	@Override
	public String getSpecialityPackages(String packageCode, String hospitalCode) throws Exception {
		JSONArray jsonArray2 = new JSONArray();
		JSONArray jsonArray3 = new JSONArray();
		JSONObject jsonObject3;
		JSONObject jsonObject;
		ResultSet resObj1 = null;
		ResultSet resObj2 = null;
		JSONObject details = new JSONObject();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_speciality_packagedetails")
					.registerStoredProcedureParameter("p_packageheadercode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospital_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_OUT1", void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.setParameter("p_packageheadercode", packageCode);
			storedProcedureQuery.setParameter("p_hospital_code", hospitalCode);
			storedProcedureQuery.execute();
			resObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");
			resObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT1");

			while (resObj1.next()) {
				jsonObject3 = new JSONObject();
				jsonObject3.put("packageHeaderId", resObj1.getLong(1));
				jsonObject3.put("packageHeaderCode", resObj1.getString(2));
				jsonObject3.put("packageHeaderName", resObj1.getString(3));
				jsonObject3.put("packageSubCatagoryId", resObj1.getLong(4));
				jsonObject3.put("packageSubCode", resObj1.getString(5));
				jsonObject3.put("subPackageName", resObj1.getString(6));
				jsonObject3.put("procedureId", resObj1.getLong(7));
				jsonObject3.put("procedureCode", resObj1.getString(8));
				jsonObject3.put("proceduredescription", resObj1.getString(9));
				jsonObject3.put("packageAmount", resObj1.getLong(10));
				jsonObject3.put("packageCatagoryType", resObj1.getString(11));
				jsonObject3.put("preauthRequired", resObj1.getString(12));
				jsonObject3.put("preauthDocs", resObj1.getString(13));
				jsonObject3.put("claimProcessDocs", resObj1.getString(14));
				jsonObject3.put("isSurgical", resObj1.getString(15));
				jsonObject3.put("status", resObj1.getLong(16));
				jsonArray2.put(jsonObject3);
			}
			while (resObj2.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("procedureId", resObj2.getLong(1));
				jsonObject.put("procedureCode", resObj2.getString(2));
				jsonObject.put("headerId", resObj2.getLong(3));
				jsonObject.put("headerCode", resObj2.getString(4));
				jsonArray3.put(jsonObject);
			}
			details.put("packages", jsonArray2);
			details.put("procedureList", jsonArray3);
		} catch (Exception e) {
			logger.error("Error in getSpecialityPackages method of HospitalSpecialityUpdationServiceImpl class:", e);
			throw e;
		} finally {
			try {
				if (resObj1 != null) {
					resObj1.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getSpecialityPackages method of HospitalSpecialityUpdationServiceImpl class:",
						e2);
			}

		}
		return details.toString();
	}

	@Override
	public Response updatePackageSpecility(HospitalSpecialistBean hospitalSpecialistBean) {
		Response response = new Response();
		ResultSet rs = null;
		try {
			String str = "";
			List<Specilistbean> specialityList = hospitalSpecialistBean.getSpecialist();
			for (Specilistbean speciality : specialityList) {
				str += speciality.getPackageid() + "#" + speciality.getPackagecode() + "#"
						+ speciality.getPackageSubCatagoryId() + "#" + speciality.getPackageSubCode() + "#"
						+ speciality.getProcedureId() + "#" + speciality.getProcedureCode() + "#"
						+ speciality.getStatus() + ",";
			}
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_PACKAGE_TAGGING")
					.registerStoredProcedureParameter("p_hospital_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagemappings", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.OUT);

			storedProcedure.setParameter("p_hospital_id", hospitalSpecialistBean.getHospitalId());
			storedProcedure.setParameter("p_hospitalcode", hospitalSpecialistBean.getHospitalcode());
			storedProcedure.setParameter("p_packagemappings", str);
			storedProcedure.setParameter("P_USERID", hospitalSpecialistBean.getCreatedby());
			storedProcedure.execute();
			int output = (int) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			if (output == 1) {
				response.setStatus("200");
				response.setMessage("Success");
			} else {
				response.setStatus("400");
				response.setMessage("failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Error -: " + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return response;
	}

	@Override
	public List<Object> getSerachDataHospitalSpecialityApprovalList(String statecodeval, String districtcodeval,
			String userId) {
		List<Object> approvalist = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_SPECIALIST_VIEW_BYQCADMIN")
					.registerStoredProcedureParameter("p_actioncode", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_distcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospital_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_actioncode", 1);
			storedProcedureQuery.setParameter("p_userid", Long.parseLong(userId));
			storedProcedureQuery.setParameter("p_statecode", statecodeval.trim());
			storedProcedureQuery.setParameter("p_distcode", districtcodeval.trim());
			storedProcedureQuery.setParameter("p_hospital_id", null);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				HospitalSpecialityapproval hospitalspeciality = new HospitalSpecialityapproval();
				hospitalspeciality.setStatename(rs.getString(1));
				hospitalspeciality.setDistrictname(rs.getString(2));
				hospitalspeciality.setHospitalid(rs.getString(3));
				hospitalspeciality.setHospitalName(rs.getString(4));
				hospitalspeciality.setHospitalCode(rs.getString(5));
				hospitalspeciality.setAppliedon(rs.getString(6));
				approvalist.add(hospitalspeciality);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return approvalist;
	}

	@Override
	public List<Object> getsearchdataofhospitallspecdetailslist(String hospitalid) {
		List<Object> existinglist = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_SPECIALIST_VIEW_BYQCADMIN")
					.registerStoredProcedureParameter("p_actioncode", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_distcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospital_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_actioncode", 2);
			storedProcedureQuery.setParameter("p_userid", null);
			storedProcedureQuery.setParameter("p_statecode", null);
			storedProcedureQuery.setParameter("p_distcode", null);
			storedProcedureQuery.setParameter("p_hospital_id", Long.parseLong(hospitalid));
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				HospitalSpecialityapproval exsitinglist = new HospitalSpecialityapproval();
				exsitinglist.setSpecialityname(rs.getString(1));
				exsitinglist.setSpecilaitycode(rs.getString(2));
				exsitinglist.setHospitalTypeName(rs.getString(3));
				existinglist.add(exsitinglist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
			}
		}
		return existinglist;
	}

	@Override
	public List<Object> getsearchdataofhospitallspecdetailstpendingcase(String hospitalid) {
		List<Object> pendinglist = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_SPECIALIST_VIEW_BYQCADMIN")
					.registerStoredProcedureParameter("p_actioncode", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_distcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospital_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_actioncode", 3);
			storedProcedureQuery.setParameter("p_userid", null);
			storedProcedureQuery.setParameter("p_statecode", null);
			storedProcedureQuery.setParameter("p_distcode", null);
			storedProcedureQuery.setParameter("p_hospital_id", Long.parseLong(hospitalid));
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				HospitalSpecialityapproval pendinlist = new HospitalSpecialityapproval();
				pendinlist.setSpecialityid(rs.getString(1));
				pendinlist.setHospitalid(rs.getString(2));
				pendinlist.setPackageid(rs.getString(3));
				pendinlist.setSpecialityname(rs.getString(4));
				pendinlist.setSpecilaitycode(rs.getString(5));
				pendinglist.add(pendinlist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return pendinglist;
	}

	@Override
	public Response SavegQcapprovalofhospitalspeciality(
			QcApprovalHospitalSpecialitybean qcApprovalHospitalSpecialitybean) throws Exception {
		Response response = new Response();
		StringBuffer specilaityidlist = new StringBuffer();
		StringBuffer typeId = new StringBuffer();
		ResultSet rs = null;
		int count = 0;
		int size = qcApprovalHospitalSpecialitybean.getSpecialistid().size();
		try {
			for (String element : qcApprovalHospitalSpecialitybean.getSpecialistid()) {
				String[] strArr = element.split("~");
				if (count == (size - 1) || size == 1) {
					specilaityidlist.append(strArr[0]);
					typeId.append(strArr.length > 1 ? strArr[1] : "");
				} else {
					specilaityidlist.append(strArr[0] + ",");
					typeId.append(strArr.length > 1 ? strArr[1] + "," : "");
				}
				count++;
			}
		} catch (Exception e) {
			throw new Exception("Invalid");
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_SPECIALIST_APV_BYQCADMIN")
					.registerStoredProcedureParameter("p_actioncode", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_SPECIALITY_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_SPECIALITY_TYPE_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_actioncode", qcApprovalHospitalSpecialitybean.getActiontype());
			storedProcedureQuery.setParameter("p_userid", qcApprovalHospitalSpecialitybean.getUserid());
			storedProcedureQuery.setParameter("p_SPECIALITY_ID", specilaityidlist.toString().trim());
			storedProcedureQuery.setParameter("p_SPECIALITY_TYPE_ID", typeId.toString().trim());
			storedProcedureQuery.execute();
			int output = (int) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			if (output == 1) {
				response.setStatus("200");
				response.setMessage("Success");
			} else if (output == 2) {
				response.setStatus("400");
				response.setMessage("failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Error -: " + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return response;
	}

	@Override
	public List<Object> specialityapprovelist(String statecode, String distcode, String hospitalcode, Integer type,
			Long userid) {
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SPECIALITY_APPROVAL_STATUS_RPT")
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_distcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action_code", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_userid", userid);
			storedProcedureQuery.setParameter("p_statecode", statecode);
			storedProcedureQuery.setParameter("p_distcode", distcode);
			storedProcedureQuery.setParameter("p_hospitalcode", hospitalcode);
			storedProcedureQuery.setParameter("p_action_code", type);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				HospitalSpecialistListBean bean = new HospitalSpecialistListBean();
				bean.setStateName(rs.getString(1));
				bean.setDistName(rs.getString(2));
				bean.setHospitalcode(rs.getString(3));
				bean.setHospitalname(rs.getString(4) + " (" + rs.getString(3) + ")");
				bean.setPackagecode(rs.getString(5));
				bean.setPackagename(rs.getString(6) + " (" + rs.getString(5) + ")");
				bean.setActionby(rs.getString(7));
				bean.setActionon(rs.getString(8));
				bean.setActiontype(rs.getInt(9) == 0 ? "Approved" : "Rejected");
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return list;
	}

	@Override
	public Map<String,Object> getschemepackagelistbyhospitalid(String hospitalCode, Integer schemeid) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<HospitalSpecialistListBean> list = new ArrayList<HospitalSpecialistListBean>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_SCHEMECATEGORY_SPECIALITY_MAPPING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORYID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTIONCODE", 1);
			storedProcedure.setParameter("P_SCHEMECATEGORYID", schemeid);
			storedProcedure.setParameter("P_HOSPITAL_CODE", hospitalCode);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			Integer checkall=0;
			while (rs.next()) {
				HospitalSpecialistListBean bean = new HospitalSpecialistListBean();
				bean.setPackageid(rs.getInt(2));
				bean.setPackagecode(rs.getString(3));
				bean.setPackagename(rs.getString(4));
				bean.setStatus(rs.getInt(5));
				bean.setShowstatus(rs.getInt(5));
				if(rs.getInt(5)==1) {
					checkall=1;
				}	
				bean.setPreauth(rs.getString(6));
				list.add(bean);
			}
			map.put("checkall", checkall);
			map.put("data", list);
			map.put("tagged", list.stream().filter(x->x.getStatus()==0).count());
			map.put("untag", list.stream().filter(x->x.getStatus()==1).count());
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> updateschemepackage(HospitalSpecialistBean hospitalSpecialistBean) throws Exception {
		Map<String, Object> response=new HashMap<>();
		try {
			StructDescriptor structDescriptor;
	        
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pass);	
			
			STRUCT[] quesbean = new STRUCT[hospitalSpecialistBean.getSpecialist().size()];
			int i=0;
			for(Specilistbean bean:hospitalSpecialistBean.getSpecialist()){
				structDescriptor = new StructDescriptor("SCHEME_SPECIALITY_MAPPING", connection);
				 Object[] ObjArr = {
						 bean.getPackagecode(),
						 bean.getStatus(),
						 bean.getFlag()
		            };
				 quesbean[i] = new STRUCT(structDescriptor, connection, ObjArr);
		            i++;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYPE_SPECIALITY_MAPPING_LIST", connection);
			ARRAY array_to_pass = new ARRAY(des, connection, quesbean);
			
			statement = connection.prepareCall("call USP_SCHEMECATEGORY_SPECIALITY_MAPPING_SUBMIT(?,?,?,?,?)");
			statement.setArray(1, array_to_pass);//P_PACKAGEHEADERCODELIST
			statement.setString(2, hospitalSpecialistBean.getHospitalcode());//P_HOSPITALCODE
			statement.setLong(3, hospitalSpecialistBean.getSchemeid());//P_SCHEMECATEGORYID
			statement.setLong(4, hospitalSpecialistBean.getCreatedby());//P_CREATEDBY
			statement.registerOutParameter(5, Types.INTEGER);//P_OUT
			statement.execute();
			Integer out = statement.getInt(5);
			if(out==1) {
				response.put("status",200);
				response.put("message","Record Updated Successfully");
			}else {
				response.put("status",400);
				response.put("message","Something Went Wrong");
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(connection!=null) {
				connection.close();
			}
		}
		return response;
	}

	@Override
	public Map<String, Object> getschemehospitalmappingrpt(String state, String dist, String hospitalCode,
			Integer schemeid) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_SCHEMECATEGORY_SPECIALITY_MAPPING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORYID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTIONCODE", 2);
			storedProcedure.setParameter("P_SCHEMECATEGORYID", schemeid);
			storedProcedure.setParameter("P_HOSPITAL_CODE", hospitalCode);
			storedProcedure.setParameter("P_STATE_CODE", state);
			storedProcedure.setParameter("P_DISTRICT_CODE", dist);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Map<String, Object> map1 = new HashMap<>();
				map1.put("stateName", rs.getString(1));
				map1.put("distName", rs.getString(2));
				map1.put("hospitalname", rs.getString(3));
				map1.put("packagename", rs.getString(4));
				map1.put("scheme", rs.getString(5));
				map1.put("preauth", rs.getString(6));
				list.add(map1);
			}
			map.put("data", list);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return map;
	}
}
