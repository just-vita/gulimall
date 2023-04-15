package top.vita.gulimall.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpUtils;
import java.util.HashMap;
import java.util.Map;

@Controller
public class OAuth2Controller {
    @GetMapping("oauth2/success")
    public String getToken(@PathVariable("code") String code){
        Map<String, String> header = new HashMap<>();
        Map<String, String> query = new HashMap<>();

        Map<String, String> map = new HashMap<>();

        map.put("client_id", "9b43dba6f72115e13ec9e5a880cebb7a5a8173a7ac5dbf75e4b9dfad6981867a");
        map.put("client_secret", "775441c318f5dbfa382a8a910b986e8d124ef25b810d359cbbe48c300610aaca");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://auth.gulimall.com/oauth2/success");
        map.put("code", code);
        //1、根据code换取accessToken；
//        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", header, query, map);
        return null;
    }

}
