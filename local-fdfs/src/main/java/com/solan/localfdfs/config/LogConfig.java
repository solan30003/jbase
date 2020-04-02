package com.solan.localfdfs.config;

import com.solan.jbase.aspect.LogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO:
 *
 * @author: hyl
 * @date: 2020/3/31 8:17
 */
@Configuration
public class LogConfig {
    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
