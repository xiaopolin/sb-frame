package com.xpl.web.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@EnableScheduling
public class SystemTimer {

    @Scheduled(cron = "0/5 * * * * ? ")
    public void speak(){
            log.info("定时任务执行时间：" + System.currentTimeMillis());
    }
}
