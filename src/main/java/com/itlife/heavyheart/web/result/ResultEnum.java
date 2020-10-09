package com.itlife.heavyheart.web.result;

/**
 * @Author kex
 * @Create 2020/8/14 9:46
 * @Description
 */
public enum ResultEnum {
    UNKONW_ERROR("-1", "未知错误"),
    SUCCESS("0", "成功"),
    VALID_ERROR("9", "验证失败"),
    REDIRECT("REDIRECT", "重定向");

    private String code;
    private String msg;

    private ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
