package cn.bugstack.springframework.beans.factory.config;

/**
 * @PackageName: cn.bugstack.springframework.beans.factory.config
 * @Author 彭仁杰
 * @Date 2025/5/26 20:53
 * @Description
 **/
public class BeanDefinition {
    private Class beanClass;

    public BeanDefinition(Class beanClass){
        this.beanClass = beanClass;
    }

    public Class getBeanClass(){
        return beanClass;
    }

    public void setBeanClass(Class beanClass){
        this.beanClass = beanClass;
    }
}
