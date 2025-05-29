package test.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @PackageName: test.bean
 * @Author 彭仁杰
 * @Date 2025/5/29 22:51
 * @Description
 **/
public class UserDao {
    private static Map<String,String> hashMap = new HashMap<>();
    static {
        hashMap.put("10001","小傅哥");
        hashMap.put("10002","八杯水");
        hashMap.put("10003","阿毛");
    }

    public String queryUserName(String uId){
        return hashMap.get(uId);
    }
}
