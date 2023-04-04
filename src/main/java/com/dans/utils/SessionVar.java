package com.dans.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SessionVar {

  private static final Logger log = LoggerFactory.getLogger(SessionVar.class);

  public String ip_white_list;
  public String allowed_methods;

  @Autowired
  private Environment environment;

  public void loadProperties() {
    try {
      ip_white_list = environment.getProperty("ip.white.list");
      allowed_methods = environment.getProperty("allowed.methods");

      log.info("### LOAD PROPERTIES DONE ###");
    } catch (Exception e) {
      log.info(ExceptionUtils.getStackTrace(e));
    }
  }
}
