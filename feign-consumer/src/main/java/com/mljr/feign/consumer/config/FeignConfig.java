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
 * Author:daoyuanXiong
 * Description:
 * Date:Created in 下午10:28 2017/8/16
 * Copyright (c)  daoyuan.xiong@mljr.com All Rights Reserved.
 */
@Configuration
@RibbonClient(name = "spring-cloud-eureka-provider")
public class FeignConfig {

    /**
     * FeignClient的重试次数，重试间隔为100ms，最大重试时间为1s,重试次数为5次。
     * @return
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1), 5);
    }


    @Bean
    public IRule ribbonRule(){
        System.out.println("<<<<<<<<<<<<<<<<<ribbon rule called!>>>>>>>>>>>>");
        //默认轮询策略（RoundRobinRule） 随机轮询(RandomRule)
        // 权重（WeightedResponseTimeRule） 最小的并发策略（BestAvailableRule）
        // 自动切换(RetryRule)
        return new RoundRobinRule();
//        return new RandomRule();
    }
}

