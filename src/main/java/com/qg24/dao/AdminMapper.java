package com.qg24.dao;

import com.qg24.po.entity.Project;
import com.qg24.po.entity.User;
import com.qg24.po.entity.UserApplicationProject;
import com.qg24.po.entity.UserMonitorProject;
import com.qg24.po.vo.ProjectDetailedInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    //管理员对于项目发布、更新项目的审核
    int updateUserApplicationProject(UserApplicationProject userApplicationProject);

    //更新项目申请记录的id
    int updateApplicationProjectId(@Param("userApplicationProjectId")int userApplicationProjectId,@Param("projectId")int projectId);

    //查看用户详情
    User selectUserByUserId(int userId);

    //冻结用户
    int freezeUser(User user);

    //冻结项目
    int freezeProject(Project project);

    //不分页管理查看已发布的项目，（未冻结）
    List<ProjectDetailedInfoVO> queryPublishedProjectUnFroze();

    //分页管理查看已发布的项目，（未冻结）
    List<ProjectDetailedInfoVO> pagedQueryPublishedProjectUnFroze(@Param("page") int page, @Param("pageSize") int pageSize, @Param("keyWord") String keyWord);

    //查询项目总条数是是未冻结的
    int totalCountOnPublishedProjectUnFroze(@Param("keyWord") String keyWord);

    //不分页管理查看已发布的项目，（冻结）
    List<ProjectDetailedInfoVO> queryPublishedProjectFroze();

    //分页管理查看已发布的项目，（冻结）
    List<ProjectDetailedInfoVO> pagedQueryPublishedProjectFroze(@Param("page") int page, @Param("pageSize") int pageSize);

    //查询项目总条数是是冻结的
    int totalCountOnPublishedProjectFroze();

    //查询项目的申请情况(待审核/被拒绝)
    List<UserApplicationProject> pagedQueryProjectApplication(@Param("page") int page, @Param("pageSize") int pageSize, @Param("status") int status,@Param("keyWord") String keyWord);

    //查询所有项目申请情况
    List<UserApplicationProject> allQueryProjectApplication(@Param("status") int status);

    //通过用户ID查询用户名
    String selectUserNameByUserId(int userId);


    //查询项目申请总条数是待定状态或者是通过/拒绝的
    int totalCountOnApplicationProject(@Param("status") int status , @Param("keyWord") String keyWord);

    //根据申请ID查找申请
    UserApplicationProject selectByApplicationId(int userApplicationProjectId);

    //发布新的项目
    int addProject(Project project);

    //根据项目ID更新项目
    int updateProject(Project project);

    //添加用户权限表关联
    int addUserMonitorProject(UserMonitorProject userMonitorProject);

    //根据项目名查找项目ID
    int selectProjectIdByProjectName(String projectName);

    //更新项目操作日志
    int insertProjectOperateLog(@Param("description")String description, @Param("operateType")String operateType, @Param("projectId")int projectId);

}
