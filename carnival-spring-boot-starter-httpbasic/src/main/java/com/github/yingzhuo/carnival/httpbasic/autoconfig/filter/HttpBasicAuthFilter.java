/*
 *  ____    _    ____  _   _ _____     ___    _
 * / ___|  / \  |  _ \| \ | |_ _\ \   / / \  | |
 * | |    / _ \ | |_) |  \| || | \ \ / / _ \ | |
 * | |___/ ___ \|  _ <| |\  || |  \ V / ___ \| |___
 * \____/_/   \_\_| \_\_| \_|___|  \_/_/   \_\_____|
 *
 * https://github.com/yingzhuo/carnival
 */
package com.github.yingzhuo.carnival.httpbasic.autoconfig.filter;

import com.github.yingzhuo.carnival.httpbasic.autoconfig.handler.HttpBasicAuthFailureHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author 应卓
 */
public class HttpBasicAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";

    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final HttpBasicAuthFailureHandler failureHandler;
    private final String username;
    private final String password;
    private final String[] antPatterns;

    public HttpBasicAuthFilter(HttpBasicAuthFailureHandler failureHandler, String username, String password, String[] antPatterns) {
        this.failureHandler = failureHandler;
        this.username = username;
        this.password = password;
        this.antPatterns = antPatterns;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (Arrays.stream(antPatterns).noneMatch(it -> pathMatcher.match(it, request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }

        final String header = request.getHeader(AUTHORIZATION);

        if (header == null) {
            failureHandler.handle(request, response);
            return;
        }

        if (!header.startsWith(BASIC)) {
            failureHandler.handle(request, response);
            return;
        }

        String usernameAndPassword = header.substring(BASIC.length());
        usernameAndPassword = new String(Base64.getUrlDecoder().decode(usernameAndPassword.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

        final String[] up = usernameAndPassword.split(":");

        if (up.length != 2) {
            failureHandler.handle(request, response);
            return;
        }

        if (!StringUtils.equals(username, up[0]) || !StringUtils.equals(password, up[1])) {
            failureHandler.handle(request, response);
            return;
        }

        doFilter(request, response, filterChain);
    }

}
