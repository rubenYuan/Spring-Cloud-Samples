package com.mljr.zuul.service.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:daoyuanXiong
 * Description:
 * Date:Created in 下午3:47 2017/8/16
 * Copyright (c)  daoyuan.xiong@mljr.com All Rights Reserved.
 */
@RestController
@RequestMapping("/wap")
public class ZuulController {

    @RequestMapping("/{name}")
    public String find (@PathVariable String name){
        System.out.println("<<<<<<<<<<<<<<name:"+name);
        return name;
    }

    @RequestMapping({"","/"})
    public String index(){
        return "Hi,dy_bom,this is zuul server!";
    }
}
