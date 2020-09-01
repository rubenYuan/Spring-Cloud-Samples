package com.xiu.consul.consumer.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Mr.xiu
 * @Description:
 * @Date: 2020/5/19 11:54
 */
@ConfigurationProperties(prefix = "user")
@Configuration
public class UserConfig {

  private  String name;

  private int age;

  private String address;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "UserConfig{" +
        "name='" + name + '\'' +
        ", age=" + age +
        ", address='" + address + '\'' +
        '}';
  }
}
