package com.qg24.service.impl;

import com.qg24.dao.LogMapper;
import com.qg24.dao.UserMapper;
import com.qg24.po.dto.ShowDetailedLogDTO;
import com.qg24.po.dto.ShowLogNumberOneWeekForGroupDTO;
import com.qg24.po.entity.*;
import com.qg24.po.result.PageBean;
import com.qg24.po.vo.*;
import com.qg24.service.LogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private UserMapper userMapper;
    //分页查询攻击服务器记录
    @Override
    public PageBean<QueryAttackServerLogVO> selectAllAttackServerLog(int page, int pageSize) {
        PageBean<QueryAttackServerLogVO> pageBean = new PageBean<>();
        //查询总数量
        int count = logMapper.getTotalAttackRecord();
        pageBean.setTotal((long) count);
        page = (page-1) * pageSize;
        //具体回显数据
        List<QueryAttackServerLogVO> list = new ArrayList<>();
        List<BannedIp> ips = new ArrayList<>();
        if(pageSize==0){
            //获取所有信息
            ips = logMapper.getBannedIps(0,count);
        }else{
            //分页查询
            ips = logMapper.getBannedIps(page,pageSize);
        }
        for(BannedIp ip : ips){
            QueryAttackServerLogVO queryAttackServerLogVO = new QueryAttackServerLogVO();
            queryAttackServerLogVO.setIpAddress(ip.getIp());
            queryAttackServerLogVO.setIllegalRequest(ip.getBannedReason());
            queryAttackServerLogVO.setIllegalRequestTIme(ip.getBannedTime());
            list.add(queryAttackServerLogVO);
        }
        pageBean.setData(list);
        return pageBean;
    }

    //分页查询用户操作日志
    @Override
    public PageBean<QueryAllUserOperationLogVO> selectAllUserOperationLog(int page, int pageSize) {
        PageBean<QueryAllUserOperationLogVO> pageBean = new PageBean<>();
        //查询总数量
        int count = logMapper.getTotalUserOperateRecord();
        pageBean.setTotal((long) count);
        page=(page-1)*pageSize;
        //具体回显数据
        List<QueryAllUserOperationLogVO> list = new ArrayList<>();
        List<UserOperateLog> logs =  new ArrayList<>();
        if(pageSize==0){
            //获取所有信息
            logs =  logMapper.getUserOperateLogs(0,count);
        }else{
            //分页查询
            logs =  logMapper.getUserOperateLogs(page,pageSize);
        }
        for(UserOperateLog userOperateLog : logs){
            String username = userMapper.selectUsernameByUserId(userOperateLog.getUserId());
            QueryAllUserOperationLogVO queryAllUserOperationLogVO = new QueryAllUserOperationLogVO();
            queryAllUserOperationLogVO.setUserName(username);
            queryAllUserOperationLogVO.setOperate(userOperateLog.getOperateType());
            queryAllUserOperationLogVO.setOperateTime(userOperateLog.getDate());
            list.add(queryAllUserOperationLogVO);
        }
        pageBean.setData(list);
        return pageBean;
    }

    //分页查询后台异常项目日志
    @Override
    public PageBean<ViewLogVO> selectBackendExceptionLogs(int projectId,int page, int pageSize) {
        PageBean<ViewLogVO> pageBean = new PageBean<>();
        //查询总数量
        int count = logMapper.getTotalBackendExceptionLogs(projectId);
        pageBean.setTotal((long) count);
        page=(page-1)*pageSize;
        //回显数据
        List<ViewLogVO> list = new ArrayList<>();
        List<BackendExceptionInfo> infos = new ArrayList<>();
        if(pageSize==0){
            //获取所有信息
            infos = logMapper.getBackendExceptionInfos(projectId,0,count);
        }else {
            infos = logMapper.getBackendExceptionInfos(projectId,page,pageSize);
        }
        for(BackendExceptionInfo exceptionInfo : infos){
            ViewLogVO vo = new ViewLogVO();
            vo.setLogType("exception");//异常日志
            vo.setLogInfo(exceptionInfo.getException());
            vo.setLogTime(exceptionInfo.getErrorTime());
            vo.setLogId(exceptionInfo.getBackendExceptionInfoId());
            list.add(vo);
        }
        pageBean.setData(list);
        return pageBean;
    }

    //分页查询后台访问日志
    @Override
    public PageBean<ViewLogVO> selectBackendRequests(int projectId,int page, int pageSize) {
        PageBean<ViewLogVO> pageBean = new PageBean<>();
        //查询总数量
        int count = logMapper.getTotalBackendRequestNum(projectId);
        pageBean.setTotal((long) count);
        page=(page-1)*pageSize;
        //回显数据
        List<ViewLogVO> list = new ArrayList<>();
        List<BackendRequestInfo> infos = new ArrayList<>();
        if(pageSize==0){
            //获取所有数据
            infos = logMapper.getBackendRequestInfos(projectId,0,count);
        }else{
            infos = logMapper.getBackendRequestInfos(projectId,page,pageSize);
        }
        for(BackendRequestInfo requestInfo : infos){
            ViewLogVO vo = new ViewLogVO();
            vo.setLogType("request");
            vo.setLogInfo("Ip:"+requestInfo.getRequestIp()+" "+"Api:"+requestInfo.getRequestApi()+" "+"Controller:"+requestInfo.getRequestController()+" "+"Url:"+requestInfo.getRequestUrl()+" "+"Method:"+requestInfo.getRequestMethod());
            vo.setLogTime(requestInfo.getRequestTime());
            vo.setLogId(requestInfo.getBackendRequestInfoId());
            list.add(vo);
        }
        pageBean.setData(list);
        return pageBean;
    }



    //分页查询前端异常项目日志
    @Override
    public PageBean<ViewLogVO> selectFrontExceptionLogs(int projectId,int page, int pageSize) {
        PageBean<ViewLogVO> pageBean = new PageBean<>();
        //查询总数量
        int count = logMapper.getFrontExceptionLogsNum(projectId);
        pageBean.setTotal((long) count);
        page=(page-1)*pageSize;
        //回显数据
        List<ViewLogVO> list = new ArrayList<>();
        List<FrontExceptionLog> logs = new ArrayList<>();
        if(pageSize==0){
            //获取所有数据
            logs = logMapper.getFrontExceptionInfos(projectId,0, count);
        }else{
            logs = logMapper.getFrontExceptionInfos(projectId,page,pageSize);
        }
        for(FrontExceptionLog frontExceptionLog : logs){
            ViewLogVO vo = new ViewLogVO();
            vo.setLogType("exception");
            vo.setLogInfo("Data:"+frontExceptionLog.getData()+" "+"Type:"+frontExceptionLog.getType());
            vo.setLogTime(frontExceptionLog.getLogTime());
            vo.setLogId(frontExceptionLog.getFrontExceptionLogId());
            list.add(vo);
        }
        pageBean.setData(list);
        return pageBean;
    }

    //查询前端性能项目日志
    @Override
    public PageBean<ViewLogVO> selectFrontPerformanceLogs(int projectId,int page, int pageSize) {
        PageBean<ViewLogVO> pageBean = new PageBean<>();
        //查询总数量
        int count = logMapper.getTotalFrontPerformanceNum(projectId);
        pageBean.setTotal((long) count);
        page=(page-1)*pageSize;
        //回显数据
        List<ViewLogVO> list = new ArrayList<>();
        List<FrontPerformanceLog> logs = new ArrayList<>();
        if(pageSize==0){
            //获取所有数据
            logs = logMapper.getFrontPerformanceLogs(projectId,0,count);
        }else{
            logs = logMapper.getFrontPerformanceLogs(projectId,page,pageSize);
        }
        for(FrontPerformanceLog frontPerformanceLog : logs){
            ViewLogVO vo = new ViewLogVO();
            vo.setLogTime(frontPerformanceLog.getLogTime());
            vo.setLogInfo("Type:"+frontPerformanceLog.getType()+" "+"Data:"+frontPerformanceLog.getData()+" "+"Url:"+frontPerformanceLog.getUrl());
            vo.setLogType("performance");
            vo.setLogId(frontPerformanceLog.getFrontPerformanceLogId());
            list.add(vo);
        }
        pageBean.setData(list);
        return pageBean;
    }

    //查询移动性能日志
    @Override
    public PageBean<ViewLogVO> selectMobilePerformanceLogs(int projectId, int page, int pageSize) {
        PageBean<ViewLogVO> pageBean = new PageBean<>();
        //查询总数量
        int count = logMapper.getMobilePerformanceNum(projectId);
        pageBean.setTotal((long) count);
        page=(page-1)*pageSize;
        //回显数据
        List<ViewLogVO> list = new ArrayList<>();
        List<MobilePerformanceLog> logs = new ArrayList<>();
        if(pageSize==0){
            //获取所有数据
            logs = logMapper.getMobilePerformanceLogs(projectId,0,count);
        }else{
            logs = logMapper.getMobilePerformanceLogs(projectId,page,pageSize);
        }
        for(MobilePerformanceLog mobilePerformanceLog : logs){
            ViewLogVO vo = new ViewLogVO();
            vo.setLogTime(mobilePerformanceLog.getLogTime());
            vo.setLogInfo("View:"+mobilePerformanceLog.getView()+ " " +"Fps:"+mobilePerformanceLog.getFps()+" " +"Memory:"+mobilePerformanceLog.getMemory());
            vo.setLogType("performance");
            vo.setLogId(mobilePerformanceLog.getMobilePerformanceLogId());
            list.add(vo);
        }
        pageBean.setData(list);
        return pageBean;
    }

    //查询移动异常日志
    @Override
    public PageBean<ViewLogVO> selectMobileExceptionLogs(int projectId, int page, int pageSize) {
        PageBean<ViewLogVO> pageBean = new PageBean<>();
        //查询总数量
        int count = logMapper.getMobileExceptionNum(projectId);
        pageBean.setTotal((long) count);
        page=(page-1)*pageSize;
        //回显数据
        List<ViewLogVO> list = new ArrayList<>();
        List<MobileExceptionLog> logs = new ArrayList<>();
        if(pageSize==0){
            //所有数据
            logs = logMapper.getMobileExceptionLogs(projectId,0,count);
        }else{
            logs = logMapper.getMobileExceptionLogs(projectId,page,pageSize);
        }
        for(MobileExceptionLog mobileExceptionLog : logs){
            ViewLogVO vo = new ViewLogVO();
            vo.setLogTime(mobileExceptionLog.getLogTime());
            vo.setLogInfo("Data:"+mobileExceptionLog.getData());
            vo.setLogType("exception");
            vo.setLogId(mobileExceptionLog.getMobileExceptionLogId());
            list.add(vo);
        }
        pageBean.setData(list);
        return pageBean;
    }


    //查询一周项目访问量，报错数等
    @Override
    public List<ProjectPresentationDateOneWeek> selectOneWeekData(int projectId) {
        //查询数据
        List<ProjectPresentationData> dataList = logMapper.getOneWeekData(projectId);
        //回显数据
        List<ProjectPresentationDateOneWeek> reData = new ArrayList<>();
        if(dataList!=null){
            for(ProjectPresentationData projectPresentationData : dataList ){
                ProjectPresentationDateOneWeek data = new ProjectPresentationDateOneWeek();
                BeanUtils.copyProperties(projectPresentationData,data);
                reData.add(data);
            }
            //返回数据
            return reData;
        }else{
            return null;
        }
    }

    //查询项目操作日志
    @Override
    public List<ProjectOpearteLogVO> selectProjectOperateLogs(int projectId) {
        //查询数据
        List<ProjectOperateLog> datalist = logMapper.getProjectOperateLogs(projectId);
        //回显数据
        List<ProjectOpearteLogVO> reData = new ArrayList<>();
        if(datalist!=null){
            for(ProjectOperateLog projectOperateLog : datalist){
                ProjectOpearteLogVO vo = new ProjectOpearteLogVO();
                BeanUtils.copyProperties(projectOperateLog,vo);
                reData.add(vo);
            }
            //返回数据
            return reData;
        }else {
            return null;
        }
    }

    //增加访问量
    @Override
    public int increaseVisits(int projectId) {
        ProjectPresentationData data = logMapper.getPresentationData(projectId);
        return logMapper.updateVisits(data.getVisits()+1,data.getTotalVisits()+1,projectId);
    }

    //分页查询后台自定义日志
    @Override
    public PageBean<ViewLogVO> selectBackendLogInfo(int projectId, int page, int pageSize) {
        PageBean<ViewLogVO> pageBean = new PageBean<>();
        //查询总数量
        int count = logMapper.getTotalBackendLogInfo(projectId);
        pageBean.setTotal((long) count);
        page=(page-1)*pageSize;
        //回显数据
        List<ViewLogVO> list = new ArrayList<>();
        List<BackendLogInfo> logs = new ArrayList<>();
        if(pageSize==0){
            //所有数据
            logs = logMapper.getBackendLogInfos(projectId,0,count);
        }else{
            logs = logMapper.getBackendLogInfos(projectId,page,pageSize);
        }
        for(BackendLogInfo backendLogInfo : logs){
            ViewLogVO vo = new ViewLogVO();
            vo.setLogTime(backendLogInfo.getLogTime());
            vo.setLogInfo("Content:"+backendLogInfo.getLogContent()+" "+"Level:"+backendLogInfo.getLogLevel());
            vo.setLogType("Log");
            vo.setLogId(backendLogInfo.getBackendLogInfoId());
            list.add(vo);
        }
        pageBean.setData(list);
        return pageBean;
    }

    //根据日志id查看详细的日志信息(移动)
    @Override
    public ViewLogVO showDetailedLog(ShowDetailedLogDTO showDetailedLogDTO) {
        ViewLogVO viewLogVO = new ViewLogVO();
        viewLogVO.setLogId(showDetailedLogDTO.getLogId());
        if (showDetailedLogDTO.getGroupType() == 0) {
            // 后台
            if (showDetailedLogDTO.getLogType() == 0){
                // 异常日志
                viewLogVO.setLogType("异常");
                BackendExceptionInfo backendExceptionInfo
                        = logMapper.selectBackendExceptionInfoByLogId(showDetailedLogDTO.getLogId());
                // 包装数据
                viewLogVO.setLogInfo(backendExceptionInfo.getException());
                viewLogVO.setLogTime(backendExceptionInfo.getErrorTime());

            }else if (showDetailedLogDTO.getLogType() == 1){
                // 普通日志
                viewLogVO.setLogType("访问日志");
                BackendRequestInfo backendRequestInfo =
                        logMapper.selectBackendRequestInfoByLogId(showDetailedLogDTO.getLogId());
                // 包装数据
                viewLogVO.setLogInfo("Ip:" + backendRequestInfo.getRequestIp() + " " +
                                     "url:" + backendRequestInfo.getRequestUrl() + " " +
                                     "method:" + backendRequestInfo.getRequestMethod() + " " +
                                     "controller:" + backendRequestInfo.getRequestController() + " " +
                                     "Api:" + backendRequestInfo.getRequestApi());
                viewLogVO.setLogTime(backendRequestInfo.getRequestTime());
            }else {
                // 自定义日志
                viewLogVO.setLogType("自定义");
                BackendLogInfo backendLogInfo =
                        logMapper.selectBackendLogInfoByLogId(showDetailedLogDTO.getLogId());
                // 包装数据
                viewLogVO.setLogInfo("logLevel:" + backendLogInfo.getLogLevel() + " " +
                                     "logContent:" + backendLogInfo.getLogContent());
                viewLogVO.setLogTime(backendLogInfo.getLogTime());
            }
        }else if (showDetailedLogDTO.getGroupType() == 1){
            // 前端
            if (showDetailedLogDTO.getLogType() == 0){
                // 异常日志
                viewLogVO.setLogType("异常");
                FrontExceptionLog frontExceptionLog =
                        logMapper.selectFrontExceptionLogByLogId(showDetailedLogDTO.getLogId());
                // 包装数据
                viewLogVO.setLogInfo(
                        "url:" + frontExceptionLog.getUrl() + " " +
                        "type:" + frontExceptionLog.getType() + " " +
                        "data:" + frontExceptionLog.getData()
                );
                viewLogVO.setLogTime(frontExceptionLog.getLogTime());
            }else {
                // 性能日志
                FrontPerformanceLog frontPerformanceLog =
                        logMapper.selectFrontPerformanceLogByLogId(showDetailedLogDTO.getLogId());
                //包装数据
                viewLogVO.setLogType(frontPerformanceLog.getType());
                viewLogVO.setLogInfo(
                        "url:" + frontPerformanceLog.getUrl() +  " " +
                                "type:" + frontPerformanceLog.getType() + " " +
                                "data:" + frontPerformanceLog.getData()
                );
                viewLogVO.setLogTime(frontPerformanceLog.getLogTime());
            }
        }else {
            // 移动
            if (showDetailedLogDTO.getLogType() == 0){
                // 异常日志
                viewLogVO.setLogType("异常");
                MobileExceptionLog mobileExceptionLog =
                        logMapper.selectMobileExceptionLogByLogId(showDetailedLogDTO.getLogId());
                // 包装数据
                viewLogVO.setLogInfo(mobileExceptionLog.getData());
                viewLogVO.setLogTime(mobileExceptionLog.getLogTime());

            }else {
                // 性能日志
                viewLogVO.setLogType("性能");
                MobilePerformanceLog mobilePerformanceLog =
                        logMapper.selectMobilePerformanceLogByLogId(showDetailedLogDTO.getLogId());
                viewLogVO.setLogInfo(
                        "view:" + mobilePerformanceLog.getView() + " " +
                        "fps:" + mobilePerformanceLog.getFps() + " " +
                        "memory:" + mobilePerformanceLog.getMemory()
                );
                viewLogVO.setLogTime(mobilePerformanceLog.getLogTime());
            }
        }
        return viewLogVO;
    }

    @Override
    public List<ShowLogNumberOneWeekForGroupVO> showLogNumberOneWeekForGroup(ShowLogNumberOneWeekForGroupDTO showLogNumberOneWeekForGroupDTO) {
        List<ShowLogNumberOneWeekForGroupVO> showLogNumberOneWeekForGroupVOList = null;
        int projectId = showLogNumberOneWeekForGroupDTO.getProjectId();
        if (showLogNumberOneWeekForGroupDTO.getGroupType() == 0) {
            // 后台
            if (showLogNumberOneWeekForGroupDTO.getLogType() == 0){
                // 异常日志
                showLogNumberOneWeekForGroupVOList = logMapper.selectBackendExceptionInfoCountByProjectId(projectId);
            } else if (showLogNumberOneWeekForGroupDTO.getLogType() == 1) {
                // 正常日志
                showLogNumberOneWeekForGroupVOList = logMapper.selectBackendRequestInfoCountByProjectId(projectId);
            }else {
                // 自定义日志
                showLogNumberOneWeekForGroupVOList = logMapper.selectBackendLogInfoCountByProjectId(projectId);
            }
        }else if (showLogNumberOneWeekForGroupDTO.getGroupType() == 1) {
            // 前端
            if (showLogNumberOneWeekForGroupDTO.getLogType() == 0){
                // 异常日志
                showLogNumberOneWeekForGroupVOList = logMapper.selectFrontExceptionLogCountByProjectId(projectId);
            } else {
                // 性能日志
                showLogNumberOneWeekForGroupVOList = logMapper.selectFrontPerformanceLogCountByProjectId(projectId);
            }
        }else {
            // 移动
            if (showLogNumberOneWeekForGroupDTO.getLogType() == 0){
                // 异常日志
                showLogNumberOneWeekForGroupVOList = logMapper.selectMobileExceptionLogCountByProjectId(projectId);
            } else {
                // 性能日志
                showLogNumberOneWeekForGroupVOList = logMapper.selectMobilePerformanceLogCountByProjectId(projectId);
            }
        }

        return showLogNumberOneWeekForGroupVOList;
    }

    @Override
    public int getTotalLogNum(int projectId) {
        return logMapper.getFrontExceptionLogsNum(projectId)+
                logMapper.getTotalBackendExceptionLogs(projectId)+
                logMapper.getTotalBackendLogInfo(projectId)+
                logMapper.getTotalBackendRequestNum(projectId)+
                logMapper.getTotalFrontPerformanceNum(projectId)+
                logMapper.getMobileExceptionNum(projectId)+
                logMapper.getMobilePerformanceNum(projectId);
    }

    /**
     * 查询项目的前端日志性能
     * @param projectId
     * @return
     */
    @Override
    public List<QueryFrontPerformanceLogVO> queryFrontPerformanceLog(int projectId) {
        List<FrontPerformanceLog> frontPerformanceLogs = logMapper.selectFrontPerformanceLogByUserId(projectId);
        List<QueryFrontPerformanceLogVO> list = new ArrayList<>();
        if (frontPerformanceLogs.isEmpty()){
            return null;
        }else {
            for (FrontPerformanceLog frontPerformanceLog : frontPerformanceLogs) {
                QueryFrontPerformanceLogVO queryFrontPerformanceLogVO = new QueryFrontPerformanceLogVO();
                BeanUtils.copyProperties(frontPerformanceLog, queryFrontPerformanceLogVO);
                list.add(queryFrontPerformanceLogVO);
            }
            return list;
        }

    }

    // 根据id查找日志信息
    @Override
    public ViewLogFrontVO showDetailedLogForFront(ShowDetailedLogDTO showDetailedLogDTO) {
        ViewLogFrontVO viewLogFrontVO = new ViewLogFrontVO();
        viewLogFrontVO.setLogId(showDetailedLogDTO.getLogId());
        if (showDetailedLogDTO.getGroupType() == 0) {
            // 后台
            if (showDetailedLogDTO.getLogType() == 0){
                // 异常日志
                viewLogFrontVO.setLogType("异常");
                BackendExceptionInfo backendExceptionInfo
                        = logMapper.selectBackendExceptionInfoByLogId(showDetailedLogDTO.getLogId());
                // 包装数据
                viewLogFrontVO.setLogInfo(backendExceptionInfo);
                viewLogFrontVO.setLogTime(backendExceptionInfo.getErrorTime());

            }else if (showDetailedLogDTO.getLogType() == 1){
                // 普通日志
                viewLogFrontVO.setLogType("访问日志");
                BackendRequestInfo backendRequestInfo =
                        logMapper.selectBackendRequestInfoByLogId(showDetailedLogDTO.getLogId());
                // 包装数据
                viewLogFrontVO.setLogInfo(backendRequestInfo);
                viewLogFrontVO.setLogTime(backendRequestInfo.getRequestTime());
            }else {
                // 自定义日志
                viewLogFrontVO.setLogType("自定义");
                BackendLogInfo backendLogInfo =
                        logMapper.selectBackendLogInfoByLogId(showDetailedLogDTO.getLogId());
                // 包装数据
                viewLogFrontVO.setLogInfo(backendLogInfo);
                viewLogFrontVO.setLogTime(backendLogInfo.getLogTime());
            }
        }else if (showDetailedLogDTO.getGroupType() == 1){
            // 前端
            if (showDetailedLogDTO.getLogType() == 0){
                // 异常日志
                viewLogFrontVO.setLogType("异常");
                FrontExceptionLog frontExceptionLog =
                        logMapper.selectFrontExceptionLogByLogId(showDetailedLogDTO.getLogId());
                // 包装数据
                viewLogFrontVO.setLogInfo(frontExceptionLog);
                viewLogFrontVO.setLogTime(frontExceptionLog.getLogTime());
            }else {
                // 性能日志
                FrontPerformanceLog frontPerformanceLog =
                        logMapper.selectFrontPerformanceLogByLogId(showDetailedLogDTO.getLogId());
                //包装数据
                viewLogFrontVO.setLogType(frontPerformanceLog.getType());
                viewLogFrontVO.setLogInfo(frontPerformanceLog);
                viewLogFrontVO.setLogTime(frontPerformanceLog.getLogTime());
            }
        }else {
            // 移动
            if (showDetailedLogDTO.getLogType() == 0){
                // 异常日志
                viewLogFrontVO.setLogType("异常");
                MobileExceptionLog mobileExceptionLog =
                        logMapper.selectMobileExceptionLogByLogId(showDetailedLogDTO.getLogId());
                // 包装数据
                viewLogFrontVO.setLogInfo(mobileExceptionLog);
                viewLogFrontVO.setLogTime(mobileExceptionLog.getLogTime());

            }else {
                // 性能日志
                viewLogFrontVO.setLogType("性能");
                MobilePerformanceLog mobilePerformanceLog =
                        logMapper.selectMobilePerformanceLogByLogId(showDetailedLogDTO.getLogId());
                viewLogFrontVO.setLogInfo(mobilePerformanceLog);
                viewLogFrontVO.setLogTime(mobilePerformanceLog.getLogTime());
            }
        }
        return viewLogFrontVO;
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void runEachDay() {
        List<Integer> projectIds = logMapper.getProjectIds();
        logMapper.insertProjectPresentationData(projectIds);
    }

}
