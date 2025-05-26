package cn.bugstack.springframework.test;

import cn.bugstack.springframework.beans.MyApplication;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.bugstack.springframework.test.bean.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @PackageName: cn.bugstack.springframework.test
 * @Author 彭仁杰
 * @Date 2025/5/26 22:11
 * @Description
 **/
@SpringBootTest(classes = MyApplication.class)
public class ApiTest {

    @Test
    public void test_BeanFactory(){
        //1、初始化BeanFactory类
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        //2、注册Bean对象
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        defaultListableBeanFactory.registerBeanDefinition("userService",beanDefinition);
        //3、获取Bean对象调用方法
        UserService userService = (UserService)defaultListableBeanFactory.getBean("userService");
        userService.queryUserInfo();
        //4、再次获取和调用Bean对象
        UserService userService_singleton = (UserService)defaultListableBeanFactory.getBean("userService");
        userService_singleton.queryUserInfo();
    }
}
