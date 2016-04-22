package org.ow2.proactive.scheduler.sms;

import org.apache.log4j.Logger;
import org.ow2.proactive.scheduler.core.properties.PASchedulerProperties;
import org.ow2.proactive.scheduler.job.InternalJob;
import org.ow2.proactive.scheduler.task.internal.InternalTask;
import org.ow2.proactive.scheduler.util.DateUtil;

/**
 * Created by leven2 on 16-4-22.
 */
public class Sms {

    private static final Logger logger = Logger.getLogger(Sms.class);

    private static final String SMS_RECIPIENT = "SMS_RECIPIENT";

    private static final String SMS_MSG = "Proactive Scheduling 告警：job %s(%s) 的task %s(%s)执行失败，失败时间为%s";

    public static void initSms(InternalJob job,InternalTask task){
        String recipient = job.getGenericInformation().get(SMS_RECIPIENT);
        String path = PASchedulerProperties.SCHEDULER_SMS_PATH.getValueAsString();
        if(recipient!=null && path!=null){
            String msg = String.format(SMS_MSG,job.getId().value(),job.getName(),task.getId().value()
                    ,task.getName(), DateUtil.getCurrentDay("yyyy-MM-dd hh:mm:ss"));
            logger.info("initSms jobId="+job.getId()+",+taskId="+task.getId() +",msg="+msg
                    +",path="+path+",recipient="+recipient);
            SendSms sendSms = new SendSms(path,recipient,msg);
            sendSms.send();
        }
    }
}
