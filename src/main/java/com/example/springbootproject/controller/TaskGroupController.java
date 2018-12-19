package com.example.springbootproject.controller;

import com.alibaba.fastjson.JSON;
import com.example.springbootproject.mapper.TaskGroupMapper;
import com.example.springbootproject.model.TaskGroup;
import com.example.springbootproject.model.resp.BaseResponse;
import com.example.springbootproject.model.resp.ReturnInfo;
import com.example.springbootproject.utils.ZookeeperServer;
import com.example.springbootproject.zk.watcher.task.TaskConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xp-zhao on 2018/12/19.
 */
@RestController
public class TaskGroupController
{
	private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

	@Autowired
	private TaskGroupMapper groupMapper;
	@Autowired
	private ZookeeperServer zkServer;

	@RequestMapping("/addGroup")
	public BaseResponse addGroup(TaskGroup group){
		try
		{
			groupMapper.insert(group);
		}
		catch (Exception e)
		{
			logger.error("添加任务组失败",e);
			return new BaseResponse(ReturnInfo.FAILED);
		}
		try
		{
			String path = zkServer.createPersistentNode(TaskConstants.TASK_ROOTPATH + "/" + group.getGroupcode() ,
				JSON.toJSONString(group));
			logger.info("任务组节点创建成功："+path);
		}
		catch (Exception e)
		{
			logger.error("创建任务组节点失败！",e);
		}
		return new BaseResponse(ReturnInfo.SUCCESS);
	}
}
