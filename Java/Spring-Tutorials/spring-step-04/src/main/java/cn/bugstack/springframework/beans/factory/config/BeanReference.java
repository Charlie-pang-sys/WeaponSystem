package cn.bugstack.springframework.beans.factory.config;

/**
 * @PackageName: cn.bugstack.springframework.beans.factory.config
 * @Author 彭仁杰
 * @Date 2025/5/29 22:32
 * @Description
 **/
public class BeanReference {
    private final String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
