/*
 *  ____    _    ____  _   _ _____     ___    _
 * / ___|  / \  |  _ \| \ | |_ _\ \   / / \  | |
 * | |    / _ \ | |_) |  \| || | \ \ / / _ \ | |
 * | |___/ ___ \|  _ <| |\  || |  \ V / ___ \| |___
 * \____/_/   \_\_| \_\_| \_|___|  \_/_/   \_\_____|
 *
 * https://github.com/yingzhuo/carnival
 */
package com.github.yingzhuo.carnival.restful.security;

import com.github.yingzhuo.carnival.restful.security.annotation.AuthenticationComponent;
import com.github.yingzhuo.carnival.restful.security.annotation.Requires;
import com.github.yingzhuo.carnival.restful.security.core.CheckUtils;
import com.github.yingzhuo.carnival.restful.security.exception.RestfulSecurityException;
import com.github.yingzhuo.carnival.restful.security.userdetails.UserDetails;

import java.lang.annotation.*;

/**
 * @author 应卓
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Requires(RequiresPermissions.AuthComponent.class)
public @interface RequiresPermissions {

    public String[] value();

    public Logical logical() default Logical.OR;

    public String errorMessage() default ":::<NO MESSAGE>:::";

    public static class AuthComponent implements AuthenticationComponent<RequiresPermissions> {

        @Override
        public void authenticate(UserDetails userDetails, RequiresPermissions annotation) throws RestfulSecurityException {
            CheckUtils.check(annotation);
        }
    }

}
