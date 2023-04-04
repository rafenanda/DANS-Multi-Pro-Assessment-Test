package com.dans.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Util {
  private static final Logger log = LogManager.getLogger(Util.class);
  // private static final Gson GSON = new
  // GsonBuilder().serializeNulls().setPrettyPrinting().create(); // pretty print
  // private static final Gson GSONs = new
  // GsonBuilder().serializeNulls().create(); // simplify

  public String mapToJson(HashMap<String, Object> hm) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      String json = objectMapper.writeValueAsString(hm);

      return json;
    } catch (Exception e) {
      log.info(ExceptionUtils.getStackTrace(e));
    }

    return null;
  }

  public void writeMessageForLogging(HashMap<String, Object> hm, String msg) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      String json = objectMapper.writeValueAsString(hm);
      log.info(msg + " = " + json);
    } catch (Exception e) {
      log.info(msg + " = " + hm.toString());
      log.info(ExceptionUtils.getStackTrace(e));
    }
  }

  // public void loggingResp(final Object object, Logger logger) {
  // try {
  // logger.info("RESPONSE = " + GSONs.toJson(object));
  // // logger.info("RESPONSE = " +
  // //
  // GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(object));
  // } catch (Exception e) {
  // logger.info(e.toString());
  // logger.info("RESPONSE = " + object.toString());
  // }
  // }

  public void loggingResp(final Object object, Logger logger) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      logger.info("RESPONSE = " + mapper.writeValueAsString(object));
      // logger.info("RESPONSE = " +
      // GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(object));
    } catch (Exception e) {
      logger.info(e.toString());
      logger.info("RESPONSE = " + object.toString());
    }
  }

  public static String decryptDb(String data) {
    data = convertHexToString(data);
    data = data.substring(4) + data.substring(0, 4);
    return data;
  }

  public static String encryptDb(String data) {
    data = data.substring(data.length() - 4) + data.substring(0, data.length() - 4);
    data = convertStringToHex(data);
    return data;
  }

  public static String convertStringToHex(String str) {
    char[] chars = str.toCharArray();
    StringBuffer hex = new StringBuffer();
    for (int i = 0; i < chars.length; i++) {
      String sr = Integer.toHexString(chars[i]);
      if (sr.length() < 2)
        sr = ("0").concat(sr);
      hex.append(sr);
    }
    return hex.toString();
  }

  public static String convertHexToString(String hex) {
    StringBuilder sb = new StringBuilder();
    StringBuilder temp = new StringBuilder();
    for (int i = 0; i < hex.length() - 1; i += 2) {
      String output = hex.substring(i, (i + 2));
      int decimal = Integer.parseInt(output, 16);
      sb.append((char) decimal);
      temp.append(decimal);
    }
    return sb.toString();
  }

  public String getCurrentDateV1() {
    try {
      ZoneId zid = ZoneId.of("Asia/Jakarta");

      LocalDateTime lt = LocalDateTime.now(zid);
      String str = lt.toString();
      log.info(str);
    } catch (Exception e) {
      log.info(ExceptionUtils.getStackTrace(e));
    }

    return "";
  }

  public Map<String, Object> jsonToMap(String str) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(str);
      JSONObject jsonObject = (JSONObject) obj;
      map = toMap(jsonObject);
    } catch (Exception e) {
      log.info(ExceptionUtils.getStackTrace(e));
    }

    return map;

  }

  private Map<String, Object> toMap(JSONObject object) throws Exception {

    Map<String, Object> map = new HashMap<String, Object>();

    for (Object key : object.keySet()) {

      Object value = object.get(key);

      if (value instanceof JSONObject) {

        value = toMap((JSONObject) value);

      }

      map.put(key + "", value);

    }

    return map;

  }

  public String getCurrentDateTimeV1() {
    SimpleDateFormat sdfdtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String datetime = sdfdtime.format(new Date());
    return datetime;
  }

  public Map<String, Object> stringToMap(String str) {
    str = str.replace("{", "").replace("}", "");

    // Memisahkan string berdasarkan koma
    String[] pairs = str.split(",");

    // Membuat map kosong
    Map<String, Object> map = new HashMap<>();

    // Looping setiap pasangan data dan menambahkannya ke map
    for (String pair : pairs) {
      // Memisahkan setiap pasangan data berdasarkan tanda sama dengan
      String[] keyValue = pair.trim().split("=");

      // Menambahkan pasangan key dan value ke map
      map.put(keyValue[0].trim(), keyValue[1].trim());
    }

    return map;
  }

}
