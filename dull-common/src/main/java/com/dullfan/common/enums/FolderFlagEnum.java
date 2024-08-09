package com.dullfan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件夹标识枚举类
 */
@AllArgsConstructor
@Getter
public enum FolderFlagEnum {
    NO(0),YES(1);
    private final Integer code;
}

