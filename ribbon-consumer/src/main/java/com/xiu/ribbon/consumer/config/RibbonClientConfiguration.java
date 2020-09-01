package com.xiu.ribbon.consumer.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Mr.xiu
 */
@Configuration
@RibbonClient(name = "eureka-provider")
public class RibbonClientConfiguration {
    @Bean
    public IRule ribbonRule(){
        System.out.println("<<<<<<<<<<<<<<<<<ribbon rule called!>>>>>>>>>>>>");
        //默认轮询策略（RoundRobinRule） 随机轮询(RandomRule) 权重（WeightedResponseTimeRule） 最小的并发策略（BestAvailableRule）
//         return new RoundRobinRule();
        return new RandomRule();
    }
}
