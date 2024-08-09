package com.dullfan.system.listener.search;

import com.dullfan.common.utils.DateUtils;
import com.dullfan.common.utils.encryption.IdUtil;
import com.dullfan.system.entity.po.BoxUserSearchHistory;
import com.dullfan.system.event.search.UserSearchEvent;
import com.dullfan.system.service.BoxUserSearchHistoryService;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 用户搜索事件监听器
 */
@Component
public class  UserSearchEventListener {

    @Resource
    private BoxUserSearchHistoryService boxUserSearchHistoryService;

    /**
     * 保存用户搜索历史
     */
    @EventListener(classes = UserSearchEvent.class)
    @Async(value = "eventListenerTaskExecutor")
    public void saveSearchHistory(UserSearchEvent event){
        BoxUserSearchHistory boxUserSearchHistory = new BoxUserSearchHistory();
        boxUserSearchHistory.setId(IdUtil.get());
        boxUserSearchHistory.setUserId(event.getUserId());
        boxUserSearchHistory.setSearchContent(event.getKeyword());
        boxUserSearchHistory.setCreateTime(DateUtils.getNowDate());
        boxUserSearchHistory.setUpdateTime(DateUtils.getNowDate());
        try {
            // 执行插入
            boxUserSearchHistoryService.insert(boxUserSearchHistory);
        } catch (Exception e) {
            // 执行更新操作
            BoxUserSearchHistory query = new BoxUserSearchHistory();
            query.setUpdateTime(DateUtils.getNowDate());
            boxUserSearchHistoryService.updateByUserIdAndSearchContent(query, event.getUserId(), event.getKeyword());
        }
    }
}
