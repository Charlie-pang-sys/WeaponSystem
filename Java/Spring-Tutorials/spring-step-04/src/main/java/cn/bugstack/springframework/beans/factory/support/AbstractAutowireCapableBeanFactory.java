package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanReference;
import cn.hutool.core.bean.BeanUtil;

import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * @PackageName: cn.bugstack.springframework.beans.factory.support
 * @Author 彭仁杰
 * @Date 2025/5/29 21:02
 * @Description
 **/
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{
     InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
    @Override
    public Object createBean(String name, BeanDefinition beanDefinition,Object[] args){
        Object bean;
        try {
             bean = createBeanInstance(name,beanDefinition,args);
             //给Bean对象填充属性
            applyPropertyValues(name,bean,beanDefinition);
            super.registerSingleton(name,bean);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed",e);
        }
        return bean;
    }

    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition){
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                if(value instanceof BeanReference){
                    //例如：A依赖B，获取B的实例化对象
                    BeanReference beanReference = (BeanReference)value;
                    value = getBean(beanReference.getBeanName());
                }
                //属性填充
                BeanUtil.setFieldValue(bean,name,value);
            }
        } catch (BeansException e) {
            throw new BeansException("Error setting property values：" + beanName,e);
        }
    }

    protected Object createBeanInstance(String name, BeanDefinition beanDefinition,Object[] args){
        Constructor constructorToUse=null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            if(Objects.nonNull(args) && constructor.getParameterTypes().length==args.length){
                constructorToUse = constructor;
                break;
            }
        }
        return getInstantiationStrategy().instantiate(beanDefinition,name,constructorToUse,args);
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
