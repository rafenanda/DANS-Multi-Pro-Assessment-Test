package com.dans.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestService {

  public RestService() {
  }

  private static final Logger log = LoggerFactory.getLogger(RestService.class);
  private String url;
  private String ip;
  private String port;
  private String path;
  private int rto;
  private int cto;
  private HashMap<String, Object> headers = new HashMap<String, Object>();

  public HashMap<String, Object> restPost(HashMap<String, Object> requestBody) {

    HashMap<String, Object> responsebuild = new HashMap<String, Object>();
    ObjectMapper mapper = new ObjectMapper();

    try {
      String url = "http://" + this.ip + ":" + this.port + this.path;

      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      con.setRequestProperty("User-Agent", "Mulesoft/1.0 ( compatible ) ");
      con.setRequestProperty("Accept", "*/*");
      con.setRequestProperty("Accept-Encoding", "UTF-8");
      con.setRequestProperty("Connection", "keep-alive");
      con.setRequestProperty("Cache-Control", "no-cache");
      con.setRequestProperty("http.version", "HTTP/1.1");
      con.setRequestProperty("http.scheme", "http");

      for (String key : headers.keySet()) {
        con.setRequestProperty(key, headers.get(key).toString().trim());
      }

      con.setReadTimeout(rto);
      con.setConnectTimeout(cto);

      con.setDoOutput(true);
      con.setDoInput(true);
      con.setRequestMethod("POST");

      String jj = mapper.writeValueAsString(requestBody);

      log.info("URL 'POST' -> " + url);
      log.info("Request 'POST' -> " + jj);

      OutputStream os = con.getOutputStream();
      os.write(jj.getBytes("UTF-8"));
      os.close();

      int status = con.getResponseCode();

      BufferedReader br = null;
      if (100 <= status && status <= 399) {
        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {
        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      }

      String result = org.apache.commons.io.IOUtils.toString(br);

      log.info("RC 'POST' -> " + status);

      log.info("Response 'POST' -> " + result);

      br.close();
      con.disconnect();

      if (status != 200) {
        responsebuild.put("conn_rc", "H99");
        responsebuild.put("conn_rd", "ERROR OCCURED");

        return responsebuild;
      }

      responsebuild = stringToMap(result);
      return responsebuild;
    } catch (ConnectException e) {
      responsebuild.put("conn_rc", "H68");
      responsebuild.put("conn_rd", "connection refused");

      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;
    } catch (java.net.SocketTimeoutException e) {
      responsebuild.put("conn_rc", "H68");
      responsebuild.put("conn_rd", "connection timeout");

      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;
    } catch (SocketException e) {
      responsebuild.put("conn_rc", "H68");
      responsebuild.put("conn_rd", "socket issue");

      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;
    } catch (IOException e) {
      responsebuild.put("conn_rc", "H68");
      responsebuild.put("conn_rd", "io issue");

      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;
    } catch (Exception e) {
      responsebuild.put("conn_rc", "H68");
      responsebuild.put("conn_rd", "unhandled exception");

      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;
    }
  }

  public HashMap<String, Object> restGet(HashMap<String, Object> requestParam) {

    HashMap<String, Object> responsebuild = new HashMap<String, Object>();

    try {
      if (!requestParam.isEmpty()) {
        for (String key : requestParam.keySet()) {
          url = url + requestParam.get(key) + "/";
        }

        url = url.substring(0, url.length() - 1);
      }

      log.info("URL 'GET' -> " + url);

      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      con.setRequestProperty("User-Agent", "Mulesoft/1.0 ( compatible ) ");
      con.setRequestProperty("Accept", "*/*");
      con.setRequestProperty("Accept-Encoding", "UTF-8");
      con.setRequestProperty("Connection", "keep-alive");
      con.setRequestProperty("Cache-Control", "no-cache");
      con.setRequestProperty("http.version", "HTTP/1.1");
      con.setRequestProperty("http.scheme", "http");

      for (String key : headers.keySet()) {
        con.setRequestProperty(key, headers.get(key).toString().trim());
      }

      con.setReadTimeout(rto);
      con.setConnectTimeout(cto);
      con.setDoOutput(true);
      con.setDoInput(true);
      con.setRequestMethod("GET");

      int status = con.getResponseCode();

      BufferedReader br = null;
      if (100 <= status && status <= 399) {
        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String result = org.apache.commons.io.IOUtils.toString(br);

        log.info("RC 'GET' -> " + status);

        log.info("Response 'GET' -> " + result);

        br.close();
        con.disconnect();

        responsebuild = stringToMap(result);
        return responsebuild;
      } else {
        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        String result = org.apache.commons.io.IOUtils.toString(br);

        log.info("RC 'GET' -> " + status);

        log.info("Response 'GET' -> " + result.replace("\n", "").replace("\t", ""));

        br.close();
        con.disconnect();

        if (status != 200) {
          responsebuild.put("conn_rc", "H99");
          responsebuild.put("conn_rd", "ERROR OCCURED");

          return responsebuild;
        }

        responsebuild = stringToMap(result);
        return responsebuild;
      }
    } catch (ConnectException e) {
      responsebuild.put("conn_rc", "H68");
      responsebuild.put("conn_rd", "connection refused");

      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;
    } catch (java.net.SocketTimeoutException e) {
      responsebuild.put("conn_rc", "H68");
      responsebuild.put("conn_rd", "connection timeout");

      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;
    } catch (SocketException e) {
      responsebuild.put("conn_rc", "H68");
      responsebuild.put("conn_rd", "socket issue");

      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;
    } catch (IOException e) {
      responsebuild.put("conn_rc", "H68");
      responsebuild.put("conn_rd", "io issue");

      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;
    } catch (Exception e) {
      responsebuild.put("conn_rc", "H68");
      responsebuild.put("conn_rd", "unhandled exception");

      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;
    }
  }

  public ArrayList<HashMap<String, Object>> restGet() {

    ArrayList<HashMap<String, Object>> responsebuild = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> hm = new HashMap<String, Object>();
    try {
      log.info("URL 'GET' -> " + url);

      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      con.setRequestProperty("User-Agent", "Mulesoft/1.0 ( compatible ) ");
      con.setRequestProperty("Accept", "*/*");
      con.setRequestProperty("Accept-Encoding", "UTF-8");
      con.setRequestProperty("Connection", "keep-alive");
      con.setRequestProperty("Cache-Control", "no-cache");
      con.setRequestProperty("http.version", "HTTP/1.1");
      con.setRequestProperty("http.scheme", "http");

      for (String key : headers.keySet()) {
        con.setRequestProperty(key, headers.get(key).toString().trim());
      }

      con.setReadTimeout(rto);
      con.setConnectTimeout(cto);
      con.setDoOutput(true);
      con.setDoInput(true);
      con.setRequestMethod("GET");

      int status = con.getResponseCode();

      BufferedReader br = null;
      if (100 <= status && status <= 399) {
        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String result = org.apache.commons.io.IOUtils.toString(br);

        log.info("RC 'GET' -> " + status);

        log.info("Response 'GET' -> " + result);

        br.close();
        con.disconnect();

        responsebuild = stringToArray(result);
        return responsebuild;
      } else {
        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        String result = org.apache.commons.io.IOUtils.toString(br);

        log.info("RC 'GET' -> " + status);

        log.info("Response 'GET' -> " + result.replace("\n", "").replace("\t", ""));

        br.close();
        con.disconnect();

        if (status != 200) {
          hm.put("conn_rc", "H99");
          hm.put("conn_rd", "ERROR OCCURED");

          return responsebuild;
        }

        responsebuild = stringToArray(result);
        return responsebuild;
      }
    } catch (ConnectException e) {
      hm.put("conn_rc", "H68");
      hm.put("conn_rd", "connection refused");
      responsebuild.add(hm);
      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;

    } catch (java.net.SocketTimeoutException e) {
      hm.put("conn_rc", "H68");
      hm.put("conn_rd", "connection timeout");
      responsebuild.add(hm);
      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;

    } catch (SocketException e) {
      hm.put("conn_rc", "H68");
      hm.put("conn_rd", "socket issue");
      responsebuild.add(hm);
      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;

    } catch (IOException e) {
      hm.put("conn_rc", "H68");
      hm.put("conn_rd", "io issue");
      responsebuild.add(hm);
      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;

    } catch (Exception e) {
      hm.put("conn_rc", "H68");
      hm.put("conn_rd", "unhandled exception");
      responsebuild.add(hm);
      log.info(ExceptionUtils.getStackTrace(e));
      return responsebuild;
    }
  }

  private HashMap<String, Object> stringToMap(String json) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      HashMap<String, Object> datahashmap = new HashMap<String, Object>();
      datahashmap = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
      });

      return datahashmap;
    } catch (Exception e) {
      log.info(ExceptionUtils.getStackTrace(e));
    }

    return null;

  }

  private ArrayList<HashMap<String, Object>> stringToArray(String json) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      ArrayList<HashMap<String, Object>> datahashmap = new ArrayList<HashMap<String, Object>>();
      datahashmap = mapper.readValue(json, new TypeReference<ArrayList<HashMap<String, Object>>>() {
      });

      return datahashmap;
    } catch (Exception e) {
      log.info(ExceptionUtils.getStackTrace(e));
    }

    return null;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public int getRto() {
    return rto;
  }

  public void setRto(int rto) {
    this.rto = rto;
  }

  public int getCto() {
    return cto;
  }

  public void setCto(int cto) {
    this.cto = cto;
  }

  public HashMap<String, Object> getHeaders() {
    return headers;
  }

  public void setHeaders(HashMap<String, Object> headers) {
    this.headers = headers;
  }

}
