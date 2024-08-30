package com.qg24.filters;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 记录认证授权等异常信息
 */
@Component
public class CustomExceptionTranslationFilter extends ExceptionTranslationFilter {
    public CustomExceptionTranslationFilter() {
        super(new LoginUrlAuthenticationEntryPoint("/login"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            try {
                chain.doFilter(request, response);
            } catch (Exception e) {
                // 记录异常信息
                System.out.println("Security exception: " + e.getMessage());
                // 你可以在这里添加更多的处理逻辑，例如记录到数据库或发送通知
                throw e;
            }
    }
}
