package com.xiu.config.node2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:dy_boom
 * Description:
 * Date:Created in 下午4:49 2017/8/8
 * Copyright (c)  xdy_0722@sina.com All Rights Reserved.
 */
@RestController
@RefreshScope //Config客户端在服务器参数刷新时，也刷新注入的属性值
public class ConfigClientController {

    @Value("${bar:test}")
    String bar;

    /**
     * 项目介绍
     * @return
     */
    @GetMapping({"/",""})
    public  String index() {
        return  "Hi,dy_bom! this is  config client node2!";
    }

    /**
     * 获取远程配置中心的配置信息
     * @return
     */
    @GetMapping("/bar")
    public  String getBar() {
        return  bar;
    }
}
