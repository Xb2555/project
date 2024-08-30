package com.qg24.service.impl;

import com.qg24.dao.LogMapper;
import com.qg24.dao.ProjectMapper;
import com.qg24.dao.UserMapper;
import com.qg24.dao.WarningMessageMapper;
import com.qg24.po.dto.*;
import com.qg24.po.entity.*;
import com.qg24.po.result.PageBean;
import com.qg24.po.vo.ProjectDetailedInfoVO;
import com.qg24.po.vo.ProjectDisplayVO;
import com.qg24.po.vo.UserOwnMonitorForProjectVO;
import com.qg24.service.LogService;
import com.qg24.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProjectServiceImpl implements ProjectService {


    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LogService logService;
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private WarningMessageMapper warningMessageMapper;

    /**
     *
     * 普通用户主界面，查看未冻结的所有项目
     *
     */
    @Override
    public PageBean<ProjectDisplayVO> showAllProjectForUser(int page, int pageSize,String projectName) {
        PageBean<ProjectDisplayVO> pageBean = new PageBean<>();
        List<Project> projectList = new ArrayList<>();
        if (projectName != null && !"".equals(projectName)) {
            //projectName有数据
            projectName = "%" + projectName + "%";
        }
        if (pageSize == 0){
            // 如果pageSize等于0则查看全表信息
            projectList = projectMapper.selectAllProjectForUser(projectName);
        }else {
            // 否则分页查询
            int offset = (page - 1) * pageSize;
            projectList = projectMapper.pageQueryAllProjectForUser(offset, pageSize,projectName);
        }
        // 获取总的数据数
        long total = projectMapper.selectTotalNumberProjectForUser(projectName);
        pageBean.setTotal(total);
        //进行VO包装
        List<ProjectDisplayVO> projectDisplayVOList = new ArrayList<>();
        for (Project project : projectList){
            ProjectDisplayVO projectDisplayVO = new ProjectDisplayVO();
            BeanUtils.copyProperties(project, projectDisplayVO);
            //将无法映射的属性进行设置
            projectDisplayVO.setCreator(project.getUsername());
            //新增到list集合当中
            projectDisplayVOList.add(projectDisplayVO);
        }
        pageBean.setData(projectDisplayVOList);
        return pageBean;
    }

    /**
     * 用户发布软件申请
     *
     */
    @Override
    public synchronized int publishProject(PublishProjectDTO publishProjectDTO) {
        // 获取项目名
        String projectName = publishProjectDTO.getProjectName();
        // 查找是否存在这个项目
        if (projectMapper.selectProjectByProjectName(projectName) != null){
            // 存在则无法上传这个项目名对应的项目
            return 0;
        }else {
            // 不存在则可以申请该项目
            //向用户操作表加入数据
            int count = logMapper.insertUserOperateLog((int) publishProjectDTO.getUserId(),"发布新项目");
            return projectMapper.addUserApplicationProject(publishProjectDTO)+count;

        }
    }

    /**
     * 查看软件详情信息
     *
     */
    @Override
    public ProjectDetailedInfoVO detaliedInfo(int projectId) {
        // 根据项目id获取项目信息
        Project project = projectMapper.selectProjectByProjectId(projectId);
        ProjectDetailedInfoVO projectDetailedInfoVO = null;
        if (project != null){
            // 对查询到的数据进行包装
            projectDetailedInfoVO = new ProjectDetailedInfoVO();
            BeanUtils.copyProperties(project, projectDetailedInfoVO);
            //将无法映射的数据手动配置
            projectDetailedInfoVO.setCreator(project.getUsername());
        }

        return projectDetailedInfoVO;
    }

    /**
     * 用户申请监测项目权限
     *
     */
    @Override
    public synchronized int applyMonitorPermission(UserApplyMonitorPermissionDTO userApplyMonitorPermissionDTO) {

        // 根据项目id 和 申请人id 查找是否存在该待办的申请
        ApplyMonitorProject applyMonitorProject = projectMapper.selectMonitorApplicationByProjectIdAndApplicantIdInPending(userApplyMonitorPermissionDTO.getUserId(),userApplyMonitorPermissionDTO.getProjectId());
        if (applyMonitorProject != null){
            //存在该申请
            return -1;
        }else {
            //不存在该申请

            // 根据项目id查找对应的项目信息
            Project project = projectMapper.selectProjectByProjectId(userApplyMonitorPermissionDTO.getProjectId());
            // 根据项目查找到对应的项目发布者
            String username = project.getUsername();
            // 根据项目发布者的username获取到项目发布者的用户信息
            User user = userMapper.selectUserByUsername(username);
            // 根据用户信息获取到用户的id
            int masterId = user.getUserId();
            if (masterId == 0){
                // 若不存在该用户则不存在该项目
                return 0;
            }else {
                // 若用户存在则发送监测申请
                //向用户操作日志插入新数据
                int count = logMapper.insertUserOperateLog(userApplyMonitorPermissionDTO.getUserId(),"申请监测项目权限");
                return count+projectMapper.addApplyMonitorProject(userApplyMonitorPermissionDTO.getUserId(),userApplyMonitorPermissionDTO.getProjectId(),masterId);
            }
        }

    }

    /**
     * 发布者更新项目
     *
     */
    @Override
    public synchronized int updateProject(UpdateProjectDTO updateProjectDTO) {
        // 判断是否已经存在这个更新申请，是待处理的申请
        UserApplicationProject userApplicationProject = projectMapper.selectApplicationUpdateProjectByProjectIdInPending(updateProjectDTO);
        if (userApplicationProject != null){
            // 存在这个申请
            return 0;
        }else {
            // 不存在这个申请
            // 根据项目id查找项目信息
            Project project = projectMapper.selectProjectByProjectId(updateProjectDTO.getProjectId());
            if (!project.getProjectPassword().equals(updateProjectDTO.getProjectPassword())){
                // 口令错误
                return -1;
            }else {
                // 获取项目名称
                String projectName = project.getProjectName();
                UpdateProjectInfoDTO updateProjectInfoDTO = new UpdateProjectInfoDTO();
                // 封装数据
                BeanUtils.copyProperties(updateProjectDTO, updateProjectInfoDTO);
                updateProjectInfoDTO.setProjectName(projectName);
                // 插表
                int count = logMapper.insertUserOperateLog(updateProjectDTO.getUserId(),"更新项目");
                return count+projectMapper.addUserApplicationUpdateProject(updateProjectInfoDTO);
            }


        }

    }

    /**
     * 查询对某个项目有监测权限的用户
     * @param projectId
     * @return
     */
    @Override
    public List<UserOwnMonitorForProjectVO> queryOwnMonitorUser(int projectId) {
        List<UserMonitorProject> userMonitorProjectList = projectMapper.selectUserMonitorProjectByProjectId(projectId);
        List<UserOwnMonitorForProjectVO> userOwnMonitorForProjectVOList = new ArrayList<>();
        for (UserMonitorProject userMonitorProject : userMonitorProjectList){
            // 根据用户id查询用户名
            int userId = userMonitorProject.getUserId();
            User user = userMapper.selectUserByUserId(userId);
            String userName = user.getUsername();
            // 包装数据
            UserOwnMonitorForProjectVO userOwnMonitorForProjectVO = new UserOwnMonitorForProjectVO();
            BeanUtils.copyProperties(userMonitorProject, userOwnMonitorForProjectVO);
            // 将无法映射的属性进行手动包装
            userOwnMonitorForProjectVO.setUsername(userName);
            // 封装到list集合当中
            userOwnMonitorForProjectVOList.add(userOwnMonitorForProjectVO);
        }
        return userOwnMonitorForProjectVOList;

    }

    /**
     * 撤销某个用户对项目的监测权限
     * @param cancelUserMonitorPermissionDTO
     * @return
     */
    @Override
    public int cancelUserMonitorPermission(CancelUserMonitorPermissionDTO cancelUserMonitorPermissionDTO) {
        int count = logMapper.insertUserOperateLog(cancelUserMonitorPermissionDTO.getUserId(),"被撤销监测权限");
        return count+projectMapper.deleteUserMonitorProjectByProjectIdAndUserId(cancelUserMonitorPermissionDTO);
    }

    /**
     * 发布者移除项目，需要项目口令
     * @param deleteProjectDTO
     * @return
     */
    @Override
    public int deleteProject(DeleteProjectDTO deleteProjectDTO) {
        // 根据项目id 查询项目信息
        Project project = projectMapper.selectProjectByProjectId(deleteProjectDTO.getProjectId());
        if (project == null){
            // 该项目已经不存在了
            return -2;
        }
        String projectPassword = project.getProjectPassword();
        if (projectPassword.equals(deleteProjectDTO.getProjectPassword())){
            // 项目口令正确
            //插入用户操作表
            String username = project.getUsername();
            User user = userMapper.selectUserByUsername(username);
            int count = logMapper.insertUserOperateLog(user.getUserId(),"移除项目");
            return count+projectMapper.deleteProjectByProjectId(deleteProjectDTO.getProjectId());
        }else {
            // 项目口令错误
            return -1;
        }
    }

    @Override
    public String checkMonitorAuth(CheckMonitorAuthDTO checkMonitorAuthDTO) {
        //根据用户id查询用户信息
        User user = userMapper.selectUserByUserId(checkMonitorAuthDTO.getUserId());
        String username = user.getUsername();
        // 查询用户是否是该项目的发布者
        Project project = projectMapper.selectProjectByProjectIdAndUsername(checkMonitorAuthDTO.getProjectId(),username);
        if (project != null){
            // 是项目发布者
            return "发布者";
        }else {
            // 不是项目发布者

            // 查询用户是否有项目监测权限
            UserMonitorProject userMonitorProject = projectMapper.selectUserMonitorProjectByProjectIdAndUserId(checkMonitorAuthDTO.getProjectId(),checkMonitorAuthDTO.getUserId());
            if (userMonitorProject != null){
                //拥有监测权限
                return "拥有监测权限的用户";
            }else {
                //没有监测权限
                return "普通用户";
            }
        }
    }

    //更新报警阈值
    @Override
    public int updateWarningRate(SetErrorRateDTO setErrorRateDTO) {
        if(setErrorRateDTO.getErrorRate()>1){
            //报警阈值不能大于1
            return 0;
        }
        WarningMessage wm = warningMessageMapper.selectNewMessageByProjectId(setErrorRateDTO.getProjectId());
        if(wm != null){
            warningMessageMapper.checkMessage(setErrorRateDTO.getProjectId(),0);
        }
        return projectMapper.updateWarningRate(setErrorRateDTO.getProjectId(),setErrorRateDTO.getErrorRate());
    }

    //更新报错数报错率
    @Override
    public void updateErrorNumAndRate(int insertNum,int projectId) {
        //拿到原始数据
        ProjectPresentationData projectPresentationData = projectMapper.getProjectData(projectId);
        //更新数据
        int errorNum = projectPresentationData.getErrorNumber()+insertNum;
        //获取所有的日志数
        int total = logService.getTotalLogNum(projectId)+insertNum;
        //更新报错率
        double errorRate = (double) errorNum / total;
        //TODO 若报错率大于阈值，提醒发布者和管理员
        //更新数据
        int count = projectMapper.updateErrorRateAndNum(errorNum,errorRate,projectId);
    }

    //更新报错率
    @Override
    public void updateErrorRate(int insertNum,int projectId) {
        //拿到原始数据
        ProjectPresentationData projectPresentationData = projectMapper.getProjectData(projectId);
        //更新数据
        int errorNum = projectPresentationData.getErrorNumber();
        //获取所有的日志数
        int total = logService.getTotalLogNum(projectId)+insertNum;
        //更新报错率
        double errorRate = (double) errorNum / total;
        //更新数据
        int count = projectMapper.updateErrorRateAndNum(errorNum,errorRate,projectId);
    }

    //更新后台报错数
    @Override
    public void updateBackendErrorNum(int insertNum, int projectId) {
        ProjectPresentationData data = projectMapper.getProjectData(projectId);
        int errorNum = data.getBackendErrorNumber()+insertNum;
        projectMapper.updateBackendErr(errorNum, projectId);
    }

    //更新前端报错数
    @Override
    public void updateFrontErrorNum(int insertNum, int projectId) {
        ProjectPresentationData data = projectMapper.getProjectData(projectId);
        int errorNum = data.getFrontErrorNumber()+insertNum;
        projectMapper.updateFrontErr(errorNum, projectId);
    }

    //更新移动报错数
    @Override
    public void updateMobileErrorNum(int insertNum, int projectId) {
        ProjectPresentationData data = projectMapper.getProjectData(projectId);
        int errorNum = data.getMobileErrorNumber()+insertNum;
        projectMapper.updateMobileErr(errorNum, projectId);
    }


}
