package cn.bugstack.springframework.beans.factory;


import cn.bugstack.springframework.beans.BeansException;

/**
 * @PackageName: cn.bugstack.springframework.beans.factory
 * @Author 彭仁杰
 * @Date 2025/5/26 21:18
 * @Description
 **/
public interface BeanFactory {

    Object getBean(String name)throws BeansException;
}
