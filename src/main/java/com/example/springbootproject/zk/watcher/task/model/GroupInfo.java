package com.example.springbootproject.zk.watcher.task.model;

import lombok.Data;

import java.util.List;

/**
 * Created by xp-zhao on 2018/12/20.
 */
@Data
public class GroupInfo
{
	private String       groupCode;

	private List<String> taksCode;
}
