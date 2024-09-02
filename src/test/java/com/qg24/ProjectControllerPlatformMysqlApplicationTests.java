package com.qg24;

import com.qg24.AIExplain.ExplainLogs;
import com.qg24.dao.LogMapper;
import com.qg24.dao.UserMapper;
import com.qg24.po.entity.BackendExceptionInfo;
import com.qg24.po.entity.DSReMessage;
import com.qg24.utils.SpringContextUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProjectControllerPlatformMysqlApplicationTests {

    @Test
    void contextLoads() {
        LogMapper logMapper = SpringContextUtil.getBean(LogMapper.class);
        List<BackendExceptionInfo> logs =  logMapper.getBackendExceptionInfos(49,1,0);
        ExplainLogs explainLogs = new ExplainLogs();
        DSReMessage reMessage = explainLogs.LogsExplain(logs);
        System.out.println(reMessage);
    }

}
