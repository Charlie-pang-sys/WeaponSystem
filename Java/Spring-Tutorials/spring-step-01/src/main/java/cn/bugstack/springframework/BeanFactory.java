package cn.bugstack.springframework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @PackageName: cn.bugstack.springframework
 * @Author 彭仁杰
 * @Date 2025/5/25 20:37
 * @Description 该类用于生成和使用对象的Bean工厂，包括Bean对象的注册和获取。
 **/
public class BeanFactory {
    private Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    public Object getBean(String name){
        return beanDefinitionMap.get(name).getBean();
    }

    /**
     * 注册beanDefinition
     * @param name bean名称
     * @param beanDefinition beanDefinition对象
     */
    public void registerBeanDefinition(String name,BeanDefinition beanDefinition){
        beanDefinitionMap.put(name,beanDefinition);
    }
}
