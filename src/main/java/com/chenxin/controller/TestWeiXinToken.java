package com.chenxin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestWeiXinToken {
    @RequestMapping("/confirm")
    public String confirm(@RequestParam("signature") String signature ,@RequestParam("token") String token ,
                          @RequestParam("timestamp") String timestamp ,@RequestParam("nonce") String nonce){
        System.out.println("hello");
        return signature;
    }
}
