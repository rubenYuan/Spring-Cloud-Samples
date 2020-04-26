package com.xiu.sleuth.consumer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Author:dy_boom
 * Description:
 * Date:Created in 上午10:12 2017/8/16
 * Copyright (c)  xdy_0722@sina.com All Rights Reserved.
 */
@RestController
public class SleuthConsumerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SleuthConsumerController.class);

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 项目说明
     * @return
     */
    @GetMapping({"/",""})
    public String index(){
        return "this is sleuth-zikpin-consumer";
    }

    /**
     * 远程服务
     * @return
     */
    @GetMapping( "/index")
    public Object sleuthProviderIndex() {

        LOGGER.info("<<<<<<<<<<<<<<<<calling trace demo provider>>>>>>>>>>>>>>>");
        String str =  restTemplate.getForEntity("http://sleuth-zipkin-provider/index", String.class).getBody();
        return str;
    }
}
