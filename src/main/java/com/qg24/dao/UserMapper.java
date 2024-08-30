package com.qg24.dao;

import com.qg24.po.entity.ApplyMonitorProject;
import com.qg24.po.entity.Project;
import com.qg24.po.entity.User;
import com.qg24.po.entity.UserApplicationProject;
import com.qg24.po.vo.MyApplicationOnMonitorProjectTempVO;
import com.qg24.po.vo.ReceivedMonitorApplicationTempVO;
import com.qg24.po.vo.ShowSelfProjectsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    //添加用户
    int addUser(User user);


    //根据用户名查询用户
    User selectUserByUsername(String username);

    //冻结用户
    int updateEnabled(User user);

    //查询用户自己发布的项目
    List<ShowSelfProjectsVO> selectProjectsByUserId(int userId);

    //分页查询用户自己发布的项目
    List<ShowSelfProjectsVO> pagedSelectProjectsByUserId(@Param("userId") int userId,@Param("page") int page, @Param("pageSize") int pageSize);

    //查询用户自己发布的项目的总条数
    int selectProjectsByUserIdCount(int userId);

    //展示用户拥有监控权限的项目
    List<Project> selectHaveMonitorPermissionProjectsByUserId(int userId);

    //分页展示用户拥有监控权限的项目
    List<Project> pagedSelectHaveMonitorPermissionProjectsByUserId(@Param("userId") int userId,@Param("page") int page, @Param("pageSize") int pageSize);

    //查询展示用户拥有监控权限的项目总条数
    int selectHaveMonitorPermissionProjectsByUserIdCount(int userId);

    //查看用户发出的申请（申请监控权限）
    List<MyApplicationOnMonitorProjectTempVO> selectUserApplicationByUserId(int userId);

    //分页查看用户发出的申请（申请监控权限）
    List<MyApplicationOnMonitorProjectTempVO> pagedSelectUserApplicationByUserId(@Param("userId") int userId,@Param("page") int page, @Param("pageSize") int pageSize);

    //查询用户发出的申请（申请监控权限）总条数
    int selectUserApplicationByUserIdCount(int userId);


    //通过用户ID查询用户名
    String selectUsernameByUserId(int userId);

    //查看用户发布或更新项目的申请
    List<UserApplicationProject> selectProjectApplicationByUserId(int userId);

    //分页查看用户发布或更新项目的申请
    List<UserApplicationProject> pagedSelectProjectApplicationByUserId(@Param("userId") int userId,@Param("page") int page, @Param("pageSize") int pageSize);

    //查看用户发布或更新项目的申请总条数
    int selectProjectApplicationByUserIdCount(int userId);


    //查看我收到的申请
    List<ReceivedMonitorApplicationTempVO> selectReceivedMonitorApplicationByUserId(int userId);

    //分页查看我收到的申请
    List<ReceivedMonitorApplicationTempVO> pagedSelectReceivedMonitorApplicationByUserId(@Param("userId") int userId,@Param("page") int page, @Param("pageSize") int pageSize);

    //查看我收到的申请总条数
    int selectReceivedMonitorApplicationByUserIdCount(int userId);


    // 根据用户id查找用户信息
    User selectUserByUserId(int userId);

    //查看所有用户
    List<User> selectAllUsers();

    int updateApplyMonitorProject(ApplyMonitorProject applyMonitorProject);

    ApplyMonitorProject selectApplyMonitorProjectByApplicationId(int applyMonitorProjectId);
}
