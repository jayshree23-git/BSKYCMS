package com.project.bsky.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.util.*;

/**
 * @Project : BSKY Backend
 * @Auther : Sambit Kumar Pradhan
 * @Created On : 21/06/2023 - 12:26 PM
 */
public class CommonUtils {
    public static String convertClobToString(Clob data) {
        try {
            return (data != null ? data.getSubString(1, (int) data.length()) : null);
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, Object> convertStringToMap(String data) {
        try {
            return new ObjectMapper().readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static List<Map<String, Object>> convertStringToListMap(String data) {
        try {
            return new ObjectMapper().readValue(data, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static List<Integer> ConvertStringToListOfIntegers(String data) {
        try {
            return new ObjectMapper().readValue(data, new TypeReference<List<Integer>>() {
            });
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
