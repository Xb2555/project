package com.qg24.service.impl;

import com.qg24.dao.AdminMapper;
import com.qg24.dao.ProjectMapper;
import com.qg24.po.dto.FreezeProjectDTO;
import com.qg24.po.dto.FreezeUserDTO;
import com.qg24.po.dto.VerifyApplicationDTO;
import com.qg24.po.entity.Project;
import com.qg24.po.entity.User;
import com.qg24.po.entity.UserApplicationProject;
import com.qg24.po.result.PageBean;
import com.qg24.po.vo.AdminQueryProjectApplicationVO;
import com.qg24.po.vo.AdminQueryUserVO;
import com.qg24.po.vo.ProjectDetailedInfoVO;
import com.qg24.service.AdminService;
import com.qg24.utils.TimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service

public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private ProjectMapper projectMapper;


    /**
     * 管理员对于项目发布、更新项目的审核
     * @param verifyApplicationDTO
     * @return
     */
    @Override
    public int verifyApplication(VerifyApplicationDTO verifyApplicationDTO) {
        UserApplicationProject userApplicationProject = new UserApplicationProject();
        BeanUtils.copyProperties(verifyApplicationDTO, userApplicationProject);
        userApplicationProject.setUserApplicationProjectId(verifyApplicationDTO.getApplicationId());
        //判断是否通过审核
        if (verifyApplicationDTO.getStatus() == 1){
            //查找该申请信息
            UserApplicationProject userApplicationProject1 = adminMapper.selectByApplicationId(verifyApplicationDTO.getApplicationId());
            //查看是发布新软件还是更新软件
            if (userApplicationProject1.getApplicationType() == 0){
                //上传新软件
                Project project = new Project();
                project.setProjectName(userApplicationProject1.getProjectName());
                project.setProjectUrl(userApplicationProject1.getProjectUrl());
                project.setDescription(userApplicationProject1.getDescription());
                project.setUsername(adminMapper.selectUserNameByUserId(userApplicationProject1.getUserId()));
                project.setProjectPassword(userApplicationProject1.getProjectPassword());
                //上传操作
                adminMapper.addProject(project);
                //查询新发布项目的id
                int i = adminMapper.selectProjectIdByProjectName(userApplicationProject1.getProjectName());
//                //添加用户拥有监控项目的权限
//                UserMonitorProject userMonitorProject = new UserMonitorProject();
//                userMonitorProject.setProjectId(i);
//                userMonitorProject.setUserId(userApplicationProject1.getUserId());
//                adminMapper.addUserMonitorProject(userMonitorProject);
                //添加新的项目数据
                projectMapper.insertProjectData(i);
                //更新申请表中的项目id
                adminMapper.updateApplicationProjectId(verifyApplicationDTO.getApplicationId(),i);
                //更新项目操作日志
                adminMapper.insertProjectOperateLog("upload","发布项目",i);
            } else if (userApplicationProject1.getApplicationType() == 1) {
                //根据项目ID查找到该项目,并修改
                Project project = new Project();
                project.setProjectId(userApplicationProject1.getProjectId());
                project.setProjectName(userApplicationProject1.getProjectName());
                project.setProjectUrl(userApplicationProject1.getProjectUrl());
                project.setDescription(userApplicationProject1.getDescription());
                project.setUsername(adminMapper.selectUserNameByUserId(userApplicationProject1.getUserId()));
                project.setProjectPassword(userApplicationProject1.getProjectPassword());
                //更新项目
                adminMapper.updateProject(project);
                //更新项目操作日志
                adminMapper.insertProjectOperateLog("update","更新项目",userApplicationProject1.getProjectId());
            }
        }
        //执行操作
        int i = adminMapper.updateUserApplicationProject(userApplicationProject);
        return i;
    }

    /**
     * 查看用户详情
     * @param userId
     * @return
     */
    @Override
    public AdminQueryUserVO showUserDetailedInfo(int userId) {
        User user = adminMapper.selectUserByUserId(userId);
        if (user == null){
            return null;
        }else {
            AdminQueryUserVO adminQueryUserVO = new AdminQueryUserVO();
            BeanUtils.copyProperties(user, adminQueryUserVO);
            if(user.getEnabled() == 0){
                adminQueryUserVO.setEnabled("冻结");
            }else if (user.getEnabled() == 1){
                adminQueryUserVO.setEnabled("未冻结");
            }
            // TODO 判断用户是否在线
            return adminQueryUserVO;
        }
    }

    /**
     * 冻结用户
     * @param freezeUserDTO
     * @return
     */
    @Override
    public int freezeUser(FreezeUserDTO freezeUserDTO) {
        User user = new User();
        user.setUserId(freezeUserDTO.getUserId());
        user.setDeadline(TimeUtil.formatDateTime(LocalDateTime.now().plus(freezeUserDTO.getFreezeHour(), ChronoUnit.HOURS)));
        //更改冻结状态
        // TODO 不确定用户表是否需要冻结状态
        user.setEnabled(0); //被冻结
        //进行冻结
        int i = adminMapper.freezeUser(user);
        return i;
    }

    /**
     * 冻结项目
     * @param freezeProjectDTO
     * @return
     */
    @Override
    public int freezeProject(FreezeProjectDTO freezeProjectDTO) {
        Project project = new Project();
        project.setProjectId(freezeProjectDTO.getProjectId());
        project.setFreezeTime(TimeUtil.formatDateTime(LocalDateTime.now().plus(freezeProjectDTO.getFreezeHour(), ChronoUnit.HOURS)));
        int i = adminMapper.freezeProject(project);
        return i;
    }

    /**
     * 管理查看已发布的项目，（未冻结/已冻结）
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<ProjectDetailedInfoVO> pagedQueryPublishedProject(int page, int pageSize, int projectStatus, String keyWord) {
        int offset = (page - 1) * pageSize;
        List<ProjectDetailedInfoVO> projectDetailedInfoVOS;
        if (projectStatus == 1){
            //查看未冻结的项目
            if (pageSize == 0){
                //直接查询全部
                projectDetailedInfoVOS = adminMapper.queryPublishedProjectUnFroze();
            }else {
                projectDetailedInfoVOS = adminMapper.pagedQueryPublishedProjectUnFroze(offset, pageSize, keyWord);
            }
            if (projectDetailedInfoVOS.isEmpty()){
                return null; //没有该类型的项目
            }else {
                int i = adminMapper.totalCountOnPublishedProjectUnFroze(keyWord);
                PageBean<ProjectDetailedInfoVO> pageBean = new PageBean<>();
                pageBean.setTotal((long) i);
                pageBean.setData(projectDetailedInfoVOS);
                return pageBean;
            }
        }
        else if (projectStatus == 0){
            //被冻结的
            if (pageSize == 0){
                projectDetailedInfoVOS = adminMapper.queryPublishedProjectFroze();
            }else {
                projectDetailedInfoVOS = adminMapper.pagedQueryPublishedProjectFroze(page, pageSize);
            }
            if (projectDetailedInfoVOS.isEmpty()){
                return null; //没有该类型的项目
            }else {
                int i = adminMapper.totalCountOnPublishedProjectFroze();
                PageBean<ProjectDetailedInfoVO> pageBean = new PageBean<>();
                pageBean.setTotal((long) i);
                pageBean.setData(projectDetailedInfoVOS);
                return pageBean;
            }
        }
        return null;
    }

    /**
     * 管理员查看项目申请情况（待审核/被拒绝）
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageBean<AdminQueryProjectApplicationVO> pagedQueryProjectApplication(int page, int pageSize, int status,String keyWord) {
        page = (page - 1) * pageSize;
        if ("".equals(keyWord)){
            keyWord = null;
        }
        List<UserApplicationProject> userApplicationProjects = adminMapper.pagedQueryProjectApplication(page, pageSize, status,keyWord);
        if (userApplicationProjects.isEmpty()){
            return null; //没有数据
        }else {
            List<AdminQueryProjectApplicationVO> list = new ArrayList<>();
            for (UserApplicationProject userApplicationProject : userApplicationProjects) {
                AdminQueryProjectApplicationVO adminQueryProjectApplicationVO = new AdminQueryProjectApplicationVO();
                BeanUtils.copyProperties(userApplicationProject, adminQueryProjectApplicationVO);
                //加入申请时间
                adminQueryProjectApplicationVO.setCreateTime(userApplicationProject.getApplicationTime());
                //申请ID
                adminQueryProjectApplicationVO.setApplicationId(userApplicationProject.getUserApplicationProjectId());
                //申请人用户名
                adminQueryProjectApplicationVO.setCreator(adminMapper.selectUserNameByUserId(userApplicationProject.getUserId()));
                //申请类型
                if (userApplicationProject.getApplicationType() == 0){
                    adminQueryProjectApplicationVO.setApplicationType("发布新项目");
                }else if (userApplicationProject.getApplicationType() == 1){
                    adminQueryProjectApplicationVO.setApplicationType("更新项目");
                }
                //申请的状态
                if (userApplicationProject.getStatus() == 0){
                    adminQueryProjectApplicationVO.setStatus("待定");
                }else if (userApplicationProject.getStatus() == 1){
                    adminQueryProjectApplicationVO.setStatus("通过");
                }else if (userApplicationProject.getStatus() == 2){
                    adminQueryProjectApplicationVO.setStatus("拒绝");
                }
                list.add(adminQueryProjectApplicationVO);
            }
            //记录总条数
            int i = adminMapper.totalCountOnApplicationProject(status,keyWord);
            PageBean<AdminQueryProjectApplicationVO> pageBean = new PageBean<>();
            pageBean.setData(list);
            pageBean.setTotal((long) i);
            return pageBean;
        }
    }

    @Override
    public PageBean<AdminQueryProjectApplicationVO> allQueryProjectApplication(int status) {
        //拿到所有数据
        List<UserApplicationProject> userApplicationProjects = adminMapper.allQueryProjectApplication(status);
        if (userApplicationProjects.isEmpty()){
            return null; //没有数据
        }else {
            List<AdminQueryProjectApplicationVO> list = new ArrayList<>();
            for (UserApplicationProject userApplicationProject : userApplicationProjects) {
                AdminQueryProjectApplicationVO adminQueryProjectApplicationVO = new AdminQueryProjectApplicationVO();
                BeanUtils.copyProperties(userApplicationProject, adminQueryProjectApplicationVO);
                //加入申请时间
                adminQueryProjectApplicationVO.setCreateTime(userApplicationProject.getApplicationTime());
                //申请ID
                adminQueryProjectApplicationVO.setApplicationId(userApplicationProject.getUserApplicationProjectId());
                //申请人用户名
                adminQueryProjectApplicationVO.setCreator(adminMapper.selectUserNameByUserId(userApplicationProject.getUserId()));
                //申请类型
                if (userApplicationProject.getApplicationType() == 0){
                    adminQueryProjectApplicationVO.setApplicationType("发布新项目");
                }else if (userApplicationProject.getApplicationType() == 1){
                    adminQueryProjectApplicationVO.setApplicationType("更新项目");
                }
                //申请的状态
                if (userApplicationProject.getStatus() == 0){
                    adminQueryProjectApplicationVO.setStatus("待定");
                }else if (userApplicationProject.getStatus() == 1){
                    adminQueryProjectApplicationVO.setStatus("通过");
                }else if (userApplicationProject.getStatus() == 2){
                    adminQueryProjectApplicationVO.setStatus("拒绝");
                }
                list.add(adminQueryProjectApplicationVO);
            }
            //记录总条数
            int i = adminMapper.totalCountOnApplicationProject(status,null);
            PageBean<AdminQueryProjectApplicationVO> pageBean = new PageBean<>();
            pageBean.setData(list);
            pageBean.setTotal((long) i);
            return pageBean;
        }
    }
}
