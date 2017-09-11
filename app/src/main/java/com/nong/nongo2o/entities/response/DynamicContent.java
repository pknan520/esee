package com.nong.nongo2o.entities.response;

import java.util.List;

/**
 * Created by Administrator on 2017-8-25.
 */

public class DynamicContent {

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
