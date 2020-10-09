package com.itlife.heavyheart.project.controller;

import com.itlife.heavyheart.project.model.TreeNode;
import com.itlife.heavyheart.project.service.AddressService;
import com.itlife.heavyheart.web.result.ResultBean;
import com.itlife.heavyheart.web.result.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author kex
 * @Create 2020/9/17 14:40
 * @Description
 */
@RestController
@RequestMapping(value = "/address")
@Api(value = "导航list")
@Slf4j
public class CityRest {
    @Resource
    AddressService addressService;

    @PostMapping(value = "/treenode")
    @ApiOperation(value = "获取导航list", httpMethod = "POST")
    public ResultBean<List<TreeNode>> getCity(){
        log.info("获取导航");
        List<TreeNode> treeNodeList = addressService.getCity();
        return ResultUtils.success(treeNodeList);
    }
}
