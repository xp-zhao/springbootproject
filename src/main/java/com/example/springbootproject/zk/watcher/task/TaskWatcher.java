package com.example.springbootproject.zk.watcher.task;

import com.alibaba.fastjson.JSON;
import com.example.springbootproject.model.TaskInfo;
import com.example.springbootproject.utils.ZookeeperServer;
import com.example.springbootproject.zk.storage.TaskStorage;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xp-zhao on 2018/12/19.
 */
public class TaskWatcher implements Watcher
{
	private static final Logger logger = LoggerFactory.getLogger(TaskWatcher.class);

	private ZookeeperServer zkServer;

	public TaskWatcher(ZookeeperServer zkServer){
		this.zkServer = zkServer;
	}

	@Override
	public void process(WatchedEvent event)
	{
		try
		{
			if(event.getType().equals(Event.EventType.NodeDataChanged)){
				// 获取数据重新监听
				String str = zkServer.getData(event.getPath() , this , null);
				TaskInfo taskInfo = JSON.parseObject(str , TaskInfo.class);
				logger.info("任务节点数据变化："+ taskInfo);
			}else if(event.getType() == Event.EventType.NodeDeleted){
				logger.info("任务节点被删除："+event.getPath());
				TaskStorage.INSTANCE.del(event.getPath());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
