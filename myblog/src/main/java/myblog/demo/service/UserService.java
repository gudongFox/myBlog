package myblog.demo.service;

import myblog.demo.po.User;
//定义用户接口
public interface UserService {

    User checkUser(String username, String password);
}
