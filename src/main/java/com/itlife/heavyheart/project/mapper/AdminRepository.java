package com.itlife.heavyheart.project.mapper;

import com.itlife.heavyheart.project.model.Member;
import com.itlife.heavyheart.project.model.UserInfo;
import com.itlife.heavyheart.security.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author kex
 * @Create 2020/9/2 16:41
 * @Description
 */
@Mapper
@Repository
public interface AdminRepository {

    User login(String username, String password);

    Integer setup(String id, String username, String password);

    Integer saveMember(@Param("member") Member member);

    List<Member> searchMember();

    Integer saveUserInfo(@Param("id") String id, @Param("userInfo") UserInfo userInfo);

    Integer saveUser(@Param("user") User user);

    User findUser(String username, String password);

    User findUsername(String username);
}
