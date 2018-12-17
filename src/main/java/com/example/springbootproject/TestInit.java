package com.example.springbootproject;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by xp-zhao on 2018/12/17.
 */
@Component
public class TestInit implements ApplicationRunner
{
	@Autowired
	private ZooKeeper zk;

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		String stat = zk.create("/controller", "hellozk".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("created path is -->: " + stat);
	}
}
