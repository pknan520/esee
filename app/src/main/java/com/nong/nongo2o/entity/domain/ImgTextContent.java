package com.nong.nongo2o.entity.domain;

import com.nong.nongo2o.entities.response.DynamicContent;

import java.util.List;

/**
 * Created by Administrator on 2017-9-13.
 */

public class ImgTextContent {

    private String content;
    private List<String> img;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }
}
