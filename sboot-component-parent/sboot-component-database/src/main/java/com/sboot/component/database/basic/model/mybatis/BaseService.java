package com.sboot.component.database.basic.model.mybatis;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sboot.component.database.Pageable;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/10.
 */

public class BaseService <R extends BaseRepository<E, ID>, E extends BaseEntity<ID>, ID> {

    @Autowired
    protected R repository;

    public BaseService() {
    }

    public int insert(E entity) {
        this.beforeInsert(entity);
        return this.repository.insert(entity);
    }

    public int insertSelective(E entity) {
        this.beforeInsert(entity);
        return this.repository.insertSelective(entity);
    }

    public int deleteByPrimaryKey(ID id) {
        return this.repository.deleteByPrimaryKey(id);
    }

    public int deleteByPrimaryKeys(Collection<ID> ids) {
        return this.repository.deleteByPrimaryKeys(new ArrayList(ids));
    }

    public int updateByPrimaryKey(E entity) {
        this.beforeUpdate(entity);
        return this.repository.updateByPrimaryKey(entity);
    }

    public int updateByPrimaryKeySelective(E entity) {
        this.beforeUpdate(entity);
        return this.repository.updateByPrimaryKeySelective(entity);
    }

    public E selectByPrimaryKey(ID id) {
        return this.repository.selectByPrimaryKey(id);
    }

    public List<E> selectByPrimaryKeys(Collection<ID> ids) {
        return this.repository.selectByPrimaryKeys(new ArrayList(ids));
    }

    public List<E> selectAll() {
        return this.repository.selectAll();
    }

    public List<E> selectByEntity(E entity) {
        return this.repository.selectByEntity(entity);
    }

    public E selectOneByEntity(E entity) {
        List<E> entities = this.repository.selectByEntity(entity);
        return !Objects.isNull(entities) && !entities.isEmpty() ? (E)entities.get(0) : null;
    }

    public List<E> selectByConditions(Map<String, Object> conditions) {
        return this.repository.selectByConditions(conditions);
    }

    public E selectOneByConditions(Map<String, Object> conditions) {
        List<E> entities = this.repository.selectByConditions(conditions);
        return !Objects.isNull(entities) && !entities.isEmpty() ? (E)entities.get(0) : null;
    }

    public Pageable selectPage(Pageable page) {
        PageInfo pageInfo = PageHelper.offsetPage(page.getOffset(), page.getPageSize()).doSelectPageInfo(() -> {
            this.repository.selectAll();
        });
        page.setTotalRecords(pageInfo.getTotal());
        page.setContent(pageInfo.getList());
        return page;
    }

    public Pageable selectPage(Pageable page, E entity) {
        PageInfo pageInfo = PageHelper.offsetPage(page.getOffset(), page.getPageSize()).doSelectPageInfo(() -> {
            this.repository.selectByEntity(entity);
        });
        page.setTotalRecords(pageInfo.getTotal());
        page.setContent(pageInfo.getList());
        return page;
    }

    public Pageable selectPage(Pageable page, Map<String, Object> conditions) {
        PageInfo pageInfo = PageHelper.offsetPage(page.getOffset(), page.getPageSize()).doSelectPageInfo(() -> {
            this.repository.selectByConditions(conditions);
        });
        page.setTotalRecords(pageInfo.getTotal());
        page.setContent(pageInfo.getList());
        return page;
    }

    public int insertBatch(List<E> entities) {
        return this.insertBatch(entities, 500);
    }

    public int insertBatch(List<E> entities, int batchSize) {
        this.beforeInsert(entities);
        int size = entities.size();
        int batchTime = size / batchSize;
        int remaining = size % batchSize;
        int updateCount = 0;

        for(int i = 0; i < batchTime; ++i) {
            updateCount += this.repository.insertBatch(entities.subList(i * batchSize, (i + 1) * batchSize));
        }

        if (remaining > 0) {
            updateCount += this.repository.insertBatch(entities.subList(size - remaining, size));
        }

        return updateCount;
    }

    public int updateBatch(List<E> entities) {
        return this.updateBatch(entities, 500);
    }

    public int updateBatch(List<E> entities, int batchSize) {
        this.beforeUpdate(entities);
        int size = entities.size();
        int batchTime = size / batchSize;
        int remaining = size % batchSize;
        int updateCount = 0;

        for(int i = 0; i < batchTime; ++i) {
            updateCount += this.repository.updateBatch(entities.subList(i * batchSize, (i + 1) * batchSize));
        }

        if (remaining > 0) {
            updateCount += this.repository.updateBatch(entities.subList(size - remaining, size));
        }

        return updateCount;
    }

    public Long count() {
        return this.repository.count();
    }

    public Object selectMax(String column) {
        return this.selectByFunction(Func.MAX, column);
    }

    public Object selectMin(String column) {
        return this.selectByFunction(Func.MIN, column);
    }

    public Object selectAvg(String column) {
        return this.selectByFunction(Func.AVG, column);
    }

    public Object selectSum(String column) {
        return this.selectByFunction(Func.SUM, column);
    }

    private Object selectByFunction(Func func, String column) {
        Map<String, Object> result = this.repository.selectByFunction(func.name(), column);
        return !Objects.isNull(result) && !result.isEmpty() ? result.values().iterator().next() : null;
    }

    private void beforeInsert(E entity) {
        Class<? extends BaseEntity> entityClass = entity.getClass();
        /*if (Dateable.class.isAssignableFrom(entityClass) && Objects.isNull(((Dateable)entity).getExtCreatedDate())) {
            ((Dateable)entity).setExtCreatedDate(new Date());
        }*/
    }

    private void beforeUpdate(E entity) {
        Class<? extends BaseEntity> entityClass = entity.getClass();
        /*if (Dateable.class.isAssignableFrom(entityClass) && Objects.isNull(((Dateable)entity).getExtLastModifiedDate())) {
            ((Dateable)entity).setExtLastModifiedDate(new Date());
        }*/
    }

    private void beforeInsert(List<E> entities) {
        entities.forEach(this::beforeInsert);
    }

    private void beforeUpdate(List<E> entities) {
        entities.forEach(this::beforeUpdate);
    }

}
