package com.qg24.handlers;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qg24.dao.LogMapper;
import com.qg24.dao.UserMapper;
import com.qg24.po.entity.User;
import com.qg24.po.result.Result;
import com.qg24.utils.JwtUtils;
import com.qg24.utils.SpringContextUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义登录成功响应
 */

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();

    private UserMapper userMapper = SpringContextUtil.getBean(UserMapper.class);

    private LogMapper logMapper = SpringContextUtil.getBean(LogMapper.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        Map<String, Object> claims = new HashMap<>();
        String username = request.getParameter("username");
        //获取用户信息
        User user = userMapper.selectUserByUsername(username);
        claims.put("userRole",user.getRole());
        //生成token
        String token = JwtUtils.generateToken(claims);
        // 设置响应内容类型和状态码
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> responseData = new HashMap<>();
        //存入token
        responseData.put("token",token);
        //存入userId
        responseData.put("userId",user.getUserId());       //返回数据
        //插入数据
        logMapper.insertUserOperateLog(user.getUserId(), "登录");
        response.getWriter().write(JSON.toJSONString(Result.success("登录成功",responseData)));
        //更新冻结状态
        if(user.getEnabled()==0){
            user.setEnabled(1);
            //将冻结状态改回未冻结
            userMapper.updateEnabled(user);
        }
    }
}
