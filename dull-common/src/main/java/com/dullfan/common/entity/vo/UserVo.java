package com.dullfan.common.entity.vo;

import com.dullfan.common.entity.po.User;
import com.dullfan.common.utils.serializer.IdEncryptSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserVo extends User {

    /**
     * 根目录文件ID
     */
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long rootFileId;

    /**
     * 根目录文件名称
     */
    private String rootFilename;
}
