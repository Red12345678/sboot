package com.sboot.component.database;

import java.util.List;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/10.
 */
public interface Pageable {

    Integer getPageNumber();

    Integer getPageSize();

    Integer getOffset();

    Integer getTotalPages();

    Long getTotalRecords();

    void setTotalRecords(Long var1);

    List<?> getContent();

    void setContent(List<?> var1);

}
