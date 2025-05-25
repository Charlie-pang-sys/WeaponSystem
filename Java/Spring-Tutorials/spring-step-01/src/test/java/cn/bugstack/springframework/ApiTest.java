package cn.bugstack.springframework;

import cn.bugstack.springframework.bean.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @PackageName: cn.bugstack.springframework.bean
 * @Author 彭仁杰
 * @Date 2025/5/25 20:44
 * @Description
 **/
@SpringBootTest(classes = MyApplication.class)
public class ApiTest {
    @Test
    public void test_BeanFactory(){
        //1、初始化 BeanFactory对象
        BeanFactory beanFactory = new BeanFactory();

        //2、注册 Bean 对象
        beanFactory.registerBeanDefinition("userService",new BeanDefinition(new UserService()));

        //3、获取Bean对象
        UserService userService = (UserService)beanFactory.getBean("userService");
        userService.queryUserInfo();
    }
}
