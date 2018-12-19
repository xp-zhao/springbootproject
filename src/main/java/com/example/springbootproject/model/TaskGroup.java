package com.example.springbootproject.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by xp-zhao on 2018/12/19.
 */
@Data
@TableName("task_group")
public class TaskGroup
{
	private String groupcode;
	private String groupname;
	private String groupdesc;
	private String createuid;
	private String createtime;
}
