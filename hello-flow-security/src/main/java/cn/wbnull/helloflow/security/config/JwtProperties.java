/*
 * Copyright (c) 2017-2026 null. All rights reserved.
 */

package cn.wbnull.helloflow.security.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置属性
 *
 * @author null
 * @date 2026-05-28
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;

    @PostConstruct
    public void validate() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT密钥未配置，请设置环境变量 JWT_SECRET");
        }
        if (secret.length() < 32) {
            throw new IllegalStateException("JWT密钥长度不足，至少需要32个字符");
        }
    }
}
