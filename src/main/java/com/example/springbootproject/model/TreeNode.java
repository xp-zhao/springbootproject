package com.example.springbootproject.model;

import lombok.Data;

@Data
public class TreeNode {
    private String id;
    private String parent;
    private String name;
    private String email;
    private String desc;

    public TreeNode(String id, String parent){
        this.id = id;
        this.parent = parent;
    }
}
