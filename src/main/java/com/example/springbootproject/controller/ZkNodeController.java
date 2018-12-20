package com.example.springbootproject.controller;

import com.alibaba.fastjson.JSON;
import com.example.springbootproject.mapper.TaskGroupMapper;
import com.example.springbootproject.mapper.TaskInfoMapper;
import com.example.springbootproject.model.TaskGroup;
import com.example.springbootproject.model.TaskInfo;
import com.example.springbootproject.utils.ZookeeperServer;
import com.example.springbootproject.zk.storage.TaskGroupStorage;
import com.example.springbootproject.zk.storage.TaskStorage;
import com.example.springbootproject.zk.watcher.task.GroupWatcher;
import com.example.springbootproject.zk.watcher.task.RootWatcher;
import com.example.springbootproject.zk.watcher.task.TaskConstants;
import com.example.springbootproject.zk.watcher.task.TaskWatcher;
import com.example.springbootproject.zk.watcher.task.model.GroupInfo;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xp-zhao on 2018/12/20.
 */
@RestController
public class ZkNodeController
{
	@Autowired
	private TaskInfoMapper  taskInfoMapper;
	@Autowired
	private TaskGroupMapper taskGroupMapper;
	@Autowired
	private ZookeeperServer zkServer;

	@RequestMapping("syncNode")
	public void syncNode() throws KeeperException, InterruptedException
	{
		List<TaskInfo> taskInfos = taskInfoMapper.selectList(null);
		List<String> groups = taskInfos.stream().map(TaskInfo::getGroupCode).distinct().collect(Collectors.toList());
		zkServer.getChildren(TaskConstants.TASK_ROOTPATH , new RootWatcher(zkServer));
		for(String group : groups)
		{
			String path = zkServer.createPersistentNode(TaskConstants.TASK_ROOTPATH + "/" + group ,
				JSON.toJSONString(group));
			// 为所有任务组添加监听
			zkServer.getChildren(TaskConstants.TASK_ROOTPATH + "/" + group , new GroupWatcher(zkServer));

			GroupInfo groupInfo = new GroupInfo();
			groupInfo.setGroupCode(group);
			// 保存已经添加了监听的任务组
			TaskGroupStorage.INSTANCE.setGroup(group,groupInfo);
		}

		for(TaskInfo task : taskInfos)
		{
			// 为每个任务添加监听
			String taskInfoData = zkServer.getData(TaskConstants.TASK_ROOTPATH + "/" + task.getGroupCode() + "/" + task.getTaskCode() , new TaskWatcher(zkServer) ,
				null);
			TaskInfo taskInfo = JSON.parseObject(taskInfoData , TaskInfo.class);
			// 保存已经监听了的任务
			TaskStorage.INSTANCE.set(task.getGroupCode()+"|"+task.getTaskCode() , taskInfo);
		}
	}
}
