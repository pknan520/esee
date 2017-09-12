package com.nong.nongo2o.entity.base;

import java.util.Date;

/**
 * Created by Administrator on 2017-9-12.
 */

public class PagingEntity extends OperateEntity{

    private static final long serialVersionUID = 2442811476914489832L;
    private Integer offset = Integer.valueOf(0);
    private Integer limit = Integer.valueOf(15);
    private String orderByType = "asc";
    private String orderBy = "1";
    private Date startTime;
    private Date endTime;

    public PagingEntity() {
    }

    //    @JsonIgnore
    public String getOrderByType() {
        return this.orderByType;
    }

    public void setOrderByType(String orderByType) {
        this.orderByType = orderByType;
    }

//    @JsonIgnore
    public Integer getOffset() {
        return this.offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

//    @JsonIgnore
    public Integer getLimit() {
        return this.limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

//    @JsonIgnore
    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

//    @JsonIgnore
    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

//    @JsonIgnore
    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
