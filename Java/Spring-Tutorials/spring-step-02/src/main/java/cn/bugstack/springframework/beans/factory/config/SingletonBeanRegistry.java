package cn.bugstack.springframework.beans.factory.config;

/**
 * @PackageName: cn.bugstack.springframework.beans.factory.config
 * @Author 彭仁杰
 * @Date 2025/5/26 21:01
 * @Description 单例对象注册接口
 **/
public interface SingletonBeanRegistry {
    //获取单实例bean
    Object getSingleton(String beanName);
    //注册单实例bean
    void registerSingleton(String beanName, Object singletonObject);
}
