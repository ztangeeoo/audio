package list.exception;

import list.dto.ResultEnum;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ztang
 * @date 13:48 2018/3/28
 */
public class AudioException extends RuntimeException{
    protected final HttpStatus httpStatus;

    protected final ResultEnum resultEnum;

    protected final transient Map<String, Object> data;


    public AudioException(ResultEnum resultEnum) {
        this(HttpStatus.OK, resultEnum, new HashMap<>());
    }

    public AudioException(ResultEnum resultEnum, HttpStatus httpStatus) {
        this(httpStatus, resultEnum, new HashMap<>());
    }

    public AudioException(ResultEnum resultEnum, Map<String, Object> data) {
        this(HttpStatus.OK, resultEnum, data);
    }

    public AudioException(HttpStatus httpStatus, ResultEnum resultEnum, Map<String, Object> data) {
        this.httpStatus = httpStatus;
        this.resultEnum = resultEnum;
        this.data = data;
    }

    public ResultEnum getResultEnum() {
        return resultEnum;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
