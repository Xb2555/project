package com.qg24.dao;

import com.qg24.po.dto.CancelUserMonitorPermissionDTO;
import com.qg24.po.dto.PublishProjectDTO;
import com.qg24.po.dto.UpdateProjectDTO;
import com.qg24.po.dto.UpdateProjectInfoDTO;
import com.qg24.po.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProjectMapper {

    // 为用户查询未被冻结的所有项目
    List<Project> selectAllProjectForUser(@Param("projectName") String projectName);

    // 为用户分页查询未被冻结的所有项目
    List<Project> pageQueryAllProjectForUser(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("projectName") String projectName);

    // 为用户分页查询未被冻结的
    int selectTotalNumberProjectForUser(@Param("projectName") String projectName);

    // 向用户申请项目表插入一条要上传新项目的数据
    int addUserApplicationProject(PublishProjectDTO publishProjectDTO);

    // 根据项目名称查找项目
    Project selectProjectByProjectName(String projectName);

    // 根据项目id查找项目信息
    Project selectProjectByProjectId(int projectId);

    // 向用户申请项目监测权限表插入一条数据
    int addApplyMonitorProject(@Param("userId") int userId, @Param("projectId")int projectId, @Param("masterId")int masterId);

    // 通过申请人id，项目id查找用户申请监测权限表中是否存在待办的申请
    ApplyMonitorProject selectMonitorApplicationByProjectIdAndApplicantIdInPending(@Param("userId")int userId, @Param("projectId")int projectId);

    // 通过项目id查找待处理的用户申请项目表且是更新项目的数据
    UserApplicationProject selectApplicationUpdateProjectByProjectIdInPending(UpdateProjectDTO updateProjectDTO);

    // 增添用户更新项目的申请数据
    int addUserApplicationUpdateProject(UpdateProjectInfoDTO updateProjectInfoDTO);

    // 根据项目id查找用户项目监测权限关系表
    List<UserMonitorProject> selectUserMonitorProjectByProjectId(int projectId);

    // 根据项目id和用户id 撤销用户的项目监测权限
    int deleteUserMonitorProjectByProjectIdAndUserId(CancelUserMonitorPermissionDTO cancelUserMonitorPermissionDTO);

    // 根据项目id删除项目数据
    int deleteProjectByProjectId(int projectId);

    // 根据项目id和用户名查找项目信息
    Project selectProjectByProjectIdAndUsername(@Param("projectId") int projectId, @Param("username") String username);

    UserMonitorProject selectUserMonitorProjectByProjectIdAndUserId(@Param("projectId") int projectId, @Param("userId") int userId);

    int insertUserMonitorProject(ApplyMonitorProject applyMonitorProject);

    // 发布者更新报警阈值
    int updateWarningRate(@Param("projectId")int projectId, @Param("warningRate") double warningRate);

    // 更新报警数，报警率
    int updateErrorRateAndNum(@Param("errorNum")int errorNum,@Param("errorRate")double errorRate,@Param("projectId")int projectId);

    // 获取所有项目数据
    ProjectPresentationData getProjectData(@Param("projectId")int projectId);

    // 更新后台报错数
    int updateBackendErr(@Param("errorNum")int errorNum,@Param("projectId")int projectId);

    // 更新前端报错数
    int updateFrontErr(@Param("errorNum")int errorNum,@Param("projectId")int projectId);

    // 更新移动报错数
    int updateMobileErr(@Param("errorNum")int errorNum, @Param("projectId")int projectId);

    //向项目数据表插入一条新的数据
    int insertProjectData(@Param("projectId")int projectId);

    // 更新项目访问数
    @Update("update project_presentation_data set visits = visits + #{accessCount}, total_visits = total_visits + #{accessCount} where project_id = #{projectId} and date = (" +
            "select MAX(date) from project_presentation_data where project_id = #{projectId})")
    void updateProjectAccessCount(@Param("accessCount")int accessCount, @Param("projectId")int projectId);

    //通过用户名判断是否为发布者
    List<Project> selectProjectByUserId(@Param("userName")String userName);

    //获取最新项目数据
    ProjectPresentationData getNewProjectData(@Param("projectId")int projectId);
}
