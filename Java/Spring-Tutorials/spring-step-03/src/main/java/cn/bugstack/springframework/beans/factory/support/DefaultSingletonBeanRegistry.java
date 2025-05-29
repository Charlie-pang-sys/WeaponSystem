package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @PackageName: com.cn.bugstack.springframework.beans.factory.support
 * @Author 彭仁杰
 * @Date 2025/5/28 21:38
 * @Description
 **/
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry{
    Map<String,Object> singletonObjects = new HashMap<>();
    @Override
    public Object getSingleton(String name) {
        return singletonObjects.get(name);
    }

    @Override
    public void registerSingleton(String name, Object singletonObject) {
        singletonObjects.put(name,singletonObject);
    }
}
