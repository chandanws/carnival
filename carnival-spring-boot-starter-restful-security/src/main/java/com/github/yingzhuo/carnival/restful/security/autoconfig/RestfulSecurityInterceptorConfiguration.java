/*
 *  ____    _    ____  _   _ _____     ___    _
 * / ___|  / \  |  _ \| \ | |_ _\ \   / / \  | |
 * | |    / _ \ | |_) |  \| || | \ \ / / _ \ | |
 * | |___/ ___ \|  _ <| |\  || |  \ V / ___ \| |___
 * \____/_/   \_\_| \_\_| \_|___|  \_/_/   \_\_____|
 *
 * https://github.com/yingzhuo/carnival
 */
package com.github.yingzhuo.carnival.restful.security.autoconfig;

import com.github.yingzhuo.carnival.restful.security.AuthenticationListener;
import com.github.yingzhuo.carnival.restful.security.TokenParser;
import com.github.yingzhuo.carnival.restful.security.UserDetailsRealm;
import com.github.yingzhuo.carnival.restful.security.impl.RestfulSecurityInterceptor;
import com.github.yingzhuo.carnival.restful.security.mvc.RestfulSecurityHandlerMethodArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@ConditionalOnWebApplication
@AutoConfigureAfter(RestfulSecurityBeanConfiguration.class)
public class RestfulSecurityInterceptorConfiguration implements WebMvcConfigurer {

    private final TokenParser tokenParser;
    private final UserDetailsRealm userDetailsRealm;
    private final AuthenticationListener authenticationListener;

    public RestfulSecurityInterceptorConfiguration(TokenParser tokenParser, UserDetailsRealm userDetailsRealm, AuthenticationListener authenticationListener) {
        this.tokenParser = tokenParser;
        this.userDetailsRealm = userDetailsRealm;
        this.authenticationListener = authenticationListener;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RestfulSecurityInterceptor(tokenParser, userDetailsRealm, authenticationListener)).addPathPatterns("/", "/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new RestfulSecurityHandlerMethodArgumentResolver());
    }

}
