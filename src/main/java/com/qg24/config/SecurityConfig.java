package com.qg24.config;

import com.qg24.handlers.CustomAuthenticationFailedHandler;
import com.qg24.handlers.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    //配置用户权限安全认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //从数据库中获取用户信息进行比对
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    //配置http请求安全防护
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()//配置请求授权
                .antMatchers("/websocket/**").permitAll()//websocket路径
                .antMatchers("/user/register").permitAll()//所有人都可注册
                .antMatchers("/login").permitAll()
                .antMatchers("/sdk/**").permitAll()//sdk访问路径
                .and()
                .formLogin()
                .loginPage("/login")//登录
                .successHandler(customAuthenticationSuccessHandler())//使用自定义成功处理器
                .failureHandler(customAuthenticationFailedHandler())//使用自定义失败处理器
                .permitAll()
                .and()
                .logout().permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //返回一个密码编码器
        return new BCryptPasswordEncoder(10);
    }

    //自定义认证成功处理器
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    //自定义认证失败处理器
    @Bean
    public CustomAuthenticationFailedHandler customAuthenticationFailedHandler() {
        return new CustomAuthenticationFailedHandler();
    }

    //cookie Csrf令牌生成
    @Bean
    public CookieCsrfTokenRepository csrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }
}
