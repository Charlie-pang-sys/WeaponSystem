package com.charlie.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private T data;
    private int total;
    private int pageNum;
    private int pageSize;
    private int pages;

    public PageResult(T data, int total, int pageNum, int pages) {
        this.data = data;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = pages;
    }

    public static PageResult<List<OrderDO>> of(List<OrderDO> data, int total, int pageNum, int pageSize) {
        int pages = (int) Math.ceil((double) total / pageSize);
        return new PageResult(data, total, pageNum, pages);
    }

    public static PageResult<List<OrderDO>> empty() {
        return new PageResult(null, 0, 1, 10);
    }
}
