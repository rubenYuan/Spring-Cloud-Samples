package com.xiu.eurekaprovidernode2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Author:dy_boom
 * Description:
 * Date:Created in 下午6:26 2017/8/7
 * Copyright (c)  xdy_0722@sina.com All Rights Reserved.
 */
@RestController
public class ServiceInstanceRestController {

    /**
     * 用来测试与节点1,3的负载均衡
     * @return
     */
    @GetMapping("/index")
    public String index(){
        return "Hi,dy_bom! this is  provider-node2 of peer!";
    }

}
