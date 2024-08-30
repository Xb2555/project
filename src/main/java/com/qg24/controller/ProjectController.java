package com.qg24.controller;

import com.qg24.po.dto.*;
import com.qg24.po.result.PageBean;
import com.qg24.po.result.Result;
import com.qg24.po.vo.ProjectDetailedInfoVO;
import com.qg24.po.vo.UserOwnMonitorForProjectVO;
import com.qg24.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {


    @Autowired
    private ProjectService projectService;

    /**
     *
     * 普通用户主界面，查看未冻结的所有项目
     * @return
     */
    //pageSize为0将全部数据发送
    @GetMapping("/showAllProjectForUser")
    public Result<?> showAllProjectForUser(@RequestParam int page,@RequestParam int pageSize,@RequestParam String projectName) {
        PageBean pageBean = projectService.showAllProjectForUser(page,pageSize,projectName);
        if (pageBean != null) {
            return Result.success("success", pageBean);
        }else {
            return Result.error("error");
        }
    }

    /**
     * 用户发布软件申请
     * @param publishProjectDTO
     * @return
     */
    @PostMapping("/publishProject")
    public Result<?> publishProject(@RequestBody PublishProjectDTO publishProjectDTO) {
        int i = projectService.publishProject(publishProjectDTO);
        if (i > 0) {
            return Result.success("success");
        }else {
            return Result.error("error");
        }

    }

    /**
     * 查看软件详情信息
     * @param projectId
     * @return
     */
    @GetMapping("/detailedInfo")
    public Result<?> detailedInfo(@RequestParam int projectId) {
        ProjectDetailedInfoVO projectDetailedInfoVO = projectService.detaliedInfo(projectId);
        if (projectDetailedInfoVO != null) {
            return Result.success("success", projectDetailedInfoVO);
        }else {
            return Result.error("error");
        }
    }

    /**
     * 用户申请监测项目权限
     * @param userApplyMonitorPermissionDTO
     * @return
     */
    @PostMapping("/applyMonitorPermission")
    public Result<?> applyMonitorPermission(@RequestBody UserApplyMonitorPermissionDTO userApplyMonitorPermissionDTO) {

        int i = projectService.applyMonitorPermission(userApplyMonitorPermissionDTO);
        if (i != 0){
            if (i == -1){
                return Result.error("请勿重复申请");
            }
            return Result.success("success");
        }else {
            return Result.error("error");
        }
    }

    /**
     * 发布者更新项目
     * @param updateProjectDTO
     * @return
     */
    @PostMapping("/updateProject")
    public Result<?> updateProject(@RequestBody UpdateProjectDTO updateProjectDTO) {
        int i = projectService.updateProject(updateProjectDTO);
        if (i != 0){
            if (i == -1){
                return Result.error("项目口令错误");
            }
            return Result.success("success");
        }else {
            return Result.error("该申请已存在");
        }
    }

    /**
     * 查询对某个项目有监测权限的用户
     * @param projectId
     * @return
     */
    @GetMapping("/queryOwnMonitorUser")
    public Result<?> queryOwnMonitorUser(@RequestParam int projectId) {
        List<UserOwnMonitorForProjectVO> userOwnMonitorForProjectVOList = projectService.queryOwnMonitorUser(projectId);
        if (userOwnMonitorForProjectVOList.size() != 0) {
            return Result.success("success", userOwnMonitorForProjectVOList);
        }else {
            return Result.error("error");
        }
    }

    /**
     * 撤销某个用户对项目的监测权限
     * @param cancelUserMonitorPermissionDTO
     * @return
     */
    @PostMapping("/cancelUserMonitorPermission")
    public Result<?> cancelUserMonitorPermission(@RequestBody CancelUserMonitorPermissionDTO cancelUserMonitorPermissionDTO) {
        int i = projectService.cancelUserMonitorPermission(cancelUserMonitorPermissionDTO);
        if (i != 0){
            return Result.success("success");
        }else {
            return Result.error("error");
        }
    }

    /**
     * 发布者移除项目，需要项目口令
     * @param deleteProjectDTO
     * @return
     */
    @DeleteMapping("/deleteProject")
    public Result<?> deleteProject(@RequestBody DeleteProjectDTO deleteProjectDTO) {
        int i = projectService.deleteProject(deleteProjectDTO);
        if (i != 0){
            if (i == -1){
                return Result.error("项目口令错误");
            }else if(i == -2){
                return Result.error("项目不存在");
            }else {
                return Result.success("success");

            }
        }else {
            return Result.error("error");
        }
    }

    @PostMapping("/checkMonitorAuth")
    public Result<?> checkMonitorAuth(@RequestBody CheckMonitorAuthDTO checkMonitorAuthDTO){
        String data = projectService.checkMonitorAuth(checkMonitorAuthDTO);
        if (data != null) {
            return Result.success("success",data);
        }else {
            return Result.error("error");
        }
    }


    /**
     * 发布者设置项目报警阈值
     * @param setErrorRateDTO
     * @return
     */
    @PostMapping("/setErrorRate")
    public Result<?> setErrorRate(@RequestBody SetErrorRateDTO setErrorRateDTO){
        int count = projectService.updateWarningRate(setErrorRateDTO);
        if(count >= 1){
            //更新成功
            return Result.success("更新成功");
        }else if(setErrorRateDTO.getErrorRate()>1){
            //更新失败
            return Result.error("报警阈值不能大于1");
        }else{
            return Result.error("更新失败");
        }
    }
}
