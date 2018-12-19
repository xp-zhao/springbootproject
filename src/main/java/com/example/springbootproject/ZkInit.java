package com.example.springbootproject;

import com.example.springbootproject.utils.ZookeeperServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by xp-zhao on 2018/12/19.
 */
@Component
public class ZkInit implements ApplicationRunner
{
	@Autowired
	private ZookeeperServer zkServer;

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		zkServer.initWatcher();
	}
}
