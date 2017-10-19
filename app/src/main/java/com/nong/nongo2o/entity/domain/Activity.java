package com.nong.nongo2o.entity.domain;

import com.nong.nongo2o.entity.base.PagingEntity;

import java.util.Date;

/**
 * <p>Description: 实体类Entity <p>
 * <p>Module:	 <p>
 *
 *
 * @author wisdom
 * @date: 2017-07-18 21:20:25
 * @since V1.0
 */
public class Activity extends PagingEntity {

    /**
     * 活动编码
     */
    private String activityCode;
    /**
     * 标题
     */
    private String title;
    /**
     * 子标题
     */
    private String subTitle;
    /**
     * 封面
     */
    private String cover;
    /**
     * 活动开始时间
     */
//    private Date startTime;
    /**
     * 活动结束时间
     */
//    private Date endTime;

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

//    @Override
//    public Date getStartTime() {
//        return startTime;
//    }
//
//    @Override
//    public void setStartTime(Date startTime) {
//        this.startTime = startTime;
//    }
//
//    @Override
//    public Date getEndTime() {
//        return endTime;
//    }
//
//    @Override
//    public void setEndTime(Date endTime) {
//        this.endTime = endTime;
//    }
}