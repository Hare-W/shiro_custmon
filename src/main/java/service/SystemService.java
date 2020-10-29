package service;

import dao.UserDao;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemService {
    @Autowired
    private UserDao userdao;

    public User findUserByUsername(String username){
        return userdao.findUserByUsername(username);
    }
}
