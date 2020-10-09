package com.itlife.heavyheart.utils;

import com.itlife.heavyheart.project.model.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author kex
 * @Create 2020/9/17 15:16
 * @Description
 */
public class RecursionTreeUtil {
    public static List<TreeNode> getChildTreeNodes(List<TreeNode> list, long parentId) {
        List<TreeNode> treeNodes = new ArrayList<>();
        for (TreeNode node : list) {
            if (node.getPid() == null) {
                continue;
            }

            if (Objects.equals(node.getPid(), parentId)) {
                recursionFn(list, node);
                treeNodes.add(node);
            }
        }
        return treeNodes;
    }

    /**
     * 递归列表
     */
    private static void recursionFn(List<TreeNode> list, TreeNode node) {
        List<TreeNode> childList = getChildList(list, node);
        if (GeneralUtil.isEmpty(childList)) {
            return;
        }
        node.setChildren(childList);
        for (TreeNode tChild : childList) {
            recursionFn(list, tChild);
        }
    }

    /**
     * 得到子节点列表
     */
    private static List<TreeNode> getChildList(List<TreeNode> list, TreeNode t) {
        List<TreeNode> tList = new ArrayList<>();

        for (TreeNode treeNode : list) {
            if (GeneralUtil.isEmpty(treeNode.getPid())) {
                continue;
            }
            if (Objects.equals(treeNode.getPid(), t.getId())) {
                tList.add(treeNode);
            }
        }
        return tList;
    }
}
