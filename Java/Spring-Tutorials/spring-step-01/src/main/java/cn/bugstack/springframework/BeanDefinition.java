package cn.bugstack.springframework;

/**
 * @PackageName: cn.bugstack.springframework
 * @Author 彭仁杰
 * @Date 2025/5/25 20:31
 * @Description 用于定义Bean对象，它的视线方式是以一个Object类型存储对象。
 **/
public class BeanDefinition {
    private Object bean;

    public BeanDefinition (Object bean){
        this.bean = bean;
    }
    public Object getBean(){
        return this.bean;
    }
}
