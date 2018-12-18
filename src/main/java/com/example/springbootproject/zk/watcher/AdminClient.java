package com.example.springbootproject.zk.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Date;

/**
 * Created by xp-zhao on 2018/12/18.
 */
public class AdminClient implements Watcher
{
	ZooKeeper zk;
	String    host;

	public AdminClient(String host){
		this.host = host;
	}

	public void startZK() throws IOException
	{
		zk = new ZooKeeper(host , 15000 , this);
	}

	public void listState() throws KeeperException, InterruptedException
	{
		try{
			Stat stat = new Stat();
			byte[] masterData = zk.getData("/mater",false,stat);
			Date startDate = new Date(stat.getCtime());
			System.out.println("Master: " + new String(masterData) + " since " + startDate);
		}catch (KeeperException.NoNodeException e){
			System.out.println("No Master");
		}
		System.out.println("Workers: ");
		for(String w : zk.getChildren("/workers",false)){
			byte[] data = zk.getData("/workers/" + w , false , null);
			String state = new String(data);
			System.out.println("\t" + w + ": " + state);
		}
		System.out.println("Tasks: ");
		for(String t : zk.getChildren("/assign",false)){
			System.out.println("\t" + t);
		}
	}

	@Override
	public void process(WatchedEvent event)
	{
		System.out.println(event);
	}

	public static void main(String[] args) throws Exception
	{
		AdminClient client = new AdminClient("47.98.49.140:2181,47.98.49.140:2182,47.98.49.140:2183");
		client.startZK();
		client.listState();
	}
}
