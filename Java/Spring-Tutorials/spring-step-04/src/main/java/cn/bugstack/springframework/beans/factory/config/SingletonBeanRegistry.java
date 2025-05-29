package cn.bugstack.springframework.beans.factory.config;

/**
 * @PackageName: cn.bugstack.springframework.beans.factory.config
 * @Author 彭仁杰
 * @Date 2025/5/29 20:50
 * @Description
 **/
public interface SingletonBeanRegistry {
    void registerSingleton(String name,Object singletonObject);

    Object getSingleton(String name);
}
