package com.dullfan.system.event.search;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 * 用户搜索事件
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserSearchEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 453729602512524695L;

    /**
     * 搜索文件关键字
     */
    private String keyword;

    /**
     * 用户ID
     */
    private Long userId;

    public UserSearchEvent(Object source, String keyword,Long userId) {
        super(source);
        this.keyword = keyword;
        this.userId = userId;
    }
}
