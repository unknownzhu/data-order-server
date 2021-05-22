package com.ctjsoft.orderserver.schedule;


import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zyx
 * @date 2020/9/17
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Resource
    IScheduleService scheduleService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("-------------->启动定时任务<--------------");
        scheduleService.startAllCronJobs();
        scheduleService.checkJob();
       // scheduleService.startAllCronJobs();
    }

}