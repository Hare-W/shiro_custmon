package security;

import dao.UserDao;
import entity.Role;
import entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import service.SystemService;
import utils.SpringContextHolder;

import java.security.Principal;

@Component
public class MyRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(MyRealm.class);

    private SystemService systemService;

    //@Autowired
    private RedisTemplate redisTemplate;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        systemService = SpringContextHolder.getBean(SystemService.class);
        if (principalCollection == null){
            return null;
        }
        User user = systemService.findUserByUsername((String) principalCollection.getPrimaryPrincipal());
        if (user != null){
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for(Role role:systemService.findRoleByRoleId(user.getUserId())){
                String rolePermission = role.getHref();
                for(String permission:StringUtils.split(rolePermission,","))
                {
                    info.addStringPermission(permission);
                }
            }
            return info;
        }

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
        User user = new User();
        user.setUsername("root");
        user.setPassword("hikari");
        redisTemplate.opsForValue().set("user_1",user);
        logger.info("redis key(user_1) value = " + redisTemplate.opsForValue().get("user_1").toString());
        User userTest = (User)redisTemplate.opsForValue().get("user_1");
//
//        //systemService = SpringContextHolder.getBean(SystemService.class);
//        //User user = systemService.findUserByUsername((String)authenticationToken.getPrincipal());
        assert userTest != null;
        return new SimpleAuthenticationInfo(userTest.getUsername(),userTest.getPassword(),"MyRealm");
    }
}
