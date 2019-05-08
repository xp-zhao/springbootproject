package com.example.springbootproject.controller;

import com.example.springbootproject.model.req.ValidateReq;
import com.example.springbootproject.model.resp.BaseResponse;
import com.example.springbootproject.model.resp.ReturnInfo;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 参数验证测试
 * @author: zhaoxp
 * @create: 2019/05/08
 **/
@RestController
public class ValidateController {
  @RequestMapping("/validate")
  public BaseResponse test(@Valid ValidateReq req)
  {
    return new BaseResponse(ReturnInfo.SUCCESS);
  }
}