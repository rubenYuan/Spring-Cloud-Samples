package com.mljr.eureka.provider.node1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Author:daoyuanXiong
 * Description:
 * Date:Created in 下午6:26 2017/8/7
 * Copyright (c)  daoyuan.xiong@mljr.com All Rights Reserved.
 */
@RestController
public class ProviderNode1Controller {

    @GetMapping({"","/"})
    public String index(){
        System.out.println("<<<<<<<<<<<<<<<<<<URL地址："+serviceUrl()+">>>>>>>>>>>>>>>>>>");
        return "this is a spring-cloud eureka provider-node1!";
    }

    /**
     * 用来测试与节点2.3的负载均衡
     * @return
     */
    @GetMapping("/index")
    public String providerIndex(){
        return "Hello dy_bom ,this is a spring-cloud eureka provider-node1!";
    }
    @Autowired
    private DiscoveryClient discoveryClient;

    public URI serviceUrl() {
        List<ServiceInstance> list = discoveryClient.getInstances("spring-cloud-eureka-provider");
        if (list != null && list.size() > 0 ) {
            System.out.println("<<<<<<<<<<<<<<<<<元数据："+list.get(0).getMetadata()+">>>>>>>>>>>>>>>>>");
            return list.get(0).getUri();
        }

        return null;
    }
}
