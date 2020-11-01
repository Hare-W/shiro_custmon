package dao;

import entity.User;

public interface UserDao {

    User findUserByUsername(String username);

    User findUserByUserId(String userId);

}
