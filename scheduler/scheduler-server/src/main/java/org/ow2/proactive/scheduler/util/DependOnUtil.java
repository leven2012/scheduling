package org.ow2.proactive.scheduler.util;


import org.apache.log4j.Logger;
import org.ow2.proactive.scheduler.core.db.SchedulerDBManager;
import org.ow2.proactive.scheduler.descriptor.JobDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DependOnUtil {

    private static final Logger logger = Logger.getLogger(DependOnUtil.class);

    public static final String LOOP_END="LOOP_END";

    public static final String LOOP_BATCH="LOOP_BATCH";

    public static final String GENERIC_INFORMATION_KEY_DEPEND_JOBS= "DEPEND_JOBS";

    public static boolean validateDependOn(JobDescriptor jobDesc, SchedulerDBManager dbManager){
        boolean validateBoolean = true;
        List<String> dependJobList = new ArrayList<String>();
        dependJobList = getDependOnJob(jobDesc);
        String batch = getDependOnBatch(jobDesc);
        logger.info("validateDependOn jobDesc.getJobId().value()="+jobDesc.getJobId().value()
                +",dependJobList="+dependJobList.toString()+",batch="+batch);
        if (dependJobList.size()!=0){
            Map<String,Long> map = dbManager.getJobsEventNumber(dependJobList,batch);
            for(String dependJob:dependJobList){
                Long value=map.get(dependJob);
                logger.info("validateDependOn value="+value);
                if(value==null){
                    logger.info("validateDependOn validateBoolean false");
                    validateBoolean=false;
                    break;
                }
            }
        }

        return validateBoolean;
    }


    private static List<String> getDependOnJob(JobDescriptor jobDesc) {
        List<String> dependOn = new ArrayList<String>();
        String dependOnStr = jobDesc.getInternal().getGenericInformation().get(GENERIC_INFORMATION_KEY_DEPEND_JOBS);
        if (dependOnStr != null){
            dependOn = Arrays.asList(dependOnStr.split(","));
        }
        return dependOn;
    }

    private static String getDependOnBatch(JobDescriptor jobDesc) {
        String loopBatch = jobDesc.getInternal().getVariables().get(DependOnUtil.LOOP_BATCH);
        return loopBatch;
    }
}