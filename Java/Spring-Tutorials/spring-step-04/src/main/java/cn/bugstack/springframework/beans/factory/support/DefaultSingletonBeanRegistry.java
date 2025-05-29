package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @PackageName: cn.bugstack.springframework.beans.factory.support
 * @Author 彭仁杰
 * @Date 2025/5/29 20:51
 * @Description
 **/
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private Map<String,Object> singletonObjects = new HashMap<>();


    @Override
    public void registerSingleton(String name, Object singletonObject) {
        singletonObjects.put(name,singletonObject);
    }

    @Override
    public Object getSingleton(String name) {
        return singletonObjects.get(name);
    }
}
