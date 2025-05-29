package cn.bugstack.springframework.beans.factory.config;

/**
 * @PackageName: com.cn.bugstack.springframework.beans.factory.config
 * @Author 彭仁杰
 * @Date 2025/5/28 21:35
 * @Description
 **/
public interface SingletonBeanRegistry {

    Object getSingleton(String name);

    void registerSingleton(String name, Object singletonObject);
}
