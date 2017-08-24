package com.mljr.feign.consumer.depend;

import com.mljr.feign.consumer.hystrix.ConsumerFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Author:daoyuanXiong
 * Description:
 * Date:Created in 下午4:23 2017/8/8
 * Copyright (c)  daoyuan.xiong@mljr.com All Rights Reserved.
 * @FeignClient注解后自动运用了ribbon的负载均衡
 */
@FeignClient(name = "spring-cloud-eureka-provider", fallback = ConsumerFallback.class)//整个类所有方法熔断
public interface RemoteClient {

    @RequestMapping(value = "/index")
    String index();
}

