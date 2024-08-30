package com.qg24.service.impl;

import com.qg24.dao.ProjectMapper;
import com.qg24.dao.UserMapper;
import com.qg24.po.dto.RegisterDTO;
import com.qg24.po.dto.VerifyApplicationDTO;
import com.qg24.po.entity.ApplyMonitorProject;
import com.qg24.po.entity.Project;
import com.qg24.po.entity.User;
import com.qg24.po.entity.UserApplicationProject;
import com.qg24.po.result.PageBean;
import com.qg24.po.vo.*;
import com.qg24.service.UserService;
import com.qg24.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProjectMapper projectMapper;



    /**
     * 注册新用户
     * @param registerDTO
     * @return
     */
    @Override
    public UserAndTokenVO addUser(RegisterDTO registerDTO) {
        //先判断是否已存在该用户
        User oldUser = userMapper.selectUserByUsername(registerDTO.getUsername());
        if (oldUser != null){
            return null; //已存在该账户 ，直接返回kong
        }else {
            //先前未存在，继续注册
            User newUser = new User();
            //对用户密码进行加密
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
            String encode = encoder.encode(registerDTO.getPassword());
            //注册用户
            newUser.setUsername(registerDTO.getUsername());
            newUser.setPassword(encode);
            userMapper.addUser(newUser); //添加新用户

            //获取令牌
            //获取用户ID
            User user = userMapper.selectUserByUsername(newUser.getUsername());

            Map<String, Object> claims = new HashMap<>();
            UserAndTokenVO userAndTokenVO = new UserAndTokenVO();
            claims.put("userId", user.getUserId());
            String token = JwtUtils.generateToken(claims);

            userAndTokenVO.setToken(token);
            userAndTokenVO.setUserId(user.getUserId());
            return userAndTokenVO;
        }
    }

    /**
     * 通过用户名返回用户信息
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
       return userMapper.selectUserByUsername(username);
    }

    /**
     * 展示用户自己发布的项目
     * @param userId
     * @return
     */
    @Override
    public PageBean<ShowSelfProjectsVO> showUserSelfProjects(int userId, int page, int pageSize) {
        List<ShowSelfProjectsVO> showSelfProjectsVOS = new ArrayList<>();
        //判断PageSize是否为0,为0直接查询全部
        if (pageSize == 0){
            //直接查询全部
            showSelfProjectsVOS = userMapper.selectProjectsByUserId(userId);
            //判断有无发布过项目
        }else {
            //分页查询
            page = (page - 1) * pageSize;
            showSelfProjectsVOS = userMapper.pagedSelectProjectsByUserId(userId, page, pageSize);
            //判断有无发布过项目
        }
        int count = userMapper.selectProjectsByUserIdCount(userId);
        //返回数据
        if(count == 0){
            return null; //没有发布过项目
        }else {
            PageBean<ShowSelfProjectsVO> pageBean = new PageBean<>();
            pageBean.setTotal((long) count);
            pageBean.setData(showSelfProjectsVOS);
            return pageBean; //返回数据
        }

    }

    /**
     * 展示用户拥有监控权限的项目
     * @param userId
     * @return
     */
    @Override
    public PageBean<ShowHaveMonitorPermissionProjectsVO> showHaveMonitorPermissionProjects(int userId, int page, int pageSize) {
        PageBean<ShowHaveMonitorPermissionProjectsVO> pageBean = new PageBean<>();
        List<Project> projects = new ArrayList<>();
        //判断PageSize是否为0,为0直接查询全部
        if (pageSize == 0){
            projects = userMapper.selectHaveMonitorPermissionProjectsByUserId(userId);
        }else {
            //分页查询
            page = (page - 1) * pageSize;
            projects = userMapper.pagedSelectHaveMonitorPermissionProjectsByUserId(userId, page, pageSize);
        }
        //查询总数
        int count = userMapper.selectHaveMonitorPermissionProjectsByUserIdCount(userId);
        if (count == 0){
            return null; //没有可以监控的权限
        }else {
            List<ShowHaveMonitorPermissionProjectsVO> list = new ArrayList<>();
            for (Project project : projects) {
                //创建需要返回的结果
                ShowHaveMonitorPermissionProjectsVO showHaveMonitorPermissionProjectsVO = new ShowHaveMonitorPermissionProjectsVO();
                BeanUtils.copyProperties(project, showHaveMonitorPermissionProjectsVO);
                showHaveMonitorPermissionProjectsVO.setCreator(project.getUsername());
                list.add(showHaveMonitorPermissionProjectsVO);
            }
            pageBean.setTotal((long) count);
            pageBean.setData(list);
            return pageBean; //返回结果
        }
    }

    /**
     * 查看用户的申请（申请监控权限）
     * @param userId
     * @return
     */
    @Override
    public PageBean<MyApplicationOnMonitorProjectVO> myApplicationOnMonitorProject(int userId, int page, int pageSize) {
        PageBean<MyApplicationOnMonitorProjectVO> pageBean = new PageBean<>();
        List<MyApplicationOnMonitorProjectTempVO> myApplicationOnMonitorProjectTempVOS = new ArrayList<>();
        //判断PageSize是否为0,为0直接查询全部
        if (pageSize == 0){
            myApplicationOnMonitorProjectTempVOS = userMapper.selectUserApplicationByUserId(userId);
        }
        else {
            page = (page - 1) * pageSize;
            myApplicationOnMonitorProjectTempVOS = userMapper.pagedSelectUserApplicationByUserId(userId, page, pageSize);
        }
        int count = userMapper.selectUserApplicationByUserIdCount(userId);
        if (count == 0){
            return null; //没有发出过申请（申请监控权限）
        }else {
            List<MyApplicationOnMonitorProjectVO> myApplicationOnMonitorProjectVOS = new ArrayList<>();
            for (MyApplicationOnMonitorProjectTempVO myApplicationOnMonitorProjectTempVO : myApplicationOnMonitorProjectTempVOS) {
                //传递数据
                MyApplicationOnMonitorProjectVO myApplicationOnMonitorProjectVO = new MyApplicationOnMonitorProjectVO();
                BeanUtils.copyProperties(myApplicationOnMonitorProjectTempVO,myApplicationOnMonitorProjectVO);
                //获取开发者的用户名
                myApplicationOnMonitorProjectVO.setCreator(userMapper.selectUsernameByUserId(myApplicationOnMonitorProjectTempVO.getMasterId()));
                myApplicationOnMonitorProjectVOS.add(myApplicationOnMonitorProjectVO);
            }
            //返回结果
            pageBean.setData(myApplicationOnMonitorProjectVOS);
            pageBean.setTotal((long) count);
            return pageBean;
        }
    }

    /**
     * 查看用户发布或更新项目申请
     * @param userId
     * @return
     */
    @Override
    public PageBean<MyApplicationProjectVO> myApplicationProject(int userId, int page, int pageSize) {
        PageBean<MyApplicationProjectVO> pageBean = new PageBean<>();
        List<UserApplicationProject> userApplicationProjects = new ArrayList<>();
        //判断PageSize是否为0,为0直接查询全部
        if (pageSize == 0){
            userApplicationProjects = userMapper.selectProjectApplicationByUserId(userId);
        }else {
            //分页查询
            page = (page - 1) * pageSize;
            userApplicationProjects = userMapper.pagedSelectProjectApplicationByUserId(userId, page, pageSize);
        }
        //查询总条数
        int count = userMapper.selectProjectApplicationByUserIdCount(userId);
        if (count == 0){
            return null; //没有申请过
        }else {
            List<MyApplicationProjectVO> myApplicationProjectVOS = new ArrayList<>();
            for (UserApplicationProject userApplicationProject : userApplicationProjects) {
                //回显数据
                MyApplicationProjectVO myApplicationProjectVO = new MyApplicationProjectVO();
                BeanUtils.copyProperties(userApplicationProject, myApplicationProjectVO);
                //判断时哪种申请类型
                if (userApplicationProject.getApplicationType() == 0){
                    myApplicationProjectVO.setApplicationType("上传新软件");
                }else if(userApplicationProject.getApplicationType() == 1){
                    myApplicationProjectVO.setApplicationType("更新软件");
                }
                //判断申请状态
                if (userApplicationProject.getStatus() == 0){
                    myApplicationProjectVO.setApplicationStatus("待定");
                }else if (userApplicationProject.getStatus() == 1){
                    myApplicationProjectVO.setApplicationStatus("通过");
                }else if (userApplicationProject.getStatus() == 2){
                    myApplicationProjectVO.setApplicationStatus("拒绝");
                }
                myApplicationProjectVOS.add(myApplicationProjectVO);
            }
            //返回结果
            pageBean.setTotal((long) count);
            pageBean.setData(myApplicationProjectVOS);
            return pageBean;
        }
    }

    /**
     * 查看我收到的申请
     * @param userId
     * @return
     */
    @Override
    public PageBean<ReceivedMonitorApplicationVO> receivedMonitorApplication(int userId, int page, int pageSize) {
        PageBean<ReceivedMonitorApplicationVO> pageBean = new PageBean<>();
        List<ReceivedMonitorApplicationTempVO> receivedMonitorApplicationTempVOS = new ArrayList<>();
        //判断PageSize是否为0,为0直接查询全部
        if (pageSize == 0){
            receivedMonitorApplicationTempVOS = userMapper.selectReceivedMonitorApplicationByUserId(userId);

        }else {
            //分页查询
            page = (page - 1) * pageSize;
            receivedMonitorApplicationTempVOS = userMapper.pagedSelectReceivedMonitorApplicationByUserId(userId, page, pageSize);
        }
        int count = userMapper.selectReceivedMonitorApplicationByUserIdCount(userId);
        if (count == 0){
            return null; //没有收到别人的申请
        }else {
            List<ReceivedMonitorApplicationVO> list = new ArrayList<>();
            for (ReceivedMonitorApplicationTempVO receivedMonitorApplicationTempVO : receivedMonitorApplicationTempVOS) {
                //获取数据
                ReceivedMonitorApplicationVO receivedMonitorApplicationVO = new ReceivedMonitorApplicationVO();
                BeanUtils.copyProperties(receivedMonitorApplicationTempVO, receivedMonitorApplicationVO);
                //获取申请人的用户名
                receivedMonitorApplicationVO.setApplicant(userMapper.selectUsernameByUserId(receivedMonitorApplicationTempVO.getApplicantId()));
                list.add(receivedMonitorApplicationVO);
            }
            pageBean.setData(list);
            pageBean.setTotal((long) count);
            return pageBean;
        }

    }

    @Override
    public int verifyMonitorApplication(VerifyApplicationDTO verifyApplicationDTO) {

        //将数据包装
        ApplyMonitorProject applyMonitorProject = new ApplyMonitorProject();
        applyMonitorProject.setApplyMonitorProjectId(verifyApplicationDTO.getApplicationId());
        applyMonitorProject.setApplicationStatus(verifyApplicationDTO.getStatus() == 1 ? "通过" : "拒绝");
        applyMonitorProject.setRejectReason(verifyApplicationDTO.getRejectReason());

        //更新申请表
        if (userMapper.updateApplyMonitorProject(applyMonitorProject) == 1){
            //修改成功
            //判断是通过还是拒绝
            if (verifyApplicationDTO.getStatus() == 1){
                //通过,添加用户监测项目关系表
                applyMonitorProject = userMapper.selectApplyMonitorProjectByApplicationId(applyMonitorProject.getApplyMonitorProjectId());
                return projectMapper.insertUserMonitorProject(applyMonitorProject);
            }else {
                //拒绝,无需操作
                return 1;
            }
        }else {
            //修改失败
            return 0;
        }
    }


}
