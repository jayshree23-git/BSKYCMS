package com.project.bsky.bean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;



public class JsonToMapConverter {
	
	 @SuppressWarnings("unchecked")
	    public static Map<String, Object> convertToDatabaseColumn(String attribute)
	    {
	        if (attribute == null) {
	           return new HashMap<>();
	        }
	        try
	        {
	            ObjectMapper objectMapper = new ObjectMapper();
	            return objectMapper.readValue(attribute, HashMap.class);
	        }
	        catch (IOException e) {
	           e.printStackTrace();
	        }
	        return new HashMap<>();
	    }

}
