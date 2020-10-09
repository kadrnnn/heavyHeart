package com.itlife.heavyheart.project.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author kex
 * @Create 2020/9/17 14:53
 * @Description
 */
@Data
public class TreeNode implements Serializable {
    private static final long serialVersionUID = -499010884211394847L;
    /**
     * 节点编码
     */
    private String nodeCode;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * ID
     */
    private Long id;
    /**
     * 父ID
     */
    private Long pid;
    /**
     * 孩子节点信息
     */
    private List<TreeNode> children;
}
