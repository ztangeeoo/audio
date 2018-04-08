package list.exception;

import list.dto.Result;
import list.dto.ResultEnum;
import list.dto.ResultUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 仅能处理查找到@RequestMapping 的Exception, 对Dispatcher之前发生的Exception无能为力，例如404
 */
@ControllerAdvice
public class AudioExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handle(Exception e, WebRequest request) {
        if (e instanceof AudioException) {
            AudioException re = (AudioException) e;
            HttpStatus httpStatus = re.getHttpStatus();
            return handleExceptionInternal(e, ResultUtil.fail(re.getResultEnum()), new HttpHeaders(),
                    httpStatus, request);
        } else {
            logger.error("系统异常", e);
            return handleExceptionInternal(e, ResultUtil.fail(ResultEnum.RC_0401001), new HttpHeaders(),
                    HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }
}