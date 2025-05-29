package cn.bugstack.springframework.beans;

/**
 * @PackageName: cn.bugstack.springframework.beans
 * @Author 彭仁杰
 * @Date 2025/5/29 22:32
 * @Description
 **/
public class PropertyValue {
    private final String name;

    private final Object value;

    public PropertyValue(String name, Object value) {

        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
