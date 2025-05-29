package test.bean;

/**
 * @PackageName: cn.bugstack.springframework.test.bean
 * @Author 彭仁杰
 * @Date 2025/5/26 22:11
 * @Description
 **/
public class UserService {
    private String uId;
    private UserDao userDao;
    //public UserService(String name) {
    //    this.uId = name;
    //}

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void queryUserInfo(){
        System.out.println("查询用户信息"+userDao.queryUserName(uId));
    }
}
