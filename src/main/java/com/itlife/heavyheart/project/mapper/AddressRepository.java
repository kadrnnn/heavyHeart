package com.itlife.heavyheart.project.mapper;

import com.itlife.heavyheart.project.model.Address;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author kex
 * @Create 2020/9/17 15:09
 * @Description
 */
@Mapper
@Repository
public interface AddressRepository  {

    List<Address> selectAll();
}
