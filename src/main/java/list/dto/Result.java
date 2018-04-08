package list.dto;

/**
 * @author ztang
 * @date 13:39 2018/3/28
 */
public class Result {
    private String code;
    private String message;
    private Object data;

    public Result(){}



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
