package cn.itcast.core.controller;

import cn.itcast.core.common.PhoneFormatCheckUtils;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {


    @Reference
    private UserService userService;

    /**
     * 生成随机六位数字作为验证码, 发送到指定的手机
     *
     * @param phone
     * @return
     */
    @RequestMapping("/sendCode")
    public Result sendCode(String phone) {
        try {
            if (phone == null || "".equals(phone)) {
                return new Result(false, "请正确填写手机号!");
            }
            if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
                return new Result(false, "手机号不正确!");
            }
            userService.sendCode(phone);
            return new Result(true , "短信发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , "短信发送失败!");
        }
    }
    @RequestMapping("/add")
    public Result add(@RequestBody User user,String smscode){
        try {
            //校验验证码是否正确
            boolean isCheck = userService.checkSmsCode(user.getPhone(), smscode);
            if (!isCheck){
                return new Result(false,"手机号或者验证码填写错误");
            }
            //如果验证码正确保存用户
            user.setCreated(new Date());
            user.setUpdated(new Date());
            //用户注册来源
            user.setSourceType("1");
            //用户状态默认为正常
            user.setStatus("Y");
            userService.add(user);
            return new Result(true,"注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"注册失败");
        }
    }
}
