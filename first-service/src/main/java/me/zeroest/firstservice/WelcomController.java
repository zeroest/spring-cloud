package me.zeroest.firstservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first-service")
public class WelcomController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome first service";
    }

}
