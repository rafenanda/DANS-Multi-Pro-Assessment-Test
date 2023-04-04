package com.dans.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dans.rest.RestService;
import com.dans.service.UserService;
import com.dans.utils.JwtUtil;
import com.dans.utils.Util;

@RestController
@RequestMapping
@PropertySource(value = { "classpath:application.properties" })
public class MainController {
    private static final Logger log = LogManager.getLogger(MainController.class);

    @Autowired
    private Util util;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(HttpServletRequest request,
            @RequestBody HashMap<String, Object> payload, @RequestHeader HashMap<String, Object> headers) {
        try {
            util.writeMessageForLogging(payload, "REQUEST");
            util.writeMessageForLogging(headers, "HEADERS");

            String msg = (String) payload.get("msg");

            if (!jwtUtil.validateJwt(msg)) {
                log.info("JWT EXPIRED");
                ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("JWT invalid or expired");
                util.loggingResp(responseEntity, log);

                return responseEntity;
            }

            Map<String, Object> data = jwtUtil.getBodyFromJwt(msg);
            String username = (String) data.get("username");
            String password = (String) data.get("password");

            if (userService.authenticate(username, password)) {
                log.info("Login successful");
                return ResponseEntity.ok("Login successful");
            }
            log.info("Login failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            log.info(ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
    }

    @GetMapping(path = "job/list")
    public ResponseEntity<Object> jobList(@RequestHeader HashMap<String, Object> headers) {
        try {
            util.writeMessageForLogging(headers, "HEADERS");
            String msg = (String) headers.get("authorization");
            String jwt = msg.substring(7);
            log.info("JWT : " + jwt);

            if (!jwtUtil.validateJwt(jwt)) {
                log.info("JWT EXPIRED");
                ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("JWT invalid or expired");
                util.loggingResp(responseEntity, log);

                return responseEntity;
            }
            RestService getConfig = new RestService();
            getConfig.setUrl("http://dev3.dansmultipro.co.id/api/recruitment/positions.json");
            getConfig.setCto(6000);
            getConfig.setRto(6000);
            return ResponseEntity.ok().body(getConfig.restGet());

        } catch (Exception e) {
            log.info(ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
    }

    @GetMapping(path = "job/list/{id}")
    public ResponseEntity<Object> jobDetail(@RequestHeader HashMap<String, Object> headers, @PathVariable("id") String id) {
        try {
            util.writeMessageForLogging(headers, "HEADERS");
            String msg = (String) headers.get("authorization");
            String jwt = msg.substring(7);
            log.info("JWT : " + jwt);

            if (!jwtUtil.validateJwt(jwt)) {
                log.info("JWT EXPIRED");
                ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("JWT invalid or expired");
                util.loggingResp(responseEntity, log);

                return responseEntity;
            }
            RestService getConfig = new RestService();
            getConfig.setUrl("http://dev3.dansmultipro.co.id/api/recruitment/positions/");
            getConfig.setCto(6000);
            getConfig.setRto(6000);
            HashMap<String, Object> hm = new HashMap<String, Object>();
            hm.put("id", id);
            return ResponseEntity.ok().body(getConfig.restGet(hm));

        } catch (Exception e) {
            log.info(ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
    }

}
