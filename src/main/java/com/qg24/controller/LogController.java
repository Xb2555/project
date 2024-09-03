package com.qg24.controller;

import com.qg24.AIExplain.ExplainLogs;
import com.qg24.dao.LogMapper;
import com.qg24.po.dto.ShowDetailedLogDTO;
import com.qg24.po.dto.ShowLogNumberOneWeekForGroupDTO;
import com.qg24.po.dto.ViewLogDTO;
import com.qg24.po.entity.BackendExceptionInfo;
import com.qg24.po.entity.DSReMessage;
import com.qg24.po.entity.FrontExceptionLog;
import com.qg24.po.entity.MobileExceptionLog;
import com.qg24.po.result.PageBean;
import com.qg24.po.result.Result;
import com.qg24.po.vo.*;
import com.qg24.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private LogService logService;

    @Autowired
    private LogMapper logMapper;

    /**
     * 引用ai辅助分析项目异常日志，给出意见
     * @param groupType
     * @param projectId
     * @return
     */

    @GetMapping("/explainLogs")
    public Result<String> explainLogs(@RequestParam("groupType") int groupType, @RequestParam("projectId") int projectId){
        if(groupType==0){
            //分析后台异常日志
            List<ViewLogVO> backendExceptionInfos = new ArrayList<>();
            backendExceptionInfos = logService.selectBackendExceptionLogs(projectId,1,0).getData();
            if(backendExceptionInfos.isEmpty()){
                return Result.error("没有日志数据，无法进行ai分析");
            }
            if(backendExceptionInfos.size()>20){
                backendExceptionInfos=backendExceptionInfos.subList(0,21);//截取20个数据
            }
            ExplainLogs explainLogs = new ExplainLogs();
            DSReMessage reMessage = explainLogs.LogsExplain(backendExceptionInfos);
            if(reMessage==null){
                return Result.error("无法连接ai，请等待管理员修复");
            }
            return Result.success("success",reMessage.getChoices().get(0).getMessage().getContent());
        }else if(groupType==1){
            //分析前端异常日志
            List<ViewLogVO> frontExceptionLogs = new ArrayList<>();
            frontExceptionLogs = logService.selectFrontExceptionLogs(projectId,1,0).getData();
            if(frontExceptionLogs.isEmpty()){
                return Result.error("没有日志数据，无法进行ai分析");
            }
            if(frontExceptionLogs.size()>20){
                frontExceptionLogs=frontExceptionLogs.subList(0,21);
            }
            ExplainLogs explainLogs = new ExplainLogs();
            DSReMessage reMessage = explainLogs.LogsExplain(frontExceptionLogs);
            if(reMessage==null){
                return Result.error("无法连接ai，请等待管理员修复");
            }
            return Result.success("success",reMessage.getChoices().get(0).getMessage().getContent());
        }else{
            //分析移动异常日志
            List<ViewLogVO> mobileExceptionLogs = new ArrayList<>();
            mobileExceptionLogs = logService.selectMobileExceptionLogs(projectId,1,0).getData();
            if(mobileExceptionLogs.isEmpty()){
                return Result.error("没有日志数据，无法进行ai分析");
            }
            if(mobileExceptionLogs.size()>20){
                mobileExceptionLogs=mobileExceptionLogs.subList(0,21);
            }
            ExplainLogs explainLogs = new ExplainLogs();
            DSReMessage reMessage = explainLogs.LogsExplain(mobileExceptionLogs);
            if(reMessage==null){
                return Result.error("无法连接ai，请等待管理员修复");
            }
            return Result.success("success",reMessage.getChoices().get(0).getMessage().getContent());
        }
    }

    /**
     * 分页查询攻击服务器记录
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/queryAttackServerLog")
    public Result<PageBean<QueryAttackServerLogVO>> getAttackServerLog(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        PageBean<QueryAttackServerLogVO> pages = logService.selectAllAttackServerLog(page,pageSize);
        //返回结果
        return Result.success("success",pages);
    }

    /**
     * 分页查询用户操作日志
     */
    @GetMapping("/queryAllUserOperationLog")
    public Result<PageBean<QueryAllUserOperationLogVO>> getAllUserOperateLog(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        // TODO VO中userid改为username了需要修改
        PageBean<QueryAllUserOperationLogVO> pageBean = logService.selectAllUserOperationLog(page,pageSize);
        //返回结果
        return Result.success("success",pageBean);
    }

    /**
     * 分页查询后台项目日志
     */
    @GetMapping("/viewLogForGroup")
    public Result<PageBean<ViewLogVO>> getLogForGroup(@RequestParam("groupType") int groupType, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("projectId") int projectId, @RequestParam("logType") int logType){
        ViewLogDTO viewLogDTO = new ViewLogDTO();
        viewLogDTO.setGroupType(groupType);
        viewLogDTO.setPage(page);
        viewLogDTO.setPageSize(pageSize);
        viewLogDTO.setProjectId(projectId);
        viewLogDTO.setLogType(logType);
        if(viewLogDTO.getGroupType()==0){
           if(viewLogDTO.getLogType()==0){
               //查询后台异常日志
               PageBean<ViewLogVO> pageBean = logService.selectBackendExceptionLogs(viewLogDTO.getProjectId(),viewLogDTO.getPage(), viewLogDTO.getPageSize());
               //返回结果
               return Result.success("success",pageBean);
           }else if(viewLogDTO.getLogType()==1){
               //查询后台请求日志
               PageBean<ViewLogVO> pageBean = logService.selectBackendRequests(viewLogDTO.getProjectId(),viewLogDTO.getPage(), viewLogDTO.getPageSize());
               //返回结果
               return Result.success("success",pageBean);
           }else{
               //查询后台自定义日志
               PageBean<ViewLogVO> pageBean = logService.selectBackendLogInfo(viewLogDTO.getProjectId(),viewLogDTO.getPage(), viewLogDTO.getPageSize());
               //返回结果
               return Result.success("success", pageBean);
           }
        }else if(viewLogDTO.getGroupType()==1){
            //查询前端日志
            if(viewLogDTO.getLogType()==0){
                //查询前端异常日志
                PageBean<ViewLogVO> pageBean = logService.selectFrontExceptionLogs(viewLogDTO.getProjectId(),viewLogDTO.getPage(), viewLogDTO.getPageSize());
                return Result.success("success",pageBean);
            }else{
                //查询前端性能日志
                PageBean<ViewLogVO> pageBean = logService.selectFrontPerformanceLogs(viewLogDTO.getProjectId(),viewLogDTO.getPage(), viewLogDTO.getPageSize());
                return Result.success("success",pageBean);
            }
        }else{
            //查询移动日志
            if(viewLogDTO.getLogType()==0){
                //查异常日志
                PageBean<ViewLogVO> pageBean = logService.selectMobileExceptionLogs(viewLogDTO.getProjectId(),viewLogDTO.getPage(), viewLogDTO.getPageSize());
                return Result.success("success",pageBean);
            }else {
                //查性能日志
                PageBean<ViewLogVO> pageBean = logService.selectMobilePerformanceLogs(viewLogDTO.getProjectId(),viewLogDTO.getPage(), viewLogDTO.getPageSize());
                return Result.success("success",pageBean);
            }
        }
    }

    /**
     * 查询一周内项目的访问数据和报错统计
     */
    @GetMapping("/projectPresentationDateOneWeek")
    public Result<?> getPresentationDataOneWeek(@RequestParam("projectId") int projectId){
        //查询数据
        List<ProjectPresentationDateOneWeek> data = logService.selectOneWeekData(projectId);
        if(data!=null){
            return Result.success("success",data);
        }else{
            return Result.error("无数据");
        }
    }

    /**
     * 查询项目操作日志
     */
    @GetMapping("/ViewProjectOperateLog")
    public Result<?> getProjectOperateLog(@RequestParam("projectId")int projectId){
        //查询数据
        List<ProjectOpearteLogVO> data = logService.selectProjectOperateLogs(projectId);
        if(data.isEmpty()){
            return Result.error("无数据");
        }else {
            return Result.success("success",data);
        }
    }

    /**
     * 更新项目访问量
     */
    @PostMapping("/increaseVisits")
    public Result<?> increaseVisits(@RequestParam("projectId")int projectId){
        int count = logService.increaseVisits(projectId);
        if(count==0){
            return Result.error("error");
        }else {
            return Result.success("success");
        }
    }

    /**
     * 根据日志id查看详细的日志信息(移动)
     * @param showDetailedLogDTO
     * @return
     */
    @PostMapping("/showDetailedLog")
    public Result<?> showDetailedLog(@RequestBody ShowDetailedLogDTO showDetailedLogDTO){
        ViewLogVO viewLogVO =  logService.showDetailedLog(showDetailedLogDTO);
        if (viewLogVO.getLogInfo() != null) {
            return Result.success("success",viewLogVO);
        }else {
            return Result.error("error");
        }

    }
    /**
     * 根据日志id查看详细的日志信息(移动)
     * @param showDetailedLogDTO
     * @return
     */
    @PostMapping("/showDetailedLogForFront")
    public Result<?> showDetailedLogForFront(@RequestBody ShowDetailedLogDTO showDetailedLogDTO){
        ViewLogFrontVO viewLogFrontVO =  logService.showDetailedLogForFront(showDetailedLogDTO);
        if (viewLogFrontVO.getLogInfo() != null) {
            return Result.success("success",viewLogFrontVO);
        }else {
            return Result.error("error");
        }

    }

    @PostMapping("/showLogNumberOneWeekForGroup")
    public Result<?> showLogNumberOneWeekForGroup(@RequestBody ShowLogNumberOneWeekForGroupDTO showLogNumberOneWeekForGroupDTO){
        List<ShowLogNumberOneWeekForGroupVO> showLogNumberOneWeekForGroupVOList = logService.showLogNumberOneWeekForGroup(showLogNumberOneWeekForGroupDTO);
        if (showLogNumberOneWeekForGroupVOList != null){
            return Result.success("success",showLogNumberOneWeekForGroupVOList);
        }else {
            return Result.error("error");
        }
    }

    /**
     * 查询项目的前端日志性能
     * @param projectId
     * @return
     */
    @GetMapping("/queryFrontPerformanceLog")
    public Result<?> queryFrontPerformanceLog(@RequestParam("projectId")int projectId){
        List<QueryFrontPerformanceLogVO> list = logService.queryFrontPerformanceLog(projectId);
        if (list == null){
            return Result.error("还没有日志记录");
        }else {
            return Result.success("查询成功", list);
        }
    }

}
