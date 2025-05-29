package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.BeanFactory;

/**
 * @PackageName: com.cn.bugstack.springframework.beans.factory.support
 * @Author 彭仁杰
 * @Date 2025/5/28 21:37
 * @Description
 **/
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    @Override
    public Object getBean(String name){
       return doGetBean(name,null);
    }

    @Override
    public Object getBean(String name,Object... args){
        return doGetBean(name,args);
    }

    protected <T> T doGetBean(final String name,final Object[] args){
        Object singleton = super.getSingleton(name);
        if(singleton != null){
            return (T)singleton;
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        return (T)createBean(name,beanDefinition,args);
    }
    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition,Object args[]);
}
