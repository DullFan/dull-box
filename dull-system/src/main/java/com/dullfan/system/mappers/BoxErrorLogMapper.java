package com.dullfan.system.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import com.dullfan.system.entity.po.BoxErrorLog;
import com.dullfan.system.entity.query.BoxErrorLogQuery;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author DullFan
 * @date 2024-07-15 11:25:06
 */
@Mapper
public interface BoxErrorLogMapper extends ABaseMapper<BoxErrorLog, BoxErrorLogQuery> {

    /**
     * 根据 Id 查询
     */
    BoxErrorLog selectById(@Param("id") Long id);

    /**
     * 根据 Id 删除
     */
    Integer deleteById(@Param("id") Long id);


    /**
     * 根据 Id 批量删除
     */
    Integer deleteByIdBatch(@Param("list") List<Integer> list);

    @Override
    default BoxErrorLog selectByIdCache(Long id) {
        return selectById(id);
    }

    @Override
    default Integer updateByIdCache(BoxErrorLog boxErrorLog, Long id) {
        BoxErrorLogQuery boxErrorLogQuery = new BoxErrorLogQuery();
        boxErrorLogQuery.setId(id);
        return this.updateByParam(boxErrorLog, boxErrorLogQuery);
    }

    @Override
    default Integer deleteByIdCache(Long id) {
        return this.deleteById(id);
    }
}

