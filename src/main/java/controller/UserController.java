package controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/")
public class UserController {
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public Map<String,Object> shiroTest(@RequestParam String username, @RequestParam String password){
        Map<String,Object> resultMap = new HashMap<>();
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentSubject = SecurityUtils.getSubject();
        if(!currentSubject.isAuthenticated()){
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("root","hikari");
            try{
                currentSubject.login(usernamePasswordToken);
                resultMap.put("msg","登录成功");
            }catch (AuthenticationException e){
                resultMap.put("msg","登录失败");
            }
            return resultMap;
        }
        return null;
    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public String test(@RequestParam String testValue){
        return testValue;
    }
}
