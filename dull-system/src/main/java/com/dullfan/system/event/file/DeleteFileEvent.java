package com.dullfan.system.event.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.util.List;

/**
 * 文件删除事件
 */
@Getter
@Setter
@ToString
public class DeleteFileEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = -4715568269720761334L;

    private List<Long> fileIdList;

    public DeleteFileEvent(Object source, List<Long> fileIdList) {
        super(source);
        this.fileIdList = fileIdList;
    }

}
