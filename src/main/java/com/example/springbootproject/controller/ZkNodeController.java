package com.example.springbootproject.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
//		List<String> groups = taskInfos.stream().map(TaskInfo::getGroupCode).distinct().collect(Collectors.toList());
		Map<String,List<TaskInfo>> taskByGroup = taskInfos.stream().collect(Collectors.groupingBy(TaskInfo::getGroupCode));
		List<TaskGroup> taskGroups = taskGroupMapper.selectList(null);
		zkServer.getChildren(TaskConstants.TASK_ROOTPATH , new RootWatcher(zkServer));
		for(TaskGroup group : taskGroups)
		{
			GroupInfo temp = TaskGroupStorage.INSTANCE.getGroup(group.getGroupcode());
			if(null == temp){
				String path = zkServer.createPersistentNode(TaskConstants.TASK_ROOTPATH + "/" + group.getGroupcode() ,
					JSON.toJSONString(group));
			}
			// 为所有任务组添加监听
			zkServer.getChildren(TaskConstants.TASK_ROOTPATH + "/" + group.getGroupcode() , new GroupWatcher(zkServer));
			List<TaskInfo> tasks = taskByGroup.get(group.getGroupcode());
			List<String> taskCode = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(tasks))
			{
				for(TaskInfo task : tasks)
				{
					taskCode.add(task.getTaskCode());
					TaskInfo taskTemp = TaskStorage.INSTANCE.get(group.getGroupcode()+"|"+task.getTaskCode());
					if(null == taskTemp){
						zkServer.createPersistentNode(TaskConstants.TASK_ROOTPATH + "/" + group.getGroupcode()+"/"+task.getTaskCode() ,
							JSON.toJSONString(task));
					}
					// 为每个任务添加监听
					String taskInfoData = zkServer.getData(TaskConstants.TASK_ROOTPATH + "/" + group.getGroupcode() + "/" + task.getTaskCode() , new TaskWatcher(zkServer) ,
						null);
					// 保存已经监听了的任务
					TaskStorage.INSTANCE.set(group.getGroupcode()+"|"+task.getTaskCode() , task);
				}
			}
			GroupInfo groupInfo = new GroupInfo();
			groupInfo.setGroupCode(group.getGroupcode());
			groupInfo.setTaksCode(taskCode);
			// 保存已经添加了监听的任务组
			TaskGroupStorage.INSTANCE.setGroup(group.getGroupcode(),groupInfo);
		}
	}
}
