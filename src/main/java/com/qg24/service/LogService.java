package com.qg24.service;

import com.qg24.po.dto.ShowDetailedLogDTO;
import com.qg24.po.dto.ShowLogNumberOneWeekForGroupDTO;
import com.qg24.po.result.PageBean;
import com.qg24.po.vo.*;

import java.util.List;

public interface LogService {
    PageBean<QueryAttackServerLogVO> selectAllAttackServerLog(int page,int pageSize);

    PageBean<QueryAllUserOperationLogVO> selectAllUserOperationLog(int page,int pageSize);

    PageBean<ViewLogVO> selectBackendExceptionLogs(int projectId, int page, int pageSize);

    PageBean<ViewLogVO> selectFrontExceptionLogs(int projectId, int page, int pageSize);

    PageBean<ViewLogVO> selectBackendRequests(int projectId, int page, int pageSize);

    PageBean<ViewLogVO> selectFrontPerformanceLogs(int projectId, int page, int pageSize);

    PageBean<ViewLogVO> selectMobilePerformanceLogs(int projectId, int page, int pageSize);

    PageBean<ViewLogVO> selectMobileExceptionLogs(int projectId, int page, int pageSize);

    List<ProjectPresentationDateOneWeek> selectOneWeekData(int projectId);

    List<ProjectOpearteLogVO> selectProjectOperateLogs(int projectId);

    int increaseVisits(int projectId);

    PageBean<ViewLogVO> selectBackendLogInfo(int projectId, int page, int pageSize);

    ViewLogVO showDetailedLog(ShowDetailedLogDTO showDetailedLogDTO);

    List<ShowLogNumberOneWeekForGroupVO> showLogNumberOneWeekForGroup(ShowLogNumberOneWeekForGroupDTO showLogNumberOneWeekForGroupDTO);

    int getTotalLogNum(int projectId);

    List<QueryFrontPerformanceLogVO> queryFrontPerformanceLog(int projectId);

    ViewLogFrontVO showDetailedLogForFront(ShowDetailedLogDTO showDetailedLogDTO);
}
