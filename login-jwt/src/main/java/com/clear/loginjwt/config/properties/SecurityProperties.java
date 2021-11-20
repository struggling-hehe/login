package com.clear.loginjwt.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  10:42 上午
 */

@Data
@ConfigurationProperties(SecurityProperties.PREFIX)
public class SecurityProperties {

    /**
     * 认证配置前缀{@value}
     */
    public static final String PREFIX = "surge";

    private Map<String, List<String>> antMatchers;
}
