package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.HighEndDrugsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HighEndDrugs;
import com.project.bsky.model.Implant;
import com.project.bsky.service.HighEndDrugsService;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class HighEndDrugsController {
    @Autowired
    private HighEndDrugsService highEndDrugsService;
    
    @Autowired
    private Logger logger;
    
    
    @GetMapping(value = "/getImplantName")
    @ResponseBody
    public List<Implant> getImplantName(){
        return highEndDrugsService.getImplantName();
    }

    @PostMapping(value="/saveHighEndDrugs")
    @ResponseBody
    public Response saveHighEndDrugs(@RequestBody HighEndDrugsBean highEndDrugsBean){
    	System.out.println(highEndDrugsBean);
        return highEndDrugsService.saveHighEndDrugs(highEndDrugsBean);
    }

    @GetMapping(value="/getAllHighEndDrugs")
    @ResponseBody
    public List<HighEndDrugs> getAllHighEndDrugs(){
        return highEndDrugsService.getAllHighEndDrugs();
    }

    @GetMapping(value = "/getHighEndDrugsById/{id}")
    @ResponseBody
    public HighEndDrugs getHighEndDrugsById(@PathVariable(value = "id", required = false) Long id){
        return highEndDrugsService.getHighEndDrugsById(id);
    }

    @DeleteMapping(value = "/deleteHighEndDrugsById/{id}")
    @ResponseBody
    public Response deleteHighEndDrugsById(@PathVariable(value = "id", required = false) Long id){
        return highEndDrugsService.deleteHighEndDrugsById(id);
    }

    @PutMapping(value = "/updateHighEndDrugsById/{id}")
    @ResponseBody
    public Response updateHighEndDrugsById(@RequestBody HighEndDrugsBean highEndDrugsBean,
                                              @PathVariable(value = "id", required = false) Long id) {
        ////System.out.println("==========" + highEndDrugsBean);
        return highEndDrugsService.updateHighEndDrugsById(id, highEndDrugsBean);
    }

@GetMapping(value = "/getWardName")
@ResponseBody
public List<Object> getWardName(
        @RequestParam(value = "action", required = false) String action)
{
    ////System.out.println(action);
    List<Object> getWardName = null;

    try {
        getWardName = highEndDrugsService.getWardName(action);
    } catch (Exception e) {
    	logger.error(ExceptionUtils.getStackTrace(e));
    }
    return getWardName;
}
    @GetMapping(value = "/getImplantCode")
    @ResponseBody
    public List<Object> getImplantCode(
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "wardCategoryId", required = false) Integer wardCategoryId)
    {
        ////System.out.println(action);
        List<Object> getImplantCode = null;
        try {
            getImplantCode = highEndDrugsService.getImplantCode(action,wardCategoryId);
        } catch (Exception e) {
        	logger.error(ExceptionUtils.getStackTrace(e));
        }
        return getImplantCode;
    }
    @ResponseBody
    @GetMapping(value = "/checkDuplicateDrugCode")
    public ResponseEntity<Response> checkDuplicateDrugCode(@RequestParam(value = "drugCode", required =false)String drugCode,
                                                          // @RequestParam(value = "implantCode", required =false)String implantCode,
                                                             Response response){
        ////System.out.println("Inside------>>");
        ////System.out.println("drugCode : " + drugCode);
        try {
            Long hedId = highEndDrugsService.checkDrugCode(drugCode);

            if (hedId != null)
                response.setStatus("Present");
            else
                response.setStatus("Absent");

        }catch (Exception e) {
        	logger.error(ExceptionUtils.getStackTrace(e));
        }

        return ResponseEntity.ok(response);
    }

}
