package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * @PackageName: com.cn.bugstack.springframework.beans.factory.support
 * @Author 彭仁杰
 * @Date 2025/5/28 22:32
 * @Description
 **/
public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args);
}
