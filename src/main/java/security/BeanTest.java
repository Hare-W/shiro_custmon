package security;

import dao.UserDao;
import entity.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanTest {
    @Autowired
    private UserDao userDao;
    public void test(){
        User user = userDao.findUserByUsername("root");
        System.out.println("user");
    }
}
