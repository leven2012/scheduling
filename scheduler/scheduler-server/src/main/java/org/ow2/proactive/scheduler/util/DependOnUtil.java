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

    public static final String LOOP_DATE="LOOP_DATE";

    public static final String GENERIC_INFORMATION_KEY_DEPEND_JOBS= "DEPEND_JOBS";

    public static final String THIRD_NAME="THIRD_NAME";

    public static final String THIRD_PATH="THIRD_PATH";

    public static final String THIRD_FILE_NAME="THIRD_FILE_NAME";

    public static final String THIRD_IP="THIRD_IP";

    public static final String THIRD_PORT="THIRD_PORT";

    public static final String THIRD_USER="THIRD_USER";

    public static final String THIRD_PWD="THIRD_PWD";

    public static final String THIRD_FILE_NAME_START="THIRD_NAME_START";


    public static boolean validateDependOn(JobDescriptor jobDesc, SchedulerDBManager dbManager){
        boolean validateBoolean = true;
        List<String> dependJobList = new ArrayList<String>();
        dependJobList = getDependOnJob(jobDesc);
        logger.info("validateDependOn jobId="+jobDesc.getJobId().value()+",dependJobList="+dependJobList.toString());
        if (dependJobList.size()!=0){
            String batch = getDependOnBatch(jobDesc);
            logger.debug("validateDependOn jobId="+jobDesc.getJobId().value()+",batch="+batch);
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

        if(validateBoolean){
            validateBoolean = dependOnFile(jobDesc, dbManager);
        }

        return validateBoolean;
    }

    private static boolean dependOnFile(JobDescriptor jobDesc, SchedulerDBManager dbManager){
        boolean fileBoolean = true;

        Map<String,String> genericMaps = jobDesc.getInternal().getGenericInformation();
        String thirdName = genericMaps.get(THIRD_NAME);
        String thirdPath = genericMaps.get(THIRD_PATH);
        String thirdIp = genericMaps.get(THIRD_IP);
        String thirdPort = genericMaps.get(THIRD_PORT);
        String thirdUser = genericMaps.get(THIRD_USER);
        String thirdPwd = genericMaps.get(THIRD_PWD);
        String nameStart = genericMaps.get(THIRD_FILE_NAME_START);
        logger.info("dependOnFile jobId="+jobDesc.getJobId().value()+",thirdName="+thirdName+",thirdPath="+thirdPath
                +",thirdIp="+thirdIp+",thirdPort="+thirdPort+",thirdUser="+thirdUser+",thirdPwd="+thirdPwd+",nameStart="+nameStart);

        List<String> fileNameList = new ArrayList<String>();
        if (thirdName!=null && thirdPath!=null &&thirdIp!=null && thirdPort!=null && thirdUser!=null && thirdPwd!=null){
            ItemFtp t = new ItemFtp();
            try {
                t.connect(thirdPath, thirdIp, Integer.valueOf(thirdPort), thirdUser, thirdPwd);
                if(nameStart!=null){
                    fileNameList = t.listNames(nameStart);
                }else{
                    fileNameList = t.listNames();
                }

            } catch (Exception e) {
                logger.error("dependOnFile jobId="+jobDesc.getJobId().value()+",e:"+e);
                e.printStackTrace();
            }finally {
                t.close();
            }
            List<String> dbFileNames = dbManager.getJobThirds(jobDesc.getInternal().getName(),thirdName);
            if(fileNameList!=null && dbFileNames!=null){
                fileNameList.removeAll(dbFileNames);
            }
            logger.info("dependOnFile jobId="+jobDesc.getJobId().value()+",fileNameList.size()="+fileNameList.size());
            if (fileNameList!=null && fileNameList.size()>0){
                String fileName = fileNameList.get(0);
                genericMaps.put(THIRD_FILE_NAME,fileName);
                jobDesc.getInternal().setGenericInformations(genericMaps);
            }else{
                fileBoolean = false;
            }
        }

        return  fileBoolean;
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
        if(loopBatch==null){
            loopBatch = DateUtil.getCurrentDay();
        }
        return loopBatch;
    }
}