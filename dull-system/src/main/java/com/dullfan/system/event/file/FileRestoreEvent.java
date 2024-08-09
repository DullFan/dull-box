package com.dullfan.system.event.file;

import lombok.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.context.ApplicationEvent;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.Serial;
import java.util.List;

@ToString
@Getter
@Setter
public class FileRestoreEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 6352329562721385171L;

    /**
     * 被成功还原的文件ID
     */
    private List<Long> fileIdList;


    public FileRestoreEvent(Object source,List<Long> fileIdList) {
        super(source);
        this.fileIdList = fileIdList;
    }
}
