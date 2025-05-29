package cn.bugstack.springframework.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @PackageName: cn.bugstack.springframework.beans
 * @Author 彭仁杰
 * @Date 2025/5/29 22:33
 * @Description
 **/
public class PropertyValues {
    private final List<PropertyValue> propertyValues = new ArrayList<>();
    public void addPropertyValue(PropertyValue propertyValue) {
        propertyValues.add(propertyValue);
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValues.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue propertyValue : propertyValues) {
            if (propertyValue.getName().equals(propertyName)) {
                return propertyValue;
            }
        }
        return null;
    }
}
