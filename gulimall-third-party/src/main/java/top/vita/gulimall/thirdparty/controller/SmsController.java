package top.vita.gulimall.thirdparty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.vita.common.utils.R;
import top.vita.gulimall.thirdparty.utils.CodeUtils;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    CodeUtils codeUtils;

    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone){
        String code = codeUtils.generator(phone);
        return R.ok().put("code", code);
    }
}
