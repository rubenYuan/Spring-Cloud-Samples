package com.mljr.zuul.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author:daoyuanXiong
 * Description:
 * Date:Created in 下午3:47 2017/8/16
 * Copyright (c)  daoyuan.xiong@mljr.com All Rights Reserved.
 */
@Controller
@RequestMapping("/wap")
public class ZuulController {

    @RequestMapping("/hello")
    public String hello (){
        System.out.println("<<<<<<<<<<<<<<<<hello called!>>>>>>>>>>>>>>>>");
        return "hello";
    }

    @RequestMapping({"","/"})
    public String index(){
        System.out.println("<<<<<<<<<<<<<<<<index called!>>>>>>>>>>>>>>>>");
        return "Hi,dy_bom,this is zuul server!";
    }
}
