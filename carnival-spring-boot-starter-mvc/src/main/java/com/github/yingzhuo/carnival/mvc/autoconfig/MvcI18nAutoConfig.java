/*
 *  ____    _    ____  _   _ _____     ___    _
 * / ___|  / \  |  _ \| \ | |_ _\ \   / / \  | |
 * | |    / _ \ | |_) |  \| || | \ \ / / _ \ | |
 * | |___/ ___ \|  _ <| |\  || |  \ V / ___ \| |___
 * \____/_/   \_\_| \_\_| \_|___|  \_/_/   \_\_____|
 *
 * https://github.com/yingzhuo/carnival
 */
package com.github.yingzhuo.carnival.mvc.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * @author 应卓
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "carnival.mvc.i18n", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(MvcI18nAutoConfig.Props.class)
public class MvcI18nAutoConfig implements WebMvcConfigurer {

    private final Props props;

    public MvcI18nAutoConfig(Props props) {
        this.props = props;
    }

    @Bean
    @ConditionalOnMissingBean(LocaleResolver.class)
    public LocaleResolver localeResolver() {
        return new CookieLocaleResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName(props.getParamName());
        interceptor.setIgnoreInvalidLocale(props.isIgnoreInvalidLocale());
        registry.addInterceptor(interceptor).addPathPatterns("/", "/**");
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "carnival.mvc.i18n")
    static class Props {
        private boolean enabled = false;
        private String paramName = "locale";
        private boolean ignoreInvalidLocale = true;
    }

}
