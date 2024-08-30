package com.qg24.dao;

import com.qg24.po.entity.*;
import com.qg24.po.vo.ShowLogNumberOneWeekForGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {
    //拿到项目数据
    ProjectPresentationData getPresentationData(@Param("projectId")int projectId);
    //查询总攻击记录
    int getTotalAttackRecord();

    //分页查询攻击记录
    List<BannedIp> getBannedIps(@Param("page") int page,@Param("pageSize") int pageSize);

    //查询总用户操作记录
    int getTotalUserOperateRecord();

    //分页查询用户操作记录
    List<UserOperateLog> getUserOperateLogs(@Param("page") int page,@Param("pageSize") int pageSize);

    //查询总后台异常项目日志数
    int getTotalBackendExceptionLogs(@Param("projectId")int projectId);


    //查询总后台访问日志
    int getTotalBackendRequestNum(@Param("projectId")int projectId);

    //分页查询后台请求日志
    List<BackendRequestInfo> getBackendRequestInfos(@Param("projectId") int projectId,@Param("page") int page,@Param("pageSize") int pageSize);

    //分页查询后台项目异常日志
    List<BackendExceptionInfo> getBackendExceptionInfos(@Param("projectId") int projectId,@Param("page") int page,@Param("pageSize") int pageSize);

    //查询前端总异常日志数
    int getFrontExceptionLogsNum(@Param("projectId")int projectId);

    //分页查询前端项目异常日志
    List<FrontExceptionLog> getFrontExceptionInfos(@Param("projectId") int projectId,@Param("page") int page,@Param("pageSize") int pageSize);

    //查询总前端性能日志
    int getTotalFrontPerformanceNum(@Param("projectId")int projectId);

    //分页查询前端性能日志
    List<FrontPerformanceLog> getFrontPerformanceLogs(@Param("projectId") int projectId,@Param("page") int page,@Param("pageSize") int pageSize);

    //查询移动总性能日志数
    int getMobilePerformanceNum(@Param("projectId")int projectId);

    //分页查询移动性能日志
    List<MobilePerformanceLog> getMobilePerformanceLogs(@Param("projectId") int projectId,@Param("page") int page,@Param("pageSize") int pageSize);

    //查询移动总异常日志数
    int getMobileExceptionNum(@Param("projectId")int projectId);

    //分页查询移动异常日志
    List<MobileExceptionLog> getMobileExceptionLogs(@Param("projectId") int projectId,@Param("page") int page,@Param("pageSize") int pageSize);

    //查询项目从今天开始的前7天数据和报错数
    List<ProjectPresentationData> getOneWeekData(@Param("projectId")int projectId);

    //查询项目操作日志
    List<ProjectOperateLog> getProjectOperateLogs(@Param("projectId")int projectId);

    //增加项目访问量
    int updateVisits(@Param("visits")int visits,@Param("totalVisits")int totalVisits,@Param("projectId")int projectId);

    //查询后台自定义日志总数
    int getTotalBackendLogInfo(@Param("projectId")int projectId);

    //分页查询后台自定义日志
    List<BackendLogInfo> getBackendLogInfos(@Param("projectId") int projectId,@Param("page") int page,@Param("pageSize") int pageSize);

    BackendExceptionInfo selectBackendExceptionInfoByLogId(int logId);

    BackendRequestInfo selectBackendRequestInfoByLogId(int logId);

    BackendLogInfo selectBackendLogInfoByLogId(int logId);

    FrontExceptionLog selectFrontExceptionLogByLogId(int logId);

    FrontPerformanceLog selectFrontPerformanceLogByLogId(int logId);

    MobileExceptionLog selectMobileExceptionLogByLogId(int logId);

    MobilePerformanceLog selectMobilePerformanceLogByLogId(int logId);

    List<ShowLogNumberOneWeekForGroupVO> selectBackendExceptionInfoCountByProjectId(int projectId);

    List<ShowLogNumberOneWeekForGroupVO> selectBackendRequestInfoCountByProjectId(int projectId);

    List<ShowLogNumberOneWeekForGroupVO> selectBackendLogInfoCountByProjectId(int projectId);

    List<ShowLogNumberOneWeekForGroupVO> selectFrontExceptionLogCountByProjectId(int projectId);

    List<ShowLogNumberOneWeekForGroupVO> selectFrontPerformanceLogCountByProjectId(int projectId);

    List<ShowLogNumberOneWeekForGroupVO> selectMobileExceptionLogCountByProjectId(int projectId);

    List<ShowLogNumberOneWeekForGroupVO> selectMobilePerformanceLogCountByProjectId(int projectId);

    //向用户操作日志中插入数据
    int insertUserOperateLog(@Param("userId")int userId,@Param("operateType")String operateType);

    //查询项目的前端日志性能
    List<FrontPerformanceLog> selectFrontPerformanceLogByUserId(@Param("projectId") int projectId);

    @Select("select project_id from project")
    List<Integer> getProjectIds();

    void insertProjectPresentationData(@Param("projectIds") List<Integer> projectIds);
}
