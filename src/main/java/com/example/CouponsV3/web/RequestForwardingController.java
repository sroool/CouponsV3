package com.example.CouponsV3.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RequestForwardingController {
    @RequestMapping(value = "/**/{[path:[^\\\\.]*}")
    public String redirect() {
        // Forward to home page so that angular routing is preserved.
        return "forward:/";
    }
}