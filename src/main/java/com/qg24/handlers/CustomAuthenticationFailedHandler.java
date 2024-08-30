package com.qg24.handlers;

import com.alibaba.fastjson.JSON;
import com.qg24.dao.BannedIpMapper;
import com.qg24.dao.UserMapper;
import com.qg24.po.entity.User;
import com.qg24.po.result.Result;
import com.qg24.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomAuthenticationFailedHandler implements AuthenticationFailureHandler {
    private UserMapper userMapper = SpringContextUtil.getBean(UserMapper.class);
    @Autowired
    private BannedIpMapper bannedIpMapper;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 设置响应内容类型和状态码
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        User user = userMapper.selectUserByUsername(username);
        if(user==null){
            //不存在该账户
            response.getWriter().write(JSON.toJSONString(Result.error("账号不存在")));
        }else{
            //存在该账户
            if(user.getEnabled()==0 && compareDate(user.getDeadline())>=0){
                //被冻结且未到达冻结截至时间
                response.getWriter().write(JSON.toJSONString(Result.error("账号被冻结,有疑问联系管理员")));
            }else{
                //密码错误
                response.getWriter().write(JSON.toJSONString(Result.error("密码错误")));
            }
        }
    }
    private int compareDate(String Time){
        //比较时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(Time,formatter);
        LocalDateTime now = LocalDateTime.now().withNano(0);
        return  dateTime.compareTo(now);
    }
}
