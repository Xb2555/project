package com.qg24.controller;

import com.qg24.po.dto.FreezeProjectDTO;
import com.qg24.po.dto.FreezeUserDTO;
import com.qg24.po.dto.VerifyApplicationDTO;
import com.qg24.po.result.PageBean;
import com.qg24.po.result.Result;
import com.qg24.po.vo.AdminQueryProjectApplicationVO;
import com.qg24.po.vo.AdminQueryUserVO;
import com.qg24.po.vo.ProjectDetailedInfoVO;
import com.qg24.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 管理员对于项目发布、更新项目的审核
     * @param verifyApplicationDTO
     * @return
     */
    @PostMapping("/verifyApplication")
    public Result<?> verifyApplication(@RequestBody VerifyApplicationDTO verifyApplicationDTO){
        int i = adminService.verifyApplication(verifyApplicationDTO);
        if (i == 0){
            return Result.error("审核失败");
        }else if (i == 1){
            return Result.success("审核成功");
        }
        return Result.error("系统繁忙，请稍后重试");
    }

    /**
     * 查看用户详情
     * @param userId
     * @return
     */
    @GetMapping("/showUserDetailedInfo")
    public Result<?> showUserDetailedInfo(@RequestParam("userId") int userId){
        AdminQueryUserVO adminQueryUserVO = adminService.showUserDetailedInfo(userId);
        if (adminQueryUserVO == null){
            return Result.error("没有该用户");
        }else {
            return Result.success("查看成功", adminQueryUserVO);
        }
    }

    /**
     * 冻结用户
     * @param freezeUserDTO
     * @return
     */
    @PostMapping("/freezeUser")
    public Result<?> freezeUser(@RequestBody FreezeUserDTO freezeUserDTO){
        int i = adminService.freezeUser(freezeUserDTO);
        if (i == 0){
            return Result.error("操作失败");
        }else if(i == 1){
            return Result.success("操作成功");
        }
        return Result.error("系统繁忙，请稍后重试");
    }

    /**
     * 冻结项目
     * @param freezeProjectDTO
     * @return
     */
    @PostMapping("/freezeProject")
    public Result<?> freezeProject(@RequestBody FreezeProjectDTO freezeProjectDTO){
        int i = adminService.freezeProject(freezeProjectDTO);
        if (i == 0){
            return Result.error("操作失败");
        }else if(i == 1){
            return Result.success("操作成功");
        }
        return Result.error("系统繁忙，请稍后重试");
    }

    /**
     * 管理查看已发布的项目，（未冻结/已冻结）
     * @param page
     * @param pageSize
     * @param projectStatus
     * @return
     */
    @GetMapping("/pagedQueryPublishedProject")
    public Result<?> pagedQueryPublishedProject(@RequestParam("page") int page,
                                                @RequestParam("pageSize") int pageSize,
                                                @RequestParam("projectStatus") int projectStatus,
                                                @RequestParam("keyWord") String keyWord)
    {
        PageBean<ProjectDetailedInfoVO> projectDetailedInfoVOPageBean = adminService.pagedQueryPublishedProject(page, pageSize, projectStatus, keyWord);
        if (projectDetailedInfoVOPageBean == null){
            return Result.error("没有数据");
        }else {
            return Result.success("查询成功", projectDetailedInfoVOPageBean);
        }
    }

    /**
     * 管理员查看项目申请情况（待审核/被拒绝）
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("/pagedQueryProjectApplication")
    public Result<?> pagedQueryProjectApplication(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("applicationStatus") int status,@RequestParam("keyWord") String keyWord){
        PageBean<AdminQueryProjectApplicationVO> pageBean = new PageBean<>();
        if(pageSize==0){
            //拿到所有数据
            pageBean = adminService.allQueryProjectApplication(status);
        }else{
            pageBean = adminService.pagedQueryProjectApplication(page, pageSize, status,keyWord);
        }
        if (pageBean == null){
            return Result.error("没有数据");
        }else {
            return Result.success("查询成功", pageBean);
        }
    }
}
