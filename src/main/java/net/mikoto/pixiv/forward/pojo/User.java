package net.mikoto.pixiv.forward.pojo;

/**
 * @author mikoto
 * @date 2021/12/4 0:07
 */
public class User {
    private int id;
    private String userName;
    private String userKey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
