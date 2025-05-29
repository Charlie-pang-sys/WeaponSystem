package test;

import cn.bugstack.springframework.beans.MyApplication;
import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanReference;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import test.bean.UserDao;
import test.bean.UserService;

/**
 * @PackageName: cn.bugstack.springframework.test
 * @Author 彭仁杰
 * @Date 2025/5/26 22:11
 * @Description
 **/
@SpringBootTest(classes = MyApplication.class)
public class ApiTest {

    @Test
    public void test_BeanFactory() {
        DefaultListableBeanFactory listableBeanFactory = new DefaultListableBeanFactory();
        listableBeanFactory.registryBeanDefinition("userDao", new BeanDefinition(UserDao.class));
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uId", "10001"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        listableBeanFactory.registryBeanDefinition("userService", beanDefinition);

        UserService userService = (UserService)listableBeanFactory.getBean("userService");
        userService.queryUserInfo();


        userService.queryUserInfo();
    }
}
