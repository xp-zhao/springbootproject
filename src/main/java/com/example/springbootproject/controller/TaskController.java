package com.example.springbootproject.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.springbootproject.mapper.TaskInfoMapper;
import com.example.springbootproject.model.TaskInfo;
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
public class TaskController
{
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private TaskInfoMapper  taskInfoMapper;
	@Autowired
	private ZookeeperServer zkServer;

	@RequestMapping("/addTask")
	public BaseResponse addTask(TaskInfo taskInfo){
		try
		{
//			taskInfoMapper.insert(taskInfo);
		}
		catch (Exception e)
		{
			logger.error("添加任务失败！",e);
			return new BaseResponse(ReturnInfo.FAILED);
		}
		try
		{
			String path = zkServer.createPersistentNode(TaskConstants.TASK_ROOTPATH + "/" + taskInfo.getGroupCode()+"/"+taskInfo.getTaskCode() ,
				JSON.toJSONString(taskInfo));
			logger.info("任务节点创建成功："+path);
		}
		catch (Exception e)
		{
			logger.error("创建任务节点失败！",e);
		}
		return new BaseResponse(ReturnInfo.SUCCESS);
	}

	@RequestMapping("/updateTask")
	public BaseResponse updateTask(TaskInfo taskInfo){
		UpdateWrapper wrapper = new UpdateWrapper();
		wrapper.eq("groupCode" , taskInfo.getGroupCode());
		wrapper.eq("taskCode" , taskInfo.getTaskCode());
		try
		{
			taskInfoMapper.update(taskInfo,wrapper);
		}
		catch (Exception e)
		{
			logger.error("更新任务失败！",e);
			return new BaseResponse(ReturnInfo.FAILED);
		}
		try
		{
			zkServer.updateNodeData(TaskConstants.TASK_ROOTPATH + "/" + taskInfo.getGroupCode()+"/"+taskInfo.getTaskCode() ,
				JSON.toJSONString(taskInfo));
			logger.info("任务节点更新成功!");
		}
		catch (Exception e)
		{
			logger.error("更新任务节点失败！",e);
		}
		return new BaseResponse(ReturnInfo.SUCCESS);
	}
}
