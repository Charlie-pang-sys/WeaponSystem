package cn.bugstack.springframework.beans;

/**
 * @PackageName: cn.bugstack.springframework.beans
 * @Author 彭仁杰
 * @Date 2025/5/29 20:46
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
