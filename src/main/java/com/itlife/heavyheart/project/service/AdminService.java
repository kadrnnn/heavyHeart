package com.itlife.heavyheart.project.service;


import com.itlife.heavyheart.project.model.Member;
import com.itlife.heavyheart.project.model.UserInfo;
import com.itlife.heavyheart.security.User;

import java.util.List;
import java.util.Map;

/**
 * @Author kex
 * @Create 2020/9/2 16:35
 * @Description
 */
public interface AdminService {
    User login(String username, String password);

    Integer setup(UserInfo userInfo, User user);

    Integer saveMember(List<Map<String, Object>> list);

    List<Member> searchMember(String idCard);

    String checkUser(String username, String password);

    User userinfo(String username);
}
