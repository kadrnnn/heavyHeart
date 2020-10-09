package com.itlife.heavyheart.web.result;

/**
 * @Author kex
 * @Create 2020/8/14 9:55
 * @Description
 */
public class ResultRedirectBean implements ResultBody {
    private String code;
    private String redirectUrl;

    public ResultRedirectBean() {
    }

    public String getCode() {
        return this.code;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
