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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.SystemService;
import utils.SpringContextHolder;

import java.security.Principal;

@Component
public class MyRealm extends AuthorizingRealm {

    private SystemService systemService;

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
        systemService = SpringContextHolder.getBean(SystemService.class);
        User user = systemService.findUserByUsername((String)authenticationToken.getPrincipal());
        try{
            return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(),"MyRealm");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
