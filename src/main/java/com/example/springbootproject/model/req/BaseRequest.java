package com.example.springbootproject.model.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class BaseRequest implements Serializable
{
	private static final long serialVersionUID = 1337694830923286472L;
	@ NotBlank (message = "渠道 不能为空")
	@ Pattern (regexp = "^[0-9a-zA-Z]{1,4}$" ,message = "渠道错误")
	private String channel;//渠道id
	//日志ID
	private String logId;

}
