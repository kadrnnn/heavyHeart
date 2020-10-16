package com.itlife.heavyheart.exception;

import com.itlife.heavyheart.web.result.ResultBean;
import com.itlife.heavyheart.web.result.ResultEnum;
import com.itlife.heavyheart.web.result.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author kex
 * @Create 2020/8/14 9:29
 * @Description
 */
@ControllerAdvice
public class BusinessExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    public BusinessExceptionHandler(){

    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({BusinessException.class})
    public ResultBean<String> businessException(BusinessException businessException) {
        log.warn(" --业务操作提醒=========" + businessException.getMessage(), businessException);
        return ResultUtils.error(ResultEnum.UNKONW_ERROR.getCode(), businessException.getMessage());
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
