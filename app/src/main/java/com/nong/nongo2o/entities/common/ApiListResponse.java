package com.nong.nongo2o.entities.common;

import java.util.List;

/**
 * Created by Administrator on 2017-8-21.
 */

public class ApiListResponse<T> {

    private List<T> rows;
    private int total;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
