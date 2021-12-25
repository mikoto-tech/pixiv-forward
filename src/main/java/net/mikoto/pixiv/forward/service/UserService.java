package net.mikoto.pixiv.forward.service;

import net.mikoto.pixiv.forward.dao.UserDao;
import net.mikoto.pixiv.forward.pojo.User;

import java.sql.SQLException;

/**
 * @author mikoto
 * @date 2021/11/20 13:33
 */
public class UserService {
    /**
     * 单例
     */
    private static final UserService INSTANCE = new UserService();

    /**
     * 构建方法
     */
    private UserService() {
    }

    /**
     * 获取单例
     *
     * @return 当前实例
     */
    public static UserService getInstance() {
        return INSTANCE;
    }

    /**
     * 通过UserDao获取用户信息
     *
     * @param key 用户的Key
     * @return 用户pojo对象
     */
    public User getUser(String key) {
        User user = null;
        try {
            user = UserDao.getInstance().getUser("SELECT * from pixiv_web_data" +
                    ".user_data WHERE user_key='" + key +
                    "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
