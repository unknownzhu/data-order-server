package com.ctjsoft.orderserver.analyze.common;

import com.alibaba.fastjson.JSONObject;
import com.ctjsoft.orderserver.analyze.domain.JxStdDatabase;
import com.ctjsoft.orderserver.analyze.mapper.JxStdDatabaseMapper;
import com.ctjsoft.orderserver.analyze.mapper.JxStdTableMapper;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 任务调度处理
 * @author yvan
 */
@Configuration
public class SchedulerQuartz {
  //  @Autowired
  //  private JxStdEvaluationServiceImpl jxStdEvaluationService;
    // 任务调度
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SchedulerQuartz schedulerQuartz;
    @Autowired
    private JxStdTableMapper jxStdTableMapper;
    @Autowired
    private JxStdDatabaseMapper jxStdDatabaseMapper;

 //  @Autowired
 //  private StdScoreVersionMapper stdScoreVersionMapper;
 //  @Autowired
 //  private StdTableMapper stdTableMapper;

 //  @Autowired
 //  private StdDatabaseMapper stdDatabaseMapper;
 //  @Autowired
 //  StdVersionMapper stdVersionMapper;
 //  @Autowired
 //  StdEvaluationService stdEvaluationService;

    //标准评估定时任务时间
    @Value("${stdCheckCron}")
    private String stdCheckCron;



    public void startCheckJob(JxStdDatabase jxStdDatabase) throws SchedulerException {
        startJob(scheduler, jxStdDatabase);
    }

    //数据库标准检查任务
    private void startJob(Scheduler scheduler, JxStdDatabase jxStdDatabase) throws SchedulerException {
      /*  JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("data", jxStdDatabase.getId());
        String typeName = "";
        if (jxStdDatabase.getType().equals("1")) {
            typeName = "生产库";
        } else if (jxStdDatabase.getType().equals("1")) {
            typeName = "汇总库";
        }
        String jobName = jxStdDatabase.getId();
        String groupName = "评估数据源：" + jxStdDatabase.getName() + ":【" + jxStdDatabase.getId() + "】 类型：" + typeName;
        String cron = jxStdDatabase.getCron();
        JobDetail jobDetail = JobBuilder.newJob(CheckDataBaseSTDJob.class).usingJobData(jobDataMap)
                .withIdentity(jobName, groupName).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(stdCheckCron);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName)
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);

*/
    }

    /**
     * 通过数据库id 重启任务
     * @throws SchedulerException
     */
    public void restartByDb(JxStdDatabase jxStdDatabase) throws SchedulerException {
        this.deleteJobByDbId(jxStdDatabase.getId());
        this.startCheckJob(jxStdDatabase);
    }


    /**
     * 通过数据库id 删除任务
     * @param jobName
     * @throws SchedulerException
     */
    public void deleteJobByDbId(String jobName) throws SchedulerException {
        String group = this.getGroupNameByJobName(jobName);
        JobKey jobKey = new JobKey(jobName, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }


    /**
     * 获取Job信息
     * @param jobName
     * @param group
     * @return
     * @throws SchedulerException
     */
    public List<JSONObject> getJobInfo(String jobName, String group) throws SchedulerException {
        List<JSONObject> list = new ArrayList<>();
        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
            String jobNameTemp = jobKey.getName();
            if (jobNameTemp.equals(jobName)) {

                String jobGroup = jobKey.getGroup();
                List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                // Date nextFireTime = triggers.get(0).getNextFireTime();

                String nextFireTime = formatDateTimeString(triggers.get(0).getNextFireTime(), 2);
                String previousFireTime = formatDateTimeString(triggers.get(0).getPreviousFireTime(), 2);
                String nowTime = formatDateTimeString(new Date(), 2);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("jobName", jobNameTemp);
                jsonObject.put("jobGroup", jobGroup);
                jsonObject.put("nextFireTime", nextFireTime);
                jsonObject.put("nowTime", nowTime);
                jsonObject.put("previousFireTime", previousFireTime);
                list.add(jsonObject);
            }
        }
        return list;
    }


    /**
     * 获取所有Job信息
     * @return
     * @throws SchedulerException
     */
    public List<JSONObject> getAllQuartzJobInfo() throws SchedulerException {
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        List<JSONObject> list = new ArrayList<>();
        for (String groupName : scheduler.getJobGroupNames()) {

            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                String jobName = jobKey.getName();
                String jobGroup = jobKey.getGroup();
                List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                // Date nextFireTime = triggers.get(0).getNextFireTime();

                String nextFireTime = formatDateTimeString(triggers.get(0).getNextFireTime(), 2);
                //      String previousFireTime = StringUtil.formatDateTimeString(triggers.get(0).getPreviousFireTime(), 2);
                String nowTime = formatDateTimeString(new Date(), 2);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("jobName", jobName);
                jsonObject.put("jobGroup", jobGroup);
                jsonObject.put("nextFireTime", nextFireTime);
                jsonObject.put("nowTime", nowTime);
                //   jsonObject.put("previousFireTime", previousFireTime);
                list.add(jsonObject);
            }
        }
        return list;

    }


    /**
     * 修改某个任务的执行时间
     * @param jobName
     * @param group
     * @param cron
     * @return
     * @throws SchedulerException
     */
    public boolean modifyJob(String jobName, String group, String cron) throws SchedulerException {
        if (group.equals(null)||group.equals("null")||group.equals("")) {
            group = this.getGroupNameByJobName(jobName);
        }
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(jobName, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(cron)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, group)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     * @param jobName
     * @param group
     * @throws SchedulerException
     */
    public void pauseJob(String jobName, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     * @param jobName
     * @param group
     * @throws SchedulerException
     */
    public void resumeJob(String jobName, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     * @param jobName
     * @param group
     * @throws SchedulerException
     */
    public void deleteJob(String jobName, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }

    public String getGroupNameByJobName(String jobName) throws SchedulerException {
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        List<JSONObject> list = new ArrayList<>();
        for (String groupName : scheduler.getJobGroupNames()) {

            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                String tempJobName = jobKey.getName();
                if (jobName.equals(jobName)) {
                    return jobKey.getGroup();
                }
            }
        }

        return "";
    }

    public static String formatDateTimeString(Date date, int style) {
        String value = null;
        int year = date.getYear();
        int month = date.getMonth();
        String day = getStrFromInt(date.getDate());
        String hour = getStrFromInt(date.getHours());
        String minute = getStrFromInt(date.getMinutes());
        String second = getStrFromInt(date.getSeconds());
        switch(style) {
            case 2:
                value = year + 1900 + "-" + getStrFromInt(month + 1) + "-" + day + " " + hour + ":" + minute + ":" + second;
                break;
            default:
                value = date.toString();
        }

        return value;
    }

    private static String getStrFromInt(int i) {
        return i < 10 ? "0" + i : String.valueOf(i);
    }
}
