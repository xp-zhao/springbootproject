package com.example.springbootproject.model.req;

import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * @description: 参数验证
 * @author: zhaoxp
 * @create: 2019/05/08
 **/
@Data
public class ValidateReq {
  @Pattern(regexp = "0|1",message = "状态错误")
  private String status;
}