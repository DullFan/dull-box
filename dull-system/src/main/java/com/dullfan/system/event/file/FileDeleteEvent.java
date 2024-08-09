package com.dullfan.system.event.file;

import com.dullfan.system.entity.po.BoxUserFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.util.List;

@ToString
@Getter
@Setter
public class FileDeleteEvent extends ApplicationEvent {


    @Serial
    private static final long serialVersionUID = -6222362466814201101L;

    /**
     * 所有被物理删除的文件
     */
    private List<BoxUserFile> fileList;

    public FileDeleteEvent(Object source,List<BoxUserFile> fileList) {
        super(source);
        this.fileList = fileList;
    }
}
