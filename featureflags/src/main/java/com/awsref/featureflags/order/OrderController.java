package com.awsref.featureflags.order;

import java.io.UnsupportedEncodingException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @GetMapping(value = "/order")
    public String orderApi(){
        return "Order API";
    }
}
