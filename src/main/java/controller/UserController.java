package controller;

import entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.SystemService;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SystemService systemService;

    private final Map<String,Object> resultMap = new HashMap<>();


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Map<String,Object> login(HttpServletRequest request,@RequestParam String username, @RequestParam String password){
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentSubject = SecurityUtils.getSubject();

        if(!currentSubject.isAuthenticated()){
            if(request.getHeader("token") == null)
            {
                UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);
                try{
                    currentSubject.login(usernamePasswordToken);
                    resultMap.put("msg","登录成功：用户名和密码登录");
                    String token = JwtUtil.sign(username);
                    if (token != null){
                        resultMap.put("token", JwtUtil.sign(username) + " 1分钟有效，生成时间：" + System.currentTimeMillis());
                        return  resultMap;
                    }
                }catch (AuthenticationException e){
                    resultMap.put("msg","登录失败:AuthenticationException error");
                    return  resultMap;
                }
            }
            String token = request.getHeader("token");
            if(JwtUtil.verify(token)){
                logger.info("token verify success, token =" + request.getHeader("token"));
                //JwtUtil.getUsername(token);
                resultMap.put("msg","登录成功：token 登录");
            } else
            {
                resultMap.put("token","认证失败");
            }
            return resultMap;
         //   return resultMap;
        }
//        if(currentSubject.isPermitted("/root/test"))
//        {
//            resultMap.put("permission","/root/test");
//            return resultMap;
//        }
        resultMap.put("msg","登录成功：已经登录");
        return resultMap;
    }

    @RequestMapping(value = "/verifyTest",method = RequestMethod.POST)
    public Map<String,Object> verifyTest(HttpServletRequest request){
        String token = request.getHeader("token");
        if(JwtUtil.verify(token)){
            resultMap.put("token","认证成功");
            JwtUtil.getUsername(token);
        }
        else
        {
            resultMap.put("token","认证失败");
        }
        return resultMap;

    }


    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public User test(@RequestBody User user){
        return user;
    }
}
