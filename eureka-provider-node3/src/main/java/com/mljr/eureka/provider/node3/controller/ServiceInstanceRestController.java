package com.mljr.eureka.provider.node3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Author:daoyuanXiong
 * Description:
 * Date:Created in 下午6:26 2017/8/7
 * Copyright (c)  daoyuan.xiong@mljr.com All Rights Reserved.
 */
@RestController
public class ServiceInstanceRestController {

    /**
     * 用来测试与节点1,2的负载均衡
     * @return
     */
    @GetMapping("/index")
    public String index(){
        return "Hello dy_bom ,this is a spring-cloud eureka provider-node3!";
    }

}
