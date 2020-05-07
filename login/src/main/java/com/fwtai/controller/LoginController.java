package com.fwtai.controller;

import com.fwtai.pojo.User;
import com.fwtai.tool.LoginCacheUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 登录认证
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-05-07 12:29
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Controller
public class LoginController{

    private static Set<User> dbUsers = new HashSet<>(4);
    static {
        dbUsers.add(new User(1,"zhangsan","123456"));
        dbUsers.add(new User(2,"admin","123456"));
        dbUsers.add(new User(3,"user","123456"));
        dbUsers.add(new User(4,"typ","123456"));
    }

    @PostMapping("/user/login")
    public String login(final User user,final HttpSession session){
        final String target = (String)session.getAttribute("target");
        if(target == null){}
        //模拟数据库登录认证
        final Optional<User> optional = dbUsers.stream().filter(dbUser -> dbUser.getUsername().equals(user.getUsername()) && dbUser.getPassword().equals(user.getPassword())).findFirst();
        //判断用户是否存在
        if(optional.isPresent()){
            //保存用户登录信息
            LoginCacheUser.loginUser.put(getToken(),optional.get());
            //重定向到 target 地址
            return "redirect:"+target;
        }else {
            //登录失败处理
            session.setAttribute("msg","账号或密码错误");
            return "login";
        }
    }

    public String getToken(){
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextInt(),random.nextInt()).toString().replaceAll("-","");
    }
}