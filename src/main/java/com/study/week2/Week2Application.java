package com.study.week2;

import com.study.week2.connection.ConnectionTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Week2Application {

    public static void main(String[] args) {
        SpringApplication.run(Week2Application.class, args);
    }

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
