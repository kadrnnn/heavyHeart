package com.itlife.heavyheart.project.service.impl;

import com.itlife.heavyheart.exception.BusinessException;
import com.itlife.heavyheart.project.model.Member;
import com.itlife.heavyheart.project.model.UserInfo;
import com.itlife.heavyheart.project.mapper.AdminRepository;
import com.itlife.heavyheart.project.service.AdminService;
import com.itlife.heavyheart.security.User;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author kex
 * @Create 2020/9/2 16:54
 * @Description
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Resource
    AdminRepository adminRepository;

    @Override
    public User login(String username, String password) {
        User user = adminRepository.login(username, password);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public Integer setup(UserInfo userInfo, User user) {
        //int setup = adminRepository.setup(UUID.randomUUID().toString(), username, password);
        int setup = adminRepository.saveUserInfo(UUID.randomUUID().toString().replaceAll("-", "").trim(), userInfo);
        int stup = adminRepository.saveUser(user);
        if (setup > 0 && stup > 0) {
            return setup;
        } else {
            throw new BusinessException("9", "未注册成功");
        }
    }

    @Override
    public Integer saveMember(List<Map<String, Object>> list) {
        int result = 0;
        for (Map<String, Object> map : list) {
            Member member = new Member();
            member.setId(UUID.randomUUID().toString().replace("-", "").trim());
            member.setName(String.valueOf(map.get("sqrxm")));
            member.setIdCard(String.valueOf(map.get("sqrzjhm")));
            result = result + adminRepository.saveMember(member);
        }
        if (result > 0) {
            return result;
        } else {
            throw new BusinessException("9", "未保存成功");
        }
    }

    @Override
    public List<Member> searchMember(String idCard) {
        return adminRepository.searchMember();
    }

    @Override
    public String checkUser(String username, String password) {
        StringBuffer string = new StringBuffer();
        User result = adminRepository.findUser(username, password);
        if (result != null) {
            string.append("已注册");
        } else {
            string.append("注册成功");
        }
        return string.toString();
    }

    @Override
    public User userinfo(String username) {
        User result = adminRepository.findUsername(username);
        return result;
    }
}
