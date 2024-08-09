package com.dullfan.system.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class UploadedChunksVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4495360150433003579L;

    @ApiModelProperty("已上传的分片编号列表")
    private List<Integer> uploadedChunks;

}
