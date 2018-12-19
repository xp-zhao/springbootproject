package com.example.springbootproject.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by xp-zhao on 2018/12/19.
 */
@Data
@TableName("t_migumanager_task_taskinfo")
public class TaskInfo
{
	private String groupCode;
	private String taskCode;
	private String taskName;
	private String status;
	private String taskDesc;
	private String cronExpression;
	private String allowConcurrent;
	private String scheduleType;
	private String createUid;
	private String createTime;
}
