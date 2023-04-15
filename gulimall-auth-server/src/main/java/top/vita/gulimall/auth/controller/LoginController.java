package top.vita.gulimall.thirdparty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.vita.common.utils.R;
import top.vita.gulimall.auth.feign.ThirdPartFeignService;

@RestController
@RequestMapping("/sms")
public class LoginController {

    @Autowired
    ThirdPartFeignService thirdPartFeignService;

    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone){
        R r = thirdPartFeignService.sendCode(phone);
        Object code = r.get("phone");
        return R.ok().put("code", code);
    }
}
