package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.HighEndDrugsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.WardBean;
import com.project.bsky.model.HighEndDrugs;
import com.project.bsky.model.Implant;
import com.project.bsky.repository.HighEndDrugsRepository;
import com.project.bsky.repository.ImplantRepository;
import com.project.bsky.service.HighEndDrugsService;

@Service
public class HighEndDrugsServiceImpl implements HighEndDrugsService {

    @Autowired
    private ImplantRepository implantRepository;

    @Autowired
    private HighEndDrugsRepository highEndDrugsRepository;
    
    @Autowired
    private Logger logger;

    @PersistenceContext
    private EntityManager entityManager;

    Calendar cal = Calendar.getInstance();
    Date date = cal.getTime();
    @Override
    public List<Implant> getImplantName() {
        List<Implant> headerResponse = new ArrayList<>();
        List<Implant> findAll = implantRepository.findAll();
        if (findAll != null) {
            for (Implant implant : findAll) {
                if (implant != null && implant.getDeletedFlag() == 0) {
                    headerResponse.add(implant);
                }
            }
        }
        return headerResponse;
    }

    @Override
    public Response saveHighEndDrugs(HighEndDrugsBean highEndDrugsBean) {
        Response response = new Response();
        try{
            HighEndDrugs highEndDrugs=new HighEndDrugs();
            Integer count=highEndDrugsRepository.countRowForCheckDuplicateDrugCode(highEndDrugsBean.getHedCode());
            if(count==0) {
                highEndDrugs.setHedCode(highEndDrugsBean.getHedCode());
                highEndDrugs.setHedName(highEndDrugsBean.getHedName());
                highEndDrugs.setUnit(highEndDrugsBean.getUnit());
                highEndDrugs.setPrice(highEndDrugsBean.getPrice());
                highEndDrugs.setMaximumUnit(highEndDrugsBean.getMaximumUnit());
                highEndDrugs.setRecomendedDose(highEndDrugsBean.getRecomendedDose());
                highEndDrugs.setIsPreAuthRequired(highEndDrugsBean.getIsPreAuthRequired());
                highEndDrugs.setUnitEditable(highEndDrugsBean.getUnitEditable());
                highEndDrugs.setPriceEditable(highEndDrugsBean.getPriceEditable());
                highEndDrugs.setMaximumUnit(highEndDrugsBean.getMaximumUnit());
                highEndDrugs.setCreatedBy(highEndDrugsBean.getCreatedBy());
                highEndDrugs.setCreatedOn(date);
                highEndDrugs.setDeletedFlag(0);
                highEndDrugsRepository.save(highEndDrugs);
                response.setMessage("High End Drugs Added");
                response.setStatus("Success");
            }
            else {
                response.setStatus("Failed");
                response.setMessage("HighEndDrug Code Already Exist");
            }
        }
        catch (Exception e){
        	logger.error(ExceptionUtils.getStackTrace(e));
            response.setMessage("Some error happen");
            response.setStatus("Failed");
        }
        return response;
    }

    @Override
    public List<HighEndDrugs> getAllHighEndDrugs() {
        List<HighEndDrugs> response = new ArrayList<>();
        List<HighEndDrugs> findAll = highEndDrugsRepository.findAll(Sort.by(Sort.Direction.ASC, "hedCode"));
        if (findAll != null) {
            for (HighEndDrugs highEndDrugs : findAll) {
                if (highEndDrugs != null && highEndDrugs.getDeletedFlag() == 0) {
                    response.add(highEndDrugs);
                }
            }
        }
        return response;
    }

    @Override
    public HighEndDrugs getHighEndDrugsById(Long id) {
        HighEndDrugs highEndDrugs=null;
        try {
            highEndDrugs=highEndDrugsRepository.findById(id).get();
        }catch(Exception e) {
        	logger.error(ExceptionUtils.getStackTrace(e));
        }
        return highEndDrugs;
    }

    @Override
    public Response deleteHighEndDrugsById(Long id) {
        Response response=new Response();
        try {
            HighEndDrugs highEndDrugs=highEndDrugsRepository.findById(id).get();
            highEndDrugs.setDeletedFlag(1);
            highEndDrugsRepository.save(highEndDrugs);
            response.setMessage("Record Successfully In-Active");
            response.setStatus("Success");
        }catch(Exception e) {
        	logger.error(ExceptionUtils.getStackTrace(e));
            response.setMessage("Some error happen");
            response.setStatus("Failed");
        }
        return response;
    }

    @Override
    public Response updateHighEndDrugsById(Long id, HighEndDrugsBean highEndDrugsBean) {
        Response response=new Response();
        HighEndDrugs highEndDrugsResponse=null;
        try{
            HighEndDrugs highEndDrugs=new HighEndDrugs();
            Integer count=highEndDrugsRepository.countRowForCheckDuplicateDrugCode(highEndDrugsBean.getHedCode());
            if(count==0) {
                HighEndDrugs highEndDrugsRes = highEndDrugsRepository.findById(id).get();
                highEndDrugs.setId(id);
                highEndDrugs.setHedCode(highEndDrugsBean.getHedCode());
                highEndDrugs.setHedName(highEndDrugsBean.getHedName());
                highEndDrugs.setUnit(highEndDrugsBean.getUnit());
                highEndDrugs.setPrice(highEndDrugsBean.getPrice());
                highEndDrugs.setMaximumUnit(highEndDrugsBean.getMaximumUnit());
                highEndDrugs.setRecomendedDose(highEndDrugsBean.getRecomendedDose());
                highEndDrugs.setIsPreAuthRequired(highEndDrugsBean.getIsPreAuthRequired());
                highEndDrugs.setUnitEditable(highEndDrugsBean.getUnitEditable());
                highEndDrugs.setPriceEditable(highEndDrugsBean.getPriceEditable());
                highEndDrugs.setCreatedBy(highEndDrugsRes.getCreatedBy());
                highEndDrugs.setCreatedOn(highEndDrugsRes.getCreatedOn());
                highEndDrugs.setDeletedFlag(highEndDrugsRes.getDeletedFlag());
                highEndDrugs.setUpdatedBy(highEndDrugsBean.getUpdatedBy());
                highEndDrugs.setUpdatedOn(date);
                highEndDrugsRepository.save(highEndDrugs);
                response.setMessage("High End Drugs Updated");
                response.setStatus("Success");
            }
            else {
                highEndDrugsResponse=highEndDrugsRepository.findByHedCode(highEndDrugsBean.getHedCode());
                if(highEndDrugsResponse.getId().equals(id)){
                    highEndDrugs.setId(id);
                    highEndDrugs.setHedCode(highEndDrugsBean.getHedCode());
                    highEndDrugs.setHedName(highEndDrugsBean.getHedName());
                    highEndDrugs.setUnit(highEndDrugsBean.getUnit());
                    highEndDrugs.setPrice(highEndDrugsBean.getPrice());
                    highEndDrugs.setMaximumUnit(highEndDrugsBean.getMaximumUnit());
                    highEndDrugs.setRecomendedDose(highEndDrugsBean.getRecomendedDose());
                    highEndDrugs.setIsPreAuthRequired(highEndDrugsBean.getIsPreAuthRequired());
                    highEndDrugs.setUnitEditable(highEndDrugsBean.getUnitEditable());
                    highEndDrugs.setPriceEditable(highEndDrugsBean.getPriceEditable());
                    highEndDrugs.setCreatedBy(highEndDrugsResponse.getCreatedBy());
                    highEndDrugs.setCreatedOn(highEndDrugsResponse.getCreatedOn());
                    highEndDrugs.setDeletedFlag(highEndDrugsResponse.getDeletedFlag());
                    highEndDrugs.setUpdatedBy(highEndDrugsBean.getUpdatedBy());
                    highEndDrugs.setUpdatedOn(date);
                    highEndDrugsRepository.save(highEndDrugs);
                    response.setMessage("High End Drugs Updated");
                    response.setStatus("Success");
                }
                else {
                    response.setMessage("HighEndDrug Code Already Exist");
                    response.setStatus("Failed");
                }
            }


        } catch (Exception e){
        	logger.error(ExceptionUtils.getStackTrace(e));
            response.setMessage("Some error happen");
            response.setStatus("Failed");
        }
        return response;
    }

    @Override
    public List<Object> getWardName(String action) {
        ResultSet wardObj = null;
        List<Object> wardDtls = new ArrayList<Object>();
        try {
            StoredProcedureQuery storedProcedureQuery = this.entityManager
                    .createStoredProcedureQuery("USP_GET_WARD_DETAILS")
                    .registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_WARDCATEGORYID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_RESULTSET", void.class, ParameterMode.REF_CURSOR);

            storedProcedureQuery.setParameter("P_ACTION", action);
            storedProcedureQuery.execute();

            wardObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULTSET");

            while (wardObj.next()) {
                WardBean bean = new WardBean();
                bean.setWardName(wardObj.getString(1));
                bean.setWardMasterId(wardObj.getInt(2));
                wardDtls.add(bean);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (wardObj != null)
                    wardObj.close();
            } catch (Exception e2) {
            	logger.error(ExceptionUtils.getStackTrace(e2));
            }
        }
        return wardDtls;

    }

    @Override
    public List<Object> getImplantCode(String action, Integer wardCategoryId) {
        ResultSet wardObj = null;
        List<Object> wardDtls = new ArrayList<Object>();
        try {
            StoredProcedureQuery storedProcedureQuery = this.entityManager
                    .createStoredProcedureQuery("USP_GET_WARD_DETAILS")
                    .registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_WARDCATEGORYID", Integer.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_RESULTSET", void.class, ParameterMode.REF_CURSOR);

            storedProcedureQuery.setParameter("P_ACTION", action);
            storedProcedureQuery.setParameter("P_WARDCATEGORYID", wardCategoryId);
            storedProcedureQuery.execute();

            wardObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULTSET");

            while (wardObj.next()) {
                WardBean bean = new WardBean();
                bean.setImplantCode(wardObj.getString(1));
                wardDtls.add(bean);
            }
        } catch (Exception e) {

            throw new RuntimeException(e);
        } finally {
            try {
                if (wardObj != null)
                    wardObj.close();
            } catch (Exception e2) {
            	logger.error(ExceptionUtils.getStackTrace(e2));
            }
        }
        return wardDtls;

    }

    @Override
    public Long checkDrugCode(String drugCode) {
        Long checkCode=null;
        try {
            checkCode = highEndDrugsRepository.getHedCodeById(drugCode);
            return checkCode;
        } catch (Exception e) {
        	logger.error(ExceptionUtils.getStackTrace(e));
        }
        return checkCode;
    }

}
