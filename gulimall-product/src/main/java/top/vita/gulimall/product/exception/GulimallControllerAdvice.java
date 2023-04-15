package top.vita.gulimall.product.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.vita.common.exception.BizCodeEnume;
import top.vita.common.utils.R;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice(basePackages = "top.vita.gulimall.product.controller")
public class GulimallControllerAdvice {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
        HashMap<String, String> map = new HashMap<>();
        BindingResult result = e.getBindingResult();
        result.getFieldErrors().forEach((item)->{
            String message = item.getDefaultMessage();
            String field = item.getField();
            map.put(field,message);
        });
        return R.error(BizCodeEnume.VALID_EXCEPTION).put("data",map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        throwable.printStackTrace();
        return R.error();
    }
}
