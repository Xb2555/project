package com.qg24.interceptor;

import com.qg24.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j

//拦截器
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws IOException {
        String requestURI = request.getRequestURI();

            // 找到第一个斜杠的位置
            int firstSlashIndex = requestURI.indexOf('/');
            if (firstSlashIndex == -1) {
                // 如果没有找到斜杠，返回 null
                return false;
            }
            // 找到第二个斜杠的位置
            int secondSlashIndex = requestURI.indexOf('/', firstSlashIndex + 1);
            if (secondSlashIndex == -1) {
                // 如果没有找到第二个斜杠，返回 null
                return false;
            }
            // 截取两个斜杠之间的部分
            String segment = requestURI.substring(firstSlashIndex + 1, secondSlashIndex);

            //获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                // 去掉 "Bearer " 前缀
                token = token.substring(7);
            }
        Claims claims = JwtUtils.parseJwt(token);
        String userRole = (String) claims.get("userRole");

        //判断访问的是不是管理员接口
        if ("admin".equals(segment)) {
            //如果是，就判断用户是不是管理员
            if (!"ADMIN".equals(userRole)) {
                //不是管理员则重定向
                response.sendRedirect("/login");
                return false;
            }
            //是管理员正常查看token是否过期
        }
        return token != null && !JwtUtils.isTokenExpired(token);
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {


    }

}
