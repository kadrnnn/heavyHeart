package com.itlife.heavyheart.project.service.impl;

import com.google.common.collect.Lists;
import com.itlife.heavyheart.project.model.Address;
import com.itlife.heavyheart.project.model.TreeNode;
import com.itlife.heavyheart.project.mapper.AddressRepository;
import com.itlife.heavyheart.project.service.AddressService;
import com.itlife.heavyheart.utils.RecursionTreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author kex
 * @Create 2020/9/17 15:06
 * @Description
 */
@Service
@Slf4j
public class AddressServiceImpl implements AddressService {
    @Resource
    AddressRepository addressRepository;

    @Override
    //@Cacheable(cacheNames = "address-cache")
    public List<TreeNode> getCity() {
        List<Address> addresses = addressRepository.selectAll();
        List<TreeNode> treeNodeList = buildGroupTree(addresses);
        log.info("treeNodeList={}", treeNodeList);
        return treeNodeList;
    }

    private List<TreeNode> buildGroupTree(List<Address> addresses) {
        List<TreeNode> list = Lists.newArrayList();
        TreeNode node;
        for (Address address : addresses) {
            node = new TreeNode();
            node.setId(address.getId());
            node.setPid(address.getPid());
            node.setNodeCode(address.getAdCode());
            node.setNodeName(address.getName());
            list.add(node);
        }
        return RecursionTreeUtil.getChildTreeNodes(list,368100107951677440L);
    }
}
