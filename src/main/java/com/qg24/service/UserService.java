package com.qg24.service;

import com.qg24.po.dto.RegisterDTO;
import com.qg24.po.dto.VerifyApplicationDTO;
import com.qg24.po.entity.User;
import com.qg24.po.result.PageBean;
import com.qg24.po.vo.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService {

    //注册新用户
    UserAndTokenVO addUser(RegisterDTO registerDTO);

    User findUserByUsername(String username);

    //展示用户自己发布的项目
    PageBean<ShowSelfProjectsVO> showUserSelfProjects(int userId, int page, int pageSize);

    //查询用户拥有监控权限的项目
    PageBean<ShowHaveMonitorPermissionProjectsVO> showHaveMonitorPermissionProjects(int userId, int page, int pageSize);

    //查看用户的申请（申请监控权限的）
    PageBean<MyApplicationOnMonitorProjectVO> myApplicationOnMonitorProject(int userId, int page, int pageSize);

    //查看用户发布或更新项目申请
    PageBean<MyApplicationProjectVO> myApplicationProject(int userId, int page, int pageSize);

    //查看我收到的申请
    PageBean<ReceivedMonitorApplicationVO> receivedMonitorApplication(int userId, int page, int pageSize);

    int verifyMonitorApplication(VerifyApplicationDTO verifyApplicationDTO);
}
