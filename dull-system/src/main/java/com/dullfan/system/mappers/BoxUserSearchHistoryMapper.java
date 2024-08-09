package com.dullfan.system.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import com.dullfan.system.entity.po.BoxUserSearchHistory;
import com.dullfan.system.entity.query.BoxUserSearchHistoryQuery;

/**
 * @author DullFan
 * @date 2024-07-15 11:25:06
 */
@Mapper
public interface BoxUserSearchHistoryMapper extends ABaseMapper<BoxUserSearchHistory,BoxUserSearchHistoryQuery> {

    /**
    * 根据 Id 查询
    */
    BoxUserSearchHistory selectById(@Param("id")Long id);

    /**
    * 根据 Id 删除
    */
    Integer deleteById(@Param("id")Long id);


    /**
    * 根据 Id 批量删除
    */
    Integer deleteByIdBatch(@Param("list") List<Integer> list);
    /**
    * 根据 UserIdAndSearchContentAndUpdateTime 查询
    */
    BoxUserSearchHistory selectByUserIdAndSearchContentAndUpdateTime(@Param("userId")Long userId,@Param("searchContent")String searchContent,@Param("updateTime") Date updateTime);

    /**
    * 根据 UserIdAndSearchContentAndUpdateTime 删除
    */
    Integer deleteByUserIdAndSearchContentAndUpdateTime(@Param("userId")Long userId,@Param("searchContent")String searchContent,@Param("updateTime")Date updateTime);

    /**
    * 根据 UserIdAndSearchContent 查询
    */
    BoxUserSearchHistory selectByUserIdAndSearchContent(@Param("userId")Long userId,@Param("searchContent")String searchContent);

    /**
    * 根据 UserIdAndSearchContent 删除
    */
    Integer deleteByUserIdAndSearchContent(@Param("userId")Long userId,@Param("searchContent")String searchContent);

    @Override
    default BoxUserSearchHistory selectByIdCache(Long id) {
        return this.selectById(id);
    }

    @Override
    default Integer updateByIdCache(BoxUserSearchHistory boxUserSearchHistory, Long id) {
        BoxUserSearchHistoryQuery query = new BoxUserSearchHistoryQuery();
        query.setId(id);
        return this.updateByParam(boxUserSearchHistory, query);
    }

    @Override
    default Integer deleteByIdCache(Long id) {
        return this.deleteById(id);
    }

    List<BoxUserSearchHistory> selectByUserId(@Param("userId") Long userId);
}

