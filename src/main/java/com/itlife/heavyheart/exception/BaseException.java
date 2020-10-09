package com.itlife.heavyheart.exception;

/**
 * @Author kex
 * @Create 2020/8/14 9:12
 * @Description
 */
public abstract class BaseException extends RuntimeException {
    private String code;

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(Throwable cause, String code){
        super(cause);
        this.code = code;
    }

    public BaseException(String code, String message, Throwable cause){
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
