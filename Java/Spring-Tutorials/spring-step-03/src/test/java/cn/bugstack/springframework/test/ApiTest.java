package cn.bugstack.springframework.test;

import cn.bugstack.springframework.test.bean.UserService;
import cn.bugstack.springframework.beans.MyApplication;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @PackageName: cn.bugstack.springframework.test.bean
 * @Author 彭仁杰
 * @Date 2025/5/28 21:56
 * @Description
 **/
@SpringBootTest(classes = MyApplication.class)
public class ApiTest {

    @Test
    public void test_bean(){
        DefaultListableBeanFactory listableBeanFactory = new DefaultListableBeanFactory();
        listableBeanFactory.registerBeanDefinition("userService",new BeanDefinition(UserService.class));

        UserService userService = (UserService)listableBeanFactory.getBean("userService","Charlie");
        userService.queryUserInfo();
    }
}
