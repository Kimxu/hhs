package kimxu.hhs.model;

/**
 * Created by xuzhiguo on 15/7/30.
 */
public class Banner {
    public int id;
    public String imgUrl;
    public String uriData;
    public String description;// banner的广告语，可以为空
    public String actionType;
    public String name;

    public String toString() {
        return id + "/" + imgUrl + "/" + name;
    }
}

