package com.itlife.heavyheart.exception;

/**
 * @Author kex
 * @Create 2020/8/14 9:18
 * @Description
 */
public class BusinessException extends BaseException {

    public BusinessException(String code, String message) {
        super(code, message);
    }

    public BusinessException(Throwable cause, String code) {
        super(cause, code);
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
