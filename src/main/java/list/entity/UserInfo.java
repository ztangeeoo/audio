package list.entity;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author ztang
 * @date 10:14 2018/4/4
 */
public class UserInfo {
    @Field
    private String name;
    @Field
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
