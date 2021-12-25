package net.mikoto.pixiv.forward.dao;

import net.mikoto.dao.BaseDao;
import net.mikoto.pixiv.forward.PixivForwardApplication;
import net.mikoto.pixiv.forward.pojo.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mikoto
 * @date 2021/11/28 14:46
 */
public class UserDao extends BaseDao {
    /**
     * 单例
     */
    private static final UserDao INSTANCE = new UserDao(PixivForwardApplication.URL, PixivForwardApplication.USER_NAME, PixivForwardApplication.USER_PASSWORD);

    /**
     * 构建方法
     *
     * @param url          JPBC_URL
     * @param userName     USERNAME
     * @param userPassword USER_PASSWORD
     */
    public UserDao(String url, String userName, String userPassword) {
        super(url, userName, userPassword);
    }

    /**
     * 获取单例
     *
     * @return 此示例
     */
    public static UserDao getInstance() {
        return INSTANCE;
    }

    /**
     * Query user in database.
     *
     * @param sql SQL statement.
     * @return A device object
     * @throws SQLException SQLException
     */
    public User getUser(String sql) throws SQLException {
        User outputUser = new User();
        ResultSet resultSet = executeQuery(sql);

        while (resultSet.next()) {
            // 从数据库取出设备数据
            outputUser.setId(resultSet.getInt("pk_id"));
            outputUser.setUserName(resultSet.getString("user_name"));
            outputUser.setUserKey(resultSet.getString("user_key"));
        }

        closeResource();
        return outputUser;
    }
}
