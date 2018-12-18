package com.example.springbootproject.zk.watcher;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Created by xp-zhao on 2018/12/18.
 */
public class Client implements Watcher
{

	ZooKeeper zk;
	String    host;

	public Client(String host){
		this.host = host;
	}

	public void startZK() throws IOException
	{
		zk = new ZooKeeper(host , 15000 , this);
	}

	public String queueCommand(String command) throws Exception
	{
		String name = null;
		while(true){
			try
			{
				name = zk
					.create("/tasks/task-" , command.getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE ,
						CreateMode.PERSISTENT_SEQUENTIAL);
				return name;
			}catch (KeeperException.NodeExistsException e){
				throw new Exception(name + " already appears to be running");
			}catch (KeeperException.ConnectionLossException e){

			}
		}
	}

	@Override
	public void process(WatchedEvent event)
	{
		System.out.println(event);
	}

	public static void main(String[] args) throws Exception
	{
		Client client = new Client("47.98.49.140:2181,47.98.49.140:2182,47.98.49.140:2183");
		client.startZK();
		String name = client.queueCommand("test");
		System.out.println("Created "+ name);
	}
}
