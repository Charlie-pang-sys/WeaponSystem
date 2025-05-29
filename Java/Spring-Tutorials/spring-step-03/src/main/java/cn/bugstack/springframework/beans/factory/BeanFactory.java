package cn.bugstack.springframework.beans.factory;


import cn.bugstack.springframework.beans.BeansException;

/**
 * @PackageName: com.cn.bugstack.springframework.beans.factory
 * @Author 彭仁杰
 * @Date 2025/5/28 21:10
 * @Description
 **/
public interface BeanFactory {

    Object getBean(String name)throws BeansException;

    Object getBean(String name, Object ... args)throws BeansException;
}
