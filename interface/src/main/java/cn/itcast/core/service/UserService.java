package cn.itcast.core.service;


import cn.itcast.core.pojo.user.User;

public interface UserService {

    void sendCode(String phone);

    boolean checkSmsCode(String phone,String smsCode );

    void add(User user);
}
