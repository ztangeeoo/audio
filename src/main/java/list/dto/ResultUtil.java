package list.dto;

/**
 * @author ztang
 * @date 13:40 2018/3/28
 */
public class ResultUtil {
    public static Result success() {
        Result result = new Result();
        result.setCode(ResultEnum.RC_0000000.getCode());
        result.setMessage(ResultEnum.RC_0000000.getMessage());
        result.setData(null);
        return result;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(ResultEnum.RC_0000000.getCode());
        result.setMessage(ResultEnum.RC_0000000.getMessage());
        result.setData(object);
        return result;
    }

    public static Result fail(ResultEnum resultEnum) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getMessage());
        return result;
    }
}
