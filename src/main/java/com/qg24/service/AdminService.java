package com.qg24.service;

import com.qg24.po.dto.FreezeProjectDTO;
import com.qg24.po.dto.FreezeUserDTO;
import com.qg24.po.dto.VerifyApplicationDTO;
import com.qg24.po.result.PageBean;
import com.qg24.po.vo.AdminQueryProjectApplicationVO;
import com.qg24.po.vo.AdminQueryUserVO;
import com.qg24.po.vo.ProjectDetailedInfoVO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AdminService {

    //管理员对于项目发布、更新项目的审核
    int verifyApplication(VerifyApplicationDTO verifyApplicationDTO);

    //查看用户详情
    AdminQueryUserVO showUserDetailedInfo(int userId);

    //冻结用户
    int freezeUser (FreezeUserDTO freezeUserDTO);

    //冻结项目
    int freezeProject(FreezeProjectDTO freezeProjectDTO);

    //管理查看已发布的项目，（未冻结/已冻结）
    PageBean<ProjectDetailedInfoVO> pagedQueryPublishedProject(int page, int pageSize, int enabled, String keyWord);

    //管理员查看项目申请情况（待审核/被拒绝）
    PageBean<AdminQueryProjectApplicationVO> pagedQueryProjectApplication(int page, int pageSize, int status,String keyWord);

    //管理员查看所有项目申请情况
    PageBean<AdminQueryProjectApplicationVO> allQueryProjectApplication(int status);
}
