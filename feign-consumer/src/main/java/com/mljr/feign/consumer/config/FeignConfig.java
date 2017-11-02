package com.mljr.feign.consumer.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import feign.Retryer;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Author:dy_boom
 * Description:
 * Date:Created in 下午10:28 2017/8/16
 * Copyright (c)  xdy_0722@sina.com All Rights Reserved.
 */
@Configuration
@RibbonClient(name = "eureka-provider")
public class FeignConfig {

    @Bean
    public IRule ribbonRule(){
        System.out.println("<<<<<<<<<<<<<<<<<ribbon rule called!>>>>>>>>>>>>");
        //1.轮询策略（RoundRobinRule）
        //2.随机轮询(RandomRule)
        //3. 权重（WeightedResponseTimeRule）
        //4.最小的并发策略（BestAvailableRule）
        return new RoundRobinRule();
//        return new RandomRule();
    }
}

