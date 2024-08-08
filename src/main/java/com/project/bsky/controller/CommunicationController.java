package com.project.bsky.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import com.project.bsky.config.dto.AppResponse;
import com.project.bsky.config.dto.MessageDto;
import com.project.bsky.config.dto.MessagePassingDto;
import com.project.bsky.entity.MessagePassingEntity;
import com.project.bsky.repository.ProcessRepository;
import com.project.bsky.service.CommunicationService;

@RestController
@CrossOrigin(origins = "*")
public class CommunicationController {
	
	@Autowired
	private CommunicationService communicationService;
	
	@Autowired
	private ProcessRepository processRepository;
	
	@PostMapping("")
	public  ResponseEntity<AppResponse<?>> saveData(@RequestBody String message) throws Exception{
		JSONObject json = new JSONObject(message);
		Integer intMessageConfigId = json.getInt("intMessageConfigId");
		return null;
		
	}
	
	@PostMapping("/manageMessageConfig")
	public ResponseEntity<?> deleteByMessageId(@RequestBody String itemStatus) throws JSONException {
		JSONObject jsonObject = new JSONObject(itemStatus);
		String str = jsonObject.getString("itemId");
		String[] arrOfStr = str.split(",");
		
		if (jsonObject.get("itemStatus").equals("1")) {
			communicationService.delete(arrOfStr);
			return ResponseEntity.ok(AppResponse.success(itemStatus).setMsg("Records has been deleted"));
		} else if (jsonObject.get("itemStatus").equals("2")) {
			communicationService.publish(arrOfStr);
			return ResponseEntity.ok(AppResponse.success(itemStatus).setMsg("Menu Published Successfully."));
		} else if (jsonObject.get("itemStatus").equals("AR")) {
			communicationService.archive(arrOfStr);
			return ResponseEntity.ok(AppResponse.success(itemStatus).setMsg("Menu Archieve Successfully."));
		} else if (jsonObject.get("itemStatus").equals("3")) {
			communicationService.unPublish(arrOfStr);
			return ResponseEntity.ok(AppResponse.success(itemStatus).setMsg("Menu Unpublish Successfully."));
		}
//		jsonObject.put("status", 200);
//		jsonObject.put("result", arrOfStr);
//		return ResponseEntity.ok(jsonObject.toString());
		return ResponseEntity.ok(AppResponse.success(itemStatus).setMsg("Menu Unpublish Successfully."));

	}

	
	@PostMapping("/viewMessageConfig")
	public  ResponseEntity<?> getViewMessageData(@RequestBody String viewMessage) throws Exception{
		List<MessagePassingDto> messagePassingDto = communicationService.getMessageDetails(viewMessage);
		JSONObject json = new JSONObject();
		json.put("status", "200");
		json.put("result", messagePassingDto);
		return ResponseEntity.ok(json.toString());
		
		
	}
	
	@PostMapping("/getForms")
	public ResponseEntity<?> getFormName() throws Exception{
		List<Object[]> messagedto = processRepository.findAllById();
		List<MessageDto> mDto = new ArrayList<>();
		for(Object[] obj:messagedto) {
			MessageDto messageDto= new MessageDto();
			messageDto.setIntProcessId((Integer) obj[0]);
			messageDto.setVchProcessName((String) obj[1]);
			mDto.add(messageDto);
		}
		JSONObject json = new JSONObject();
		json.put("status", 200);
		json.put("result", mDto);
		return ResponseEntity.ok(json.toString());
		
	}
}
