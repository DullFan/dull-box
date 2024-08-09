package com.dullfan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件删除标识
 */
@Getter
@AllArgsConstructor
public enum DelFlagEnum {
    NO(0),
    YES(1);
    private Integer code;
}
