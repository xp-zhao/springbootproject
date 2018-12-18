package com.example.springbootproject.zk.watcher;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by xp-zhao on 2018/12/18.
 */
public class Master implements Watcher
{
	ZooKeeper zk;
	String host;
	String serverId = Integer.toHexString(new Random().nextInt());
	static boolean isLeader = false;

	public Master(String host){
		this.host = host;
	}

	boolean checkMaster(){
		while(true){
			try
			{
				Stat stat = new Stat();
				byte[] data = zk.getData("/master" , false , stat);
				isLeader = new String(data).equals(serverId);
				return true;
			}catch (KeeperException.NoNodeException e){
				return false;
			}catch (KeeperException.ConnectionLossException e){

			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			catch (KeeperException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void runForMater() throws KeeperException, InterruptedException
	{
		while(true){
			try
			{
				zk.create("/master" , serverId.getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);
				isLeader = true;
				break;
			}
			catch (KeeperException.NodeExistsException e)
			{
				e.printStackTrace();
			}
			catch (KeeperException.ConnectionLossException e)
			{
				e.printStackTrace();
			}
			if(checkMaster()){
				break;
			}
		}
	}

	public void startZK() throws IOException
	{
		zk = new ZooKeeper(host , 15000 , this);
	}

	public void stopZK() throws InterruptedException
	{
		zk.close();
	}

	@Override
	public void process(WatchedEvent event)
	{
		System.out.println(event);
	}

	public static void main(String[] args) throws Exception
	{
		Master master = new Master("47.98.49.140:2181,47.98.49.140:2182,47.98.49.140:2183");
		master.startZK();
		master.runForMater();
		if(isLeader){
			System.out.println("I'm the leader");
			TimeUnit.SECONDS.sleep(6);
		}else{
			System.out.println("someone else is the leader");
		}
		master.stopZK();
	}
}
