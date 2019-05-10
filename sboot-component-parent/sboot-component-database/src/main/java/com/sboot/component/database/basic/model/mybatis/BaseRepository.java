package com.sboot.component.database.basic.model.mybatis;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author tuozq
 * @description: 数据层基类
 * @date 2019/5/10.
 */
public interface BaseRepository<M extends BaseEntity<ID>, ID> {

    int insert(M model);

    int insertSelective(M model);

    int deleteByPrimaryKey(ID primaryKey);

    int deleteByPrimaryKeys(List<ID> primaryKeys);

    int updateByPrimaryKey(M model);

    int updateByPrimaryKeySelective(M model);

    M selectByPrimaryKey(ID primaryKey);

    List<M> selectByPrimaryKeys(List<ID> var1);

    List<M> selectAll();

    List<M> selectByEntity(M model);

    List<M> selectByConditions(Map conditions);

    int insertBatch(List<M> list);

    int updateBatch(List<M> list);

    Long count();

    Map<String, Object> selectByFunction(@Param("function") String var1, @Param("column") String var2);

}
