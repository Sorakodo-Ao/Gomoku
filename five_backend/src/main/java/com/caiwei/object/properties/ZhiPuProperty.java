package com.caiwei.object.properties;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zhipu")
@Data
public class ZhiPuProperty {
    private String secretKey;

    @Bean
    public ClientV4 clientV4() {
        return new ClientV4.Builder(secretKey).build();
    }
}
