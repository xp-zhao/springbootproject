package com.example.springbootproject.zk.watcher.task;

import com.alibaba.fastjson.JSON;
import com.example.springbootproject.model.TaskInfo;
import com.example.springbootproject.utils.ZookeeperServer;
import com.example.springbootproject.zk.storage.TaskGroupStorage;
import com.example.springbootproject.zk.storage.TaskStorage;
import com.example.springbootproject.zk.watcher.task.model.GroupInfo;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xp-zhao on 2018/12/20.
 */
public class GroupWatcher implements Watcher
{
	private static final Logger logger = LoggerFactory.getLogger(GroupWatcher.class);

	private ZookeeperServer zkServer;

	public GroupWatcher(ZookeeperServer zkServer){
		this.zkServer = zkServer;
	}

	@Override
	public void process(WatchedEvent event)
	{
		if(event.getState() == Event.KeeperState.SyncConnected){
			if(event.getType() == Event.EventType.NodeChildrenChanged){
				try
				{
					// 获取当前任务组下的任务
					List<String> tasks = zkServer.getChildren(event.getPath(),this);
					String groupCode = event.getPath().substring(event.getPath().lastIndexOf("/")+1);
					GroupInfo groupInfo = TaskGroupStorage.INSTANCE.getGroup(groupCode);
					List<String> taskList = groupInfo.getTaksCode();
					List<String> delTasks = new ArrayList<>();
					for(String s : taskList)
					{
						if(!tasks.contains(s)){
							delTasks.add(s);
						}
					}
					taskList.removeAll(delTasks);
					tasks.removeAll(taskList);
					for(String task : tasks)
					{
						String taskData = zkServer.getData(
							TaskConstants.TASK_ROOTPATH + "/" + groupInfo.getGroupCode() +
								"/" + task , new TaskWatcher(zkServer) , null);
						TaskInfo taskInfo = JSON.parseObject(taskData , TaskInfo.class);
						TaskStorage.INSTANCE.set(groupCode+"|"+task,taskInfo);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
