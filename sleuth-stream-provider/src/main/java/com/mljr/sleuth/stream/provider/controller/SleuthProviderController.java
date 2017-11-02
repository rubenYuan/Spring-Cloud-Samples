package com.mljr.sleuth.stream.provider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:dy_boom
 * Description:
 * Date:Created in 下午6:26 2017/8/7
 * Copyright (c)  xdy_0722@sina.com All Rights Reserved.
 */
@RestController
public class SleuthProviderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SleuthProviderController.class);

    @GetMapping({"","/"})
    public String index(){
        return "this is a spring-cloud sleuth provider!";
    }

    /**
     * 用来测试链路
     * @return
     */
    @GetMapping("/index")
    public String providerIndex(){
        LOGGER.info("<<<<<<<<<<<<<<<<trace sleuth demo provider is being called! >>>>>>>>>>>>>>>");
        return "Hello dy_bom ,this is a spring-cloud sleuth-stream-mysql provider!";
    }
}
