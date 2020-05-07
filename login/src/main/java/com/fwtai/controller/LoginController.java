package com.fwtai.controller;

import com.fwtai.pojo.User;
import com.fwtai.tool.LoginCacheUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/auth")
    public String login(final User user,final HttpSession session,final HttpServletResponse response){
        final String target = (String)session.getAttribute("target");
        //模拟数据库登录认证
        final Optional<User> optional = dbUsers.stream().filter(dbUser -> dbUser.getUsername().equals(user.getUsername()) && dbUser.getPassword().equals(user.getPassword())).findFirst();
        //判断用户是否存在
        if(optional.isPresent()){
            final String token = getToken();
            //保存用户登录信息
            LoginCacheUser.loginUser.put(token,optional.get());
            //重定向到 target 地址,响应时写入cookie
            final Cookie cookie = new Cookie("token",token);
            cookie.setDomain("codeshop.com");//这个很重要!!!
            cookie.setPath("/");//这个很重要!!!
            response.addCookie(cookie);
            return "redirect:"+target;
        }else {
            //登录失败处理
            session.setAttribute("msg","账号或密码错误");
            return "login";
        }
    }

    // 验证token是否有效
    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<User> getUserInfo(final String token){
        if(!StringUtils.isEmpty(token)){
            final User user = LoginCacheUser.loginUser.get(token);
            final Optional<User> optional = Optional.ofNullable(user);
            //return ResponseEntity.of(optional,HttpStatus.OK);
            return ResponseEntity.of(optional);
        }
        return new ResponseEntity(null,HttpStatus.BAD_REQUEST);
    }

    private String getToken(){
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextInt(),random.nextInt()).toString().replaceAll("-","");
    }
}