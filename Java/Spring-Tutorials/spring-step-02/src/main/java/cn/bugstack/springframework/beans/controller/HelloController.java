package cn.bugstack.springframework.beans.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PackageName: cn.bugstack.springframework.beans.controller
 * @Author 彭仁杰
 * @Date 2025/5/28 19:50
 * @Description
 **/
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String sayHello() {
        return "Hello Prometheus!";
    }
}

