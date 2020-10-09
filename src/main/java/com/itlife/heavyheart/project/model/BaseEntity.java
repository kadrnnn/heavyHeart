package com.itlife.heavyheart.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author kex
 * @Create 2020/9/17 15:01
 * @Description
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 2393269568666085258L;
    @Id
    private Long id;

    /**
     * 版本号
     */
    private Integer version;
    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    /**
     * 最近操作人
     */
    private String lastOperator;

    /**
     * 最后操作人ID
     */
    private Long lastOperatorId;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Transient
    private Integer pageNum;

    @Transient
    private Integer pageSize;

    @Transient
    private String orderBy;

    /**
     * Is new boolean.
     *
     * @return the boolean
     */
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return this.id == null;
    }

//    /**
//     * Sets update info.
//     *
//     * @param user the user
//     */
//    @Transient
//    @JsonIgnore
//    public void setUpdateInfo(LoginAuthDto user) {
//
//        if (isNew()) {
//            this.creatorId = (this.lastOperatorId = user.getUserId());
//            this.creator = user.getUserName();
//            this.createdTime = (this.updateTime = new Date());
//        }
//        this.lastOperatorId = user.getUserId();
//        this.lastOperator = user.getUserName() == null ? user.getLoginName() : user.getUserName();
//        this.updateTime = new Date();
//    }
}
