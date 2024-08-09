import com.dullfan.AdminApplication;
import com.dullfan.common.config.DullConfig;
import com.dullfan.common.core.cache.CaffeineCacheProperties;
import com.dullfan.common.utils.encryption.FileUtils;
import com.dullfan.common.utils.file.FileUploadUtils;
import com.dullfan.common.utils.schedule.ScheduleManage;
import com.dullfan.common.utils.schedule.SimpleScheduleTask;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AdminApplication.class)
public class TestCode {

    @Resource
    private ScheduleManage scheduleManage;

    @Resource
    private SimpleScheduleTask scheduleTask;

    @SneakyThrows
    @Test
    public void testCode() {

    }
}
