package cn.bugstack.springframework.test.bean;

/**
 * @PackageName: cn.bugstack.springframework.test.bean
 * @Author 彭仁杰
 * @Date 2025/5/26 22:11
 * @Description
 **/
public class UserService {

    private String name;

    public UserService(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "UserService{" +
                "name='" + name + '\'' +
                '}';
    }

    public void queryUserInfo(){
        System.out.println("查询用户信息"+name);
    }
}
