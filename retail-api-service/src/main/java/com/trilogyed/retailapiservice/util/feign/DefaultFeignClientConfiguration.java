package com.trilogyed.retailapiservice.util.feign;

import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DefaultFeignClientConfiguration {
    @Autowired
    FeignClientDecoder decoder;

    @Autowired
    FeignClientErrorDecoder errorDecoder;

    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .decoder(decoder)
                .errorDecoder(errorDecoder)
                .decode404();
    }
}
