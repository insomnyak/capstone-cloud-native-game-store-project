package com.trilogyed.retailapiservice.util.feign;

import feign.Feign;
import feign.hystrix.HystrixFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class HystrixFeignClientConfiguration {
    @Autowired
    FeignClientDecoder decoder;

    @Autowired
    FeignClientErrorDecoder errorDecoder;

    @Bean
    @Scope("prototype")
    public HystrixFeign.Builder hystrixFeignBuilder() {
        return HystrixFeign.builder()
                .decoder(decoder)
                .errorDecoder(errorDecoder)
                .decode404();
    }
}
