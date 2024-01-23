package com.htnova.security.config;

import com.htnova.common.constant.AppConst;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.dto.Result;
import com.htnova.common.util.response.ResponseHandler;
import com.htnova.mt.order.dto.UserPoiDto;
import com.htnova.mt.order.mapper.UserPoiMapper;
import com.htnova.security.entity.UserDetail;
import com.htnova.security.exception.EmptyPermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //  启用方法级别的权限认证
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private SecurityConfig securityConfig;

    @Resource
    private ServerProperties serverProperties;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private UserPoiMapper userPoiMapper;

    @Override
    // 认证和授权的逻辑自定义，需要注意的是：不能在httpSecurity里直接配置 authenticationProvider
    // 这样会配置出一个 ProviderManager，parent 是 另一个 ProviderManager，其中只有一个DaoAuthenticationProvider
    // 如果我们的AuthenticationProvider认证失败，则结果由 parent ProviderManager 的
    // DaoAuthenticationProvider决定，不符合我们的预期
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(getAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(serverProperties.getError().getPath(), AppConst.APP_URL_PREFIX + "/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
            .csrf()
            .disable() // 禁用 CSRF
            .requestCache()
            .requestCache(new HttpSessionRequestCache())
            .and()
            // ExceptionTranslationFilter 异常处理使用
            .exceptionHandling()
            // 未登录的请求，请求失败处理
            .authenticationEntryPoint(getAuthenticationEntryPoint())
            // 未授权的请求，请求失败处理
            .accessDeniedHandler(getAccessDeniedHandler())

            .and()
            .formLogin()
            .loginProcessingUrl(securityConfig.getLoginProcessingUrl())
            // 登录成功后的处理
            .successHandler(getAuthenticationSuccessHandler())
            // 登录失败后的处理
            .failureHandler(getAuthenticationFailureHandler())
            .and()
            // 退出处理
            .logout()

            .permitAll()
            .logoutUrl(securityConfig.getLogoutUrl())
            // 退出默认302到登录页
            .logoutSuccessUrl(securityConfig.getLoginPage())
            // ajax请求退出后，返回 LOGOUT_SUCCESS
            .defaultLogoutSuccessHandlerFor(
                (request, response, authentication) ->
                    ResponseHandler.create(Result.build(ResultStatus.LOGOUT_SUCCESS)).handle(request, response),
                getAjaxHttpRequestMatcher()
            )
            .and()
            .authorizeRequests()
            .antMatchers(
                securityConfig.getLoginPage(),
                "/druid/**",
                "/file/download/**",
                "/order/**",
                "/mt/**",
                "/ele/**",
                "/swagger*//**",
                "/v2/api-docs",
                "/webjars*//**",
                "/socket*//**"
            )
            .permitAll()
            .anyRequest()
            .authenticated() // 其他地址的访问均需验证权限
            .and()
            .headers()
            .frameOptions()
            .disable(); // sql监控页面 iframe 配置
    }

    /**
     * 允许跨域
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");  // 允许所有来源的跨域请求
        configuration.addAllowedMethod("*");  // 允许所有 HTTP 方法
        configuration.addAllowedHeader("*");  // 允许所有头部

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsWebFilter(source);
    }

    private RequestMatcher getAjaxHttpRequestMatcher() {
        return new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest");
    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider() {

            @Override
            protected Authentication createSuccessAuthentication(
                Object principal,
                Authentication authentication,
                UserDetails user
            ) {
                UserDetail userDetail = (UserDetail) user;
                if (CollectionUtils.isEmpty(userDetail.getUser().getPermissionList())) {
                    throw new EmptyPermissionException("未分配权限，不能登录");
                }
                return super.createSuccessAuthentication(principal, authentication, user);
            }
        };
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }

    private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            private final RequestCache requestCache = new HttpSessionRequestCache();
            private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            @Override
            public void onAuthenticationSuccess(
                HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication
            )
                throws IOException, ServletException {
                SavedRequest savedRequest = this.requestCache.getRequest(request, response);
                // 登录成功后跳转的URL，由后端决定。优先级如下：
                // 1. 上次失败的 ajax 请求的 Referer
                // 2. 上次失败的 请求的 URL
                // 3. 登录成功URL
                String redirectUrl = Optional
                    .ofNullable(savedRequest)
                    .flatMap(
                        sq ->
                            sq.getHeaderNames().contains("X-Requested-With")
                                ? sq.getHeaderValues("Referer").stream().findFirst()
                                : Optional.ofNullable(sq.getRedirectUrl())
                    )
                    .orElse(securityConfig.getLoginSuccessPage());
                Map<String, Object> result = new HashMap<>();
                HttpSession session = request.getSession();
                String encodedString = Base64.getEncoder().encodeToString(session.getId().getBytes());
                result.put("SESSION",encodedString);
                result.put("redirectUrl", redirectUrl);
                result.put("userName", ((UserDetail) authentication.getPrincipal()).getUsername());
                result.put("userid", ((UserDetail) authentication.getPrincipal()).getUser().getId().toString());
                UserPoiDto up = new UserPoiDto();
                up.setUId(((UserDetail) authentication.getPrincipal()).getUser().getId());

                ResponseHandler responseHandler = ResponseHandler
                    .builder()
                    // ajax请求 返回 LOGIN_SUCCESS，并返回 redirectUrl
                    .handlerFor(
                        ResponseHandler.create(Result.build(ResultStatus.LOGIN_SUCCESS, result,userPoiMapper.findList(up))),
                        getAjaxHttpRequestMatcher()
                    )
                    // 默认 直接302 到 redirectUrl
                    .defaultHandler(
                        (request1, response1) -> redirectStrategy.sendRedirect(request1, response1, redirectUrl)
                    )
                    .build();
                responseHandler.handle(request, response);
            }
        };
    }

    private AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            @Override
            public void onAuthenticationFailure(
                HttpServletRequest request,
                HttpServletResponse response,
                AuthenticationException e
            )
                throws IOException, ServletException {
                Result<?> result = translateAuthenticationException(e);
                ResponseHandler responseHandler = ResponseHandler
                    .builder()
                    // ajax请求 返回 LOGIN_SUCCESS，并返回 redirectUrl
                    .handlerFor(ResponseHandler.create(result), getAjaxHttpRequestMatcher())
                    // 默认 直接302 到 登录页
                    .defaultHandler(
                        (request1, response1) ->
                            redirectStrategy.sendRedirect(
                                request1,
                                response1,
                                securityConfig.getLoginPage() + "?error=" + result.getCode()
                            )
                    )
                    .build();
                responseHandler.handle(request, response);
            }
        };
    }

    public static Result<?> translateAuthenticationException(AuthenticationException e) {
        log.error("error:", e);
        if (e instanceof AccountStatusException) {
            return Result.build(HttpStatus.BAD_REQUEST, ResultStatus.ACCOUNT_FREEZE);
        } else if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            return Result.build(HttpStatus.BAD_REQUEST, ResultStatus.USERNAME_PASSWORD_ERROR);
        } else if (e instanceof EmptyPermissionException) {
            return Result.build(HttpStatus.BAD_REQUEST, ResultStatus.EMPTY_PERMISSION);
        }
        return Result.build(HttpStatus.BAD_REQUEST, ResultStatus.LOGIN_ERROR);
    }

    private AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
                throws IOException, ServletException {
                ResponseHandler responseHandler = ResponseHandler
                    .builder()
                    // ajax请求 返回 FORBIDDEN
                    .handlerFor(
                        ResponseHandler.create(Result.build(HttpStatus.UNAUTHORIZED, ResultStatus.UNAUTHORIZED)),
                        getAjaxHttpRequestMatcher()
                    )
                    // 默认 直接302 到 登录页面
                    .defaultHandler(
                        (request1, response1) ->
                            redirectStrategy.sendRedirect(
                                request1,
                                response1,
                                securityConfig.getLoginPage() + "?error=" + ResultStatus.UNAUTHORIZED.getCode()
                            )
                    )
                    .build();
                responseHandler.handle(request, response);
            }
        };
    }

    private AccessDeniedHandler getAccessDeniedHandler() {
        return new AccessDeniedHandler() {
            private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
                throws IOException, ServletException {
                ResponseHandler responseHandler = ResponseHandler
                    .builder()
                    // ajax请求 返回 FORBIDDEN
                    .handlerFor(
                        ResponseHandler.create(Result.build(HttpStatus.FORBIDDEN, ResultStatus.FORBIDDEN)),
                        getAjaxHttpRequestMatcher()
                    )
                    // 默认 直接302 到 错误页面
                    .defaultHandler(
                        (request1, response1) ->
                            redirectStrategy.sendRedirect(
                                request1,
                                response1,
                                securityConfig.getErrorPage() + "?error=" + ResultStatus.FORBIDDEN.getCode()
                            )
                    )
                    .build();
                responseHandler.handle(request, response);
            }
        };
    }
}
