package com.example.springbootproject.zk.watcher.task;

import com.example.springbootproject.utils.ZookeeperServer;
import com.example.springbootproject.zk.storage.TaskGroupStorage;
import com.example.springbootproject.zk.watcher.task.model.GroupInfo;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xp-zhao on 2018/12/19.
 */
public class RootWatcher implements Watcher
{
	private static final Logger logger = LoggerFactory.getLogger(RootWatcher.class);

	private ZookeeperServer zkServer;

	public RootWatcher(ZookeeperServer zkServer){
		this.zkServer = zkServer;
	}

	@Override
	public void process(WatchedEvent event)
	{
		try
		{
			if(event.getState() == Event.KeeperState.SyncConnected){
				if(event.getType() == Event.EventType.NodeCreated){
					logger.info("Node( " + event.getPath() + " ) Created");
					zkServer.exists(event.getPath() , this);
				}else if(event.getType() == Event.EventType.NodeDeleted){
					logger.info("Node( " + event.getPath() + " ) Deleted");
					zkServer.exists(event.getPath() , this);
				}else if(event.getType() == Event.EventType.NodeDataChanged){
					logger.info("Node( " + event.getPath() + " ) DataChanged");
					zkServer.getChildren(event.getPath() , this);
				}else if(event.getType() == Event.EventType.NodeChildrenChanged){
					logger.info("Node( " + event.getPath() + " ) NodeChildrenChanged");
					List<String> groups = zkServer.getChildren(TaskConstants.TASK_ROOTPATH , this);
					logger.info("当前所有任务组："+groups);
					watcherChilden(groups);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void watcherChilden(List<String> nodes) throws KeeperException, InterruptedException
	{
		List<String> normalGroup = new ArrayList<>();
		List<String> delGroup = new ArrayList<>();
		Map<String,GroupInfo> groupInfoMap = TaskGroupStorage.INSTANCE.getGroups();
		for(Map.Entry<String,GroupInfo> entry : groupInfoMap.entrySet()){
			if(!nodes.contains(entry.getKey())){
				// 保存已经被删除的任务组
				delGroup.add(entry.getKey());
				TaskGroupStorage.INSTANCE.delGroup(entry.getKey());
			}else{
				// 保存没有变化的任务组
				normalGroup.add(entry.getKey());
			}
		}
		// 移除正常的，剩下新增的任务组
		nodes.removeAll(normalGroup);
		// 保存新增的任务组，并设置监听
		for(String node : nodes)
		{
			GroupInfo groupInfo = new GroupInfo();
			groupInfo.setGroupCode(node);
			zkServer.getChildren(TaskConstants.TASK_ROOTPATH+"/"+node,new GroupWatcher(zkServer));
			TaskGroupStorage.INSTANCE.setGroup(node,groupInfo);
		}
	}
}
