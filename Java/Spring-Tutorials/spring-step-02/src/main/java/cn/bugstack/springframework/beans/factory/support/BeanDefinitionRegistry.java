package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.factory.config.BeanDefinition;

/**
 * @PackageName: cn.bugstack.springframework.beans.factory.support
 * @Author 彭仁杰
 * @Date 2025/5/26 22:01
 * @Description
 **/
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
