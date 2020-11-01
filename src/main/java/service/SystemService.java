package service;

import dao.RoleDao;
import dao.UserDao;
import entity.Role;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemService {
    @Autowired
    private UserDao userdao;

    @Autowired
    private RoleDao roleDao;

    public User findUserByUsername(String username){
        return userdao.findUserByUsername(username);
    }
    public User findUserByUserId(String userId){
        return userdao.findUserByUserId(userId);
    }

    public List<Role> findRoleByRoleId(String roleId){
        return roleDao.findRoleByRoleId(roleId);
    }
}
