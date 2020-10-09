package com.itlife.heavyheart.web.auditorAware;

import java.io.Serializable;

/**
 * @Author kex
 * @Create 2020/8/14 10:11
 * @Description
 */
public interface Userable<PK extends Serializable> extends Serializable {
    String SUPPERADMIN_ID = "00000000";

    PK getId();

    String getLoginName();

    String getName();

    String getPassword();

    default boolean isSuperAdmin() {
        return "00000000".equals(this.getId());
    }
}
