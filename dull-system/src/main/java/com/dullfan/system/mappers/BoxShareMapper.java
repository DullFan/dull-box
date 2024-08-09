package com.dullfan.system.mappers;

import com.dullfan.system.entity.vo.BoxShareUrlListVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import com.dullfan.system.entity.po.BoxShare;
import com.dullfan.system.entity.query.BoxShareQuery;

/**
 * @author DullFan
 * @date 2024-07-15 11:25:06
 */
@Mapper
public interface BoxShareMapper extends ABaseMapper<BoxShare,BoxShareQuery> {

    /**
    * 根据 ShareId 查询
    */
    BoxShare selectByShareId(@Param("shareId")Long shareId);

    /**
    * 根据 ShareId 删除
    */
    Integer deleteByShareId(@Param("shareId")Long shareId);

    /**
    * 根据 CreateUserAndCreateTime 查询
    */
    BoxShare selectByCreateUserAndCreateTime(@Param("createUser")Long createUser,@Param("createTime") Date createTime);

    /**
    * 根据 CreateUserAndCreateTime 删除
    */
    Integer deleteByCreateUserAndCreateTime(@Param("createUser")Long createUser,@Param("createTime")Date createTime);


    List<BoxShareUrlListVO> selectShareVOListByUserId(@Param("user_id") Long userId);

    List<BoxShare> selectByShareIds(@Param("ids") List<Long> shareIds,@Param("userId") Long userId);

    Integer deleteByShareIds(@Param("ids")List<Long> shareIds, @Param("userId")Long userId);

    @Override
    default BoxShare selectByIdCache(Long id) {
        return this.selectByShareId(id);
    }

    @Override
    default Integer updateByIdCache(BoxShare boxShare, Long id) {
        BoxShareQuery boxShareQuery = new BoxShareQuery();
        boxShareQuery.setShareId(id);
        return this.updateByParam(boxShare, boxShareQuery);
    }

    @Override
    default Integer deleteByIdCache(Long id) {
        return this.deleteByShareId(id);
    }

    /**
     * 滚动查询数据
     */
    List<Long> rollingQueryShareId(@Param("startId") long startId,@Param("limit") long limit);
}

