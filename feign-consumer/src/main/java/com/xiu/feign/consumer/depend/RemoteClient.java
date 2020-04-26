package com.xiu.feign.consumer.depend;

import com.xiu.feign.consumer.hystrix.ConsumerFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Author:dy_boom
 * Description:
 * Date:Created in 下午4:23 2017/8/8
 * Copyright (c)  xdy_0722@sina.com All Rights Reserved.
 * @FeignClient注解后自动运用了ribbon的负载均衡
 */
@FeignClient(name = "eureka-provider", fallback = ConsumerFallback.class)
public interface RemoteClient {

    @RequestMapping(value = "/index")
    String index();
}

