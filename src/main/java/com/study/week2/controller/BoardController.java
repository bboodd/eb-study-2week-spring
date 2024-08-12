package com.study.week2.controller;

import com.study.week2.connection.ConnectionTest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
    @GetMapping(value = "/")
    public String doGetHelloWorld() {
        try{
            ConnectionTest t = new ConnectionTest();
            return "connection: " + t.getConnection();
        } catch (Exception e){
            return "Connection err" + e;
        }
    }
}
