package kimxu.hhs.db.dao.model;

/**
 * Created by xuzhiguo on 15/8/3.
 */
public class User {
    private String name;
    private String password;
    private String portrait;

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

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
