package com.example.springbootproject.controller;

import com.example.springbootproject.model.TreeNode;
import com.example.springbootproject.model.resp.BaseResponse;
import com.example.springbootproject.model.resp.ReturnInfo;
import com.example.springbootproject.utils.TreeNodeUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("tree")
public class TreeController {

    @RequestMapping("/getList")
    public BaseResponse getList(){
        return new BaseResponse(ReturnInfo.SUCCESS, init());
    }

    @RequestMapping("/getTree")
    public BaseResponse getTree() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        BaseResponse baseResponse = new BaseResponse(ReturnInfo.SUCCESS, new TreeNodeUtil<TreeNode,String>().generateTree(init(), "id", "parent"));
        return baseResponse;
    }

    private List<TreeNode> init(){
        List<TreeNode> list = new ArrayList<>();
        list.add(new TreeNode("1", null));
        list.add(new TreeNode("2", "1"));
        list.add(new TreeNode("3", "1"));
        list.add(new TreeNode("4", null));
        list.add(new TreeNode("5", "4"));
        list.add(new TreeNode("6", "5"));
        list.add(new TreeNode("7", "4"));
        list.add(new TreeNode("8", "4"));
        return list;
    }

}
