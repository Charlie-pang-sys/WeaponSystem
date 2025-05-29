package cn.bugstack.springframework.beans;

/**
 * @PackageName: com.cn.bugstack.springframework.beans.factory
 * @Author 彭仁杰
 * @Date 2025/5/28 21:10
 * @Description
 **/
public class BeansException extends RuntimeException{
    public BeansException(String msg){
        super(msg);
    }

    public BeansException(String msg, Throwable cause){
        super(msg, cause);
    }
}
