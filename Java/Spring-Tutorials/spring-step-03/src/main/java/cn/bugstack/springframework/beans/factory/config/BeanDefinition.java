package cn.bugstack.springframework.beans.factory.config;

/**
 * @PackageName: com.cn.bugstack.springframework.beans.factory.config
 * @Author 彭仁杰
 * @Date 2025/5/28 21:33
 * @Description
 **/
public class BeanDefinition {
    private Class beanClass;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Class getBeanClass(){
        return this.beanClass;
    }

    public void setBeanClass(Class beanClass){
        this.beanClass = beanClass;
    }
}
