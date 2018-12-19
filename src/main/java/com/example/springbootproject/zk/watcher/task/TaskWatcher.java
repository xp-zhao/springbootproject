package com.example.springbootproject.zk.watcher.task;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xp-zhao on 2018/12/19.
 */
@Service
public class TaskWatcher implements Watcher
{
	@Autowired
	private ZooKeeper zk;

	@Override
	public void process(WatchedEvent event)
	{
		try
		{
			if(event.getType().equals(Event.EventType.NodeDataChanged)){
				// 获取数据重新监听
				zk.getData(event.getPath() , this , null);
			}
		}
		catch (KeeperException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
