import dao.UserDao;
import entity.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import security.BeanTest;

public class ShiroTest {
    public ApplicationContext applicationContext;
    private Subject currentSubject;
    private Session subjectSession;
    private static final transient Logger log = LoggerFactory.getLogger(ShiroTest.class);
    @Before
    public void initMethod(){
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        currentSubject = SecurityUtils.getSubject();
        subjectSession = currentSubject.getSession();
    }
    @After
    public void destroyMethod(){
    }

    @Test
    public void shiroTest(){
        if(!currentSubject.isAuthenticated()){
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("root","HIKARI");
            try{
                currentSubject.login(usernamePasswordToken);
                log.info("login success");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Test
    public void test(){
        for (String name : applicationContext.getBeanDefinitionNames()){
            System.out.println(name);
        }
        BeanTest beanTest = new BeanTest();
        beanTest.test();
    }


}
