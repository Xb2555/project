package com.qg24.controller;

import com.qg24.dao.LogMapper;
import com.qg24.po.dto.RegisterDTO;
import com.qg24.po.dto.VerifyApplicationDTO;
import com.qg24.po.entity.User;
import com.qg24.po.result.PageBean;
import com.qg24.po.result.Result;
import com.qg24.po.vo.*;
import com.qg24.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CsrfTokenRepository csrfTokenRepository;
    @Autowired
    private LogMapper logMapper;

    /**
     * 注册新用户
     * @param registerDTO
     * @return
     */
    @PostMapping("/register")
    public Result<?> addUser(@RequestBody RegisterDTO registerDTO, HttpServletRequest request, HttpServletResponse response){
        UserAndTokenVO userAndTokenVO = userService.addUser(registerDTO);
        if (userAndTokenVO == null){
            return Result.error("注册失败，账号已存在");
        }else {
            //返回cookies
            User user = userService.findUserByUsername(registerDTO.getUsername());//获得用户信息
            // 创建认证对象
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, Collections.singleton(user.getAuthority()));
            // 设置认证细节
            authentication.setDetails(new WebAuthenticationDetails(request));
            // 设置安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 生成和设置Cookies
            request.getSession(); // 这会触发JSESSIONID Cookie的生成
            response.setHeader("Set-Cookie", "JSESSIONID=" + request.getSession().getId() + "; Path=/; HttpOnly");
            //更新用户操作日志
            int count = logMapper.insertUserOperateLog(userAndTokenVO.getUserId(),"注册");
            return Result.success("注册成功",userAndTokenVO);
        }
    }

    /**
     * 展示用户自己发布的项目
     * @param userId
     * @return
     */
    @GetMapping("/showSelfProjects")
    public Result<?> showUserSelfProjects(@RequestParam("userId") int userId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        PageBean<ShowSelfProjectsVO> pageBean = userService.showUserSelfProjects(userId, page, pageSize);
        if (pageBean == null){
            return Result.error("用户没有发布过项目");
        }else {
            return Result.success("查询成功", pageBean);
        }
    }

    /**
     * 查询用户拥有监控权限的项目
     * @param userId
     * @return
     */
    @GetMapping("/showHaveMonitorPermissionProjects")
    public Result<?> showHaveMonitorPermissionProjects (@RequestParam("userId") int userId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        PageBean<ShowHaveMonitorPermissionProjectsVO> list = userService.showHaveMonitorPermissionProjects(userId, page, pageSize);
        if (list == null){
            return Result.error("该用户没有拥有监控权限的项目");
        }else {
            return Result.success("查询成功", list);
        }
    }

    /**
     * 查看我的申请(申请监控权限)
     * @param userId
     * @return
     */
    @GetMapping("/myApplicationOnMonitorProject")
    public Result<?> myApplicationOnMonitorProject (@RequestParam("userId") int userId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        PageBean<MyApplicationOnMonitorProjectVO> myApplicationOnMonitorProjectVOS = userService.myApplicationOnMonitorProject(userId, page, pageSize);
        if (myApplicationOnMonitorProjectVOS == null){
            return Result.error("没有发出过申请（申请权限）");
        }else {
            return Result.success("查询成功", myApplicationOnMonitorProjectVOS);
        }
    }

    /**
     * 查看用户发布或更新项目的申请
     * @param userId
     * @return
     */
    @GetMapping("/myApplicationProject")
    public Result<?> myApplicationProject(@RequestParam("userId") int userId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        PageBean<MyApplicationProjectVO> myApplicationProjectVOS = userService.myApplicationProject(userId, page, pageSize);
        if (myApplicationProjectVOS == null){
            return Result.error("没有申请过发布或更新项目");
        }else {
            return Result.success("查询成功", myApplicationProjectVOS);
        }
    }

    /**
     * 查看我收到的申请
     * @param userId
     * @return
     */
    @GetMapping("/receivedMonitorApplication")
    public Result<?> receivedMonitorApplication(@RequestParam("userId") int userId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        PageBean<ReceivedMonitorApplicationVO> list = userService.receivedMonitorApplication(userId, page, pageSize);
        if (list == null){
            return Result.error("没有收到申请");
        }else {
            return Result.success("查询成功", list);
        }
    }

    /**
     * 用户审核项目监控申请
     * @param verifyApplicationDTO
     * @return
     */
    @PostMapping("/verifyMonitorApplication")
    public Result<?> verifyMonitorApplication(@RequestBody VerifyApplicationDTO verifyApplicationDTO){
         int i = userService.verifyMonitorApplication(verifyApplicationDTO);
         if (i != 0 ){
             return Result.success("审核成功");
         }else {
             return Result.error("审核失败");
         }
    }

}
