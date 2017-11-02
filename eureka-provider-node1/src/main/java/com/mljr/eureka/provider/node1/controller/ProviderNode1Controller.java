package com.mljr.eureka.provider.node1.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Author:dy_boom
 * Description:
 * Date:Created in 下午6:26 2017/8/7
 * Copyright (c)  xdy_0722@sina.com All Rights Reserved.
 */
@RestController
public class ProviderNode1Controller {

    @Autowired
    private EurekaClient eurekaClient;


    //服务发现
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping({"","/"})
    public String index(){
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<通过DiscoveryClient获取服务的元数据，"+getServiceMetadataByLocalClient()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<通过EurekaClient获取服务的元数据，"+getServiceMetadataByEurekaClient()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return "Hi,dy_bom! this is  provider-node1 of peer!";
    }
    /**
     * 用来测试与节点2.3的负载均衡
     * @return
     */
    @GetMapping("/index")
    public String providerIndex(){
        return "Hi,dy_bom! this is  provider-node1 of peer!";
    }

    /**
     * 通过本地的EurekaClient获取服务的元数据
     * @return
     */
    public Object getServiceMetadataByEurekaClient(){
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("eureka-provider",false);
        return instance.getMetadata();
    }

    /**
     * 通过DiscoveryClient获取服务的元数据
     * @return
     */
    public Object getServiceMetadataByLocalClient() {
        List<ServiceInstance> list = discoveryClient.getInstances("eureka-provider");
        if (list != null && list.size() > 0 ) {
            return list.get(0).getMetadata();
        }
        return null;
    }
}
