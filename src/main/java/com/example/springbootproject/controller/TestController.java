package com.example.springbootproject.controller;

import com.example.springbootproject.model.req.BaseRequest;
import com.example.springbootproject.model.resp.BaseResponse;
import com.example.springbootproject.model.resp.ReturnInfo;
import com.example.springbootproject.service.TestService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xp-zhao on 2018/11/15.
 */
@RestController
public class TestController {

  private static final Logger logger = LoggerFactory.getLogger(TestController.class);

  @Autowired
  private TestService service;

  @RequestMapping("/test")
  public BaseResponse test(@Valid BaseRequest req,
      @NotNull(message = "不能为空") @RequestHeader("test-header") String testHeader) {
    System.out.println(service.get("2013051233", "xp"));
    logger.info("test-log-info");
    logger.debug("test-log-debug");
    return new BaseResponse(ReturnInfo.SUCCESS);
  }

  @RequestMapping("/log")
  public BaseResponse log(@Valid BaseRequest req) {
    return new BaseResponse(ReturnInfo.SUCCESS);
  }

  @RequestMapping("/list")
  public BaseResponse list(@RequestBody List<Integer> list) {
    System.out.println(list);
    return new BaseResponse(ReturnInfo.SUCCESS);
  }
}
