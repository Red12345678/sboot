package com.sboot.component.database;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/10.
 */
public class PageImpl implements  Pageable {

    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalRecords;
    private List<?> content = new ArrayList();

    public PageImpl(Integer pageNumber, Integer pageSize) {
        Preconditions.checkState(pageNumber > 0, "页码必须大于0");
        Preconditions.checkState(pageSize > 0, "每页的记录数必须大于0");
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    public Integer getOffset() {
        return (this.getPageNumber() - 1) * this.getPageSize();
    }

    @Override
    public Integer getTotalPages() {
        if (Objects.isNull(this.getTotalRecords())) {
            return null;
        } else {
            Integer totalRecords = this.getTotalRecords().intValue();
            Integer pageSize = this.getPageSize();
            return totalRecords % pageSize == 0 ? totalRecords / pageSize : totalRecords / pageSize + 1;
        }
    }

    @Override
    public Integer getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public Integer getPageSize() {
        return this.pageSize;
    }

    @Override
    public Long getTotalRecords() {
        return this.totalRecords;
    }

    @Override
    public List<?> getContent() {
        return this.content;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    @Override
    public void setContent(List<?> content) {
        this.content = content;
    }

}
