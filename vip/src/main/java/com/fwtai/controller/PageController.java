package com.fwtai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面跳转
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-05-07 12:29
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Controller
public class PageController{

    @GetMapping({"/","/index"})
    public String index(){
        return "index";
    }
}