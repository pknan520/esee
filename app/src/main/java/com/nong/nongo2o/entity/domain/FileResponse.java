package com.nong.nongo2o.entity.domain;

/**
 * Created by Administrator on 2017-9-12.
 */

public class FileResponse {

    private String fileName;
    private String filePath;

    public FileResponse() {
    }

    public FileResponse(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
