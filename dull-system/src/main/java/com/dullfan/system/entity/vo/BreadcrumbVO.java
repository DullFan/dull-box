package com.dullfan.system.entity.vo;

import com.dullfan.common.utils.serializer.IdEncryptSerializer;
import com.dullfan.system.entity.po.BoxUserFile;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
public class BreadcrumbVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1206494807051775274L;

    /**
     * 文件ID
     */
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long id;

    /**
     * 父文件夹ID
     */
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long parentId;

    /**
     * 文件夹名称
     */
    private String name;

    /**
     * 实体转换
     */
    public static BreadcrumbVO transfer(BoxUserFile record){
        BreadcrumbVO breadcrumbVO = new BreadcrumbVO();
        if(Objects.nonNull(record)){
            breadcrumbVO.setId(record.getFileId());
            breadcrumbVO.setParentId(record.getParentId());
            breadcrumbVO.setName(record.getFilename());
        }
        return breadcrumbVO;
    }
}
