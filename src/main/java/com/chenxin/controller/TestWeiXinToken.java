package com.chenxin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestWeiXinToken {
    @RequestMapping("/test")
    public String confirm(){
        return "signature";
    }

    @RequestMapping(value = "/confirm" , method = RequestMethod.GET)
    public String confirm(@RequestParam(value = "signature" , required = true) String signature ,@RequestParam(value = "token" , required = true) String token ,
                          @RequestParam(value = "timestamp" , required = true) String timestamp ,@RequestParam(value = "nonce" , required = true) String nonce){
        System.out.println("hello");
        return signature;
    }
}
